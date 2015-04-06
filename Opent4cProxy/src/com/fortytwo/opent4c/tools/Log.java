package com.fortytwo.opent4c.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log {
	public static final Logger client = LogManager.getLogger("Client");
	public static final Logger server = LogManager.getLogger("Server");
	public static final Logger proxy = LogManager.getLogger("proxy");
	public static final Logger bot = LogManager.getLogger("bot");
	
	public static void initLogger() {
		Log.proxy.fatal("Log Level Test.");
		Log.proxy.fatal("FATAL OK");
		Log.proxy.error("ERROR OK");
		Log.proxy.warn("WARN OK");
		Log.proxy.info("INFO OK");
		Log.proxy.debug("DEBUG OK");
		Log.proxy.trace("TRACE OK");
		Log.proxy.fatal("================================");
	}
}
