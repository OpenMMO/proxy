package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fortytwo.opent4c.tools.ByteArrayToBinaryString;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.MSRand;
import com.fortytwo.opent4c.tools.PakTypes;

public class Pak150 {
	private boolean isTest = false;//only used to print infos while decrypting, for testing purpose
	private boolean isServerToClient = false;//direction of the pak
	//private DatagramPacket packet;//not needed when everything will be working
	private long timeStamp = -1;//System.currentTimeMillis() just after we received the pak
	private long micros = -1;//((System.nanoTime()-Proxy.startTime)%1000000)/1000 just after we received the pak
	//private ByteBuffer header = ByteBuffer.allocate(16);//will be filled with bytes 0 to 15 of received data
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

	public static void test(){
		Pak150 p = new Pak150(PakTypes.PAK_CLIENT_MessageOfTheDay, false, "");
		ByteBuffer header = p.encodeHeader();
		p.decodeHeader(header);
		p.print();
	}
	
	/**
	 * T4C 1.50 pak, created
	 * @param type
	 * @param isServerToClient
	 * @param data
	 */
	public Pak150(short type, boolean isServerToClient, String data){
		this.isServerToClient = isServerToClient;
		this.type = type;
		timeStamp = System.currentTimeMillis();
		micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
		pak = ByteBuffer.allocate(data.length()+4);
		pak.putShort(type);
		pak.put(data.getBytes());
		pak.putShort(createChecksum_150());
		seed = encrypt_150();
	}
	
	
	/**
	 * T4C 1.50 pak, received encrypted
	 * @param pack
	 * @param direction : true = server->client
	 * @param stamp
	 * @param microseconds
	 */
	public Pak150(DatagramPacket pack, boolean direction, long stamp, long microseconds) {
		//isTest = true;//as long as we do live tests, enables verbose mode
		timeStamp = stamp;
		micros = microseconds;
		isServerToClient = direction;
		ByteBuffer header = ByteBuffer.allocate(16);
		//packet = pack;
		header .put(pack.getData(),0,16);
		header.order(ByteOrder.LITTLE_ENDIAN);
		header.rewind();
		decodeHeader(header);
		pak = ByteBuffer.allocate(length-16);
		pak.put(pack.getData(), 16, length-16);
		pak.rewind();
		pak_crypt = pak.array().clone();
		if(isTest)System.out.println("SOURCE "+HexString.from(pack.getData()));
		if(!isFragment) {
			if(!isPong){
				valid = decrypt_150();
			}
		}else{
			System.err.println("FRAGMENT : "+isFragment);
			//TODO manage fragments
		}
		print();
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
		if(isPong){
			printPong();
		}else if(isFragment){
			printFragment();
		}else{
			StringBuilder sb = new StringBuilder();

			sb.append("********************************************************************************************************************************");
			sb.append(System.lineSeparator());
			sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
			sb.append(System.lineSeparator());
			if(isPing){
				sb.append("PING ");
				sb.append(datagramID);
				sb.append(System.lineSeparator());
			}
			sb.append("TYPE ");
			sb.append(HexString.from(type));
			sb.append(System.lineSeparator());
			sb.append(PakTypes.getTypeInfos(isServerToClient, pak));
			sb.append("********************************************************************************************************************************");
			sb.append(System.lineSeparator());

			System.out.println(sb.toString());
		}
	}
	
	private void printPong() {
		StringBuilder sb = new StringBuilder();

		sb.append("********************************************************************************************************************************");
		sb.append(System.lineSeparator());
		sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
		sb.append(System.lineSeparator());
		sb.append("PONG ");
		sb.append(datagramID);
		sb.append(System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		sb.append(System.lineSeparator());

		System.out.println(sb.toString());		
	}


	private void printFragment() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("********************************************************************************************************************************");
		sb.append(System.lineSeparator());
		sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
		sb.append(System.lineSeparator());
		sb.append("FRAGMENT");
		sb.append(System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		
		System.out.println(sb.toString());		
	}


	/**
	 * Decrypts 1.50 data
	 * @return
	 */
	private boolean decrypt_150 (){
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
		if(isTest)System.out.println("SHIFTED SEED "+HexString.from(shiftedSeed));
		MSRand srand = new MSRand(shiftedSeed);
		   
		// now generate the crypto tables for the given datagram length
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) srand.prng();
			stack2[index] = (byte) srand.prng();
			if(isTest)System.out.println("STACK1("+index+") "+HexString.from(stack1[index]));
			if(isTest)System.out.println("STACK2("+index+") "+HexString.from(stack2[index]));
		}
		// xor table
		for (index = 0 ; index < pak.array().length ; index++)
		{
			crypto.xor[index] = (int) stack2[srand.prng() % 10]&0xFF;
			if(isTest)System.out.println("TMP("+index+") "+HexString.from(crypto.xor[index]));
			crypto.xor[index] *= (int) stack1[srand.prng() % 10]&0xFF;
			if(isTest)System.out.println("TMP("+index+") "+HexString.from(crypto.xor[index]));
			crypto.xor[index] += srand.prng();
			if(isTest)System.out.println("XOR("+index+") "+HexString.from(crypto.xor[index]));
		}
		// offset & algo tables
		for (index = 0; index < pak.array().length; index++){
			crypto.offsets[index] = srand.prng() % pak.array().length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			if(isTest)System.out.println("OFFSETS("+index+") "+HexString.from(crypto.offsets[index]));
			crypto.algo[index] = srand.prng() % 21;
			if(isTest)System.out.println("ALGO("+index+") "+HexString.from(crypto.algo[index]));

		}
		// cryptographic tables are generated, now apply the algorithm
		for (index=pak.array().length-1 ; index>=0; index--) {
			algo = crypto.algo[index];
			pak_offset = pak.array()[crypto.offsets[index]];
			pak_index = pak.array()[index];
			if(isTest)System.out.println("PAKINDEX "+HexString.from(pak_index));
			if(isTest)System.out.println("PAKOFFSET "+HexString.from(pak_offset));
			if 	  	(algo == 0)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			 ; 		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)		  ;}
		    else if (algo == 1)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			 ; 		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;}
		    else if (algo == 2)  { pak.array()[index] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))	 ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0));}
		    else if (algo == 3)  { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			  ;}
		    else if (algo == 4)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		 ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))		  ;}
		    else if (algo == 5)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		 ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))		  ;}
		    else if (algo == 6)  { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4)& 0xF0))	 ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F)) ;}
		    else if (algo == 7)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | (((pak_offset >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;}
		    else if (algo == 8)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		 ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		  ;}
		    else if (algo == 9)  { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;}
		    else if (algo == 10) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0)| (pak_index & 0x0F))		  	 ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))		  ;}
		    else if (algo == 11) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0)| (((pak_index >>> 4) & 0x0F)));		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)		  ;}
		    else if (algo == 12) { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) &0xF0)) ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))  ;}
		    else if (algo == 13) { pak.array()[index] = (byte) pak_offset									  			 ;		pak.array()[crypto.offsets[index]] = (byte) pak_index												  ;}
		    else if (algo == 14) { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))		  ;}
		    else if (algo == 15) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset) 			 ; 		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			  ;}
		    else if (algo == 16) { pak.array()[index] = (byte) pak_offset									  			 ;		pak.array()[crypto.offsets[index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))  ;}
		    else if (algo == 17) { pak.array()[index] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))         ;		pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		  ;}
		    else if (algo == 18) { pak.array()[index] = (byte) (((pak_offset << 4) &0xF0) | (((pak_index >>> 4) & 0x0F)));		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;}
		    else if (algo == 19) { pak.array()[index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0));		pak.array()[crypto.offsets[index]] = (byte) pak_index												  ;}
		    else if (algo == 20) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)			 ; 		pak.array()[crypto.offsets[index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F)) ;}
			
			if(isTest)System.out.println("["+algo+"]PAK("+index+") "+HexString.from(pak.array()[index]));
			if(isTest)System.out.println("["+algo+"]PAK("+crypto.offsets[index]+") "+HexString.from(pak.array()[crypto.offsets[index]]));
		}
		// and finally, quadruple-XOR the data out
		for (index=pak.array().length-1 ; index>=0; index--) {
			if (index <= pak.array().length-4) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				if(isTest)System.out.println("PAXOR("+index+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
				pak.array()[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				if(isTest)System.out.println("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2]));
				pak.array()[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
				if(isTest)System.out.println("PAXOR("+(index+3)+") "+HexString.from(pak.array()[index+3]));
			}
			else if (index == pak.array().length-3) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
				pak.array()[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
				if(isTest)System.out.println("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2]));
			}
			else if (index == pak.array().length-2) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
				pak.array()[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
				if(isTest)System.out.println("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1]));
			}
			else if (index == pak.array().length-1){
				pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
				if(isTest)System.out.println("PAXOR("+(index)+") "+HexString.from(pak.array()[index]));
			}
		}
		// in the 1.50 protocol, the checksum info is at the trailing end of the pak.
		pak.position(pak.array().length-2);
		short checksum = pak.getShort();
		//short checksum = (short) ((((short)pak.array()[pak.array().length - 1])<< 8) & 0xFF00 | (pak.array()[pak.array().length-2]) & 0xFF); // so get it from there...
		pak.rewind();
		valid = checksum_150(checksum);
		if(isTest)System.out.println("CHECKSUM "+HexString.from(checksum));
		type = pak.getShort();
		if(isTest)System.out.println("TYPE "+HexString.from(type));
		pak.rewind();
		if(isTest)System.out.println("PAK "+HexString.from(pak.array()));
		return valid;
	}
	
	private int encrypt_150 (){
		// the 1.50 protocol cryptography uses extensively the standarlongd C random number generator,
		// which is a VERY BAD idea, since its implementation may differ from system to system !!!
		byte[] reverse_tree = new byte[]{0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};
		int seed;
		int return_seed;
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		//byte pak_offset;
		//byte pak_index;
		int index;
		int algo;
		TCryptoTable crypto = new TCryptoTable(pak.array().length);
		
		seed = MSRand.lrand(); // generate a random seed for this pak's encryption
		// initialize the system's pseudo-random number generator from the seed used in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
		MSRand rnd = new MSRand(seed);

		// write the seed in the pak
		//((unsigned char *) &return_seed)[0] = (unsigned char) (seed >> 24);
		//((unsigned char *) &return_seed)[3] = (unsigned char) (seed >> 16);
		//((unsigned char *) &return_seed)[1] = (unsigned char) (seed >> 8);
		//((unsigned char *) &return_seed)[2] = (unsigned char) (seed >> 0);
		byte b0,b1,b2,b3;
		b0 = (byte) ((seed>>24) & 0xFF);
		b1 = (byte) ((seed>>16) & 0xFF);
		b2 = (byte) ((seed>>8) & 0xFF);
		b3 = (byte) ((seed) & 0xFF);
		return_seed = ((int)(b3 & 0xFF)<<24) | ((int)(b0 & 0xFF)<<16) | ((int)(b2 & 0xFF)<<8) | (int)(b1 & 0xFF);

		
		// if new data size requires us to allocate more pages for pak data, do it
		//if ((pak->data_size + 2) / 1024 + 1 > pak->pages_count){
		//	pak->data = (char *) SAFE_realloc (pak->data, pak->pages_count, (pak->data_size + 2) / 1024 + 1, 1024, false);
		//	pak->pages_count = (pak->data_size + 2) / 1024 + 1; // new number of pages
		//}

		// compute the pak checksum and append this information to the data
		//short checksum = createChecksum_150();
		/*(unsigned short *) &pak->data[pak->data_size] = Pak_ComputeChecksum_150 (pak);
		pak->data_size += 2; // correct pak data size*/

		// now generate the crypto tables for the given datagram length

		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) rnd.prng();
			stack2[index] = (byte) rnd.prng();
		}

		// xor table
		for (index = 0; index < pak.array().length; index++){
			crypto.xor[index] = (byte) stack2[rnd.prng() % 10];
			crypto.xor[index] *= (byte) stack1[rnd.prng() % 10];
			crypto.xor[index] += rnd.prng();
		}

		// offset & algo tables
		for (index = 0; index < pak.array().length; index++){
			crypto.offsets[index] = rnd.prng() % pak.array().length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			crypto.algo[index] = rnd.prng() % 21;
		}

		// cryptographic tables are generated, now quadruple-XOR the data out
		for (index = pak.array().length-1; index >= 0; index--){
			if (index <= pak.array().length - 4){
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				pak.array()[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				pak.array()[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
			}
			else if (index == pak.array().length - 3){
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				pak.array()[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
			}
			else if (index == pak.array().length - 2){
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
			}
			else if (index == pak.array().length - 1) pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
		}

		// and finally, apply the algorithm
		for (index = pak.array().length - 1; index >= 0; index--){
			// actually, reverse_tree is a switch table hardcoded by cl compiler (MSVC)
			algo = reverse_tree[crypto.algo[pak.array().length - 1 - index]];
			pak_offset = pak.array()[crypto.offsets[pak.array().length - 1 - index]];
			pak_index = pak.array()[pak.array().length - 1 - index];
			if(isTest)System.out.println("PAKINDEX "+HexString.from(pak_index));
			if(isTest)System.out.println("PAKOFFSET "+HexString.from(pak_offset));
			if 	  	(algo == 0)  { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			 ; 		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)		  ;}
		    else if (algo == 1)  { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			 ; 		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;}
		    else if (algo == 2)  { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))	 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0));}
		    else if (algo == 3)  { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			  ;}
		    else if (algo == 4)  { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))		  ;}
		    else if (algo == 5)  { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))		  ;}
		    else if (algo == 6)  { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4)& 0xF0))	 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F)) ;}
		    else if (algo == 7)  { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_index & 0xF0) | (((pak_offset >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;}
		    else if (algo == 8)  { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		  ;}
		    else if (algo == 9)  { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_offset & 0x0F) | ((pak_index << 4) & 0xF0))		  ;}
		    else if (algo == 10) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset << 4) & 0xF0)| (pak_index & 0x0F))		  	 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_offset & 0xF0) | ((pak_index >>> 4) & 0x0F))		  ;}
		    else if (algo == 11) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset << 4) & 0xF0)| (((pak_index >>> 4) & 0x0F)));		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset)		  ;}
		    else if (algo == 12) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) &0xF0)) ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))  ;}
		    else if (algo == 13) { pak.array()[pak.array().length - 1 - index] = (byte) pak_offset									  			 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) pak_index												  ;}
		    else if (algo == 14) { pak.array()[pak.array().length - 1 - index] = (byte) ((pak_offset & 0xF0) | (((pak_index >>> 4) & 0x0F)))		 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))		  ;}
		    else if (algo == 15) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset) 			 ; 		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index)			  ;}
		    else if (algo == 16) { pak.array()[pak.array().length - 1 - index] = (byte) pak_offset									  			 ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_index >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0))  ;}
		    else if (algo == 17) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset << 4) & 0xF0) | (pak_index & 0x0F))         ;		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) ((pak_index & 0xF0) | ((pak_offset >>> 4) & 0x0F))		  ;}
		    else if (algo == 18) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset << 4) &0xF0) | (((pak_index >>> 4) & 0x0F)));		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_index << 4) & 0xF0)) ;}
		    else if (algo == 19) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset >>> 4) & 0x0F) | ((pak_offset << 4) & 0xF0));		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) pak_index												  ;}
		    else if (algo == 20) { pak.array()[pak.array().length - 1 - index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)			 ; 		pak.array()[crypto.offsets[pak.array().length - 1 - index]] = (byte) (((pak_offset << 4) & 0xF0) | ((pak_index >>> 4) & 0x0F)) ;}
			
			if(isTest)System.out.println("["+algo+"]PAK("+index+") "+HexString.from(pak.array()[index]));
			if(isTest)System.out.println("["+algo+"]PAK("+crypto.offsets[index]+") "+HexString.from(pak.array()[crypto.offsets[index]]));
		}
		return (return_seed); // finished, pak is encrypted
	}
	
	/**
	 * computes checksum for a pak
	 * @return
	 */
	private short createChecksum_150() {
		// this function computes and returns the pak data's checksum
		int index;
		short sum;
	   	sum = 0; // start at zero

		// for each byte of data...
		for (index = 0; index < pak.array().length-2; index++){
			sum += (~(pak.array()[index]) & 0xFF); // add its inverse value to the checksum
			//if(isTest)System.out.println("CHECK("+index+") "+HexString.from(sum));
		}
		return sum;
	}


	/**
	 * checks if we decrypted data correctly
	 * @param checksum
	 * @return
	 */
	private boolean checksum_150 (short checksum){
		short sum = createChecksum_150();
		if(sum == checksum)return true;
		System.err.println("BAD CHECKSUM : "+HexString.from(sum)+"!= "+HexString.from(checksum));
		return false;
	}
	
	
	/**
	 * Makes a ByteBuffer with proper header and data to be sent
	 * @return
	 */
	public ByteBuffer getSendData() {
		ByteBuffer sendData = ByteBuffer.allocate(length);
		sendData.put(encodeHeader());
		if(pak_crypt != null) sendData.put(pak_crypt);
		sendData.rewind();
		return sendData;
	}


	private ByteBuffer encodeHeader() {
		ByteBuffer result = ByteBuffer.allocate(16);
		result.order(ByteOrder.LITTLE_ENDIAN);
		result.put(fragmentID);
		result.put(createBitMask());
		result.putShort(length);
		result.putInt(datagramID);
		result.putInt(firstPakID);
		result.putInt(seed);
		result.rewind();
		return result;
	}


	private byte createBitMask() {
		byte bitmask = 0;
		if (isPong) bitmask += 1;
		if (isPing) bitmask += 2;
		if (isFragment) bitmask += 4;
		return bitmask;
	}
}
