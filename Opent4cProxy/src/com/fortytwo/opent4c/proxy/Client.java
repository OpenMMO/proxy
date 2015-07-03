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

import com.fortytwo.opent4c.netcode150.Pak150;
import com.fortytwo.opent4c.screens.InfoScreen;
import com.fortytwo.opent4c.screens.MonitorScreen;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;

public class Client {
	private static boolean online = true;
	private List<DatagramPacket> serverSendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static ExecutorService exService;
	public int port = -1;
	public InetAddress ip;
	private DatagramSocket toServerSocket;
	private int serverPort;
	private Runnable serverTunnel;


	public Client(InetAddress ip, int port) throws InterruptedException, IOException{
		this.port = port;
		this.ip = ip;
		InfoScreen.writeConsoleLines("New client : "+ip.toString()+":"+port);
		exService = Executors.newFixedThreadPool(2);
		createServerSocket();
		startServerSendPile();
		openServerTunnel();
	}
	
	/**
	 * Waits for a message to be sent to server
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private void startServerSendPile() throws InterruptedException, IOException {
		Runnable r = new Runnable(){
			public void run(){
				Log.server.info("Server send pile ready for "+ip.toString()+":"+port);
				InfoScreen.writeConsoleLines("Server send pile ready for "+ip.toString()+":"+port);

				while(online){
					while(serverSendpile.size() != 0){
						try {
							sendToServer(serverSendpile.remove(0));
						} catch (IOException e) {
							Log.server.fatal(e.toString());
							ProxyManager.quit();
						}
					}
					try {
						Thread.sleep(ProxyManager.netspeed);
					} catch (InterruptedException e) {
						Log.server.fatal(e.toString());
						ProxyManager.quit();
					}
				}
			}
		};
		exService.submit(r);
	}
	
	/**
	 * Creates a client socket to server.
	 * @throws SocketException 
	 */
	public void createServerSocket() throws SocketException {
		toServerSocket = new DatagramSocket();
		Log.server.info("Server Socket created");
		InfoScreen.writeConsoleLines("Server Socket created");
		serverPort = toServerSocket.getPort();
	}
	
	/**
	 * Starts waiting for server messages
	 */
	public void openServerTunnel() {
		serverTunnel = new Runnable(){
			public void run(){
				try {
					Log.server.info("Listening for server messages for client "+port);
					InfoScreen.writeConsoleLines("Listening for server messages for client "+port);

					while (online){
						listen();
					}
				} catch (IOException e) {
					Log.server.fatal(e.toString());
					ProxyManager.quit();
				}
			}
		};
		exService.submit(serverTunnel);		
	}
	
	/**
	 * Waits for a message to be received from server modifies it and sends it to client.
	 * Timestamping will be useful later.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @throws IOException
	 */
	private void listen() throws IOException {
		DatagramPacket sendPacket = null;
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		toServerSocket.receive(receivePacket);
		short length = ClientManager.getPakLength(receiveData);
		byte[] data = new byte[length];
		for (int i = 0 ; i< length ; i++){
			data[i] = receiveData[i];
		}
		long micros = ((System.nanoTime()-ProxyManager.startTime)%1000000)/1000;
		long stamp = System.currentTimeMillis();
		if(!ProxyManager.sniffer){
			Log.server.debug("Received Message from server for client "+ip+":"+port);
			if (ProxyManager.serverVersion == 150){
				//Pak150 received = new Pak150(data, Pak150.SERVER_TO_CLIENT, stamp, micros);
				//ByteBuffer toSend = received.getSendData();
				//Log.server.debug(HexString.from(toSend.array()));
				//sendPacket = new DatagramPacket(toSend.array(), toSend.array().length, ip, port);
			}else if (ProxyManager.serverVersion == 125){
				//Pak125 decrypted = new Pak125(receivePacket, Pak150.SERVER_TO_CLIENT, stamp, micros);
			}
			sendToClient(sendPacket);
		}else{
			Log.proxy.info("S=>C : "+HexString.from(data));
			sendPacket = new DatagramPacket(data, data.length, ip, port);
			sendToClient(sendPacket);
		}
	}
	
	/**
	 * Sends a message to server
	 * @param packet
	 * @throws IOException 
	 */
	private void sendToServer(DatagramPacket packet) throws IOException {
		toServerSocket.send(packet);
		Log.server.debug("Message sent to server from client "+ip.toString()+":"+port);
	}
	
	/**
	 * Sends a message to client
	 * @param packet
	 * @throws IOException 
	 */
	private static void sendToClient(DatagramPacket packet) throws IOException {
		ClientManager.pile(packet);
		Log.client.debug("Message to client sent : "+packet.getAddress()+":"+packet.getPort());
	}

	/**
	 * Waits for a message to be received from a client, modifies it and sends it to the server.
	 * Timestamping will be useful later, for logging with micro precision, compute latencies and such.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @param receivePacket 
	 * @param micros 
	 * @param stamp 
	 */
	public void getMessage(long stamp, long micros, byte[] data){
		DatagramPacket sendPacket = null;
		if(!ProxyManager.sniffer){
			if (ProxyManager.clientVersion == 150){
				
				//Pak150 received = new Pak150(data, Pak150.CLIENT_TO_SERVER, stamp, micros);
				ByteBuffer toSend = Translator.computeClientToServer(stamp, micros, data);
				Log.client.debug(HexString.from(toSend.array()));
				sendPacket = new DatagramPacket(toSend.array(), toSend.array().length, ProxyManager.serverAddress, ProxyManager.serverPort);
			}else if (ProxyManager.clientVersion == 125){
				//Pak125 decrypted = new Pak125(data, false, stamp, micros);
			}
			pile(sendPacket);
		}else{
			MonitorScreen.writeConsoleLines(HexString.from(data));
			sendPacket = new DatagramPacket(data, data.length, ProxyManager.serverAddress, ProxyManager.serverPort);
			pile(sendPacket);
		}
	}
	
	/**
	 * Adds messages to be sent to server
	 * @param packet
	 */
	public void pile(DatagramPacket packet) {
		serverSendpile.add(packet);
		Log.server.trace("Message to server added to pile ("+serverSendpile.size()+")");
	}
}
