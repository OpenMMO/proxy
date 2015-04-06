package com.fortytwo.opent4c.proxy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

import com.fortytwo.opent4c.bot.bot150;
import com.fortytwo.opent4c.tools.CalendarUtils;
import com.fortytwo.opent4c.tools.Log;
import com.fortytwo.opent4c.tools.PakTypes;

/**
 * Proxy for translating T4C paks form version to version between client and server, and to translate T4C paks to and from openT4C paks.
 * @author syno
 *
 */
public class Proxy {
	public static long startTime;
	private static final String cfgFile = "res/proxy.cfg";
	public static int max_clients;
	public static int proxyPort;
	public static int serverPort;
	public static int clientVersion;
	public static int serverVersion;
	public static InetAddress serverAddress;
	private static boolean bot;
	private static ExecutorService exService = Executors.newFixedThreadPool(4);
	private static Runnable serverSendPile;
	private static Runnable serverTunnel;
	private static Runnable botRunnable;
	private static ClientManager clientManager;
	
	public static void main(String[] args) throws IOException {
		System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "res/log4j2.xml");
		Log.initLogger();
		//Pak150 encrypted = new Pak150(PakTypes.PAK_CLIENT_MessageOfTheDay, Pak150.CLIENT_TO_SERVER, Pak150.EMPTY);
		//Log.client.fatal(HexString.from(encrypted.getData()));
		//DatagramPacket sendPacket = new DatagramPacket(encrypted.getData(), encrypted.getData().length, Proxy.serverAddress, Proxy.serverPort);
		//new Pak150(sendPacket, false, 0, 0);
		if(args.length != 0){
			Log.proxy.fatal("Program takes no arguments, see proxy.cfg file instead.");
			System.exit(1);
		}
		while(System.currentTimeMillis() % 1000 != 0){
			startTime = System.nanoTime();
		}
		readCfgFile(cfgFile);
		ServerTunnel.createSocket();
		startServerSendPile();
		openServerTunnel();
		clientManager = new ClientManager();
		if(bot) createBot();

	}

	/**
	 * creates a bot depending on server version
	 */
	private static void createBot() {
		switch(serverVersion){
		case 150 :
			botRunnable = new Runnable(){
				public void run(){
					bot150.create();
				}
			};
			exService.submit(botRunnable);
		break;
		}
	}

	/**
	 * Reads proxy parameters from proxy.cfg file
	 * @param cfg 
	 * @throws IOException 
	 */
	private static void readCfgFile(String cfg) throws IOException {
		Log.proxy.info("Reading configuration file");
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new FileReader(new File(cfg)));
		} catch (FileNotFoundException e) {
			Log.proxy.fatal("File proxy.cfg not found, please check and try again.");
			Log.proxy.fatal(cfg);
			System.exit(1);
		}			 
		String line;
		while ((line = buff.readLine()) != null) {
			readCfgLine(line);
		}
		Log.proxy.info("================================");
	}

	/**
	 * reads a line from config file escaping lines starting with # or with //
	 * @param line
	 * @throws UnknownHostException 
	 */
	private static void readCfgLine(String line) throws UnknownHostException {
		if(line.startsWith("#") || line.startsWith("//") || line.equalsIgnoreCase("")) return;
		String[] params = line.split("=");
		if(params.length != 2){
			Log.proxy.error("Syntax error : line should be PARAM=VALUE");
			Log.proxy.error(line);
			return;
		}
		if(params[0].equalsIgnoreCase("server address"))serverAddress = InetAddress.getByName(params[1]);
		else if(params[0].equalsIgnoreCase("server version"))serverVersion = Integer.parseInt(params[1]);
		else if(params[0].equalsIgnoreCase("server port"))serverPort = Integer.parseInt(params[1]);
		else if(params[0].equalsIgnoreCase("client version"))clientVersion = Integer.parseInt(params[1]);
		else if(params[0].equalsIgnoreCase("max clients"))max_clients = Integer.parseInt(params[1]);
		else if(params[0].equalsIgnoreCase("proxy port"))proxyPort = Integer.parseInt(params[1]);
		else if(params[0].equalsIgnoreCase("bot"))bot = Boolean.parseBoolean(params[1]);
		else{
			Log.proxy.error("Unknown parameter "+ line);
			return;
		}
		Log.proxy.info(params[0]+" = "+params[1]);
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
