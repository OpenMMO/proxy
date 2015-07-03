package com.fortytwo.opent4c.proxy;

import java.nio.ByteBuffer;

import com.fortytwo.opent4c.netcode150.Pak150;
import com.fortytwo.opent4c.screens.MonitorScreen;
import com.fortytwo.opent4c.tools.HexString;

public class Translator {

	public static ByteBuffer computeClientToServer(long stamp, long micros, byte[] data) {
		ByteBuffer result = ByteBuffer.allocate(data.length);
		byte[] header = null , clearData = null , cryptData;
		byte fragmentID;
		short length , type;
		int datagramID , firstPakID;
		boolean isLastFragment , isPing , isPong , isFragment , isFirstFragment , valid = false;
		switch(ProxyManager.clientVersion){
			case 150 :  
				header = getHeader150(data);
				MonitorScreen.writeConsoleLines("CLIENT SENT PAK : "+HexString.from(data));
				fragmentID = Pak150.getFragmentID(header);
				if(fragmentID == 0)isFragment = false;
				else isLastFragment = true;
				isPong = Pak150.getIsPong(header);
				isPing = Pak150.getIsPing(header);
				isFragment = Pak150.getIsFragment(header);
				length = Pak150.getLength(header);
				datagramID = Pak150.getDatagramID(header);
				firstPakID = Pak150.getFirstPakID(header);
				MonitorScreen.writeConsoleLines("HEADER:[FRAGMENTID:"+fragmentID+"][BITMASK(pong/ping/fragment):"+isPong+"/"+isPing+"/"+isFragment+"][LENGTH:"+length+"][DATAGRAMID:"+datagramID+"][FIRSTPAKID:"+firstPakID+"]");
				clearData = Pak150.decrypt(data);
				if(clearData != null)valid = true;
				type = Pak150.getType(clearData);
				MonitorScreen.writeConsoleLines("DATA:[TYPE:"+HexString.from(type)+"][DATA:"+HexString.from(Pak150.getData(clearData))+"][CHECKSUM:"+HexString.from(Pak150.getChecksum(clearData))+"]");
				break;
		}
		if(!valid)return null;
		switch(ProxyManager.serverVersion){
			case 150 : cryptData = Pak150.encrypt(clearData);
			result.put(header);
			result.put(cryptData);
			break;
		}
		return result;
	}

	private static byte[] getHeader150(byte[] data) {
		byte[] result = new byte[12];
		for (int i = 0 ; i < 12 ; i++){
			result[i] = data[i];
		}
		return result;
	}
}
