package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fortytwo.opent4c.tools.ByteArrayToBinaryString;
import com.fortytwo.opent4c.tools.ByteArrayToHexString;
import com.fortytwo.opent4c.tools.CalendarUtils;

public class Pak125 {
	private DatagramPacket packet;
	private long timeStamp = -1;
	private byte fragmentID = -1;
	private byte bitMask = -1;
	private boolean isServerToClient = false;
	private boolean isPong = false;
	private boolean isPing = false;
	private boolean isFragment = false;
	private boolean isFirstFragment = false;
	private boolean isLastFragment = false;
	private int length = 16;//Default 16 is pong length.
	private long datagramID = -1;
	private short seed = -1;
	private ByteBuffer pak = null;
	private byte[] pak_crypt = null;
	private long firstPakID = -1;
	private long pongID = -1;
	private long micros = -1;
	private short checksum = -1;
	private int type = -1;
	private ByteBuffer header = ByteBuffer.allocate(16);


	/**
	 * T4C 1.25 datagram packet
	 * @param pack
	 * @param serverToClient
	 * @param stamp
	 * @param microseconds
	 */
	public Pak125(DatagramPacket pack, boolean serverToClient, long stamp, long microseconds) {
		header.put(pack.getData(),0,16);
		header.order(ByteOrder.LITTLE_ENDIAN);
		header.rewind();
		timeStamp = stamp;
		micros = microseconds;
		isServerToClient = serverToClient;
		packet = pack;
		//test();
		decodeHeader(header);
		pak = ByteBuffer.allocate(length-16);
		pak.put(packet.getData(), 16, length-16);
		pak.rewind();
		if(!isFragment) {
			if(!isPong){
				pak_crypt = crypt125(pak.array(), seed);
			}
		}else{
			System.err.println("FRAGMENT : "+ByteArrayToBinaryString.toBinary(bitMask));
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
		bitMask = header.get();
		length = header.getShort();
		isPong = isPong(bitMask);
		if(isPong) {
			length = 16;
			pongID = header.getInt();
		}else{		
			isPing = isPing(bitMask);
			isFragment = isFragment(bitMask);
			datagramID = header.getInt();
			firstPakID = header.getInt();
			if(datagramID == firstPakID) isFirstFragment = true;
			if(fragmentID != 0) isLastFragment = true;
			seed = header.getShort();
			checksum = header.getShort();
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
	 * prints relevant infos about packet.
	 */
	private void print() {
		StringBuilder sb = new StringBuilder();
		sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
		if(isServerToClient){
			sb.append("[SERVER->CLIENT]");
		}else{
			sb.append("[CLIENT->SERVER]");
		}
		if(isPong){
			sb.append("[PONG "+pongID+"]");
			sb.append(ByteArrayToHexString.print(packet.getData()));
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
			sb.append("["+"LENGTH "+length+"]"+"[SEED "+seed+"]"+"[CHECKSUM "+checksum+"]");
			if (checksum != checksum_125(pak_crypt)){
				sb.append("[BAD "+checksum+"|"+checksum_125(pak_crypt)+"]");
			}
		}
		//sb.append("[DATA "+ByteArrayToHexString.print(pak)+"]");
		if(isServerToClient){
			sb.append("[TYPE "+type+"]");
		}else{
			sb.append("[TYPE "+type+"]");
		}
		sb.append("["+ByteArrayToHexString.print(pak.array())+"]");
		//sb.append(ByteArrayToHexString.print(packet.getData()));
		System.out.println(sb.toString());
	}
	
	public void test(){
		short seed = 0;
		byte[] pak = new byte[4];
		pak[0] = (byte) 0xFD;
		pak[1] = (byte) 0xDE;
		pak[2] = (byte) 0x8B;
		pak[3] = (byte) 0x11;
		crypt125(pak, seed);
		System.out.println("[TEST TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
	}
	
	/**
	 * used to crypt and decrypt 1.25 data
	 * @param pak
	 * @param seed
	 */
	private byte[] crypt125(byte[] pak, short seed){
		   // this function toggles Vircom's encryption on/off on the pak data. Note that the checksum
		   // must be computed on the decrypted data, so it has to be done before calling this function.

		   short sd;
		   int index;
		   byte[] result = new byte[pak.length];

		   sd = seed;

		   // for each byte of data...
		   for (index = 0; index < pak.length; index++){
		      sd = (short) (sd * 145 + 1); // apply an infinite suite to seed
		      result[index] ^= (byte) seed; // use it to mask the data (XOR)
		   }
		   
		   type = ((int)(result[1] & 0XFF)<< 8) | (int)(result[0] & 0XFF); 
		   return result; // finished
	}

	private short checksum_125 (byte[] pak){
	   // this function computes and returns the pak data's checksum

	   int index;
	   short sum;

	   sum = 0; // start at zero

	   // for each byte of data...
	   for (index = 0; index < pak.length; index++){
	      sum += pak[index]; // add its value to the checksum
	   }
	   
	   
	   return (sum); // return the checksum we found
	}

	public int getLength() {
		return length;
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
