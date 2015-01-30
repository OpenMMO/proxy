package com.fortytwo.opent4c.proxy;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Proxy {
	public static long startTime;
	public static int proxyPort = 11677;
	public static int serverPort = 11679;
	public static int clientVersion = 150;//0 for opent4c
	public static int serverVersion = 150;//0 for opent4c
	public static InetAddress serverAddress;
	private static ExecutorService exService = Executors.newFixedThreadPool(5);
	private static int paramNumber;
	private static Map<Integer,String> params = new HashMap<Integer,String>();
	private static final String usage = "java -jar proxy.jar -p PROXYPORT -sp SERVERPORT -c CLIENTVERSION -s SERVERVERSION -ip SERVERADDRESS"+System.lineSeparator()+"ie : java -jar proxy.jar -p 11677 -sp 11678 -c 150 -s 150 -ip 127.0.0.1";
	public static DatagramSocket toServerSocket;
	public static DatagramSocket fromClientSocket;
	
	public static void main(String[] args) throws IOException {
		/*MSRand rnd = new MSRand(0x11223344);
		for (int i = 0 ; i<100 ; i++){
			System.out.println(rnd.prng());
		}*/
		while(System.currentTimeMillis() % 1000 != 0){
			startTime = System.nanoTime();
		}
		serverAddress = InetAddress.getByName("192.168.1.6");
		parseArgs(args);
		createSockets();
		startSendPiles();
		openTunnels();
		//Sniffer.main(null);
	}

	/**
	 * Creates client and server sockets
	 * we are server for clients and client for server
	 * @throws SocketException
	 */
	private static void createSockets() throws SocketException {
		System.out.println("Sending messages to server on port : "+serverPort);
		toServerSocket = new DatagramSocket();
		System.out.println("Listening for client messages on port : "+proxyPort);
		fromClientSocket = new DatagramSocket(proxyPort);		
	}

	/**
	 * Opens tunnels
	 * @throws SocketException
	 */
	private static void openTunnels() throws SocketException {
		Runnable clientTunnel = new Runnable(){
			public void run(){
				try {
					ClientTunnel.listen();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		exService.submit(clientTunnel);

		Runnable serverTunnel = new Runnable(){
			public void run(){
				try {
					ServerTunnel.listen();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		exService.submit(serverTunnel);
	}

	/**
	 * parses program arguments
	 * @param args
	 * @throws UnknownHostException
	 */
	private static void parseArgs(String[] args) throws UnknownHostException {
		paramNumber = args.length;
		if(paramNumber != 0){
			for (int i = 0 ; i < paramNumber ; i++){
				params.put(i, args[i]);
			}
			Iterator<Integer> iter_param = params.keySet().iterator();
			while(iter_param.hasNext()){
				int key = iter_param.next();
				String value = params.get(key);
				if(iter_param.hasNext()){
					try{
						if (value.equals("-p") || value.equals("--port")){
							proxyPort = Integer.parseInt(params.get(key+1));
						}
						if (value.equals("-sp") || value.equals("--server-port")){
							serverPort = Integer.parseInt(params.get(key+1));
						}
						if (value.equals("-c") || value.equals("--client-version")){
							clientVersion = Integer.parseInt(params.get(key+1));
						}
						if (value.equals("-s") || value.equals("--server-version")){
							serverVersion = Integer.parseInt(params.get(key+1));
						}
						if (value.equals("-ip") || value.equals("--server-address")){
							serverAddress = InetAddress.getByName(params.get(key+1));
						}
					}catch (NumberFormatException e){
						System.out.println(usage);
						System.exit(1);						
					}
				}
			}
		}
		System.out.println("Proxy port set to : "+proxyPort);
		System.out.println("Server port set to : "+serverPort);
		System.out.println("Client version set to : "+clientVersion);
		System.out.println("Server version set to : "+serverVersion);
		System.out.println("Server address set to : "+serverAddress);		
	}


	/**
	 * creates threads to wait for messages to be sent.
	 */
	private static void startSendPiles() {
		Runnable clientSendPile = new Runnable(){
			public void run(){
				try {
					ClientTunnel.startSendPile();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		exService.submit(clientSendPile);
		
		Runnable serverSendPile = new Runnable(){
			public void run(){
				try {
					ServerTunnel.startSendPile();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};
		exService.submit(serverSendPile);
	}
}
