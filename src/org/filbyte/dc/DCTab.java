package org.filbyte.dc;


import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import org.elite.jdcbot.framework.*;
import org.filbyte.*;


public class DCTab extends JPanel implements Stopable{
	private static final long serialVersionUID = 1L;
	JButton searchButton;
	DCContext context = new DCContext ();
	public final String ip;
	public final int port;
	
	public DCTab (final JFrame window) {
		SU.assertEdt ();
		this.ip = GlobalProperties.getString ("ourIP");
		this.port = maybeOpenAppropriatePort ();
		setLayout(new BorderLayout ());
		JToolBar buttonPanel = new JToolBar ();
		buttonPanel.setFloatable (false);
		JButton newHub = new JButton ("New hub");
		newHub.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				AskForServerDialog afsd = new AskForServerDialog (window,
						DCTab.this.ip);
				afsd.setVisible(true);
				if (afsd.ip != null) {
					EventjDCBot bot = context.manager.connect(afsd.ip, afsd.port);
					if (bot != null)
						context.tabs.addTab (bot.hubname(), new DCHubTab (bot, context), true);
				}
			}
		});
		
		buttonPanel.add(newHub);
		JButton search = new JButton ("Search");
		search.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				context.tabs.addTab ("Search", new SearchTab (context.manager), true);
			}
		});
		buttonPanel.add(search);
		buttonPanel.setLayout (new BoxLayout (buttonPanel, BoxLayout.LINE_AXIS));
		add (buttonPanel, BorderLayout.NORTH);
		
		context.tabs = new TabManager ();
		add (context.tabs.jtp, BorderLayout.CENTER);
	}
	private int maybeOpenAppropriatePort () {
		if (!GlobalProperties.getBool ("upnp"))
			return GlobalProperties.getInt ("dc.port");
		
		if (GlobalProperties.getBool ("dc.auto-port"))
			return PortHandler.openPort ();
		
		int port = GlobalProperties.getInt ("dc.port");
		PortHandler.openPort (port);
		return port;
	}
	public void start () {
		SU.assertEdt ();
		context.manager = new DCManager (context, ip, port);
		//context.manager.shareManager.addListener (new FileListTabManager (context));
	}
	public void stop () {
		SU.assertEdt ();
		PortHandler.releasePort (port);
		context.manager.shutdown ();
	}
}
