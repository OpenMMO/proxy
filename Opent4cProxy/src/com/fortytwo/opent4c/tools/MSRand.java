package com.fortytwo.opent4c.tools;

public class MSRand {
	   private long next;

	/**
	 *  Thanks to Markin for that
	 *
	 *  // Microsoft srand()
     * 	int sprng(unsigned long seed){
	 *   	next = seed;
	 *	}
	 *
	 *  // Microsoft rand()
	 *	int prng(void){
	 *		next = (next * 0x343fd) + 0x269ec3;
	 *		return ((next>>16) & 0x7fff);
	 *	}
	 */
	
	public MSRand(long seed){
		next = seed;
	}
	
	public int prng(){
		next = (next * 0x343fd) + 0x269ec3;
		return (int) ((next>>16) & 0x7fff);
	}
}
