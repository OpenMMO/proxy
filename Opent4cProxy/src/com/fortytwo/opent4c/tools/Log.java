package com.fortytwo.opent4c.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log {
	public static final Logger client = LogManager.getLogger("client");
	public static final Logger server = LogManager.getLogger("server");
	public static final Logger proxy = LogManager.getLogger("proxy");
	public static final Logger bot = LogManager.getLogger("bot");
}
