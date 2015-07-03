package com.fortytwo.opent4c.proxy;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

public class GDXConfig {
	public static LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	public static final String title = "OpenT4CProxy v0";
	private static final int screen_columns = 40;
	private static final int screen_rows = 45;
	private static final int background_fps = 24;
	private static final int foreground_fps = 60;
	private static final boolean fullscreen = false;
	private static final boolean use_opengl3_if_possible = false;
	private static final boolean resizable = true;
	private static final boolean vertical_sync = true;
	private static final boolean allow_software_mode_if_hardware_fails = true;
	private static final Color initial_background_color = Color.CLEAR;
	private static final String preferences_directory = ".openT4C/";

	
	/**
	 * Charge la configuration
	 */
	public static void loadCFG() {
		//TODO faire ça depuis un fichier
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		cfg.title = title;
		cfg.preferencesDirectory = preferences_directory;
		cfg.initialBackgroundColor = initial_background_color;
		cfg.useGL30 = use_opengl3_if_possible;
		cfg.fullscreen = fullscreen;
		cfg.addIcon("res/t4c128.png", Files.FileType.Internal);
		cfg.addIcon("res/t4c16.png", Files.FileType.Internal);
		cfg.addIcon("res/t4c32.png", Files.FileType.Internal);
		cfg.addIcon("res/t4c48.png", Files.FileType.Internal);
		if(fullscreen){
			cfg.width = screenSize.width;
			cfg.height = screenSize.height;
		}else{
			cfg.width = screen_columns * 32;
			cfg.height = screen_rows * 16;
		}
		cfg.backgroundFPS = background_fps;
		cfg.foregroundFPS = foreground_fps;
		cfg.resizable = resizable;
		cfg.vSyncEnabled = vertical_sync;
		cfg.allowSoftwareMode = allow_software_mode_if_hardware_fails;
		cfg.x = -1;
		cfg.y = -1;
	}
}
