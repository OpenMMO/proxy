package com.fortytwo.opent4c.bot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fortytwo.opent4c.proxy.ClientManager;
import com.fortytwo.opent4c.proxy.ClientTunnel;
import com.fortytwo.opent4c.proxy.Pak150;
import com.fortytwo.opent4c.proxy.Proxy;
import com.fortytwo.opent4c.proxy.ServerTunnel;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;
import com.fortytwo.opent4c.tools.PakTypes;

public class Bot150 {
	private static Runnable serverTunnel;
	private List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private boolean online = true;
	private DatagramSocket toServerSocket;
	public int port;
	private InetAddress ip;
	private Runnable sendPile;
	private ExecutorService exService = Executors.newFixedThreadPool(2);

	public Bot150(){
		try {
			ip = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		port = 4242;
		create();
	}
	
	public void create() {
		Log.bot.fatal("Creating Bot 150");
		DatagramPacket sendPacket = null;
		try {
			connect();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Pak150 handshake = new Pak150(PakTypes.PAK_CLIENT_MessageOfTheDay, Pak150.CLIENT_TO_SERVER, new byte[]{}, (byte) 0, true, false, false, 0, 0);
		sendPacket = new DatagramPacket(handshake.getSendData().array(), handshake.getSendData().array().length, Proxy.serverAddress, Proxy.serverPort);
		pile(sendPacket);
	}

	private void connect() throws SocketException {
		createSocket();
		startServerSendPile();
		openServerTunnel();
	}
	
	/**
	 * Waits for a message to be sent to server
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private void startSendPile() throws InterruptedException, IOException {
		Log.bot.info("Server send pile ready for "+ip.toString()+":"+port);
		while(online){
			while(sendpile.size() != 0){
				send(sendpile.get(0));
				sendpile.remove(0);
			}
			Thread.sleep(Proxy.netspeed);
		}
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
		Log.bot.info("Listening for server messages for client "+port);
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
				Log.bot.info("Received Message from server for client "+":"+port);
				if (Proxy.serverVersion == 150){
					received = new Pak150(data, Pak150.SERVER_TO_CLIENT, stamp, micros);
				}
		}
	}
	
	private short getLength(byte[] data) {
		byte b1,b2;
		b1 = data[2];
		b2 = data[3];
		short l = (short) ((b2<<8 & 0xFF00)|(b1 & 0x00FF));
		if (l == 0) l = 16;
		Log.bot.debug("PAK LENGTH : "+l);
		return l;
	}
	
	/**
	 * Creates a client socket to server.
	 * @throws SocketException 
	 */
	public void createSocket() throws SocketException {
		toServerSocket = new DatagramSocket();
		Log.bot.info("Server Socket created");
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
					Log.bot.fatal(e.toString());
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
					Log.bot.fatal(e.toString());
					System.exit(1);
				}
			}
		};
		exService.submit(serverTunnel);		
	}
	
	/**
	 * Sends a message to server
	 * @param packet
	 * @throws IOException 
	 */
	private void send(DatagramPacket packet) throws IOException {
		toServerSocket.send(packet);
		Log.bot.debug("Message sent to server from client "+ip.toString()+":"+port);
	}
	
	/**
	 * Adds messages to be sent to server
	 * @param packet
	 */
	public void pile(DatagramPacket packet) {
		sendpile.add(packet);
		Log.bot.trace("Message to server added to pile ("+sendpile.size()+")");
	}
}
