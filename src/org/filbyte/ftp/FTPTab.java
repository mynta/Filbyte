package org.filbyte.ftp;


import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import net.sf.jftp.config.Settings;
import net.sf.jftp.net.FtpClient;
import net.sf.jftp.system.logging.Log;

import org.filbyte.*;


public class FTPTab extends JPanel implements Stopable{
	private static final long serialVersionUID = 1L;
	JButton searchButton;
	public final String ip;
	public final int port;
	List<FtpClient> clients = Collections.synchronizedList (new ArrayList<FtpClient> ());
	static LogHandler logger = new LogHandler ();

	final TabManager tabs = new TabManager ();
	static {
		Settings.bufferSize = 16384;
	 	Log.setLogger(logger);
	}
	
	
	public FTPTab (final JFrame window, String ip, int port) {
		this.ip = ip;
		this.port = port;
		
		setLayout(new BorderLayout ());
		JToolBar buttonPanel = new JToolBar ();
		buttonPanel.setFloatable (false);
		JButton connectButton = new JButton ("Connect to server");
		connectButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final UserPassDialog upd = new UserPassDialog (window,
						FTPTab.this.ip);
				upd.setVisible(true);
				if (upd.ip != null) {
					final FTPServerTab servertab = new FTPServerTab ();
					(new SwingWorker<Void, Void> () {
						@Override
						protected Void doInBackground () throws Exception {
							servertab.Connect (upd);
							return null;
						}
						public void done() {
							tabs.addTab (upd.ip, servertab, true);
					    }

					}).execute ();
				}
			}
		});
		
		buttonPanel.add(connectButton);
		buttonPanel.setLayout (new BoxLayout (buttonPanel, BoxLayout.LINE_AXIS));
		add (buttonPanel, BorderLayout.NORTH);
		
		add (tabs.jtp, BorderLayout.CENTER);
	}
	

	public void stop () {
	}
}
