package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fortytwo.opent4c.tools.ByteArrayToHexString;
import com.fortytwo.opent4c.tools.UdpUtils;

public class ServerTunnel {
	
	private static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static boolean online = true;

	
	public ServerTunnel() {}

	/**
	 * adds messages to be sent to server
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
	}

	/**
	 * waits for a message to be sent to server
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static void startSendPile() throws InterruptedException, IOException {
		while(online){
			while(sendpile.size() != 0){
				send(sendpile.get(0));
				sendpile.remove(0);
			}
			Thread.sleep(0,100);
		}
	}

	/**
	 * send a message to server
	 * @param packet
	 * @throws IOException 
	 */
	private static void send(DatagramPacket packet) throws IOException {
		Proxy.toServerSocket.send(packet);
	}

	/**
	 * waits for messages to be received from server
	 * @throws IOException 
	 */
	public static void listen() throws IOException {
		byte[] receiveData = new byte[1024];
		while(online){
			long stamp = 0;
			long micros = 0;
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			Proxy.toServerSocket.receive(receivePacket);
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			byte[] sendData = UdpUtils.extractSendData(receiveData);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ClientTunnel.ip, ClientTunnel.port);
			ClientTunnel.pile(sendPacket);
			if (Proxy.serverVersion == 150){
				Pak150 decrypted = new Pak150(receivePacket, true, stamp, micros);
			}else if (Proxy.serverVersion == 125){
				Pak125 decrypted = new Pak125(receivePacket, true, stamp, micros);
			}
		}
	}
}
