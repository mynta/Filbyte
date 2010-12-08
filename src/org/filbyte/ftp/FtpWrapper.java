package org.filbyte.ftp;

import java.io.IOException;
import java.util.*;

import org.filbyte.GlobalProperties;

import net.sf.jftp.net.*;

public class FtpWrapper {

	private FtpConnection con;
	private ArrayDeque<String> dlqueue;
	private boolean isThreadRunning;
	
	FtpWrapper (String ip) {
		con = new FtpConnection (ip);
		dlqueue = new ArrayDeque<String> ();
	}
	
	public void login (String user, String pass) {
		con.login (user, pass);
	}
	
	void setLocalPath (String path) {
		con.setLocalPath (path);
	}
	
	void  addConnectionListener (ConnectionListener l) {
		con.addConnectionListener (l);
	}
	
	void disconnect () {
		con.disconnect ();
	}
	
	void handleDownload (String path) {
		synchronized (this) {
			dlqueue.addLast (path);
			if (!isThreadRunning) {
				isThreadRunning = true;
				new Thread (new Runnable () {
					@Override
					public void run () {
						while (true) {
							synchronized (FtpWrapper.this) {
								if (dlqueue.isEmpty ()) {
									isThreadRunning = false;
									return;
								}
								con.download (dlqueue.pollFirst ());
							}
						}
					}
				}).start ();
			}
				
		}
	}

	public String getPWD () {
		return con.getPWD ();
	}

	public void chdir (String path) {
		con.chdir (path);
	}

	public String[] sortSize () {
		return con.sortSize ();
	}

	public String[] sortLs () {
		return con.sortLs ();
	}

	public Date[] sortDates () {
		return con.sortDates ();
	}

	public void list () throws IOException {
		con.list ();
	}
}
