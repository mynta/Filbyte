package org.filbyte;

import java.io.*;
import java.util.Properties;

public class GlobalProperties {
	private static Properties props = new Properties ();
	
	public synchronized static void setDefaults () {
		props.setProperty ("ourIP", "127.0.0.1");
		props.setProperty ("ourport", "5061");
		props.setProperty ("upnp", "true");
		
		props.setProperty ("downloaddir", "download");
		
		props.setProperty ("dc.auto-port", "true");
		props.setProperty ("dc.description", "");
		props.setProperty ("dc.nick", "filbyte");
		props.setProperty ("dc.pass", "");
		props.setProperty ("dc.description", "");
		props.setProperty ("dc.email", "");
		props.setProperty ("dc.connection", "DSL");
		props.setProperty ("dc.upslots", "3");
		props.setProperty ("dc.downslots", "3");
		props.setProperty ("dc.passive", "false");
		props.setProperty ("dc.miscdir", "misc");
		props.setProperty ("dc.incompleteDir", "incomplete");
		props.setProperty ("dc.sharedir", "shared");
		
	}
	
	public synchronized static String getString (String key) {
		String str = props.getProperty (key);
		if (str == null)
			throw new IllegalArgumentException ("Required property "+key+" not found");
		return str;
	}
	
	/*public static String queryString (String key, String defaultValue) {
		String str = props.getProperty (key);
		if (str == null) {
			props.setProperty (key, defaultValue);
			return defaultValue;
		}
		return str;
	}*/
	
	public synchronized static int getInt (String key) {
		String str = props.getProperty (key);
		if (str == null)
			throw new IllegalArgumentException ("Required property "+key+" not found");
		int ret = Integer.parseInt (str);
		return ret;
	}
	
	public synchronized static boolean getBool (String key) {
		String str = props.getProperty (key);
		if (str == null)
			throw new IllegalArgumentException ("Required property "+key+" not found");
		boolean ret = Boolean.parseBoolean (str);
		return ret;
	}
	
	/*public static int queryInt (String key, int defaultValue) {
		String str = props.getProperty (key);
		if (str == null) {
			props.setProperty (key, Integer.toString (defaultValue));
			return defaultValue;
		}
		try {
			return Integer.parseInt (str);
		} catch (NumberFormatException nfe) {
			props.setProperty (key, Integer.toString (defaultValue));
			return defaultValue;
		}
	}
	
	public static boolean queryBool (String key, boolean defaultValue) {
		String str = props.getProperty (key);
		if (str == null) {
			props.setProperty (key, Boolean.toString (defaultValue));
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean (str);
		} catch (NumberFormatException nfe) {
			props.setProperty (key, Boolean.toString (defaultValue));
			return defaultValue;
		}
	}*/
	
	public synchronized static void load (File file) {
		props.clear ();
		setDefaults ();
		try {
			props.load (new FileInputStream (file));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	public synchronized static void save (File file) {
		try {
			props.store (new FileOutputStream (file), "Properties for filbyte. Only edit when program is not loaded. Comments will be overwritten");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
}
