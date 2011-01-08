package org.filbyte.dc;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

import org.elite.jdcbot.framework.*;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.*;



public class DCHubTab extends JPanel implements EventjDCBotListener, Stopable{
	private static final long serialVersionUID = 298511149058311708L;
	JButton searchButton;
	private final PrettyTextArea logArea;
	private JTextField chat;
	private PrettyTable userList;
	private JSplitPane jsp;
	final EventjDCBot bot;
	UserTableModel utm;
	JTabbedPane parentJTP;
	DCContext context;


	DCHubTab (EventjDCBot bot, DCContext context) {
		SU.assertEdt ();
		this.bot = bot;
		this.context = context;

		setLayout (new BorderLayout ());

		logArea = new PrettyTextArea (false);

		User [] users = bot.GetAllUsers();
		Object [] nicks = new String [users.length];
		for (int i = 0; i < users.length; ++i) {
			nicks [i] = users[i].username();
		}
		utm = new UserTableModel (users);
		userList = new PrettyTable (utm, new UserPopup (context));

		Dimension minimum = new Dimension (0, 0);

		logArea.jsp.setMinimumSize(minimum);
		logArea.jsp.setPreferredSize (minimum);
		logArea.jsp.setBorder(new EmptyBorder (0,0,0,0));

		jsp = new SimpleSplitPane ();
		jsp.setLeftComponent (logArea.jsp);
		jsp.setRightComponent (userList.jsp);
		jsp.setResizeWeight (0.5);
		jsp.setDoubleBuffered(true);
		jsp.setContinuousLayout (true);
		jsp.resetToPreferredSizes ();
		jsp.setDividerSize (1);
		add (jsp, BorderLayout.CENTER);
		chat = new JTextField ();
		chat.addKeyListener (new KeyListener () {
			@Override
			public void keyTyped (KeyEvent arg0) {
				SU.assertEdt ();
				if (arg0.getKeyChar () == '\n') {
					try {
						DCHubTab.this.bot.SendPublicMessage (chat.getText ());
					} catch (IOException e) {
						e.printStackTrace();
					}
					chat.setText ("");
					arg0.consume ();
				}

			}

			@Override
			public void keyPressed (KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased (KeyEvent e) {
				// TODO Auto-generated method stub

			}});
		add (chat, BorderLayout.SOUTH);

		bot.addListener(this);
	}

	public void logln (final String text) {
		logArea.appendTextLater (text+"\n");
	}


	@Override
	public void on_SearchResult(jDCBot src, String senderNick, String senderIP,
			int senderPort, SearchResultSet result, int free_slots,
			int total_slots, String hubName) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_Connect(jDCBot src) {
		logln ("Connected!");
	}
	@Override
	public void on_Connect2Client(jDCBot src) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_BotQuit(jDCBot src) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_Disconnect(jDCBot src) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_PublicMessage(jDCBot src, final String user, final String message) {
		logArea.appendBoldAndTextLater ("<"+user+"> ", message+"\n");
	}

	@Override
	public void on_Join(jDCBot src, String usernick) {
		logArea.appendBoldAndTextLater (usernick, " joined.\n");
		final User user = src.getUser (usernick);
		SU.invokeLater (new Runnable () {
			public void run () {
				utm.addUser (user);
			}
		});
	}
	@Override
	public void on_Quit(jDCBot src, final String user) {
		logArea.appendBoldAndTextLater (user, " quit.\n");
		SU.invokeLater (new Runnable () {
			@Override
			public void run () {
				utm.removeUser (user);
			}
		});
	}
	@Override
	public void on_UpdateMyInfo(final jDCBot src, final String user) {
		SU.invokeLater (new Runnable () {
			@Override
			public void run () {
				utm.updateInfo (src.getUser (user));
			}
		});
	}
	@Override
	public void on_PrivateMessage(final jDCBot src, final String user, final String message) {
		SU.invokeLater (new Runnable () {
			public void run () {
				PMTab tab = PMTab.maybeBuild (src.getUser (user), message);
				if (tab != null)
					parentJTP.add ("PM - "+user, tab);
			}
		});
	}
	@Override
	public void on_ChannelMessage(jDCBot src, String user, String channel,
			String message) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_PassiveSearch(jDCBot src, String user, SearchSet search) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_ActiveSearch(jDCBot src, String ip, int port,
			SearchSet search) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_DownloadComplete(jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_DownloadStart(jDCBot src, User user, DUEntity due) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_UploadComplete(jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void on_UploadStart(jDCBot src, User user, DUEntity due) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop () {
	}
}
