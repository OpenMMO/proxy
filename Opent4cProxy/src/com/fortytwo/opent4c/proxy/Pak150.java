package com.fortytwo.opent4c.proxy;

import java.net.DatagramPacket;
import com.fortytwo.opent4c.tools.ByteArrayToHexString;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.MSRand;

public class Pak150 {
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
	private byte[] seed = null;
	private byte[] pak = null;
	private byte[] pak_crypt = null;
	private long firstPakID = -1;
	private long pongID = -1;
	private long micros = -1;
	private short checksum = -1;
	private int type = -1;

	/**
	 * T4C 1.50 datagram packet
	 * @param pack
	 * @param serverToClient
	 * @param stamp
	 * @param microseconds
	 */
	public Pak150(DatagramPacket pack, boolean serverToClient, long stamp, long microseconds) {
		timeStamp = stamp;
		micros = microseconds;
		isServerToClient = serverToClient;
		packet = pack;
		test();
		decodeHeader(packet.getData());
		pak = new byte[length-16];
		for(int i=16 ; i<length ; i++){
			pak[i-16] = packet.getData()[i];
		}
		if(!isFragment && !isPong) {
			checksum = decrypt_150(pak, seed);
			//TODO check data
		}else{
			//TODO manage fragments
		}
		print();
	}

	/**
	 * decodes datagram header
	 * @param header
	 */
	private void decodeHeader(byte[] header) {
		bitMask = header[1];
		isPong = isPong(bitMask);
		if(isPong) {
			pongID = ((long)(header[7] & 0xFF)<<24) | ((long)(header[6] & 0xFF)<<16) | ((long)(header[5] & 0xFF)<<8) | (long)(header[4] & 0xFF);
		}else{		
			isPing = isPing(bitMask);
			isFragment = isFragment(bitMask);
			fragmentID = header[0];
			if(datagramID == firstPakID) isFirstFragment = true;
			if(fragmentID != 0) isLastFragment = true;
			length = ((int)(header[3] & 0xFF)<<8) | (int)(header[2]& 0xFF);
			datagramID = ((long)(header[7] & 0xFF)<<24) | ((long)(header[6] & 0xFF)<<16) | ((long)(header[5] & 0xFF)<<8) | (long)(header[4] & 0xFF);
			firstPakID = ((long)(header[11] & 0xFF)<<24) | ((long)(header[10] & 0xFF)<<16) | ((long)(header[9] & 0xFF)<<8) | (long)(header[8] & 0xFF);
			seed = new byte[]{header[12],header[13],header[14],header[15]};
		}
	}

	private boolean isFragment(byte bitMask) {
		if((bitMask & 0x4)!=0)return true;
		return false;
	}

	private boolean isPing(byte bitMask) {
		if((bitMask & 0x2)!=0)return true;
		return false;
	}

	private boolean isPong(byte bitMask) {
		if((bitMask & 0x1)!=0)return true;
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
				sb.append("[FRAGMENT 1ST "+firstPakID+"]"+"[SEED "+ByteArrayToHexString.print(seed)+"]");
			}else if(isLastFragment){
				sb.append("[FRAGMENT LAST "+firstPakID+"]");
			}else{
				sb.append("[FRAGMENT FOR "+firstPakID+"]");
			}
		}else{
			sb.append("["+"LENGTH "+length+"]"+"[SEED "+ByteArrayToHexString.print(seed)+"]");
		}
		sb.append("[DATA "+ByteArrayToHexString.print(pak)+"]");
		if(isServerToClient){
			sb.append("[TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
		}else{
			sb.append("[TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
		}
		System.out.println(sb.toString());
	}
	
	public void test(){
		byte[] seed = new byte[4];
		seed[0] = (byte) 0x00;
		seed[1] = (byte) 0x08;
		seed[2] = (byte) 0x16;
		seed[3] = (byte) 0xA6;
		byte[] pak = new byte[4];
		pak[0] = (byte) 0xFD;
		pak[1] = (byte) 0xDE;
		pak[2] = (byte) 0x8B;
		pak[3] = (byte) 0x11;
		decrypt_150(pak, seed);
		System.out.println("[TEST TYPE "+ByteArrayToHexString.print(new byte[]{(byte)(type>>8 & 0xFF),(byte)(type & 0xFF)})+"]");
	}
	


	public short decrypt_150 (byte[] pak, byte[] seed){
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte pak_offset;
		byte pak_index;
		int index;
		long algo;
		byte[] dat = new byte[pak.length];
		for(int i=0 ; i<dat.length ; i++){
			dat[i] = pak[i];
		}
		pak_crypt = new byte[pak.length-2];
		short checksum = -1;
		TCryptoTable crypto = new TCryptoTable(dat.length);
		// initialize the system's pseudo-random number generator from the seed given in the datagram
		// (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
		long shiftedSeed = ((long)(seed[0] & 0xFF)<<24) | ((long)(seed[3] & 0xFF)<<16) | ((long)(seed[1] & 0xFF)<<8) | (long)(seed[2] & 0xFF);
		MSRand srand = new MSRand(shiftedSeed);
		   
		// now generate the crypto tables for the given datagram length
		// stack sequences
		for (index = 0; index < 10; index++){
			stack1[index] = (byte) srand.prng();
			stack2[index] = (byte) srand.prng();
		}
		// xor table
		for (index = 0 ; index < dat.length ; index++)
		{
			crypto.xor[index] = (byte) stack2[srand.prng() % 10];
			crypto.xor[index] *= (byte) stack1[srand.prng() % 10];
			crypto.xor[index] += srand.prng();
		}
		// offset & algo tables
		for (index = 0; index < dat.length; index++){
			crypto.offsets[index] = srand.prng() % dat.length;
			if (crypto.offsets[index] == index) crypto.offsets[index] = (index == 0 ? 1 : 0);
			crypto.algo[index] = srand.prng() % 21;
		}
		// cryptographic tables are generated, now apply the algorithm
		for (index=dat.length-1 ; index>=0; index--) {
			algo = crypto.algo[index];
			pak_offset = dat[crypto.offsets[index]];
			pak_index = dat[index];
			if 	  (algo == 0)  { dat[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	dat[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset); }
		      else if (algo == 1)  { dat[index] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	dat[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_index << 4)); 			}
		      else if (algo == 2)  { dat[index] = (byte) ((pak_index >> 4) | (pak_index << 4))			;	dat[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_offset << 4)); 			}
		      else if (algo == 3)  { dat[index] = (byte) ((pak_offset >> 4) | (pak_index << 4))			;	dat[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	}
		      else if (algo == 4)  { dat[index] = (byte) ((pak_offset & 0x0F) | (pak_index << 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4)); 		}
		      else if (algo == 5)  { dat[index] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index & 0x0F)); 		}
		      else if (algo == 6)  { dat[index] = (byte) ((pak_offset >> 4) | (pak_index << 4))			;	dat[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index >> 4)); 			}
		      else if (algo == 7)  { dat[index] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | (pak_index << 4)); 		}
		      else if (algo == 8)  { dat[index] = (byte) ((pak_offset & 0x0F) | (pak_index << 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4)); 		}
		      else if (algo == 9)  { dat[index] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset & 0x0F) | (pak_index << 4)); 		}
		      else if (algo == 10) { dat[index] = (byte) ((pak_offset << 4) | (pak_index & 0x0F))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4)); 		}
		      else if (algo == 11) { dat[index] = (byte) ((pak_offset << 4) | (pak_index >> 4))			;	dat[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_offset); }
		      else if (algo == 12) { dat[index] = (byte) ((pak_offset >> 4) | (pak_offset << 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_index >> 4) | (pak_index << 4)); 			}
		      else if (algo == 13) { dat[index] = (byte) pak_offset										;	dat[crypto.offsets[index]] = (byte) pak_index; 										}
		      else if (algo == 14) { dat[index] = (byte) ((pak_offset & 0xF0) | (pak_index >> 4))		;	dat[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index & 0x0F)); 		}
		      else if (algo == 15) { dat[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)	; 	dat[crypto.offsets[index]] = (byte) (((pak_offset ^ pak_index) & 0x0F) ^ pak_index); 	}
		      else if (algo == 16) { dat[index] = (byte) pak_offset										;	dat[crypto.offsets[index]] = (byte) ((pak_index >> 4) | (pak_index << 4)); 			}
		      else if (algo == 17) { dat[index] = (byte) ((pak_offset << 4) | (pak_index & 0x0F))		;	dat[crypto.offsets[index]] = (byte) ((pak_index & 0xF0) | (pak_offset >> 4)); 		}
		      else if (algo == 18) { dat[index] = (byte) ((pak_offset << 4) | (pak_index >> 4))			;	dat[crypto.offsets[index]] = (byte) ((pak_offset >> 4) | (pak_index << 4)); 			}
		      else if (algo == 19) { dat[index] = (byte) ((pak_offset >> 4) | (pak_offset << 4))		;	dat[crypto.offsets[index]] = (byte)pak_index; 										}
		      else if (algo == 20) { dat[index] = (byte) (((pak_offset ^ pak_index) & 0x0F)^pak_offset)	; 	dat[crypto.offsets[index]] = (byte) ((pak_offset << 4) | (pak_index >> 4)); 			}
		}
		// and finally, quadruple-XOR the data out
		for (index=dat.length-1 ; index>=0; index--) {
			if (index <= dat.length-4) {
				dat[index + 0] ^= (crypto.xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
				dat[index + 1] ^= (crypto.xor[index] & 0x0000FF00) >> 8;
				dat[index + 2] ^= (crypto.xor[index] & 0x00FF0000) >> 16;
				dat[index + 3] ^= (crypto.xor[index] & 0xFF000000) >> 24;
			}
			else if (index == dat.length-3) {
				dat[index + 0] ^= (crypto.xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
				dat[index + 1] ^= (crypto.xor[index] & 0x00FF00) >> 8;
				dat[index + 2] ^= (crypto.xor[index] & 0xFF0000) >> 16;
			}
			else if (index == dat.length-2) {
				dat[index + 0] ^= (crypto.xor[index] & 0x00FF); // we can XOR 2 bytes in a row
				dat[index + 1] ^= (crypto.xor[index] & 0xFF00) >> 8;
			}
			else if (index == dat.length-1){
				dat[index] ^= (crypto.xor[index] & 0xFF); // end of stream
			}
		}
		// in the 1.50 protocol, the checksum info is at the trailing end of the pak.
		checksum = (short) ((dat[dat.length - 2]<< 8) | (dat[dat.length-1])); // so get it from there...
		for (index=0 ; index<pak_crypt.length ; index++){
			pak_crypt[index] = dat[index];
		}
		type = ((int)(pak_crypt[0] & 0xFF)<<8) | (int)(pak_crypt[1]& 0xFF);
		return checksum;
	}
	
	public long encrypt_150(byte[] pak){
		byte[] reverse_tree = new byte[]{0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};
		long seed;
		long return_seed = 0;
		byte[] stack1 = new byte[10];
		byte[] stack2 = new byte[10];
		byte a;
		byte c;
		int index;
		int algo;
		MSRand rand = new MSRand(System.currentTimeMillis());
		
		seed = rand.prng();
		rand = new MSRand(seed);
		return_seed = ((long)(seed & 0xFF000000)>>24) | ((long)(seed & 0x000000FF)>>16) | ((long)(seed & 0x00FF0000)>>8) | (long)(seed & 0x0000FF00);

		//TOTOD FRAGMENTS
		
		
		return return_seed;
	}
	/**
	 * unsigned long Pak_SwitchEncryptionOn_150 (pak_t *pak)
{
   // the 1.50 protocol cryptography uses extensively the standard C random number generator,
   // which is a VERY BAD idea, since its implementation may differ from system to system !!!

   static unsigned char reverse_tree[21] = {0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};

   unsigned long seed;
   unsigned long return_seed;
   char stack1[10];
   char stack2[10];
   unsigned char a;
   unsigned char c;
   char *edi;
   char *ebp;
   int index;
   unsigned int algo;

   seed = lrand (); // generate a random seed for this pak's encryption

   // initialize the system's pseudo-random number generator from the seed used in the datagram
   // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
   srand (seed);

   // write the seed in the pak
   ((unsigned char *) &return_seed)[0] = (unsigned char) (seed >> 24);
   ((unsigned char *) &return_seed)[3] = (unsigned char) (seed >> 16);
   ((unsigned char *) &return_seed)[1] = (unsigned char) (seed >> 8);
   ((unsigned char *) &return_seed)[2] = (unsigned char) (seed >> 0);

   // if new data size requires us to allocate more pages for pak data, do it
   if ((pak->data_size + 2) / 1024 + 1 > pak->pages_count)
   {
      pak->data = (char *) SAFE_realloc (pak->data, pak->pages_count, (pak->data_size + 2) / 1024 + 1, 1024, false);
      pak->pages_count = (pak->data_size + 2) / 1024 + 1; // new number of pages
   }

   // compute the pak checksum and append this information to the data
   *(unsigned short *) &pak->data[pak->data_size] = Pak_ComputeChecksum_150 (pak);
   pak->data_size += 2; // correct pak data size

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

   // cryptographic tables are generated, now quadruple-XOR the data out
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

   // and finally, apply the algorithm
   for (index = pak->data_size - 1; index >= 0; index--)
   {
      // actually, reverse_tree is a switch table hardcoded by cl compiler (MSVC)
      algo = reverse_tree[cryptotables_150.algo[pak->data_size - 1 - index]];
      ebp = &pak->data[cryptotables_150.offsets[pak->data_size - 1 - index]];
      edi = &pak->data[pak->data_size - 1 - index];

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

   return (return_seed); // finished, pak is encrypted
}
	 */
}
