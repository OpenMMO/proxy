package com.fortytwo.opent4c.proxy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fortytwo.opent4c.tools.ByteArrayToBinaryString;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.Log;
import com.fortytwo.opent4c.tools.MSRand;
import com.fortytwo.opent4c.tools.PakTypes;

/**
 * This class manages all messages from v1.50 protocol. It can decrypt or encrypt any message.
 * Messages formats are given by PakTypes.
 * @author syno
 *
 */
public class Pak150 {
	public static final boolean CLIENT_TO_SERVER = false;
	public static final boolean SERVER_TO_CLIENT = true;
	public static final byte[] EMPTY = new byte[]{};
	
	private boolean isServerToClient = false;//direction of the pak
	private long timeStamp;//System.currentTimeMillis() just after we received/created the pak
	private long micros;//((System.nanoTime()-Proxy.startTime)%1000000)/1000 just after we received/created the pak
	private byte fragmentID;//byte 0 of header, == 0 for non fragmented paks
	private boolean isLastFragment = false;//if fragmentID != 0, this pak is the last of a fragmented message
	//byte 1 is a bitmask, it sets these 3 booleans:
	private boolean isPong = false;//if pak is a pong message
	private boolean isPing = false;//if pak is needs a pong in return
	private boolean isFragment = false;//if the pak is part of a fragmented message
	private short length;//byte 2 and 3 of header. == 0 for pong paks so it has to be manually set to 16, otherwise == pak length.
	private int datagramID;//bytes 4 to 7 of header. for pong paks, tells the id of the corresponding ping pak, otherwise it the datagram's ID.
	private int firstPakID;//bytes 8 to 11 of header. tells the id of the first pak of a fragmented message, otherwise == 0
	private boolean isFirstFragment = false;//if datagramID == firstPakID, this pak is the first of a fragmented message, otherwise == 0
	private int seed;//bytes 12 to 15 of header, it's the seed for decrypting the data of this pak.
	private ByteBuffer pak = null;//bytes 16 and following. it's first filled with crypted data and then with decrypted.
	//private byte[] pak_crypt = null;//original data, not need when everyhing will be working
	private short type;//byte 0 and 1 of decrypted data, we get it from pak after it's been decrypted.
	private short checksum;//last 2 bytes of pak. Checksum of pak, decrypted, a pak with wrong checksum has to be refused.
	private boolean valid = false;//set to true if the checksum is valid after we've decrypted the data.
	
	/**
	 * T4C 1.50 test pak, encrypted, decrypted, and compared.
	 */
	public Pak150() {
		Log.proxy.debug("TEST SOURCE "+HexString.from(PakTypes.PAK_CLIENT_MessageOfTheDay));
		this.isServerToClient = Pak150.CLIENT_TO_SERVER;
		this.type = PakTypes.PAK_CLIENT_MessageOfTheDay;
		this.isPing = false;
		this.isPong = false;
		this.isFragment = false;
		this.pak = ByteBuffer.allocate(4);//Test msg is null so length is for type and checksum so 4
		this.pak.putShort(type);
		this.seed = encrypt_150();
		//From now on data is encrypted, only need to make a proper header around
		byte[] header = encodeHeader().array().clone();
		byte[] encrypted_data = pak.array().clone();
		this.pak = ByteBuffer.allocate(header.length+encrypted_data.length);
		this.pak.put(header);
		this.pak.put(encrypted_data);
		Log.proxy.debug("TEST ENCRYPTED "+HexString.from(encrypted_data));
		//Now we try to get it back
		this.type = 0;
		ByteBuffer head = ByteBuffer.allocate(16);
		head.put(this.pak.array(),0,16);
		head.order(ByteOrder.LITTLE_ENDIAN);
		head.rewind();
		decodeHeader(head);
		this.pak = ByteBuffer.allocate(length-16);
		this.pak.put(encrypted_data, 0, length-16);
		this.pak.rewind();
		valid = decrypt_150();
		Log.proxy.debug("TEST DECRYPTED "+HexString.from(this.type)+HexString.from(this.checksum));

	}
	
	/**
	 * T4C 1.50 pak, created and being encrypted here
	 * @param type choosen from Paktypes
	 * @param direction can be one of Pak150.CLIENT_TO_SERVER or Pak150.SERVER_TO_CLIENT
	 * @param msg
	 * @param  
	 * @param firstPakID 
	 * @param datagramID 
	 * @param fragmentID2 
	 */
	public Pak150(short typ, boolean direction, byte[] msg, byte fragmentID, boolean isPing, boolean isPong, boolean isFragment, int datagramID, int firstPakID){
		this.timeStamp = System.currentTimeMillis();
		this.micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
		this.isServerToClient = direction;
		if(isServerToClient)Log.server.debug("SOURCE "+HexString.from(msg));
		else Log.client.debug("SOURCE "+HexString.from(msg));
		this.type = typ;
		this.fragmentID = fragmentID;
		this.isPing = isPing;
		this.isPong = isPong;
		this.isFragment = isFragment;
		this.datagramID = datagramID;
		this.firstPakID = firstPakID;
		this.pak = ByteBuffer.allocate(msg.length+4);
		this.pak.putShort(type);
		this.pak.put(msg);
		this.seed = encrypt_150();
		byte[] encrypted_data = pak.array().clone();
		this.pak = ByteBuffer.allocate(encrypted_data.length);
		this.pak.put(encrypted_data);
	}
	
	
	/**
	 * T4C 1.50 pak, received encrypted.
	 * @param data
	 * @param direction can be one of Pak150.CLIENT_TO_SERVER or Pak150.SERVER_TO_CLIENT
	 * @param stamp
	 * @param microseconds
	 */
	public Pak150(byte[] data, boolean direction, long stamp, long microseconds) {
		this.isServerToClient = direction;
		if(isServerToClient)Log.server.info("SOURCE "+HexString.from(data));
		else Log.client.info("SOURCE "+HexString.from(data));
		this.timeStamp = stamp;
		this.micros = microseconds;
		ByteBuffer header = null;
		header = ByteBuffer.allocate(16);
		header.put(data,0,16);
		header.order(ByteOrder.LITTLE_ENDIAN);
		header.rewind();
		decodeHeader(header);
		if(isPong){
			//Log.proxy.info("PONG : "+isPong);
		}else{
			this.pak = ByteBuffer.allocate(length-16);
			this.pak.put(data, 16, length-16);
			this.pak.rewind();
			if(!isFragment) {
				valid = decrypt_150();
			}else{
				Log.proxy.error("FRAGMENT : "+isFragment);
				//TODO manage fragments
			}
		}
	}

	/**
	 * decodes datagram header
	 * @param header
	 */
	private void decodeHeader(ByteBuffer header) {
		this.fragmentID = header.get();
		byte bitMask = header.get();
		this.length = header.getShort();
		this.isPong = isPong(bitMask);
		if(this.isPong) {
			this.length = 16;
			this.datagramID = header.getInt();
			this.firstPakID = header.getInt();
		}else{
			this.isPing = isPing(bitMask);
			this.isFragment = isFragment(bitMask);
			this.datagramID = header.getInt();
			this.firstPakID = header.getInt();
			if((this.datagramID == this.firstPakID) && (this.firstPakID != 0)) this.isFirstFragment = true;
			if(this.fragmentID != 0) this.isLastFragment = true;
			this.seed = header.getInt();
		}
		printHeader();

	}

	/**
	 * prints header information about a pak
	 */
	private void printHeader() {
		StringBuilder sb = new StringBuilder();

		sb.append("PAK HEADER INFOS :"+System.lineSeparator());
		sb.append("--------------------------------------------------------------------------------------------------"+System.lineSeparator());
		sb.append("FRAGMENT ID "+fragmentID+System.lineSeparator());
		sb.append("LENGTH "+length+System.lineSeparator());
		sb.append("IS PONG "+isPong+System.lineSeparator());
		sb.append("DATAGRAM ID "+datagramID+System.lineSeparator());
		sb.append("IS PING "+isPing+System.lineSeparator());
		sb.append("IS FRAGMENT "+isFragment+System.lineSeparator());
		sb.append("FIRST PAK ID "+firstPakID+System.lineSeparator());
		sb.append("IS FIRST FRAGMENT "+isFirstFragment+System.lineSeparator());
		sb.append("IS LAST FRAGMENT "+isLastFragment+System.lineSeparator());
		sb.append("SEED "+HexString.from(seed)+System.lineSeparator());
		sb.append("--------------------------------------------------------------------------------------------------");
		
		if(isServerToClient)Log.server.debug(sb.toString());
		else Log.client.debug(sb.toString());
	}


	/**
	 * Tells if this pak is part of a fragmented pak
	 * @param bitMask
	 * @return
	 */
	private boolean isFragment(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(5) == '1')return true;
		return false;
	}

	/**
	 * Tells if this pak contains a ping instruction and therefore needs a pong answer.
	 * @param bitMask
	 * @return
	 */
	private boolean isPing(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(6) == '1')return true;
		return false;
	}

	/**
	 * Tells if this pak contains a ping instruction and therefore needs a pong answer.
	 * @return
	 */
	public boolean isPing() {
		return this.isPing;
	}
	
	/**
	 * Tells if this pak is a pong answer.
	 * @param bitMask
	 * @return
	 */
	private boolean isPong(byte bitMask) {
		if(ByteArrayToBinaryString.toBinary(bitMask).charAt(7) == '1')return true;
		return false;
	}
	
	/**
	 * prints relevant infos about a pak
	 * @param data 
	 */
	public void print(byte[] data) {
		if(this.isPong){
			printPong(data);
		}else if(this.isFragment){
			printFragment(data);
		}else{
			StringBuilder sb = new StringBuilder();

			sb.append("PAK INFOS "+HexString.from(data)+System.lineSeparator());
			sb.append("********************************************************************************************************************************");
			sb.append(System.lineSeparator());
			sb.append(CalendarUtils.getTimeStringFromLongMillis(this.timeStamp,this.micros));
			sb.append(System.lineSeparator());
			if(this.isPing){
				sb.append("PING ");
				sb.append(this.datagramID);
				sb.append(System.lineSeparator());
			}
			sb.append("TYPE ");
			sb.append(HexString.from(this.type));
			sb.append(System.lineSeparator());
			sb.append(PakTypes.getTypeInfos(this.type, this.isServerToClient, this.pak));
			sb.append("********************************************************************************************************************************");

			if(isServerToClient)Log.server.info(sb.toString());
			else Log.client.info(sb.toString());
		}
	}
	
	private void printPong(byte[] data) {
		StringBuilder sb = new StringBuilder();

		sb.append("PAK INFOS "+HexString.from(data)+System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		sb.append(System.lineSeparator());
		sb.append(CalendarUtils.getTimeStringFromLongMillis(this.timeStamp,this.micros));
		sb.append(System.lineSeparator());
		sb.append("PONG ");
		sb.append(this.datagramID);
		sb.append(System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		if(isServerToClient)Log.server.info(sb.toString());
		else Log.client.info(sb.toString());		
	}

	private void printFragment(byte[] data) {
		StringBuilder sb = new StringBuilder();

		sb.append("PAK INFOS "+HexString.from(data)+System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		sb.append(System.lineSeparator());
		sb.append(CalendarUtils.getTimeStringFromLongMillis(this.timeStamp,this.micros));
		sb.append(System.lineSeparator());
		sb.append("FRAGMENT");
		sb.append(System.lineSeparator());
		sb.append("********************************************************************************************************************************");
		
		if(isServerToClient)Log.server.info(sb.toString());
		else Log.client.info(sb.toString());	
	}


	/**
	 * Decrypts 1.50 data
	 * @return
	 */
	private boolean decrypt_150 (){
		StringBuilder decrypt_log = new StringBuilder();
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		int index;
		int algo;
		decrypt_log.append("DECRYPT 150 INFOS"+System.lineSeparator());
		TCryptoTable crypto = new TCryptoTable(pak.array().length);
		
		// initialize the system's pseudo-random number generator from the seed given in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineers)
		byte b0,b1,b2,b3;
		b0 = (byte) ((seed>>24) & 0xFF);
		b1 = (byte) ((seed>>16) & 0xFF);
		b2 = (byte) ((seed>>8) & 0xFF);
		b3 = (byte) ((seed) & 0xFF);
		int shiftedSeed = ((int)(b3 & 0xFF)<<24) | ((int)(b0 & 0xFF)<<16) | ((int)(b2 & 0xFF)<<8) | (int)(b1 & 0xFF);
		decrypt_log.append("SHIFTED SEED "+HexString.from(shiftedSeed)+System.lineSeparator());
		MSRand srand = new MSRand(shiftedSeed);
		   
		// now generate the crypto tables for the given datagram length
		
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) srand.prng();
			decrypt_log.append("STACK1("+index+") "+HexString.from(stack1[index])+System.lineSeparator());
			stack2[index] = (byte) srand.prng();
			decrypt_log.append("STACK2("+index+") "+HexString.from(stack2[index])+System.lineSeparator());
		}
		
		// xor table
		for (index = 0 ; index < pak.array().length ; index++)
		{
			crypto.xor[index] = (int) stack2[srand.prng() % 10]&0xFF;
			decrypt_log.append("TMP("+index+") "+HexString.from(crypto.xor[index])+System.lineSeparator());
			crypto.xor[index] *= (int) stack1[srand.prng() % 10]&0xFF;
			decrypt_log.append("TMP("+index+") "+HexString.from(crypto.xor[index])+System.lineSeparator());
			crypto.xor[index] += srand.prng();
			decrypt_log.append("XOR("+index+") "+HexString.from(crypto.xor[index])+System.lineSeparator());
		}
		
		// offset & algo tables
		for (index = 0; index < pak.array().length; index++){
			crypto.offsets[index] = srand.prng() % pak.array().length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			decrypt_log.append("OFFSETS("+index+") "+HexString.from(crypto.offsets[index])+System.lineSeparator());
			crypto.algo[index] = srand.prng() % 21;
			decrypt_log.append("ALGO("+index+") "+HexString.from(crypto.algo[index])+System.lineSeparator());

		}
		
		// cryptographic tables are generated, now apply the algorithm
		for (index=pak.array().length-1 ; index>=0; index--) {
			algo = crypto.algo[index];
			pak_offset = pak.array()[crypto.offsets[index]];
			pak_index = pak.array()[index];
			decrypt_log.append("PAKINDEX "+HexString.from(pak_index)+System.lineSeparator());
			decrypt_log.append("PAKOFFSET "+HexString.from(pak_offset)+System.lineSeparator());
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
			
			decrypt_log.append("["+algo+"]PAK("+index+") "+HexString.from(pak.array()[index])+System.lineSeparator());
			decrypt_log.append("["+algo+"]PAK("+crypto.offsets[index]+") "+HexString.from(pak.array()[crypto.offsets[index]])+System.lineSeparator());
		}
		decrypt_log.append("AFTER ALGO DATA "+HexString.from(pak.array())+System.lineSeparator());
		
		// and finally, quadruple-XOR the data out
		for (index=pak.array().length-1 ; index>=0; index--) {
			if (index <= pak.array().length-4) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				decrypt_log.append("PAXOR("+index+") "+HexString.from(pak.array()[index])+System.lineSeparator());
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				decrypt_log.append("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1])+System.lineSeparator());
				pak.array()[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				decrypt_log.append("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2])+System.lineSeparator());
				pak.array()[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
				decrypt_log.append("PAXOR("+(index+3)+") "+HexString.from(pak.array()[index+3])+System.lineSeparator());
			}
			else if (index == pak.array().length-3) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				decrypt_log.append("PAXOR("+(index)+") "+HexString.from(pak.array()[index])+System.lineSeparator());
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				decrypt_log.append("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1])+System.lineSeparator());
				pak.array()[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
				decrypt_log.append("PAXOR("+(index+2)+") "+HexString.from(pak.array()[index+2])+System.lineSeparator());
			}
			else if (index == pak.array().length-2) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				decrypt_log.append("PAXOR("+(index)+") "+HexString.from(pak.array()[index])+System.lineSeparator());
				pak.array()[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
				decrypt_log.append("PAXOR("+(index+1)+") "+HexString.from(pak.array()[index+1])+System.lineSeparator());
			}
			else if (index == pak.array().length-1){
				pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
				decrypt_log.append("PAXOR("+(index)+") "+HexString.from(pak.array()[index])+System.lineSeparator());
			}
		}
		
		decrypt_log.append("DECRYPTED DATA "+HexString.from(pak.array())+System.lineSeparator());
		// in the 1.50 protocol, the checksum info is at the trailing end of the pak.
		pak.position(pak.capacity()-2);
		checksum = pak.getShort();
		decrypt_log.append("CHECKSUM "+HexString.from(checksum)+System.lineSeparator());
		//type is at the beginning
		pak.rewind();
		type = pak.getShort();
		decrypt_log.append("TYPE "+HexString.from(type)+System.lineSeparator());
		valid = checksum_150(checksum);
		byte[] final_data = new byte[]{};
		if(pak.array().length>4){
			final_data = new byte[pak.array().length-4];
			for (int i = 2 ; i< pak.array().length-2 ; i++){
				final_data[i-2] = pak.array()[i];
			}
		}
		pak = ByteBuffer.allocate(final_data.length);
		pak.put(final_data);
		pak.rewind();
		decrypt_log.append("PAK "+HexString.from(pak.array())+System.lineSeparator());
		decrypt_log.append(PakTypes.getTypeInfos(type, isServerToClient, pak)+System.lineSeparator());
		
		if(isServerToClient)Log.server.trace(decrypt_log.toString());
		else Log.client.trace(decrypt_log.toString());
		return valid;
	}
	
	/**
	 * Encrypts 1.50 data
	 * @return
	 */
	private int encrypt_150 (){
		StringBuilder encrypt_log = new StringBuilder();
		// the 1.50 protocol cryptography uses extensively the standard long C random number generator,
		// which is a VERY BAD idea, since its implementation may differ from system to system !!!
		byte[] reverse_tree = new byte[]{0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};
		int seed;
		int return_seed;
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		int index;
		int algo;
		encrypt_log.append("ENCRYPT 150 INFOS"+System.lineSeparator());
		TCryptoTable crypto = new TCryptoTable(pak.array().length);
		
		seed = MSRand.lrand();
		encrypt_log.append("SEED "+HexString.from(seed)+System.lineSeparator());

		// generate a random seed for this pak's encryption
		// initialize the system's pseudo-random number generator from the seed used in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
		MSRand rnd = new MSRand(seed);

		// write the seed in the pak
		byte b0,b1,b2,b3;
		b0 = (byte) ((seed>>24) & 0xFF);
		b1 = (byte) ((seed>>16) & 0xFF);
		b2 = (byte) ((seed>>8) & 0xFF);
		b3 = (byte) ((seed) & 0xFF);
		return_seed = ((int)(b1 & 0xFF)<<24) | ((int)(b3 & 0xFF)<<16) | ((int)(b2 & 0xFF)<<8) | (int)(b0 & 0xFF);
		encrypt_log.append("SHIFTED SEED "+HexString.from(return_seed)+System.lineSeparator());

		
		// if new data size requires us to allocate more pages for pak data, do it
		//if ((pak->data_size + 2) / 1024 + 1 > pak->pages_count){
		//	pak->data = (char *) SAFE_realloc (pak->data, pak->pages_count, (pak->data_size + 2) / 1024 + 1, 1024, false);
		//	pak->pages_count = (pak->data_size + 2) / 1024 + 1; // new number of pages
		//}

		// compute the pak checksum and append this information to the data
		byte[] dat = new byte[pak.capacity()-2];
		this.pak.rewind();
		this.pak.get(dat);
		this.checksum = createChecksum_150(dat);
		this.pak.putShort(this.checksum);
		// now generate the crypto tables for the given datagram length
		encrypt_log.append("DATA BEFORE ENCRYPTION "+HexString.from(pak.array())+System.lineSeparator());

		
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) rnd.prng();
			stack2[index] = (byte) rnd.prng();
			//Log.proxy.debug("STACK "+HexString.from(stack1[index])+" | "+HexString.from(stack2[index]));
		}

		// xor table
		for (index = 0; index < pak.array().length; index++){
			crypto.xor[index] = (byte) stack2[rnd.prng() % 10] & 0xFF;
			crypto.xor[index] *= (byte) stack1[rnd.prng() % 10] & 0xFF;
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
			else if (index == pak.array().length - 1){
				pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
			}
		}
		encrypt_log.append("BEFORE ALGO DATA "+HexString.from(pak.array())+System.lineSeparator());

		// and finally, apply the algorithm
		for (index = pak.array().length - 1; index >= 0; index--){
			// actually, reverse_tree is a switch table hardcoded by cl compiler (MSVC)
			algo = reverse_tree[crypto.algo[pak.array().length - 1 - index]];
			pak_offset = pak.array()[crypto.offsets[pak.array().length - 1 - index]];
			pak_index = pak.array()[pak.array().length - 1 - index];
			
			encrypt_log.append("PAKINDEX "+HexString.from(pak_index)+System.lineSeparator());
			encrypt_log.append("PAKOFFSET "+HexString.from(pak_offset)+System.lineSeparator());
			
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
			
			encrypt_log.append("["+algo+"]PAK("+index+") "+HexString.from(pak.array()[index])+System.lineSeparator());
			encrypt_log.append("["+algo+"]PAK("+crypto.offsets[index]+") "+HexString.from(pak.array()[crypto.offsets[index]])+System.lineSeparator());
		}
		encrypt_log.append("ENCRYPTED DATA "+HexString.from(pak.array()));
		if(isServerToClient)Log.server.trace(encrypt_log.toString());
		else Log.client.trace(encrypt_log.toString());
		return (return_seed); // finished, pak is encrypted
	}
	
	/**
	 * computes checksum for a pak
	 * @return
	 */
	private short createChecksum_150(byte[] dat) {
		// this function computes and returns the pak data's checksum
		int index;
		short sum;
	   	sum = 0; // start at zero

		// for each byte of data...
		for (index = 0; index < dat.length; index++){
			sum += (~(dat[index]) & 0xFF); // add its inverse value to the checksum
			if(isServerToClient)Log.server.trace("CHECK("+index+") "+HexString.from(sum));
			else Log.client.trace("CHECK("+index+") "+HexString.from(sum));
		}
		return (short) (((sum << 8) & 0xFF00)|((sum >> 8) & 0xFF));
	}


	/**
	 * checks if we decrypted data correctly
	 * @param checksum
	 * @return
	 */
	private boolean checksum_150 (short checksum){
		byte[] dat = new byte[pak.capacity()-2];
		pak.rewind();
		pak.get(dat);
		short sum = createChecksum_150(dat);
		if(sum == checksum)return true;
		Log.proxy.error("BAD CHECKSUM : "+HexString.from(sum)+"!= "+HexString.from(checksum));
		return false;
	}
	
	
	/**
	 * Makes a ByteBuffer with proper header and data to be sent
	 * @return
	 */
	public ByteBuffer getSendData() {
		ByteBuffer sendData = ByteBuffer.allocate(length);
		if (!isPong){
			Pak150 p = new Pak150(type, isServerToClient, pak.array().clone(),fragmentID, isPing, false, isFragment, datagramID, firstPakID); 
			sendData.put(p.encodeHeader());
			sendData.put(p.pak.array().clone());
			sendData.rewind();
		}else{
			sendData.put(encodeHeader().array());
			sendData.rewind();
		}
		print(sendData.array());
		return sendData;
	}


	/**
	 * Creates header bytes, ready to be used in a pak
	 * @return
	 */
	private ByteBuffer encodeHeader() {
		ByteBuffer result = null;
		if (!isPong){
			result = ByteBuffer.allocate(16);
			result.order(ByteOrder.LITTLE_ENDIAN);
			length = (short) (pak.array().length+16);
			result.put(fragmentID);
			result.put(createBitMask());
			result.putShort(length);
			result.putInt(datagramID);
			result.putInt(firstPakID);
			result.putInt(seed);
		}else{
			result = ByteBuffer.allocate(12);
			result.order(ByteOrder.LITTLE_ENDIAN);
			length = 0;
			result.put(fragmentID);
			result.put(createBitMask());
			result.putShort(length);
			result.putInt(datagramID);
			result.putInt(firstPakID);
		}

		result.rewind();
		Log.proxy.trace(HexString.from(result.array()));
		return result;
	}


	/**
	 * Creates a bitmask, to be put inside of a header
	 * @return
	 */
	private byte createBitMask() {
		byte bitmask = 0;
		if (isPong) bitmask += 1;
		if (isPing) bitmask += 2;
		if (isFragment) bitmask += 4;
		return bitmask;
	}

	/**
	 * Get the type of a pak, to be used with PakTypes
	 * @return
	 */
	public short getType() {
		return type;
	}

	/**
	 * Get a pak's data
	 * @return
	 */
	public byte[] getData() {
		return pak.array();
	}

	/**
	 * Get a pak's DatagramID
	 * @return
	 */
	public int getDatagramID() {
		return this.datagramID;
	}

	public boolean isPong() {
		return isPong;
	}

	public boolean isFragment() {
		return isFragment;
	}
}
