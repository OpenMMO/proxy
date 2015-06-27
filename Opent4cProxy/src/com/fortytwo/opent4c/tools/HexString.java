package com.fortytwo.opent4c.tools;

public class HexString {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String from(byte[] bytes){
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        //hexChars[j * 6 + 0] = '0';
	        //hexChars[j * 6 + 1] = 'x';
	        hexChars[j * 3 + 0] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        //hexChars[j * 6 + 4] = ',';
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}
	
	public static String from(byte b){
		byte[] bytes = new byte[]{b};
	    return from(bytes);
	}

	public static String from(int i) {
		byte[] bytes = new byte[]{
			(byte)(i>>24 & 0xFF),
			(byte)(i>>16 & 0xFF),
			(byte)(i>>8 & 0xFF),
			(byte)(i & 0xFF)
			};
		return from(bytes);
	}
	
	public static String from(short s) {
		byte[] bytes = new byte[]{
			(byte)(s>>8 & 0xFF),
			(byte)(s & 0xFF)
			};
		return from(bytes);
	}
}
