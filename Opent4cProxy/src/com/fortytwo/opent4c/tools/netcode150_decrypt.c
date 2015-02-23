#include <stdio.h>
#include <stdlib.h>

//to execute test : gcc netcode150_decrypt.c -o test && ./test && rm test
int main (void)
{
   // the 1.50 protocol cryptography uses extensively the standard C random number generator,
   // which is a VERY BAD idea, since its implementation may differ from system to system !!!

   unsigned char stack1[10];
   unsigned char stack2[10];
   unsigned char a;
   unsigned char c;
   unsigned char *edi;
   unsigned char *ebp;
   int index;
   unsigned int algo;
   signed short checksum;
   unsigned long seed = 0x00642900;
   unsigned long next;
   unsigned int data_size = 4;
   unsigned int xor[4];
   unsigned int offsets[4];
   unsigned int algos[4];
   unsigned char data[] = {0x75, 0x57, 0x28, 0x0F};
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

 	signed short check(void)
 	{
 	   // this function computes and returns the pak data's checksum
 	   sum = 0; // start at zero
 	   // for each byte of data...
 	   for (index = 0; index < data_size-2; index++){
 	      sum += (unsigned char) (~data[index]); // add its inverse value to the checksum
 	      //printf("CHECK(%d) %X\n",index + 0, sum);
 	   }
 	   return sum;
 	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	printf("SOURCE\n");
 	//printf("SHIFTED SEED %X\n",  (int) (((unsigned char *) &seed)[3] << 24)
    //      | (int) (((unsigned char *) &seed)[0] << 16)
    //      | (int) (((unsigned char *) &seed)[2] << 8)
    //      | (int) (((unsigned char *) &seed)[1]));
   // initialize the system's pseudo-random number generator from the seed given in the datagram
   // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
   sprng (  (int) (((unsigned char *) &seed)[3] << 24)
          | (int) (((unsigned char *) &seed)[0] << 16)
          | (int) (((unsigned char *) &seed)[2] << 8)
          | (int) (((unsigned char *) &seed)[1]));
	printf("SEED %X\n",seed);

   // now generate the crypto tables for the given datagram length

   // stack sequences
   for (index = 0; index < 10; index++)
   {
      stack1[index] = (unsigned char) prng () & 0xFF;
      stack2[index] = (unsigned char) prng () & 0xFF;
      //printf("STACK1(%d) %X\n",index, stack1[index]);
     // printf("STACK2(%d) %X\n",index, stack2[index]);

   }

   // xor table
   for (index = 0; index < data_size; index++)
   {
      xor[index] = (unsigned char) stack2[prng () % 10];
      xor[index] *= (unsigned char) stack1[prng () % 10];
      xor[index] += prng ();
      //printf("XOR(%d) %X\n",index, xor[index]);

   }

   // offset & algo tables
   for (index = 0; index < data_size; index++)
   {
      offsets[index] = prng () % data_size;
      if (offsets[index] == (unsigned int) index) offsets[index] = (index == 0 ? 1 : 0);
      //printf("OFFSETS(%d) %X\n",index, offsets[index]);
      algos[index] = prng () % 21;
      //printf("ALGOS(%d) %X\n",index, algos[index]);

   }
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
      //printf("[%d]PAK(%d) %X\n",algo,index, *edi);
      //printf("[%d]PAK(%d) %X\n",algo, offsets[index], *ebp);
      
   }

   // and finally, quadruple-XOR the data out
   for (index = data_size - 1; index >= 0; index--)
   {
      if (index <= data_size - 4)
      {
         data[index + 0] ^= (xor[index] & 0x000000FF); // we can XOR 4 bytes in a row
         //printf("PAXOR(%d) %X\n",index + 0, data[index + 0]);
         data[index + 1] ^= (xor[index] & 0x0000FF00) >> 8;
         //printf("PAXOR(%d) %X\n",index + 1, data[index + 1]);
         data[index + 2] ^= (xor[index] & 0x00FF0000) >> 16;
         //printf("PAXOR(%d) %X\n",index + 2, data[index + 2]);
         data[index + 3] ^= (xor[index] & 0xFF000000) >> 24;
         //printf("PAXOR(%d) %X\n",index + 3, data[index + 3]);
      }
      else if (index == data_size - 3)
      {
         data[index + 0] ^= (xor[index] & 0x0000FF); // we can XOR 3 bytes in a row
         //printf("PAXOR(%d) %X\n",index + 0, data[index + 0]);
         data[index + 1] ^= (xor[index] & 0x00FF00) >> 8;
         //printf("PAXOR(%d) %X\n",index + 1, data[index + 1]);
         data[index + 2] ^= (xor[index] & 0xFF0000) >> 16;
         //printf("PAXOR(%d) %X\n",index + 2, data[index + 2]);
      }
      else if (index == data_size - 2)
      {
         data[index + 0] ^= (xor[index] & 0x00FF); // we can XOR 2 bytes in a row
         //printf("PAXOR(%d) %X\n",index + 0, data[index + 0]);
         data[index + 1] ^= (xor[index] & 0xFF00) >> 8;
         //printf("PAXOR(%d) %X\n",index + 1, data[index + 1]);
      }
      else if (index == data_size - 1)
         data[index] ^= (xor[index] & 0xFF); // end of stream
         //printf("PAXOR(%d) %X\n",index + 0, data[index + 0]);
   }
   	printf("DECRYPTED DATA %X, %X, %X, %X\n", data[0], data[1], data[2], data[3]);
   check();
   checksum = ((unsigned short)(data[data_size-1]<<8)&0xFF00)|((unsigned short)(data[data_size-2])&0xFF);
   //printf("CHECKSUM %X\n",checksum);
   //printf("TYPE %X %X\n",data[0],data[1]);
   
	//printf("MOTD LENGTH %d\nMOTD : ",(((int)(data[2]<<8)&0xFF00)|(int)(data[3])&0xFF));
	//for (index = 4 ; index < data_size-2 ; index ++){
	//   printf("%c", data[index]);
	//}
   //printf("\n");
   // finished, pak is decrypted
   

   return 0;
}
