package com.wallet.platform.config;

import java.util.Properties;

import org.apache.log4j.Logger;

public final class Config {
	
	private static final Logger LOGGER = Logger.getLogger(Config.class);
	
	private static final String CONFIG_FILE = "/config.properties";
	
	private static final Properties properties = new Properties();
	static {
		reload();
	}
	
	private Config() {
		
	}
	
	public static boolean reload() {
		try {
			properties.load(Config.class.getResourceAsStream(CONFIG_FILE));
			return true;
		} catch (Throwable t) {
			LOGGER.error("加载配置文件[" + CONFIG_FILE + "]异常，详情：", t);
		}
		return false;
	}
	
	public static String get(String key) {
		return properties.getProperty(key, "");
	}
	
	public static String get(String key, String dftVal) {
		return properties.getProperty(key, dftVal);
	}
	
	public static int getInt(String key) {
		return Integer.parseInt(get(key, "-1"));
	}
	
	public static int getInt(String key, int dftVal) {
		return Integer.parseInt(get(key, String.valueOf(dftVal)));
	}
	
}
