package org.filbyte;
import java.text.DecimalFormat;
import java.util.regex.*;

public class StringUtils {
	private static final Pattern bp  = Pattern.compile ("(\\d*)\\.(\\d*) B");
	private static final Pattern kbp = Pattern.compile ("(\\d*)\\.(\\d*) KiB");
	private static final Pattern mbp = Pattern.compile ("(\\d*)\\.(\\d*) MiB");
	private static final Pattern gbp = Pattern.compile ("(\\d*)\\.(\\d*) GiB");
	private static final Pattern tbp = Pattern.compile ("(\\d*)\\.(\\d*) TiB");
	private static final Pattern doublep = Pattern.compile ("(\\d*)\\.(\\d*)");
	
	
	
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

	private static double getDouble (String size)  {
		Matcher m = doublep.matcher (size);
		m.find ();
		return Double.parseDouble (m.group (1));
	}
	
	public static long unPrettySize (String size) {
		if (size == null)
			return -1;

		long divider = 1;

		if (bp.matcher (size).matches ()) {
			return (int)getDouble (size);
		}
		divider <<= 10;
		
		if (kbp.matcher (size).matches ()) {
			return (int)(getDouble (size)/divider);
		}
		divider <<= 10;
		
		if (mbp.matcher (size).matches ()) {
			return (int)(getDouble (size)/divider);
		}
		divider <<= 10;
		
		if (gbp.matcher (size).matches ()) {
			return (int)(getDouble (size)/divider);
		}
		divider <<= 10;
		
		if (tbp.matcher (size).matches ()) {
			return (int)(getDouble (size)/divider);
		}

		return -1;
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
