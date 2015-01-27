package com.fortytwo.opent4c.proxy;

public class TCryptoTable {
	public int[] xor;
	public int[] offsets;
	public int[] algo;
	
	public TCryptoTable(int  length){
		xor = new int[length];
		offsets = new int[length];
		algo = new int[length];
	}
}
