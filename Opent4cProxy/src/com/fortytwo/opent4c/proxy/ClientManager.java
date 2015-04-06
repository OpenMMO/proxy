package com.fortytwo.opent4c.proxy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fortytwo.opent4c.tools.Log;

/**
 * Manages multiple clients. Receives all client messages and dispatches the to the right ClientTunnel.
 * @author syno
 *
 */
public class ClientManager {
	private static ExecutorService exService;
	private static DatagramSocket socket;
	private static Runnable clientSendPile;
	private static Runnable clientListener;
	private static boolean online = true;
	public static List<ClientTunnel> clients = Collections.synchronizedList(new ArrayList<ClientTunnel>());
	public static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static Runnable dispatcher;

	
	public ClientManager() throws SocketException {
		exService = Executors.newFixedThreadPool(2+Proxy.max_clients);
		createSocket();
		startSendPile();
		startClientListener();
	}
	
	/**
	 * Creates a server socket listening for client messages on given port
	 * @throws SocketException 
	 */
	private static void createSocket() throws SocketException {
		socket = new DatagramSocket(Proxy.proxyPort);
		Log.client.info("Client socket created");
	}

	/**
	 * Starts waiting for messages to send to client
	 */
	private static void startSendPile() {
		clientSendPile = new Runnable(){
			public void run(){
				try {
					Log.client.info("Client send pile ready");
					while(online && sendpile.size() != 0){
						send(sendpile.get(0));
						sendpile.remove(0);
					}
					Thread.sleep(0,1000);
				} catch (InterruptedException | IOException e) {
					Log.client.fatal(e.toString());
					System.exit(1);
				}
			}
		};
		exService.submit(clientSendPile);		
	}
	
	/**
	 * Sends a message to client
	 * @param packet
	 * @throws IOException 
	 */
	private static void send(DatagramPacket packet) throws IOException {
		socket.send(packet);
		Log.client.debug("Message to client sent");
	}
	
	/**
	 * Starts waiting for client messages
	 */
	private static void startClientListener() {
		clientListener = new Runnable(){
			public void run(){
				try {
					listen();
				} catch (IOException e) {
					Log.client.fatal(e.toString());
					System.exit(1);
				}
			}
		};
		exService.submit(clientListener);
	}
	
	/**
	 * Waits for a message to be received from clients and dispatches it.
	 * Timestamping will be useful later.
	 * As we don't know packet size before receiving it, we always prepare a maxed size buffer (1024 bytes).
	 * @throws IOException
	 */
	public static void listen() throws IOException {
		DatagramPacket receivePacket;
		byte[] receiveData;
		long micros;
		long stamp;
		int port;
		Log.client.info("Listening for client messages");
		while(online){
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			Log.client.debug("Message received");
			micros = ((System.nanoTime()-Proxy.startTime)%1000000)/1000;
			stamp = System.currentTimeMillis();
			port = receivePacket.getPort();
			if(!isKnownClient(port)){
				InetAddress ip = receivePacket.getAddress();
				clients.add(new ClientTunnel(ip, port));
				dispatchMessage(port, stamp, micros, receivePacket);
			}else{
				dispatchMessage(port, stamp, micros, receivePacket);
			}
		}
	}
	
	/**
	 * Dispatches a received messages to the right clientTunnel
	 * @param port
	 * @param stamp
	 * @param micros
	 * @param receivePacket
	 */
	private static void dispatchMessage(final int port, final long stamp, final long micros, final DatagramPacket receivePacket) {
		dispatcher = new Runnable(){
			public void run(){
				ClientTunnel c = getClientByPort(port);
				Log.client.debug("Message dispatched @"+c.ip.getHostAddress()+":"+c.port);
				c.getMessage(stamp, micros, receivePacket);
			}
		};
		exService.submit(dispatcher);		
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
	 * Returns a client from a port number
	 * @param port
	 * @return
	 */
	public static ClientTunnel getClientByPort(int port) {
		Iterator<ClientTunnel> it = clients.iterator();
		while(it.hasNext()){
			ClientTunnel c = it.next();
			if(c.port == port) return c;
		}
		return null;
	}
	
	/**
	 * Adds a message to send pile
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
		Log.client.debug("Message to client added to pile ("+sendpile.size()+")");
	}
}
