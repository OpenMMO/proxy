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

import com.fortytwo.opent4c.screens.InfoScreen;
import com.fortytwo.opent4c.screens.MonitorScreen;
import com.fortytwo.opent4c.tools.HexString;
import com.fortytwo.opent4c.tools.Log;

/**
 * Manages multiple clients. Receives all client messages and dispatches the to the right ClientTunnel.
 * @author aTom
 *
 */
public class ClientManager {
	private static ExecutorService exService;
	public static DatagramSocket clientsocket;
	private static Runnable clientSendPile;
	private static Runnable clientListener;
	private static boolean online = true;
	public static List<Client> cls = Collections.synchronizedList(new ArrayList<Client>());
	//public static List<ClientTunnel> clients = Collections.synchronizedList(new ArrayList<ClientTunnel>());
	//public static List<ServerTunnel> servers = Collections.synchronizedList(new ArrayList<ServerTunnel>());
	public static List<DatagramPacket> sendpile = Collections.synchronizedList(new ArrayList<DatagramPacket>());
	private static ExecutorService dispatcher;


	
	public ClientManager() throws SocketException {
		exService = Executors.newFixedThreadPool(2);
		dispatcher = Executors.newSingleThreadExecutor();
		createClientSocket();
		startClientSendPile();
		startClientListener();
	}
	
	/**
	 * Creates a server socket listening for client messages on given port
	 * @throws SocketException 
	 */
	private static void createClientSocket() throws SocketException {
		clientsocket = new DatagramSocket(ProxyManager.proxyPort);
		Log.client.info("Client socket created");
		InfoScreen.writeConsoleLines("Client socket created");
	}

	/**
	 * Starts waiting for messages to send to client
	 */
	private static void startClientSendPile() {
		clientSendPile = new Runnable(){
			public void run(){
				try {
					Log.client.info("Client send pile ready");
					InfoScreen.writeConsoleLines("Client send pile ready");
					while(online){
						while(sendpile.size() != 0){
							sendToClient(sendpile.get(0));
							sendpile.remove(0);
						}
						Thread.sleep(ProxyManager.netspeed);
					}
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
	private static void sendToClient(DatagramPacket packet) throws IOException {
		clientsocket.send(packet);
		Log.client.debug("Message to client sent : "+packet.getAddress()+":"+packet.getPort());
	}
	
	/**
	 * Starts waiting for client messages
	 */
	private static void startClientListener() {
		clientListener = new Runnable(){
			public void run(){
				try {
					Log.client.info("Listening for client messages");
					InfoScreen.writeConsoleLines("Listening for client messages");
					while(online){
						listen();						
					}
				} catch (IOException | InterruptedException e) {
					Log.client.fatal(e.toString());
					ProxyManager.quit();
				}
			}
		};
		exService.submit(clientListener);
	}
	
	/**
	 * Waits for a message to be received from clients and dispatches it.
	 * Timestamping is useful for logging.
	 * As we don't know packet size before receiving it, we always prepare a max sized buffer (1024 bytes).
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void listen() throws IOException, InterruptedException {
		byte[]receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);			
		clientsocket.receive(receivePacket);
		short length = getPakLength(receiveData);
		Log.proxy.fatal(HexString.from(receiveData));
		byte[] data = new byte[length];
		for (int i = 0 ; i < length ; i++){
			data[i] = receiveData[i];
		}
		long micros = ((System.nanoTime()-ProxyManager.startTime)%1000000)/1000;
		long stamp = System.currentTimeMillis();
		int port = receivePacket.getPort();
		Log.client.debug("Message received");
		if(!isKnownClient(port)){
			addClient(receivePacket.getAddress(), port);
			dispatchMessage(port, stamp, micros, data);
		}else{
			dispatchMessage(port, stamp, micros, data);
		}
	}
	
	/**
	 * Packet is writen in bytes 2 and 3 of received data so we get it from there
	 * If it is 0, we set it to 12 because it's a pong packet.
	 * @param data
	 * @return
	 */
	public static short getPakLength(byte[] data) {
		byte b1,b2;
		b1 = data[2];
		b2 = data[3];
		short l = (short) ((b2<<8 & 0xFF00)|(b1 & 0x00FF));
		if (l == 0) l = 12;
		MonitorScreen.writeConsoleLines("PAK LENGTH : "+l);
		return l;
	}

	/**
	 * Creates a new client, that is, keep client's port to identify it and create client socket to server
	 * @param ip
	 * @param port
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private static void addClient(InetAddress ip, int port) throws InterruptedException, IOException {
		cls.add(new Client(ip,port));
	}

	/**
	 * Dispatches messages to the right clientTunnel, to be sent to server.
	 * @param port
	 * @param stamp
	 * @param micros
	 */
	private static void dispatchMessage(final int port, final long stamp, final long micros, final byte[] data) {
		Runnable r = new Runnable(){
			public void run(){
				Client c = getClByPort(port);
				MonitorScreen.writeConsoleLines("Message dispatched @"+c.ip.getHostAddress()+":"+c.port);
				c.getMessage(stamp, micros, data);
			}
		};
		dispatcher.submit(r);		
	}

	/**
	 * Tells if a client is already known.
	 * Each client has a unique port, so we check that.
	 * @param ip
	 * @param port
	 * @return
	 */
	private static boolean isKnownClient(int port) {
		Iterator<Client> it = cls.iterator();
		while(it.hasNext()){
			Client c = it.next();
			if(c.port == port) return true;
		}
		return false;
	}
	
	/**
	 * Returns a client from a port number
	 * @param port
	 * @return
	 */
	public static Client getClByPort(int port) {
		Iterator<Client> it = cls.iterator();
		while(it.hasNext()){
			Client c = it.next();
			if(c.port == port) return c;
		}
		return null;
	}
	
	/**
	 * Adds a message to send pile, it will be sent to a client.
	 * @param packet
	 */
	public static void pile(DatagramPacket packet) {
		sendpile.add(packet);
		Log.client.debug("Message to client added to pile ("+sendpile.size()+")");
	}

	public static int getClientsSize() {
		return cls.size();
	}
	

}
