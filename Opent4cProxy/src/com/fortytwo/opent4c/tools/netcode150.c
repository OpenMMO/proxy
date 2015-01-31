#include <stdio.h>
#include <stdlib.h>

//to execute test : gcc netcode150.c -o test && ./test && rm test
void main (void)
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
   //unsigned long seed = 0x5D84FB00;
   unsigned long seed = 0x0098EA00;
   unsigned long next;
   unsigned int data_size = 103;
   unsigned int xor[103];
   unsigned int offsets[103];
   unsigned int algos[103];
   unsigned char data[] = {0x0C, 0xFC, 0xF9, 0xEC, 0xEA, 0x3C, 0x33, 0x8A, 0x48, 0x79, 0x1E, 0xD7, 0x49, 0x47, 0x96, 0xD4, 0x89, 0x34, 0xDF, 0x18, 0x3E, 0x52, 0xA3, 0x22, 0x6C, 0xEC, 0x25, 0x2E, 0xE9, 0xF2, 0x92, 0xED, 0xD5, 0xD2, 0x54, 0xE7, 0x02, 0xC0, 0x17, 0xA3, 0x39, 0x2A, 0x8C, 0x4E, 0xE9, 0x6A, 0x2A, 0xDA, 0xC7, 0x13, 0x0E, 0x43, 0xC7, 0xC0, 0x3E, 0x54, 0x99, 0x1A, 0x7D, 0xE6, 0xF2, 0x41, 0xF2, 0xBF, 0x4F, 0x20, 0x13, 0xCD, 0x47, 0x83, 0xF2, 0x42, 0x53, 0xF9, 0x71, 0xE8, 0x82, 0xAB, 0xB9, 0x16, 0x85, 0xA2, 0x54, 0x20, 0x4E, 0x2E, 0x16, 0x3E, 0x19, 0x52, 0xBD, 0x1B, 0xED, 0x70, 0x5A, 0xC4, 0x66, 0x09, 0xF2, 0xB1, 0x6E, 0x6D, 0x94};
   //unsigned char data[] = {0x54, 0x04, 0x2F, 0xDE, 0x9D, 0xF3, 0xD5, 0xA8, 0x3C, 0x2C, 0x58, 0x2F, 0x3F, 0x35, 0x71};
   signed short sum;


    // Microsoft srand()
  	int sprng(unsigned long seed){
    	next = seed;
 	}

   // Microsoft rand()
 	int prng(void){
 		next = (next * 0x343fd) + 0x269ec3;
 		return ((next>>16) & 0x7fff);
 	}

 	void check(void)
 	{
 	   // this function computes and returns the pak data's checksum
 	   sum = 0; // start at zero
 	   // for each byte of data...
 	   for (index = 0; index < data_size-2; index++)
 	      sum += (unsigned char) (~data[index]); // add its inverse value to the checksum
 	}

 	printf("[NETCODE 1.50 C TEST]\n");
 	printf("[SEED %X]\n",seed);
 	printf("[SHIFTED SEED %d]\n",  (int) (((unsigned char *) &seed)[0] << 24)
          | (int) (((unsigned char *) &seed)[3] << 16)
          | (int) (((unsigned char *) &seed)[1] << 8)
          | (int) (((unsigned char *) &seed)[2]));
   // initialize the system's pseudo-random number generator from the seed given in the datagram
   // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
   sprng (  (int) (((unsigned char *) &seed)[0] << 24)
          | (int) (((unsigned char *) &seed)[3] << 16)
          | (int) (((unsigned char *) &seed)[1] << 8)
          | (int) (((unsigned char *) &seed)[2]));

   // now generate the crypto tables for the given datagram length

   // stack sequences
   for (index = 0; index < 10; index++)
   {
      stack1[index] = (char) prng ();
      stack2[index] = (char) prng ();
      printf("[STACK1[%d] %d]\n",index, stack1[index]);
      printf("[STACK2[%d] %d]\n",index, stack2[index]);

   }

   // xor table
   for (index = 0; index < data_size; index++)
   {
      xor[index] = (unsigned char) stack2[prng () % 10];
      printf("[TMP[%d] %d]\n",index, xor[index]);
      xor[index] *= (unsigned char) stack1[prng () % 10];
      printf("[TMP[%d] %d]\n",index, xor[index]);
      xor[index] += prng ();
      printf("[XOR[%d] %d]\n",index, xor[index]);

   }

   // offset & algo tables
   for (index = 0; index < data_size; index++)
   {
      offsets[index] = prng () % data_size;
      if (offsets[index] == (unsigned int) index) offsets[index] = (index == 0 ? 1 : 0);
      printf("[OFFSETS[%d] %d]\n",index, offsets[index]);
      algos[index] = prng () % 21;
      printf("[ALGOS[%d] %d]\n",index, algos[index]);

   }
   printf("[SRC ");
   for (index = 0 ; index < data_size ; index ++){
	   printf("%X ", data[index]);
   }
   printf("]\n");
   // cryptographic tables are generated, now apply the algorithm
   for (index = data_size - 1; index >= 0; index--)
   {
      algo = algos[index];
      ebp = &data[offsets[index]];
      edi = &data[index];

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
   for (index = data_size - 1; index >= 0; index--)
   {
      if (index <= data_size - 4)
      {
         data[index + 0] ^= (xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
         data[index + 1] ^= (xor[index] & 0x0000FF00) >> 8;
         data[index + 2] ^= (xor[index] & 0x00FF0000) >> 16;
         data[index + 3] ^= (xor[index] & 0xFF000000) >> 24;
      }
      else if (index == data_size - 3)
      {
         data[index + 0] ^= (xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
         data[index + 1] ^= (xor[index] & 0x00FF00) >> 8;
         data[index + 2] ^= (xor[index] & 0xFF0000) >> 16;
      }
      else if (index == data_size - 2)
      {
         data[index + 0] ^= (xor[index] & 0x00FF); // we can XOR 2 bytes in a row
         data[index + 1] ^= (xor[index] & 0xFF00) >> 8;
      }
      else if (index == data_size - 1)
         data[index] ^= (xor[index] & 0xFF); // end of stream
   }

   printf("[DATA ");
   for (index = 0 ; index < data_size ; index ++){
	   printf("%X ", data[index]);
   }
   printf("]\n[MOTD LENGTH %d]\n[MOTD : ",(((int)(data[2]<<8)&0xFF00)|(int)(data[3])&0xFF));
   for (index = 4 ; index < data_size-2 ; index ++){
	   printf("%c", data[index]);
   }
   printf("]\n[TYPE %X %X]\n[CHECK %X]\n",data[0],data[1],(((int)(data[data_size-1]<<8)&0xFF00)|(int)(data[data_size-2])&0xFF));
   check();
   printf("[SUM %X]\n",sum);// finished, pak is decrypted
}
