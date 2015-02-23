#include <stdio.h>
#include <stdlib.h>

//to execute test : gcc netcode150_crypt.c -o test && ./test && rm test
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
   unsigned long seed = 0x00002964;
   unsigned long next;
   unsigned int data_size = 4;
   unsigned int xor[4];
   unsigned int offsets[4];
   unsigned int algos[4];
   unsigned char data[] = {0x00, 0x42, 0xBC, 0x01};
   signed short sum;
   static unsigned char reverse_tree[21] = {0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};
   unsigned long return_seed;

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
   
   printf("SEED %X\n",seed);
   // initialize the system's pseudo-random number generator from the seed used in the datagram
   // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
   sprng (seed);

   // write the seed in the pak
   ((unsigned char *) &return_seed)[0] = (unsigned char) (seed >> 24);
   ((unsigned char *) &return_seed)[3] = (unsigned char) (seed >> 16);
   ((unsigned char *) &return_seed)[1] = (unsigned char) (seed >> 8);
   ((unsigned char *) &return_seed)[2] = (unsigned char) (seed >> 0);
   printf("RETURN SEED %X\n",return_seed);
   
   // if new data size requires us to allocate more pages for pak data, do it
   //if ((pak->data_size + 2) / 1024 + 1 > pak->pages_count)
   //{
   //   pak->data = (char *) SAFE_realloc (pak->data, pak->pages_count, (pak->data_size + 2) / 1024 + 1, 1024, false);
   //   pak->pages_count = (pak->data_size + 2) / 1024 + 1; // new number of pages
   //}

   // compute the pak checksum and append this information to the data
   //*(unsigned short *) &pak->data[pak->data_size] = Pak_ComputeChecksum_150 (pak);
   //pak->data_size += 2; // correct pak data size

   // now generate the crypto tables for the given datagram length

   // stack sequences
   for (index = 0; index < 10; index++)
   {
      stack1[index] = (char) prng ();
      printf("STACK 1 %X\n",stack1[index]);
      stack2[index] = (char) prng ();
   }

   // xor table
   for (index = 0; index < data_size; index++)
   {
      xor[index] = (unsigned char) stack2[prng () % 10];
      xor[index] *= (unsigned char) stack1[prng () % 10];
      xor[index] += prng ();
   }

   // offset & algo tables
   for (index = 0; index < data_size; index++)
   {
      offsets[index] = prng () % data_size;
      if (offsets[index] == (unsigned int) index)
         offsets[index] = (index == 0 ? 1 : 0);

      algos[index] = prng () % 21;
   }

   // cryptographic tables are generated, now quadruple-XOR the data out
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

   // and finally, apply the algorithm
   for (index = data_size - 1; index >= 0; index--)
   {
      // actually, reverse_tree is a switch table hardcoded by cl compiler (MSVC)
      algo = reverse_tree[algos[data_size - 1 - index]];
      ebp = &data[offsets[data_size - 1 - index]];
      edi = &data[data_size - 1 - index];

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
   
   printf("ENCRYPTED DATA %X, %X, %X, %X\n", data[0], data[1], data[2], data[3]);
   return 0;
}
