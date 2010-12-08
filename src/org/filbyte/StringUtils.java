package org.filbyte;
import java.text.DecimalFormat;

public class StringUtils {
	public static String prettySize (long bytes) {
		double size = bytes;
		DecimalFormat df = new DecimalFormat ("0.00");
		if (size < 1024)
			return df.format (size) + " B";
		size /= 1024;
		if (size < 1024)
			return df.format (size) + " KiB";
		size /= 1024;
		if (size < 1024)
			return df.format (size) + " MiB";
		size /= 1024;
		if (size < 1024)
			return df.format (size) + " MiB";
		size /= 1024;
		if (size < 1024)
			return df.format (size) + " GiB";
		size /= 1024;
		return df.format (size) + " TiB";
	}
	public static String pathToName (String path) {
		int lastSep = path.indexOf ('/');
		if (lastSep != -1)
			path = path.substring (lastSep+1);
		lastSep = path.indexOf ('\\');
		if (lastSep != -1)
			path = path.substring (lastSep+1);
		return path;
	}
	public static String nameToExt (String name) {
		int extInd = name.lastIndexOf ('.');
		if (extInd != -1)
			name = name.substring (extInd+1);
		return name;
	}
	public static String pathToDir (String path) {
		int sep1 = path.indexOf ('/');
		int sep2 = path.indexOf ('\\');
		if (sep2 > sep1)
			sep1 = sep2;
		if (sep1 != -1)
			return path.substring (0, sep1);
		return path;
	}
}
