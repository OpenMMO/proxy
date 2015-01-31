package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fortytwo.opent4c.tools.ByteArrayToBinaryString;
import com.fortytwo.opent4c.tools.ByteArrayToHexString;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.MSRand;

public class Pak150 {
	private boolean isTest = false;
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
	private short length = 16;//Default 16 is pong length.
	private long datagramID = -1;
	private int seed = 0;
	private ByteBuffer pak = null;
	private byte[] pak_crypt = null;
	private int firstPakID = -1;
	private int pongID = -1;
	private long micros = -1;
	private short checksum = -1;
	private int type = -1;
	private ByteBuffer header = ByteBuffer.allocate(16);

	public Pak150(){
		isTest = true;
	}
	
	
	/**
	 * T4C 1.50 datagram packet
	 * @param pack
	 * @param direction : true = server->client
	 * @param stamp
	 * @param microseconds
	 */
	public Pak150(DatagramPacket pack, boolean direction, long stamp, long microseconds) {
		header.put(pack.getData(),0,16);
		header.order(ByteOrder.LITTLE_ENDIAN);
		header.rewind();
		timeStamp = stamp;
		micros = microseconds;
		isServerToClient = direction;
		packet = pack;
		decodeHeader(header);
		pak = ByteBuffer.allocate(length-16);
		pak.put(packet.getData(), 16, length-16);
		pak.rewind();
		pak_crypt = pak.array().clone();
		if(!isFragment) {
			if(!isPong){
				checksum = decrypt_150();
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
		StringBuilder sb = new StringBuilder();
		sb.append(CalendarUtils.getTimeStringFromLongMillis(timeStamp,micros));
		if(isServerToClient){
			sb.append("[SERVER->CLIENT]");
		}else{
			sb.append("[CLIENT->SERVER]");
		}
		if(isPong){
			sb.append("[PONG "+pongID+"]");
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
		sb.append("[SRC "+ByteArrayToHexString.print(pak_crypt)+"]");
		sb.append("[DATA "+ByteArrayToHexString.print(pak.array())+"]");
		if(isServerToClient){
			sb.append("[TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
		}else{
			sb.append("[TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
		}
		sb.append("[CHK "+checksum+"|"+checksum_150(pak)+"]");
		sb.append("[SRC "+ByteArrayToHexString.print(packet.getData())+"]");
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
		byte[] s = new byte[4];
		s[0] = (byte) 0x5D;
		s[1] = (byte) 0x84;
		s[2] = (byte) 0xFB;
		s[3] = (byte) 0x00;
		seedbuf.order(ByteOrder.LITTLE_ENDIAN);
		seedbuf.put(s);
		seedbuf.rewind();
		test.seed = seedbuf.getInt();
		System.out.println("[SEED BYTEBUFFER : "+ByteArrayToHexString.print(seedbuf.array()));
		System.out.println("[SEED BYTES : "+ByteArrayToHexString.print(new byte[]{(byte)(test.seed>>24 & 0xFF),(byte)(test.seed>>16 & 0xFF),(byte)(test.seed>>8 & 0xFF),(byte)(test.seed & 0xFF)}));
		test.pak = ByteBuffer.allocate(15);
		test.pak.put((byte) 0x54);
		test.pak.put((byte) 0x04);
		test.pak.put((byte) 0x2F);
		test.pak.put((byte) 0xDE);
		test.pak.put((byte) 0x9D);
		test.pak.put((byte) 0xF3);
		test.pak.put((byte) 0xD5);
		test.pak.put((byte) 0xA8);
		test.pak.put((byte) 0x3C);
		test.pak.put((byte) 0x2C);
		test.pak.put((byte) 0x58);
		test.pak.put((byte) 0x2F);
		test.pak.put((byte) 0x3F);
		test.pak.put((byte) 0x35);
		test.pak.put((byte) 0x71);
		test.pak.rewind();
		
		test.decrypt_150();
		
		System.out.println("[TEST TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(test.type>>8 & 0xFF),(byte)(test.type & 0xFF)})+"]");
		System.out.println("TYPE should be 00 42");

	}
	

	/**
	 * Decrypts 1.50 data
	 * @return
	 */
	public short decrypt_150 (){
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		int index;
		int algo;
		short checksum = -1;
		TCryptoTable crypto = new TCryptoTable(pak.array().length);
		// initialize the system's pseudo-random number generator from the seed given in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
		byte b0,b1,b2,b3;
		b0 = (byte) ((seed>>24) & 0xFF);
		b1 = (byte) ((seed>>16) & 0xFF);
		b2 = (byte) ((seed>>8) & 0xFF);
		b3 = (byte) ((seed) & 0xFF);
		int shiftedSeed = ((int)(b0 & 0xFF)<<24) | ((int)(b3 & 0xFF)<<16) | ((int)(b1 & 0xFF)<<8) | (int)(b2 & 0xFF);
		MSRand srand = new MSRand(shiftedSeed);
		   
		// now generate the crypto tables for the given datagram length
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) srand.prng();
			stack2[index] = (byte) srand.prng();
		}
		// xor table
		for (index = 0 ; index < pak.array().length ; index++)
		{
			crypto.xor[index] = (byte) stack2[srand.prng() % 10];
			crypto.xor[index] *= (byte) stack1[srand.prng() % 10];
			crypto.xor[index] += srand.prng();
		}
		// offset & algo tables
		for (index = 0; index < pak.array().length; index++){
			crypto.offsets[index] = srand.prng() % pak.array().length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			crypto.algo[index] = srand.prng() % 21;
		}
		// cryptographic tables are generated, now apply the algorithm
		for (index=pak.array().length-1 ; index>=0; index--) {
			algo = crypto.algo[index];
			pak_offset = pak.array()[crypto.offsets[index]];
			pak_index = pak.array()[index];
			if 	  (algo == 0)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset); }
		      else if (algo == 1)  { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_index << 4)); 			}
		      else if (algo == 2)  { pak.array()[index] = (byte) ((pak_index >> 4) | (pak_index << 4))			;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_offset << 4)); 			}
		      else if (algo == 3)  { pak.array()[index] = (byte) ((pak_offset >> 4) | (pak_index << 4))			;	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	}
		      else if (algo == 4)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | (pak_index << 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4)); 		}
		      else if (algo == 5)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index & 0x0F)); 		}
		      else if (algo == 6)  { pak.array()[index] = (byte) ((pak_offset >> 4) | (pak_index << 4))			;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index >> 4)); 			}
		      else if (algo == 7)  { pak.array()[index] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | (pak_index << 4)); 		}
		      else if (algo == 8)  { pak.array()[index] = (byte) ((pak_offset & 0x0F) | (pak_index << 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4)); 		}
		      else if (algo == 9)  { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | (pak_index << 4)); 		}
		      else if (algo == 10) { pak.array()[index] = (byte) ((pak_offset << 4) | (pak_index & 0x0F))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4)); 		}
		      else if (algo == 11) { pak.array()[index] = (byte) ((pak_offset << 4) | (pak_index >> 4))			;	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset); }
		      else if (algo == 12) { pak.array()[index] = (byte) ((pak_offset >> 4) | (pak_offset << 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_index >> 4) | (pak_index << 4)); 			}
		      else if (algo == 13) { pak.array()[index] = (byte) pak_offset										;	pak.array()[crypto.offsets[index]] = (byte) pak_index; 										}
		      else if (algo == 14) { pak.array()[index] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index & 0x0F)); 		}
		      else if (algo == 15) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)	; 	pak.array()[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	}
		      else if (algo == 16) { pak.array()[index] = (byte) pak_offset										;	pak.array()[crypto.offsets[index]] = (byte) ((pak_index >> 4) | (pak_index << 4)); 			}
		      else if (algo == 17) { pak.array()[index] = (byte) ((pak_offset << 4) | (pak_index & 0x0F))		;	pak.array()[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4)); 		}
		      else if (algo == 18) { pak.array()[index] = (byte) ((pak_offset << 4) | (pak_index >> 4))			;	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_index << 4)); 			}
		      else if (algo == 19) { pak.array()[index] = (byte) ((pak_offset >> 4) | (pak_offset << 4))		;	pak.array()[crypto.offsets[index]] = (byte)pak_index; 										}
		      else if (algo == 20) { pak.array()[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)	; 	pak.array()[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index >> 4)); 			}
		}
		// and finally, quadruple-XOR the data out
		for (index=pak.array().length-1 ; index>=0; index--) {
			if (index <= pak.array().length-4) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				pak.array()[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				pak.array()[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
			}
			else if (index == pak.array().length-3) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				pak.array()[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
			}
			else if (index == pak.array().length-2) {
				pak.array()[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				pak.array()[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
			}
			else if (index == pak.array().length-1){
				pak.array()[index] ^= (crypto.xor[index] & 0xFF); // end of stream
			}
		}
		// in the 1.50 protocol, the checksum info is at the trailing end of the pak.
		checksum = (short) ((pak.array()[pak.array().length - 2]<< 8) | (pak.array()[pak.array().length-1])); // so get it from there...
		pak.rewind();
		type = pak.getShort();
		return checksum;
	}
	
	private short checksum_150 (ByteBuffer decrypted)
	{
	   // this function computes and returns the pak data's checksum
	   int index;
	   short sum;
	   sum = 0; // start at zero

	   // for each byte of data...
	   for (index = 0; index < pak.array().length; index++){
	      sum += (byte) (pak.array()[index]); // add its inverse value to the checksum
	   }
	   return sum; // return the checksum we found
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
	
/* FOLLOWING IS C CODE WICH IS SUPPOSED TO BE OK

signed short Pak_SwitchEncryptionOff_150 (pak_t *pak, unsigned long seed)
{
   // the 1.50 protocol cryptography uses extensively the standard C random number generator,
   // which is a VERY BAD idea, since its implementation may differ from system to system !!!

   char stack1[10];
   char stack2[10];
   unsigned char a;
   unsigned char c;
   char *edi;
   char *ebp;
   int index;
   unsigned int algo;
   signed short checksum;

   // initialize the system's pseudo-random number generator from the seed given in the datagram
   // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
   srand (  (int) (((unsigned char *) &seed)[0] << 24)
          | (int) (((unsigned char *) &seed)[3] << 16)
          | (int) (((unsigned char *) &seed)[1] << 8)
          | (int) (((unsigned char *) &seed)[2]));

   // now generate the crypto tables for the given datagram length

   // stack sequences
   for (index = 0; index < 10; index++)
   {
      stack1[index] = (char) rand ();
      stack2[index] = (char) rand ();
   }

   // xor table
   for (index = 0; index < pak->data_size; index++)
   {
      cryptotables_150.xor[index] = (unsigned char) stack2[rand () % 10];
      cryptotables_150.xor[index] *= (unsigned char) stack1[rand () % 10];
      cryptotables_150.xor[index] += rand ();
   }

   // offset & algo tables
   for (index = 0; index < pak->data_size; index++)
   {
      cryptotables_150.offsets[index] = rand () % pak->data_size;
      if (cryptotables_150.offsets[index] == (unsigned int) index)
         cryptotables_150.offsets[index] = (index == 0 ? 1 : 0);

      cryptotables_150.algo[index] = rand () % 21;
   }

   // cryptographic tables are generated, now apply the algorithm
   for (index = pak->data_size - 1; index >= 0; index--)
   {
      algo = cryptotables_150.algo[index];
      ebp = &pak->data[cryptotables_150.offsets[index]];
      edi = &pak->data[index];

      a = *ebp;
      c = *edi;

      if      (algo == 0)  { *edi = ((a ^ c) & 0x0F) ^ c;  *ebp = ((a ^ c) & 0x0F) ^ a;  }
      else if (algo == 1)  { *edi = ((a ^ c) & 0x0F) ^ c;  *ebp = (a >> 4) | (c << 4);   }
      else if (algo == 2)  { *edi = (c >> 4) | (c << 4);   *ebp = (a >> 4) | (a << 4);   }
      else if (algo == 3)  { *edi = (a >> 4) | (c << 4);   *ebp = ((a ^ c) & 0x0F) ^ c;  }
      else if (algo == 4)  { *edi = (a & 0x0F) | (c << 4); *ebp = (a & 0xF0) | (c >> 4); }
      else if (algo == 5)  { *edi = (c & 0xF0) | (a >> 4); *ebp = (a << 4) | (c & 0x0F); }
      else if (algo == 6)  { *edi = (a >> 4) | (c << 4);   *ebp = (a << 4) | (c >> 4);   }
      else if (algo == 7)  { *edi = (c & 0xF0) | (a >> 4); *ebp = (a & 0x0F) | (c << 4); }
      else if (algo == 8)  { *edi = (a & 0x0F) | (c << 4); *ebp = (c & 0xF0) | (a >> 4); }
      else if (algo == 9)  { *edi = (a & 0xF0) | (c >> 4); *ebp = (a & 0x0F) | (c << 4); }
      else if (algo == 10) { *edi = (a << 4) | (c & 0x0F); *ebp = (a & 0xF0) | (c >> 4); }
      else if (algo == 11) { *edi = (a << 4) | (c >> 4);   *ebp = ((a ^ c) & 0x0F) ^ a;  }
      else if (algo == 12) { *edi = (a >> 4) | (a << 4);   *ebp = (c >> 4) | (c << 4);   }
      else if (algo == 13) { *edi = a;                     *ebp = c;                     }
      else if (algo == 14) { *edi = (a & 0xF0) | (c >> 4); *ebp = (a << 4) | (c & 0x0F); }
      else if (algo == 15) { *edi = ((a ^ c) & 0x0F) ^ a;  *ebp = ((a ^ c) & 0x0F) ^ c;  }
      else if (algo == 16) { *edi = a;                     *ebp = (c >> 4) | (c << 4);   }
      else if (algo == 17) { *edi = (a << 4) | (c & 0x0F); *ebp = (c & 0xF0) | (a >> 4); }
      else if (algo == 18) { *edi = (a << 4) | (c >> 4);   *ebp = (a >> 4) | (c << 4);   }
      else if (algo == 19) { *edi = (a >> 4) | (a << 4);   *ebp = c;                     }
      else if (algo == 20) { *edi = ((a ^ c) & 0x0F) ^ a;  *ebp = (a << 4) | (c >> 4);   }
   }

   // and finally, quadruple-XOR the data out
   for (index = pak->data_size - 1; index >= 0; index--)
   {
      if (index <= pak->data_size - 4)
      {
         pak->data[index + 0] ^= (cryptotables_150.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150.xor[index] & 0x0000FF00) >> 8;
         pak->data[index + 2] ^= (cryptotables_150.xor[index] & 0x00FF0000) >> 16;
         pak->data[index + 3] ^= (cryptotables_150.xor[index] & 0xFF000000) >> 24;
      }
      else if (index == pak->data_size - 3)
      {
         pak->data[index + 0] ^= (cryptotables_150.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150.xor[index] & 0x00FF00) >> 8;
         pak->data[index + 2] ^= (cryptotables_150.xor[index] & 0xFF0000) >> 16;
      }
      else if (index == pak->data_size - 2)
      {
         pak->data[index + 0] ^= (cryptotables_150.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150.xor[index] & 0xFF00) >> 8;
      }
      else if (index == pak->data_size - 1)
         pak->data[index] ^= (cryptotables_150.xor[index] & 0xFF); // end of stream
   }

   // in the 1.50 protocol, the checksum info is at the trailing end of the pak.
   checksum = *(unsigned short *) &pak->data[pak->data_size - 2]; // so get it from there...
   pak->data_size -= 2; // ...and correct the data size

   return (checksum); // finished, pak is decrypted
}

*/

}
