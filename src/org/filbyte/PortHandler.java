package org.filbyte;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import net.sbbi.upnp.impls.InternetGatewayDevice;

public class PortHandler {
	static InternetGatewayDevice IGD = null;
	private static final int STANDARD_STARTPORT = 56793;
	private static int curPort;
	private static HashSet<Integer> openedPorts = new HashSet<Integer> ();;

	public static synchronized void init (int timeOut, int startport) throws IOException {
		InternetGatewayDevice[] IGDs = InternetGatewayDevice
				.getDevices (timeOut);
		if (IGDs != null)
			IGD = IGDs[0];
		curPort = startport;
	}

	public static synchronized void init () throws IOException {
		init (-1, STANDARD_STARTPORT);
	}

	public static synchronized boolean openPort (int port) {
		if (IGD == null) {
			try {
				init ();
			} catch (IOException e1) {
				return false;
			}
		}
		if (IGD != null) {
			try {
				// let's the the first device found
				// now let's open the port
				String localHostIP = InetAddress.getLocalHost ()
						.getHostAddress ();
				// we assume that localHostIP is something else than 127.0.0.1
				boolean mapped = IGD.addPortMapping (
						"Some mapping description", null, port, port,
						localHostIP, 24 * 3600, "TCP");
				if (mapped) {
					mapped = IGD.addPortMapping (
							"Some mapping description", null, port, port,
							localHostIP, 24 * 3600, "UDP");
					if (mapped)
						openedPorts.add (port);
					else
						IGD.deletePortMapping( null, port, "TCP" );
				}
				return mapped;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public static synchronized int openPort () {
		curPort &= 65535;
		if (IGD == null) {
			try {
				init ();
			} catch (IOException e1) {
				return -1;
			}
		}
		if (IGD == null)
			return -1;
		for (int i = 0; i < 10; ++i) {
			if (openPort (curPort))
				return curPort++;
			curPort++;
		}
		return -1;
	}
	
	public static synchronized void releasePort (int port) {
		if (openedPorts.contains (port)) {
			try {
				IGD.deletePortMapping( null, port, "TCP" );
			} catch (Exception e) {
				// Whatever
			}
			openedPorts.remove (port);
		}
	}
	
	public static synchronized void releaseAllPorts () {
		for (Iterator<Integer> it = openedPorts.iterator (); it.hasNext ();) {
			int port = it.next ();
			try {
				IGD.deletePortMapping( null, port, "TCP" );
				IGD.deletePortMapping( null, port, "UDP" );
			} catch (Exception e) {
				// Whatever
			}
		}
	}

	public static String getDeviceName () {
		return IGD.getIGDRootDevice ().getModelName ();
	}
}
