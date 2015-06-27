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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;

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
	private static Runnable serverTunnel;
	private List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private boolean online = true;
	private DatagramSocket toServerSocket;
	public int port;
	private InetAddress ip;
	private Runnable sendPile;
	private ExecutorService exService = Executors.newFixedThreadPool(2);

	
	public ServerTunnel(InetAddress ip, int port) {
		this.port = port;
		this.ip = ip;
	}

	/**
	 * Adds messages to be sent to server
	 * @param packet
	 */
	public void pile(DatagramPacket packet) {
		sendpile.add(packet);
		Log.server.trace("Message to server added to pile ("+sendpile.size()+")");
	}

	/**
	 * Waits for a message to be sent to server
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private void startSendPile() throws InterruptedException, IOException {
		Log.server.info("Server send pile ready for "+ip.toString()+":"+port);
		while(online){
			while(sendpile.size() != 0){
				send(sendpile.get(0));
				sendpile.remove(0);
			}
			Thread.sleep(Proxy.netspeed);
		}
	}

	/**
	 * Sends a message to server
	 * @param packet
	 * @throws IOException 
	 */
	private void send(DatagramPacket packet) throws IOException {
		toServerSocket.send(packet);
		Log.server.debug("Message sent to server from client "+ip.toString()+":"+port);
	}

	/**
	 * Waits for a message to be received from server modifies it and sends it to client.
	 * Timestamping will be useful later.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @throws IOException
	 */
	private void listen() throws IOException {
		DatagramPacket receivePacket;
		byte[] receiveData;
		long micros;
		long stamp;
		Log.server.info("Listening for server messages for client "+port);
		DatagramPacket sendPacket = null;
		ClientTunnel c;
		Pak150 received;
		while(online){
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			toServerSocket.receive(receivePacket);
			short length = getLength(receiveData);
			byte[] data = new byte[length];
			for (int i =0 ; i< length ; i++){
				data[i] = receiveData[i];
			}
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			c = ClientManager.getClientByPort(port);
			if(!Proxy.sniffer){
				Log.server.debug("Received Message from server for client "+c.ip+":"+c.port);
				if (Proxy.serverVersion == 150){
					received = new Pak150(data, Pak150.SERVER_TO_CLIENT, stamp, micros);
					ByteBuffer toSend = received.getSendData();
					Log.server.debug(HexString.from(toSend.array()));
					sendPacket = new DatagramPacket(toSend.array(), toSend.array().length, c.ip, c.port);
				}else if (Proxy.serverVersion == 125){
					//Pak125 decrypted = new Pak125(receivePacket, Pak150.SERVER_TO_CLIENT, stamp, micros);
				}
				ClientManager.pile(sendPacket);
			}else{
				Log.proxy.info("S=>C : "+HexString.from(data));
				sendPacket = new DatagramPacket(data, data.length, c.ip, c.port);
				ClientManager.pile(sendPacket);
			}
		}
	}
	
	private short getLength(byte[] data) {
		byte b1,b2;
		b1 = data[2];
		b2 = data[3];
		short l = (short) ((b2<<8 & 0xFF00)|(b1 & 0x00FF));
		if (l == 0) l = 16;
		Log.proxy.debug("PAK LENGTH : "+l);
		return l;
	}
	
	/**
	 * Creates a client socket to server.
	 * @throws SocketException 
	 */
	public void createSocket() throws SocketException {
		toServerSocket = new DatagramSocket();
		Log.server.info("Server Socket created");
	}
	
	/**
	 * Starts waiting for messages to send to server
	 */
	public void startServerSendPile() {
		sendPile = new Runnable(){
			public void run(){
				try {
					startSendPile();
				} catch (InterruptedException | IOException e) {
					Log.server.fatal(e.toString());
					System.exit(1);
				}
			}
		};
		exService.submit(sendPile);		
	}
	
	/**
	 * Starts waiting for server messages
	 */
	public void openServerTunnel() {
		serverTunnel = new Runnable(){
			public void run(){
				try {
					listen();
				} catch (IOException e) {
					Log.server.fatal(e.toString());
					System.exit(1);
				}
			}
		};
		exService.submit(serverTunnel);		
	}
	
}
