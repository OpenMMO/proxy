package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages proxy<->server connection.
 * We have a thread looking periodically to a thread-safe list's size.
 * If it's not empty, we send the first element and delete it from the list.
 * If it's empty we wait for a while.
 * 
 * We have another thread waiting for messages from server, modifying them, and adding them to client send pile.
 * 
 * @author syno
 *
 */
public class ServerTunnel {
	private static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static boolean online = true;
	private static DatagramSocket toServerSocket;
	
	public ServerTunnel() {}

	/**
	 * Adds messages to be sent to server
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
	}

	/**
	 * Waits for a message to be sent to server
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
	 * Sends a message to server
	 * @param packet
	 * @throws IOException 
	 */
	private static void send(DatagramPacket packet) throws IOException {
		ServerTunnel.toServerSocket.send(packet);
	}

	/**
	 * Waits for a message to be received from server modifies it and sends it to client.
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
			toServerSocket.receive(receivePacket);
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			if (Proxy.serverVersion == 150){
				Pak150 decrypted = new Pak150(receivePacket, true, stamp, micros);
				sendData = decrypted.getSendData();
			}else if (Proxy.serverVersion == 125){
				Pak125 decrypted = new Pak125(receivePacket, true, stamp, micros);
				sendData = decrypted.getSendData();
			}
			DatagramPacket sendPacket = new DatagramPacket(sendData.array(), sendData.array().length, ClientTunnel.ip, ClientTunnel.port);
			ClientTunnel.pile(sendPacket);
		}
	}
	
	/**
	 * Creates a client socket to server.
	 * @throws SocketException 
	 */
	public static void createSocket() throws SocketException {
		toServerSocket = new DatagramSocket();
	}
}
