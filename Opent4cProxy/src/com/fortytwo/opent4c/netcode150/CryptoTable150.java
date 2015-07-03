package com.fortytwo.opent4c.netcode150;

public class CryptoTable150 {
	public int[] xor;
	public int[] offsets;
	public int[] algo;
	
	public CryptoTable150(int  length){
		xor = new int[length];
		offsets = new int[length];
		algo = new int[length];
	}
}
