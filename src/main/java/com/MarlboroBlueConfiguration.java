package com;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;



public class MarlboroBlueConfiguration {
	private static Properties prop = new Properties();
	private static AtomicBoolean isLoad = new AtomicBoolean(false);
	public static void load(boolean isDeveloperMode) throws Exception {
		if (isLoad.get()) 
			return;

		InputStream in = MarlboroBlueConfiguration.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(in);
			in.close();
		}
		catch (IOException e) {
		}

		isLoad.set(true);

		if (isDeveloperMode) {
			setProperty("jdbc.driver", getProperty("maven.jdbc.driver"));
			setProperty("jdbc.url", getProperty("maven.jdbc.url"));
			setProperty("jdbc.user", getProperty("maven.jdbc.user"));
			setProperty("jdbc.passwd", getProperty("maven.jdbc.passwd"));
		}

		printProps();
	}

	public static String getProperty(String key) throws Exception {
		if (isLoad.get() == false)
			load(false);
		
		return prop.getProperty(key, "");
	}

	public static void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public static void printProps() {
		Iterator<Object> keyNum = prop.keySet().iterator();
		System.out.println("* config properties");
		while ( keyNum.hasNext() ) {
			String key = (String)keyNum.next();
			System.out.println("*** " + key + ":" + prop.getProperty(key));
		}
	}
}

