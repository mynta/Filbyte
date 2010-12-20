package org.filbyte;

import java.io.*;

public class Main {

	private final static String propertyPath = "filbyte.properties";
	
	public static void main (String[] args) {
		GlobalProperties.load (new File (propertyPath));
		ensureFoldersExist ();
		new Thread () {
			@Override public void run () {
				try {
					PortHandler.init ();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start ();
		UI.start ();
		Runtime.getRuntime ().addShutdownHook (new Thread () {
			@Override public void run () {
				GlobalProperties.save (new File (propertyPath));
				PortHandler.releaseAllPorts ();
			}
		});

	}

	private static void ensureFoldersExist () {
		ensureFolderExists (GlobalProperties.getString ("dc.miscdir"));
		ensureFolderExists (GlobalProperties.getString ("dc.incompleteDir"));
		ensureFolderExists (GlobalProperties.getString ("dc.sharedir"));
		ensureFolderExists (GlobalProperties.getString ("downloaddir"));
	}
	
	private static void ensureFolderExists (String path) {
		File f = new File (path);
		f.mkdirs ();
	}
}
