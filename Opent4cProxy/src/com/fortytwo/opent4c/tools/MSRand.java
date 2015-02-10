package com.fortytwo.opent4c.tools;

public class MSRand {
	   private int next;

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
	
	public MSRand(int seed){
		next = seed;
	}
	
	public int prng(){
		next = (next * 0x343fd) + 0x269ec3;
		return (int) ((next>>16) & 0x7fff);
	}
	
	public static int lrand(){
		MSRand rnd = new MSRand((int) System.currentTimeMillis());
		return rnd.prng();
	}
}
