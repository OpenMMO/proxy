package com.fortytwo.opent4c.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.XMLConfigurationFactory;


public class Log {
	public static final Logger client = LogManager.getLogger("Client");
	public static final Logger server = LogManager.getLogger("Server");
	public static final Logger proxy = LogManager.getLogger("proxy");
	public static final Logger bot = LogManager.getLogger("bot");
	
	public static void initLogger() {
		System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "res/log4j2.xml");
	}
}
