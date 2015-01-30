package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fortytwo.opent4c.tools.ByteArrayToHexString;
import com.fortytwo.opent4c.tools.UdpUtils;

public class ClientTunnel {
	private static boolean online = true;
	public static int port = -1;
	public static InetAddress ip;
	private static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	
	public ClientTunnel() {}

	/**
	 * adds a message to be sent to client
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
	}
	
	/**
	 * waits for a message to be sent to client
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
	 * send a message to client
	 * @param packet
	 * @throws IOException 
	 */
	private static void send(DatagramPacket packet) throws IOException {
		Proxy.fromClientSocket.send(packet);
	}

	/**
	 * waits for a message to be received from client.
	 * @throws IOException
	 */
	public static void listen() throws IOException {
		byte[] receiveData = new byte[1024];
		while(online){
			long stamp = 0;
			long micros = 0;
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			Proxy.fromClientSocket.receive(receivePacket);
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			port = receivePacket.getPort();
			ip = receivePacket.getAddress();
			byte[] sendData = UdpUtils.extractSendData(receiveData);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, Proxy.serverAddress, Proxy.serverPort);
			ServerTunnel.pile(sendPacket);
			if (Proxy.clientVersion == 150){
				Pak150 decrypted = new Pak150(receivePacket, false, stamp, micros);
			}else if (Proxy.clientVersion == 125){
				Pak125 decrypted = new Pak125(receivePacket, false, stamp, micros);
			}
		}
	}
}
