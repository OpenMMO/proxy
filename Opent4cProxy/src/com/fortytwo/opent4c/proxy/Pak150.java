package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fortytwo.opent4c.tools.ByteArrayToBinaryString;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.MSRand;

public class Pak150 {
	private boolean isTest = false;//only used to print infos while decrypting, for testing purpose
	private boolean isServerToClient = false;//direction of the pak
	private DatagramPacket packet;//not needed when everything will be working
	private long timeStamp = -1;//System.currentTimeMillis() just after we received the pak
	private long micros = -1;//((System.nanoTime()-Proxy.startTime)%1000000)/1000 juste after we received the pak
	private ByteBuffer header = ByteBuffer.allocate(16);//will be filled with bytes 0 to 15 of received data
	private byte fragmentID = -1;//byte 0 of header, == 0 for non fragmented paks
	private boolean isLastFragment = false;//if fragmentID != 0, this pak is the last of a fragmented message
	//byte 1 is a bitmask, it sets these 3 booleans:
	private boolean isPong = false;//if pak is a pong message
	private boolean isPing = false;//if pak is needs a pong in return
	private boolean isFragment = false;//if the pak is part of a fragmented message
	private short length;//byte 2 and 3 of header. == 0 for pong paks so it has to be manually set to 16, otherwise == pak length.
	private int datagramID = -1;//bytes 4 to 7 of header. for pong paks, tells the id of the corresponding ping pak, otherwise it the datagram's ID.
	private int firstPakID = -1;//bytes 8 to 11 of header. tells the id of the first pak of a fragmented message, otherwise == 0
	private boolean isFirstFragment = false;//if datagramID == firstPakID, this pak is the first of a fragmented message, otherwise == 0
	private int seed = 0;//bytes 12 to 15 of header, it's the seed for decrypting the data of this pak.
	private ByteBuffer pak = null;//bytes 16 and following. it's first filled with crypted data and then with decrypted.
	private byte[] pak_crypt = null;//original data, not need when everyhing will be working
	private short type = -1;//byte 0 and 1 of decrypted data, we get it from pak after it's been decrypted.
	private boolean valid = false;//set to true if the checksum is valid after we've decrypted the data.

	/**
	 * only for testing purpose
	 */
	public Pak150(){
		isTest = true;
	}
	
	
	/**
	 * T4C 1.50 datagram packet, received encrypted
	 * @param pack
	 * @param direction : true = server->client
	 * @param stamp
	 * @param microseconds
	 */
	public Pak150(DatagramPacket pack, boolean direction, long stamp, long microseconds) {
		isTest = true;//as long as we do live tests, enables verbose mode
		timeStamp = stamp;
		micros = microseconds;
		isServerToClient = direction;
		packet = pack;
		header.put(pack.getData(),0,16);
		header.order(ByteOrder.LITTLE_ENDIAN);
		header.rewind();
		decodeHeader(header);
		pak = ByteBuffer.allocate(length-16);
		pak.put(packet.getData(), 16, length-16);
		pak.rewind();
		pak_crypt = pak.array().clone();
		if(isTest)System.out.println("SOURCE "+HexString.from(pack.getData()));
		if(!isFragment) {
			if(!isPong){
				decrypt_150();
			}
		}else{
			System.err.println("FRAGMENT : "+isFragment);
			//TODO manage fragments
		}
		//print();
	}

	/**
	 * decodes datagram header
	 * @param header
	 */
	private void decodeHeader(ByteBuffer header) {
		fragmentID = header.get();
		byte bitMask = header.get();
		length = header.getShort();
		isPong = isPong(bitMask);
		if(isPong) {
			length = 16;
			datagramID = header.getInt();
		}else{		
			isPing = isPing(bitMask);
			isFragment = isFragment(bitMask);
			datagramID = header.getInt();
			firstPakID = header.getInt();
			if(datagramID == firstPakID) isFirstFragment = true;
			if(fragmentID != 0) isLastFragment = true;
			seed = header.getInt();
		}
	}

	private boolean isFragment(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(5) == '1')return true;
		return false;
	}

	private boolean isPing(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(6) == '1')return true;
		return false;
	}

	private boolean isPong(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(7) == '1')return true;
		return false;
	}
	
	/**
	 * prints relevant infos about packet on standard output
	 */
	private void print() {
		//TODO separate fx for different pak types
		StringBuilder sb = new StringBuilder();
		sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
		if(isServerToClient){
			sb.append("[SERVER->CLIENT]");
		}else{
			sb.append("[CLIENT->SERVER]");
		}
		if(isPong){
			sb.append("[PONG "+datagramID+"]");
			System.out.println(sb.toString());
			return;
		}
		if(isPing){
			sb.append("[PING "+datagramID+"]");
		}else{
			sb.append("["+datagramID+"]");
		}
		if(isFragment){
			sb.append("["+"TOTAL LENGTH "+length+"]");
			if(isFirstFragment){
				sb.append("[FRAGMENT 1ST "+firstPakID+"]"+"[SEED "+seed+"]");
			}else if(isLastFragment){
				sb.append("[FRAGMENT LAST "+firstPakID+"]");
			}else{
				sb.append("[FRAGMENT FOR "+firstPakID+"]");
			}
		}else{
			sb.append("["+"LENGTH "+length+"]"+"[SEED "+seed+"]");
		}
		sb.append("[SRC "+HexString.from(pak_crypt)+"]");
		sb.append("[DATA "+HexString.from(pak.array())+"]");
		if(isServerToClient){
			sb.append("[TYPE "+HexString.from(type)+"]");
		}else{
			sb.append("[TYPE "+HexString.from(type)+"]");
		}
		sb.append("[SRC "+HexString.from(packet.getData())+"]");
		System.out.println(sb.toString());
	}
	
	/**
	 * test data
	 * [SERVER->CLIENT][RECEIVED BYTEBUFFER 00 02 1F 00 01 00 00 00 00 00 00 00 00 FB 84 5D 54 04 2F DE 9D F3 D5 A8 3C 2C 58 2F 3F 35 71]
	 * bytes 0 to 15 are LITTLE ENDIAN.
	 * byte 0 is 00 so pak is not fragmented
	 * byte 1 is 02 so pak is PING and asks for PONG and is not fragmented
	 * bytes 2 and 3 are 1F 00 so pak has a 31 length
	 * bytes 4 to 7 are 01 00 00 00 so pak ID is 1
	 * pak is not fragmented so bytes 8 to 11 are 00 00 00 00
	 * bytes 12 to 15 is an unsigned int : 00 FB 84 5D . It's the seed used to generate random numbers to decrypt following data 
	 * following bytes are encrypted : 54 04 2F DE 9D F3 D5 A8 3C 2C 58 2F 3F 35 71
	 * after we decrypt, we should have for the first 2 bytes : 00 42 (pak type), then an ASCII String for the MOTD ("Bienvenue" : 42 69 65 6E 76 65 6E 75 65) maybe surrounded with formatting bytes and the 2 last bytes are the checksum for decrypted data.
	 * pak type and checksum are BIG ENDIAN.
	 */
	public static void test(){
		System.out.println("[TEST PAK 150 ]");
		Pak150 test = new Pak150();
		ByteBuffer seedbuf = ByteBuffer.allocate(4);
		//byte[] s = new byte[]{0x00, (byte) 0xFB, (byte) 0x84, 0x5D};
		byte[] s = new byte[]{0x00, (byte) 0xEA, (byte) 0x98, 0x00};
		seedbuf.order(ByteOrder.LITTLE_ENDIAN);
		seedbuf.put(s);
		seedbuf.rewind();
		test.seed = seedbuf.getInt();
		System.out.println("SEED : "+HexString.from(seedbuf.array()));
		test.pak = ByteBuffer.allocate(103);
		//test.pak.put(new byte[] {0x54, 0x04, 0x2F, (byte) 0xDE , (byte) 0x9D, (byte) 0xF3, (byte) 0xD5, (byte) 0xA8, 0x3C, 0x2C, 0x58, 0x2F, 0x3F, 0x35, 0x71});
		test.pak.put(new byte[] {0x0C, (byte) 0xFC, (byte) 0xF9, (byte) 0xEC, (byte) 0xEA, 0x3C, 0x33, (byte) 0x8A, 0x48, 0x79, 0x1E, (byte) 0xD7, 0x49, 0x47, (byte) 0x96, (byte) 0xD4, (byte) 0x89, 0x34, (byte) 0xDF, 0x18, 0x3E, 0x52, (byte) 0xA3, 0x22, 0x6C, (byte) 0xEC, 0x25, 0x2E, (byte) 0xE9, (byte) 0xF2, (byte) 0x92, (byte) 0xED, (byte) 0xD5, (byte) 0xD2, 0x54, (byte) 0xE7, 0x02, (byte) 0xC0, 0x17, (byte) 0xA3, 0x39, 0x2A, (byte) 0x8C, 0x4E, (byte) 0xE9, 0x6A, 0x2A, (byte) 0xDA, (byte) 0xC7, 0x13, 0x0E, 0x43, (byte) 0xC7, (byte) 0xC0, 0x3E, 0x54, (byte) 0x99, 0x1A, 0x7D, (byte) 0xE6, (byte) 0xF2, 0x41, (byte) 0xF2, (byte) 0xBF, 0x4F, 0x20, 0x13, (byte) 0xCD, 0x47, (byte) 0x83, (byte) 0xF2, 0x42, 0x53, (byte) 0xF9, 0x71, (byte) 0xE8, (byte) 0x82, (byte) 0xAB, (byte) 0xB9, 0x16, (byte) 0x85, (byte) 0xA2, 0x54, 0x20, 0x4E, 0x2E, 0x16, 0x3E, 0x19, 0x52, (byte) 0xBD, 0x1B, (byte) 0xED, 0x70, 0x5A, (byte) 0xC4, 0x66, 0x09, (byte) 0xF2, (byte) 0xB1, 0x6E, 0x6D, (byte) 0x94});
		test.pak.rewind();
		
		test.decrypt_150();
		
		System.out.println("[TEST TYPE "+HexString.from(new byte[]{(byte)(test.type>>8 & 0xFF),(byte)(test.type & 0xFF)})+"]");
		System.out.println("TEST PAK DECRYPTED "+HexString.from(test.pak.array()));
		byte[] d = new byte[test.pak.array().length-6];
		test.pak.rewind();
		test.pak.get();
		test.pak.get();
		test.pak.get();
		test.pak.get();
		test.pak.get(d, 0, test.pak.array().length-6);
		System.out.println("TEST PAK DECRYPTED "+new String(d));
	}
	

	/**
	 * Decrypts 1.50 data
	 * @return
	 */
	public void decrypt_150 (){
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		int index;
		int algo;
		TCryptoTable crypto = new TCryptoTable(pak.array().length);
		// initialize the system's pseudo-random number generator from the seed given in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
		byte b0,b1,b2,b3;
		b0 = (byte) ((seed>>24) & 0xFF);
		b1 = (byte) ((seed>>16) & 0xFF);
		b2 = (byte) ((seed>>8) & 0xFF);
		b3 = (byte) ((seed) & 0xFF);
		int shiftedSeed = ((int)(b3 & 0xFF)<<24) | ((int)(b0 & 0xFF)<<16) | ((int)(b2 & 0xFF)<<8) | (int)(b1 & 0xFF);
		//if(isTest)System.out.println("SHIFTED SEED "+HexString.from(shiftedSeed));
		MSRand srand = new MSRand(shiftedSeed);
		   
		// now generate the crypto tables for the given datagram length
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) srand.prng();
			stack2[index] = (byte) srand.prng();
			//if(isTest)System.out.println("STACK1("+index+") "+HexString.from(stack1[index]));
			//if(isTest)System.out.println("STACK2("+index+") "+HexString.from(stack2[index]));
		}
		// xor table
		for (index = 0 ; index < pak.array().length ; index++)
		{
			crypto.xor[index] = (int) stack2[srand.prng() % 10]&0xFF;
			//if(isTest)System.out.println("TMP("+index+") "+HexString.from(crypto.xor[index]));
			crypto.xor[index] *= (int) stack1[srand.prng() % 10]&0xFF;
			//if(isTest)System.out.println("TMP("+index+") "+HexString.from(crypto.xor[index]));
			crypto.xor[index] += srand.prng();
			//if(isTest)System.out.println("XOR("+index+") "+HexString.from(crypto.xor[index]));
		}
		// offset & algo tables
		for (index = 0; index < pak.array().length; index++){
			crypto.offsets[index] = srand.prng() % pak.array().length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			//if(isTest)System.out.println("OFFSETS("+index+") "+HexString.from(crypto.offsets[index]));
			crypto.algo[index] = srand.prng() % 21;
			//if(isTest)System.out.println("ALGO("+index+") "+HexString.from(crypto.algo[index]));

		}
		// cryptographic tables are generated, now apply the algorithm
		for (index=pak.array().length-1 ; index>=0; index--) {
			algo = crypto.algo[index];
			pak_offset = pak.array()[crypto.offsets[index]];
			pak_index = pak.array()[index];
			//if(isTest)System.out.println("PAKINDEX "+HexString.from(pak_index));
			//if(isTest)System.out.println("PAKOFFSET "+HexString.from(pak_offset));
			if 	  	(algo == 0)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)	;}
		    else if (algo == 1)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))				;}
		    else if (algo == 2)  { pak.array()[index] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0))				;}
		    else if (algo == 3)  { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)		;}
		    else if (algo == 4)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))			;}
		    else if (algo == 5)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))			;}
		    else if (algo == 6)  { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4)& 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F))				;}
		    else if (algo == 7)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | (((pak_offset >>> 4) & 0x0F)))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))			;}
		    else if (algo == 8)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))			;}
		    else if (algo == 9)  { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))			;}
		    else if (algo == 10) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0)| (pak_index & 0x0F))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))			;}
		    else if (algo == 11) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0)| (((pak_index >>> 4) & 0x0F)))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)	;}
		    else if (algo == 12) { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) &0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))				;}
		    else if (algo == 13) { pak.array()[index] = (byte) pak_offset									  ;		pak.array()[crypto.offsets[index]] = (byte) pak_index											;}
		    else if (algo == 14) { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))			;}
		    else if (algo == 15) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset) ; 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)		;}
		    else if (algo == 16) { pak.array()[index] = (byte) pak_offset									  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))				;}
		    else if (algo == 17) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))		  ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))			;}
		    else if (algo == 18) { pak.array()[index] = (byte) (((pak_offset << 4) &0xF0) | (((pak_index >>> 4) & 0x0F)))		  ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))				;}
		    else if (algo == 19) { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0))		  ;		pak.array()[crypto.offsets[index]] = (byte) pak_index											;}
		    else if (algo == 20) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset) ; 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F))				;}
			//if(isTest)System.out.println("["+algo+"]PAK("+index+") "+HexString.from(pak.array()[index]));
			//if(isTest)System.out.println("["+algo+"]PAK("+crypto.offsets[index]+") "+HexString.from(pak.array()[crypto.offsets[index]]));
		}
		// and finally, quadruple-XOR the data out
		for (index=pak.array().length-1 ; index>=0; index--) {
			if (index <= pak.array().length-4) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				//if(isTest)System.out.println("PAXOR("+index+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				//if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
				pak.array()[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				//if(isTest)System.out.println("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2]));
				pak.array()[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
				//if(isTest)System.out.println("PAXOR("+(index+3)+") "+HexString.from(pak.array()[index+3]));
			}
			else if (index == pak.array().length-3) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				//if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				//if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
				pak.array()[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
				//if(isTest)System.out.println("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2]));
			}
			else if (index == pak.array().length-2) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				//if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
				//if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
			}
			else if (index == pak.array().length-1){
				pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
				//if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
			}
		}
		// in the 1.50 protocol, the checksum info is at the trailing end of the pak.
		short checksum = (short) ((((short)pak.array()[pak.array().length - 1])<< 8) & 0xFF00 | (pak.array()[pak.array().length-2]) & 0xFF); // so get it from there...
		pak.rewind();
		valid = checksum_150(checksum);
		//if(isTest)System.out.println("CHECKSUM "+HexString.from(checksum));
		type = pak.getShort();
		//if(isTest)System.out.println("TYPE "+HexString.from(type));
		pak.rewind();
		//if(isTest)System.out.println("PAK "+HexString.from(pak.array()));
	}
	
	/**
	 * checks if we decrypted data correctly
	 * @param checksum
	 * @return
	 */
	private boolean checksum_150 (short checksum)
	{
		// this function computes and returns the pak data's checksum
		int index;
		short sum;
	   	sum = 0; // start at zero

		// for each byte of data...
		for (index = 0; index < pak.array().length-2; index++){
			sum += (~(pak.array()[index]) & 0xFF); // add its inverse value to the checksum
			//if(isTest)System.out.println("CHECK("+index+") "+HexString.from(sum));
		}
		if(sum == checksum)return true;
		//System.err.println("BAD CHECKSUM : "+HexString.from(sum)+"!= "+HexString.from(checksum));
		return false;
	}

	/**
	 * Makes a ByteBuffer with proper header and data to be sent
	 * @return
	 */
	public ByteBuffer getSendData() {
		ByteBuffer sendData = ByteBuffer.allocate(length);
		sendData.put(header.array());
		if(pak_crypt != null) sendData.put(pak_crypt);
		sendData.rewind();
		return sendData;
	}
}
