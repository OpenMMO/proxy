package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages client<->proxy connection.
 * We have a thread looking periodically to a thread-safe list's size.
 * If it's not empty, we send the first element and delete it from the list.
 * If it's empty we wait for a while.
 * 
 * We have another thread waiting for messages from client, modifying them, and adding them to server send pile.
 * 
 * @author syno
 *
 */
public class ClientTunnel {
	//TODO have to instantiate ClientTunnel to manage more than 1 client at a time
	private static boolean online = true;
	public static int port = -1;
	public static InetAddress ip;
	private static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static DatagramSocket fromClientSocket;
	
	public ClientTunnel() {}

	/**
	 * Adds a message to send pile
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
	}
	
	/**
	 * Waits for a message to be sent to client. Runs in its own Thread managed by Proxy.class
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static void startSendPile() throws InterruptedException, IOException {
		while(online){
			while(sendpile.size() != 0){
				send(sendpile.get(0));
				sendpile.remove(0);
			}
			Thread.sleep(0,1000);
		}
	}

	/**
	 * Sends a message to client
	 * @param packet
	 * @throws IOException 
	 */
	private static void send(DatagramPacket packet) throws IOException {
		ClientTunnel.fromClientSocket.send(packet);
	}

	/**
	 * Waits for a message to be received from client modifies it and sends it to server.
	 * Timestamping will be useful later.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @throws IOException
	 */
	public static void listen() throws IOException {
		DatagramPacket receivePacket;
		byte[] receiveData;
		long micros;
		long stamp;
		ByteBuffer sendData = null;
		while(online){
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			fromClientSocket.receive(receivePacket);
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			port = receivePacket.getPort();
			ip = receivePacket.getAddress();
			if (Proxy.clientVersion == 150){
				Pak150 decrypted = new Pak150(receivePacket, false, stamp, micros);
				sendData = decrypted.getSendData();
			}else if (Proxy.clientVersion == 125){
				Pak125 decrypted = new Pak125(receivePacket, false, stamp, micros);
				sendData = decrypted.getSendData();
			}
			DatagramPacket sendPacket = new DatagramPacket(sendData.array(), sendData.array().length, Proxy.serverAddress, Proxy.serverPort);
			ServerTunnel.pile(sendPacket);
		}
	}

	/**
	 * Creates a server socket listening for client messages on given port
	 * @param proxyPort
	 * @throws SocketException 
	 */
	public static void createSocket(int proxyPort) throws SocketException {
		fromClientSocket = new DatagramSocket(proxyPort);
	}
}
