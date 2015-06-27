#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
#include <unistd.h>
#include <ctype.h>
#include <string.h>

/**
  *DEFINITION DES OBJETS
  **/

typedef struct pak
{
	unsigned int data_size;
	unsigned int pages_count;
	unsigned char *data;
    unsigned long seed;

} pak_t;

pak_t* createPak(unsigned int taille, unsigned char *msg)
{
    pak_t *T = malloc( sizeof(pak_t) );
    if (!T) return NULL;
    T->data_size = taille;
    T->pages_count = (taille/1024)+1;
    T->data = msg;
    T->seed = 0;//0x00A774D9;

    return T;
}

typedef struct cryptotable_150
{
	unsigned int *xors;
	unsigned int *offsets;
	unsigned int *algo;
} cryptotable;

cryptotable* createTable(unsigned int taille)
{
    cryptotable *T = malloc( sizeof(cryptotable) );
    if (!T) return NULL;
    T->xors = malloc( taille * sizeof(int));
    T->algo = malloc( taille * sizeof(int));
    T->offsets = malloc( taille * sizeof(int));
    return T;
}

typedef struct randomizer
{
    unsigned long seed;
    int next;
} randm;

randm* createRandom(unsigned long in_seed)
{
    randm *T = malloc( sizeof(randm) );
    if (!T) return NULL;
    T->seed = in_seed;
    T->next = T->seed;
    return T;
}

/**
  *FONCTIONS UTILITAIRES
  */
//get next random int from PRNG
int rndNext(randm *rnd)
{
    rnd->next = (rnd->next * 0x343fd) + 0x269ec3;
    return ((rnd->next>>16) & 0x7fff);
}

//get a random long
long lrand()
{
    srand(time(NULL));
    if (sizeof(int) < sizeof(long)) return (long)(rand() << (sizeof(int) * 7)) | rand();
    return rand();
}

//compute a pak's checksum
signed short Pak_ComputeChecksum_150 (pak_t *pak)
{
   unsigned long index;
   signed short sum;
   sum = 0;
   for (index = 0; index < pak->data_size; index++) sum += (unsigned char) (~pak->data[index]); // add its inverse value to the checksum
   return (sum); // return the checksum we found
}

/**
  *ENCRYPT & DECRYPT FUNCTIONS
  */
//encrypt a pak
void Pak_SwitchEncryptionOn_150 (pak_t *pak)
{
    // the 1.50 protocol cryptography uses extensively the standard C random number generator,
    // which is a VERY BAD idea, since its implementation may differ from system to system !!!

    static unsigned char reverse_tree[21] = {0, 5, 2, 9, 11, 1, 18, 7, 14, 3, 10, 4, 12, 13, 8, 15, 19, 20, 6, 16, 17};

    long seed;
    unsigned int mixed_seed;
    unsigned char stack1[10];
    unsigned char stack2[10];
    unsigned char a;
    unsigned char c;
    unsigned char *edi;
    unsigned char *ebp;
    int index;
    unsigned int algo;
    signed short checksum;
    cryptotable *cryptotables_150 = createTable(pak->data_size);
    //printf("Choose a random seed");

    //seed = lrand(); // generate a random seed for this pak's encryption
	seed = 1463307735;

    // initialize the system's pseudo-random number generator from the seed used in the datagram
    // (they apparently swapped the bytes in an attempt to confuse the reverse-engineerers)
    printf("|Init PRNG with random seed : %d (%X)\n",seed,seed);
    randm *rnd = createRandom(seed);
    //srand (seed);

    //printf("Mix and write the seed in the pak\n");
    // write the seed in the pak
    ((unsigned char *) &mixed_seed)[0] = (unsigned char) (seed >> 24);
    ((unsigned char *) &mixed_seed)[3] = (unsigned char) (seed >> 16);
    ((unsigned char *) &mixed_seed)[1] = (unsigned char) (seed >> 8);
    ((unsigned char *) &mixed_seed)[2] = (unsigned char) (seed >> 0);
    pak->seed =  mixed_seed;
    printf("|Shifted Seed : %X\n",mixed_seed);
    // if new data size requires us to allocate more pages for pak data, do it
    //if ((pak->data_size + 2) / 1024 + 1 > pak->pages_count)
    //{
    //   pak->data = (char *) SAFE_realloc (pak->data, pak->pages_count, (pak->data_size + 2) / 1024 + 1, 1024, false);
    //   pak->pages_count = (pak->data_size + 2) / 1024 + 1; // new number of pages
    //}
    //printf("compute the pak checksum and append this information to the data\n");
    // compute the pak checksum and append this information to the data
    *(unsigned short *) &pak->data[pak->data_size] = Pak_ComputeChecksum_150 (pak);
    pak->data_size += 2; // correct pak data size
    //checksum = *(unsigned short *) &pak->data[pak->data_size - 2];
	printf("|Data before encryption : ",pak->data);
	for (index = 0; index < pak->data_size; index++) printf("%X",pak->data[index]);
    printf("\n");

    // now generate the crypto tables for the given datagram length

    //printf("generate 2 size 10 int arrays :");
    // stack sequences
    for (index = 0; index < 10; index++)
    {
        stack1[index] = (unsigned char) rndNext(rnd);
        stack2[index] = (unsigned char) rndNext(rnd);
        //printf("|Stack %X | %X\n",stack1[index],stack2[index]);
    }
    //printf("\n");

    //printf("generate xor table with arrays :");
    // xor table
    for (index = 0; index < pak->data_size; index++)
    {
        cryptotables_150->xors[index] = (unsigned char) stack2[rndNext(rnd) % 10];
        printf("|XorA %X\n",cryptotables_150->xors[index]);
        cryptotables_150->xors[index] *= (unsigned char) stack1[rndNext(rnd) % 10];
        printf("|XorB %X\n",cryptotables_150->xors[index]);
        cryptotables_150->xors[index] += rndNext(rnd);
        printf("|XorC %X\n",cryptotables_150->xors[index]);
    }
    //printf("\n");

    //printf("generate offset & algorithm tables :");
    // offset & algo tables
    for (index = 0; index < pak->data_size; index++)
    {
        cryptotables_150->offsets[index] = rndNext(rnd) % pak->data_size;
        if (cryptotables_150->offsets[index] == (unsigned int) index) cryptotables_150->offsets[index] = (index == 0 ? 1 : 0);
        cryptotables_150->algo[index] = rndNext(rnd) % 21;
        //printf(" [%X %d]", cryptotables_150->offsets[index], cryptotables_150->algo[index]);
    }
    //printf("\n");

    //printf("quadruple-XOR the data out\n");
    // cryptographic tables are generated, now quadruple-XOR the data out
    for (index = pak->data_size - 1; index >= 0; index--)
    {
        if (index <= pak->data_size - 4)
        {
            pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x000000FF); // we can XOR 4 bytes in a row
            pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0x0000FF00) >> 8;
            pak->data[index + 2] ^= (cryptotables_150->xors[index] & 0x00FF0000) >> 16;
            pak->data[index + 3] ^= (cryptotables_150->xors[index] & 0xFF000000) >> 24;
        }
        else if (index == pak->data_size - 3)
        {
            pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x0000FF); // we can XOR 3 bytes in a row
            pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0x00FF00) >> 8;
            pak->data[index + 2] ^= (cryptotables_150->xors[index] & 0xFF0000) >> 16;
        }
        else if (index == pak->data_size - 2)
        {
            pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x00FF); // we can XOR 2 bytes in a row
            pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0xFF00) >> 8;
        }
        else if (index == pak->data_size - 1)
            pak->data[index] ^= (cryptotables_150->xors[index] & 0xFF); // end of stream
    }

    //printf("apply the algorithms\n");
    // and finally, apply the algorithm
    for (index = pak->data_size - 1; index >= 0; index--)
    {
        // actually, reverse_tree is a switch table hardcoded by cl compiler (MSVC)
        algo = reverse_tree[cryptotables_150->algo[pak->data_size - 1 - index]];
        ebp = &pak->data[cryptotables_150->offsets[pak->data_size - 1 - index]];
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
    //printf("Finish encrypt\n");
}

//decrypt a pak
signed short Pak_SwitchEncryptionOff_150 (pak_t *pak)
{
    // the 1.50 protocol cryptography uses extensively the standard C random number generator,
    // which is a VERY BAD idea, since its implementation may differ from system to system !!!

    char stack1[10];
    char stack2[10];
    unsigned char a;
    unsigned char c;
    unsigned char *edi;
    unsigned char *ebp;
    int index;
    unsigned int algo;
    signed short checksum;
    cryptotable *cryptotables_150 = createTable(pak->data_size);
    unsigned int unmixed_seed = (int) (((unsigned char *) &pak->seed)[0] << 24)
          | (int) (((unsigned char *) &pak->seed)[3] << 16)
          | (int) (((unsigned char *) &pak->seed)[1] << 8)
          | (int) (((unsigned char *) &pak->seed)[2]);
    printf("|Init PRNG with seed : %X\n", unmixed_seed);
    randm *rnd = createRandom(unmixed_seed);

   // stack sequences
   //printf("generate 2 size 10 int arrays :");
   for (index = 0; index < 10; index++)
   {
      stack1[index] = (unsigned char) rndNext(rnd);
      stack2[index] = (unsigned char) rndNext(rnd);
      //printf("  [%X %X]",stack1[index],stack2[index]);
   }
   //printf("\n");

    // xor table
    //printf("generate xor table with the arrays :");
   for (index = 0; index < pak->data_size; index++)
   {
        cryptotables_150->xors[index] = (unsigned char) stack2[rndNext(rnd) % 10];
        cryptotables_150->xors[index] *= (unsigned char) stack1[rndNext(rnd) % 10];
        cryptotables_150->xors[index] += rndNext(rnd);
        //printf(" [%X]",cryptotables_150->xors[index]);
   }
   //printf("\n");

   // offset & algo tables
   //printf("generate offset & algorithm tables :");
   for (index = 0; index < pak->data_size; index++)
   {
      cryptotables_150->offsets[index] = rndNext(rnd) % pak->data_size;
      if (cryptotables_150->offsets[index] == (unsigned int) index) cryptotables_150->offsets[index] = (index == 0 ? 1 : 0);
      cryptotables_150->algo[index] = rndNext(rnd) % 21;
      //printf(" [%X %d]", cryptotables_150->offsets[index], cryptotables_150->algo[index]);
   }
   //printf("\n");

   // cryptographic tables are generated, now apply the algorithm
   //printf("apply the algorithms\n");
   for (index = pak->data_size - 1; index >= 0; index--)
   {
      algo = cryptotables_150->algo[index];
      ebp = &pak->data[cryptotables_150->offsets[index]];
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
      //printf("  %d %X %X\n",index,*edi, *ebp);
   }
   //printf("OK\n");

   // and finally, quadruple-XOR the data out
   //printf("quadruple-XOR the data out\n");
   for (index = pak->data_size - 1; index >= 0; index--)
   {
      if (index <= pak->data_size - 4)
      {
         pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x000000FF); // we can XOR 4 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0x0000FF00) >> 8;
         pak->data[index + 2] ^= (cryptotables_150->xors[index] & 0x00FF0000) >> 16;
         pak->data[index + 3] ^= (cryptotables_150->xors[index] & 0xFF000000) >> 24;
      }
      else if (index == pak->data_size - 3)
      {
         pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x0000FF); // we can XOR 3 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0x00FF00) >> 8;
         pak->data[index + 2] ^= (cryptotables_150->xors[index] & 0xFF0000) >> 16;
      }
      else if (index == pak->data_size - 2)
      {
         pak->data[index + 0] ^= (cryptotables_150->xors[index] & 0x00FF); // we can XOR 2 bytes in a row
         pak->data[index + 1] ^= (cryptotables_150->xors[index] & 0xFF00) >> 8;
      }
      else if (index == pak->data_size - 1)
         pak->data[index] ^= (cryptotables_150->xors[index] & 0xFF); // end of stream
   //printf(" %d %X\n",index,pak->data[index]);
   }
   //printf("OK\n");

   // in the 1.50 protocol, the checksum info is at the trailing end of the pak.
   //printf("compute the checksum\n");
   checksum = *(unsigned short *) &pak->data[pak->data_size - 2]; // so get it from there...
   pak->data_size -= 2; // ...and correct the data size
   //printf("OK, new size : %d\n",pak->data_size);

   /*printf("DECRYPTED : ");
   for (index = 0 ; index < pak->data_size ; index++)
   {
       printf("%X ",pak->data[index]);
   }*/
   //printf("finished decrypt\n");
   return (checksum); // finished, pak is decrypted
}

int test(void){
    unsigned char *msg;
    int index;
    unsigned short chk;
    printf("------------------------------------------------------\n|Netcode 150 ENCRYPT TEST\n");
    msg = malloc (2 * sizeof(char));
    msg[0] = 0x00;
    msg[1] = 0x42;
    printf("|Message input : ");
    for (index = 0; index < 2; index++) printf("%X",msg[index]);
    printf("\n");
    pak_t *pack = createPak(2,msg);

    Pak_SwitchEncryptionOn_150(pack);
    printf("|Message output : ");
    for (index = 0; index < pack->data_size; index++)printf("%X",pack->data[index]);
    printf("\n------------------------------------------------------\n");
    printf("|Netcode 150 DECRYPT TEST\n|Message input : ");
    for (index = 0; index < pack->data_size; index++) printf("%X",pack->data[index]);
    printf("\n");
    chk = Pak_SwitchEncryptionOff_150(pack);
    printf("|Decrypted message : ");
    for (index = 0; index < pack->data_size; index++)
    {
        printf("%X",pack->data[index]);
    }
    printf("\n|Checksum : %X\n------------------------------------------------------\n",chk);
    return 0;
}

unsigned char* hex2data(unsigned const char* hexstring , unsigned int len){
	unsigned char *data = malloc ((len/2) * sizeof(char));;
    unsigned const char *pos = hexstring;
    char *endptr;
    size_t count = 0;
	int index = 0;
	
    if ((hexstring[0] == '\0') || (strlen(hexstring) % 2)) {
		printf("hexstring contains no data or hexstring has an odd length.\n");
		exit(-1);
    }
    for(count = 0; count < len/2; count++) {
        char buf[5] = {'0', 'x', pos[0], pos[1], 0};
        data[count] = strtol(buf, &endptr, 0);
        pos += 2 * sizeof(char);
        //printf("hex-char : %X\n",data[count]);
        
        if (endptr[0] != '\0') {
            //non-hexadecimal character encountered
            exit(-1);
        }
    }
    return data;
}

unsigned int length(unsigned char* array) {
	unsigned int count = 0;
	while(array[count]) count++;
	return count;
}

int crypt(int size , unsigned char* msg){
    int index = 0;
    unsigned short chk;
    printf("------------------------------------------------------\n|Netcode 150 ENCRYPT\n|Message input : ");
    for (index = 0; index < size; index++) printf("%X",msg[index]);
    printf("\n");
    pak_t *pack = createPak(size,msg);

    Pak_SwitchEncryptionOn_150(pack);
    printf("|Message output : ");
    for (index = 0; index < pack->data_size; index++)printf("%X",pack->data[index]);
    printf("\n------------------------------------------------------\n");
    return 0;
}

int decrypt(int size , unsigned char* msg){
    int index;
    unsigned short chk;
    printf("------------------------------------------------------\n|Netcode 150 DECRYPT\n|Message input : ");
    for (index = 0; index < size; index++) printf("%X",msg[index]);
    printf("\n");

    pak_t *pack = createPak(size,msg);
    pack->seed = (int) msg[0] << 16 | (int) msg[1] << 8;
	msg += 2 * sizeof(char);
    chk = Pak_SwitchEncryptionOff_150(pack);
    printf("|Message output : ");
    for (index = 0; index < pack->data_size; index++)printf("%X ",pack->data[index]);
    printf("\n|Checksum : %X\n------------------------------------------------------\n",chk);
    return 0;
}

/**
  *MAIN LOOP
  */
int main(int argc, char** argv)
{
    int opt;
    unsigned int size;
    unsigned int index;
    unsigned char* msg;
    unsigned char* data;
    enum { DECRYPT, CRYPT, TEST } mode = TEST;

    while ((opt = getopt(argc, argv, "d:c:")) != -1) {
        switch (opt) {
		case 'd': mode = DECRYPT;
			msg = optarg;
			size = length(msg);
			data = hex2data(msg, size);
			size = length(msg)/2;

			break;
		case 'c': mode = CRYPT;
			msg = optarg;
			size = length(msg);
			data = hex2data(msg, size);
			size = length(msg)/2;
			break;
        default: fprintf(stderr,"Usage: %s -[dc] [hexstring]\n", argv[0]);
            return EXIT_FAILURE;
        }
	}
	
	switch(mode) {
		case TEST : printf("Usage: %s -[dc] [hexstring]\n", argv[0]);
			test();
			break;
		case CRYPT : crypt(size,data);
			break;
		case DECRYPT : decrypt(size,data);
			break;
	}
	return 0;
}
