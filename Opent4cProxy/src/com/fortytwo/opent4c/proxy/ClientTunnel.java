package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;

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
	private static boolean online = true;
	public int port = -1;
	public InetAddress ip;
	public static List<ClientTunnel> clients = Collections.synchronizedList(new ArrayList<ClientTunnel>());
	public static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static DatagramSocket fromClientSocket;
	
	public ClientTunnel(InetAddress ip, int port) {
		this.port = port;
		this.ip = ip;
		clients.add(this);
		Log.client.fatal("New client : "+ip.toString()+":"+port);
	}

	/**
	 * Adds a message to send pile
	 * @param packet
	 */
	public void pile(DatagramPacket packet) {
		sendpile.add(packet);
		Log.client.fatal("Message to client added to pile ("+sendpile.size()+")");
	}
	
	/**
	 * Waits for a message to be sent to client. Runs in its own Thread managed by Proxy.class
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static void startSendPile() throws InterruptedException, IOException {
		Log.client.fatal("Client send pile ready");
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
		fromClientSocket.send(packet);
		Log.client.fatal("Message to client sent");
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
		Log.client.fatal("Listening for client messages");
		while(online){
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			fromClientSocket.receive(receivePacket);
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			int port = receivePacket.getPort();
			InetAddress ip = receivePacket.getAddress();
			if(!isKnownClient(port)){
				new ClientTunnel(ip, port);
			}
			if (Proxy.clientVersion == 150){
				Pak150 decrypted = new Pak150(receivePacket, false, stamp, micros);
				Pak150 encrypted = new Pak150(decrypted.getType(), false, decrypted.getData());
				//Log.client.fatal(HexString.from(encrypted.getData()));
				DatagramPacket sendPacket = new DatagramPacket(encrypted.getData(), encrypted.getData().length, Proxy.serverAddress, Proxy.serverPort);
				new Pak150(sendPacket, false, stamp, micros);
				//ServerTunnel.pile(sendPacket);
				//sendData = decrypted.getSendData();
			}else if (Proxy.clientVersion == 125){
				Pak125 decrypted = new Pak125(receivePacket, false, stamp, micros);
				sendData = decrypted.getSendData();
			}
			//DatagramPacket sendPacket = new DatagramPacket(receiveData, receiveData.length, Proxy.serverAddress, Proxy.serverPort);
//			DatagramPacket sendPacket = new DatagramPacket(sendData.array(), sendData.array().length, Proxy.serverAddress, Proxy.serverPort);
			//ServerTunnel.pile(sendPacket);
		}
	}

	/**
	 * Tells if a client is already known
	 * @param ip
	 * @param port
	 * @return
	 */
	private static boolean isKnownClient(int port) {
		Iterator<ClientTunnel> it = clients.iterator();
		while(it.hasNext()){
			ClientTunnel c = it.next();
			if(c.port == port) return true;
		}
		return false;
	}

	/**
	 * Creates a server socket listening for client messages on given port
	 * @param proxyPort
	 * @throws SocketException 
	 */
	public static void createSocket(int proxyPort) throws SocketException {
		fromClientSocket = new DatagramSocket(proxyPort);
		Log.client.fatal("Client Socket created");
	}

	public static ClientTunnel getByPort(int port) {
		Iterator<ClientTunnel> it = clients.iterator();
		while(it.hasNext()){
			ClientTunnel c = it.next();
			if(c.port == port) return c;
		}
		return null;
	}
}
