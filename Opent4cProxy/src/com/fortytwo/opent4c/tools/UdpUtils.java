package com.fortytwo.opent4c.tools;

public class UdpUtils {
	/**
	 * Create a datagram packet to be sent with right size
	 * @param receiveData
	 * @return
	 */
	public static byte[] extractSendData(byte[] receiveData) {
		byte[] result;
		int length = ((int)(receiveData[3] & 0xFF)<<8) | (int)(receiveData[2]& 0xFF);
		if(length != 0){
			result = new byte[length];
			for (int j = 0 ; j<length ; j++){
				result[j] = receiveData[j];
			}				
		}else{
			result = truncate(receiveData);
		}
		return result;
	}

	/**
	 * cuts off trailing 0 in byte[] for pong packets
	 * @param data
	 * @return
	 */
	private static byte[] truncate(byte[] data) {
		int trailLength = 0;
		for (int i = data.length-1 ; i >= 0 ; i--){
			if(data[i] == 0){
				trailLength++;
			}else{
				break;
			}
		}
		byte[] truncated = new byte[data.length-trailLength+4];
		for (int j = 0 ; j<=truncated.length-1 ; j++){
			truncated[j] = data[j];
		}
		return truncated;
	}
}
