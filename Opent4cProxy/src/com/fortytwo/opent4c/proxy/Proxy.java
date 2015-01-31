package com.fortytwo.opent4c.proxy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Proxy for translating T4C paks form version to version between client and server, and to translate T4C paks to and from openT4C paks.
 * @author syno
 *
 */
public class Proxy {
	public static long startTime;
	public static int proxyPort = 11679;
	public static int serverPort = 11677;//11677 is default server port, use T4C port patcher to modify T4C Server.exe
	public static int clientVersion = 150;//0 for opent4c
	public static int serverVersion = 150;//0 for opent4c
	public static InetAddress serverAddress;
	private static ExecutorService exService = Executors.newFixedThreadPool(4);
	private static final String usage = "java -jar proxy.jar -p PROXYPORT -sp SERVERPORT -c CLIENTVERSION -s SERVERVERSION -ip SERVERADDRESS"+System.lineSeparator()+"ie : java -jar proxy.jar -p 11677 -sp 11678 -c 150 -s 150 -ip 127.0.0.1";
	private static Runnable clientSendPile;
	private static Runnable serverSendPile;
	private static Runnable serverTunnel;
	private static Runnable clientTunnel;
	
	public static void main(String[] args) throws IOException {
		/*MSRand rnd = new MSRand(0x11223344);
		for (int i = 0 ; i<100 ; i++){
			System.out.println(rnd.prng());
		}*/
		Pak150.test();
		/*while(System.currentTimeMillis() % 1000 != 0){
			startTime = System.nanoTime();
		}
		serverAddress = InetAddress.getByName("192.168.1.6");
		parseArgs(args);
		createSockets();
		startSendPiles();
		openTunnels();*/
		//Sniffer.main(null);
	}

	/**
	 * parses program arguments or uses defaults
	 * @param args
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 */
	private static void parseArgs(String[] args) throws UnknownHostException, NumberFormatException {
		Map<Integer,String> params = new HashMap<Integer,String>();
		if(args.length != 0){
			for (int i = 0 ; i < args.length ; i++){
				params.put(i, args[i]);
			}
			Iterator<Integer> iter_param = params.keySet().iterator();
			while(iter_param.hasNext()){
				int key = iter_param.next();
				String param = params.get(key);
				if(iter_param.hasNext()){
					key = iter_param.next();
					String value = params.get(key);
					if (param.equals("-p") || param.equals("--port")){
						proxyPort = Integer.parseInt(value);
					}else if (param.equals("-sp") || param.equals("--server-port")){
						serverPort = Integer.parseInt(value);
					}else if (param.equals("-c") || param.equals("--client-version")){
						clientVersion = Integer.parseInt(value);
					}else if (param.equals("-s") || param.equals("--server-version")){
						serverVersion = Integer.parseInt(params.get(key+1));
					}else if (param.equals("-ip") || param.equals("--server-address")){
						serverAddress = InetAddress.getByName(value);
					}else{
						System.err.println("Unknown parameter : "+param);
						System.err.println(usage);
						System.exit(1);
					}
				}else{
					System.err.println("Parameter needs a value : "+param);
					System.err.println(usage);
					System.exit(1);
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
	 * Creates client and server sockets
	 * we are server for client and client for server
	 * @throws SocketException
	 */
	private static void createSockets() throws SocketException {
		System.out.println("Sending messages to server on port : "+serverPort);
		ServerTunnel.createSocket();
		System.out.println("Listening for client messages on port : "+proxyPort);
		ClientTunnel.createSocket(proxyPort);		
	}

	/**
	 * Starts waiting for messages
	 */
	private static void openTunnels() {
		openClientTunnel();
		openServerTunnel();
	}

	/**
	 * Starts waiting for client messages
	 */
	private static void openClientTunnel() {
		clientTunnel = new Runnable(){
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
	}

	/**
	 * Starts waiting for server messages
	 */
	private static void openServerTunnel() {
		serverTunnel = new Runnable(){
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
	 * Starts waiting for messages to send
	 */
	private static void startSendPiles() {
		startClientSendPile();
		startServerSendPile();
	}

	/**
	 * Starts waiting for messages to send to client
	 */
	private static void startClientSendPile() {
		clientSendPile = new Runnable(){
			public void run(){
				try {
					ClientTunnel.startSendPile();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		exService.submit(clientSendPile);		
	}

	/**
	 * Starts waiting for messages to send to server
	 */
	private static void startServerSendPile() {
		serverSendPile = new Runnable(){
			public void run(){
				try {
					ServerTunnel.startSendPile();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};
		exService.submit(serverSendPile);		
	}
}
