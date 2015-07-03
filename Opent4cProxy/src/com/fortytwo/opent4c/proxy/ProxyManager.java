package com.fortytwo.opent4c.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.core.config.XMLConfigurationFactory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.fortytwo.opent4c.screens.BotScreen;
import com.fortytwo.opent4c.screens.ClientScreen;
import com.fortytwo.opent4c.screens.InfoScreen;
import com.fortytwo.opent4c.screens.MonitorScreen;
import com.fortytwo.opent4c.screens.ServerScreen;
import com.fortytwo.opent4c.screens.StatScreen;
import com.fortytwo.opent4c.tools.Log;

public class ProxyManager extends Game{

	private static ShaderProgram shader;
	private static ProxyManager app;
	private SpriteBatch batch;
	private static InfoScreen infoScreen;
	private static StatScreen statScreen;
	private static MonitorScreen monitorScreen;
	private static ServerScreen serverScreen;
	private static ClientScreen clientScreen;
	private static BotScreen botScreen;
	public static long startTime;
	private static final String cfgFile = "res/proxy.cfg";
	public static int max_clients;
	public static int proxyPort;
	public static int serverPort;
	public static int clientVersion;
	public static int serverVersion;
	public static int netspeed;
	public static InetAddress serverAddress;
	public static boolean sniffer;
	public static boolean bot;
	private static ExecutorService exService = Executors.newFixedThreadPool(4);
	private static ClientManager clientManager;

	/**
	 * Initialise l'affichage
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "res/log4j2.xml");
		if(args.length != 0){
			Log.proxy.fatal("Program takes no arguments, see proxy.cfg file instead.");
			System.exit(1);
		}
		while(System.currentTimeMillis() % 1000 != 0){
			startTime = System.nanoTime();
		}
		readCfgFile(cfgFile);
		GDXConfig.loadCFG();
		app = new ProxyManager();
		new LwjglApplication(app, GDXConfig.cfg);
		Gdx.app.postRunnable(new Runnable(){
			public void run(){
				createShaders();
			}
		});
		clientManager = new ClientManager();
	}
	
	@Override
	public void create() {
		Gdx.app.log("Init", "ok");
		batch = new SpriteBatch(2048, getShader());
		infoScreen = new InfoScreen(batch); 
		statScreen = new StatScreen(batch); 
		monitorScreen = new MonitorScreen(batch); 
		serverScreen = new ServerScreen(batch); 
		clientScreen = new ClientScreen(batch); 
		botScreen = new BotScreen(batch); 
		setScreen(infoScreen);
	}

	/**
	 * crée le shader program pour utilisation d'opengl3
	 */
	private static void createShaders() {
		shader = new ShaderProgram(Shaders.vertexShader, Shaders.fragmentShader);
		Gdx.app.log("Shader","compiled");
		if(!shader.isCompiled())Gdx.app.log("Shader",shader.getLog());
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
		else if(params[0].equalsIgnoreCase("sniffer"))sniffer = Boolean.parseBoolean(params[1]);
		else{
			Log.proxy.error("Unknown parameter "+ line);
			return;
		}
		Log.proxy.info(params[0]+" = "+params[1]);
	}
	
	/**
	 * permet à l'application d'obtenir le shader program
	 * @return
	 */
	public static ShaderProgram getShader(){
		return shader;
	}

	public static String getTitle() {
		return GDXConfig.cfg.title;
	}

	public static void quit() {
		Gdx.app.exit();
	}

	public static void setPakStatsScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(statScreen);
		//	}
			
		//});
	}

	public static void setMonitorScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(monitorScreen);
		//	}
			
		//});		
	}

	public static void setServerScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(serverScreen);
		//	}
			
		//});		
	}

	public static void setClientsScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(clientScreen);
		//	}
			
		//});		
	}

	public static void setBotsScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(botScreen);
		//	}
			
		//});		
	}
	
	public static void setInfoScreen() {
		//Gdx.app.postRunnable(new Runnable(){

		//	@Override
		//	public void run() {
				app.setScreen(infoScreen);
		//	}
			
		//});		
	}
}
