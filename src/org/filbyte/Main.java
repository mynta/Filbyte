package org.filbyte;

import java.io.File;

public class Main {

	private final static String propertyPath = "filbyte.properties";
	
	public static void main (String[] args) {
		GlobalProperties.load (new File (propertyPath));
		ensureFoldersExist ();
		UI.start ();
		Runtime.getRuntime ().addShutdownHook (new Thread () {
			@Override public void run () {
				GlobalProperties.save (new File (propertyPath));
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
