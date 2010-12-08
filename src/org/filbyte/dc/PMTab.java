package org.filbyte.dc;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.*;

import org.elite.jdcbot.framework.*;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.PrettyTextArea;
import org.filbyte.SU;

public class PMTab extends JPanel implements EventjDCBotListener {
	private static final long serialVersionUID = -3252820553939348049L;
	
	private PrettyTextArea chat;
	private JTextField input;
	private final User user;
	
	static HashMap<String, PMTab> tabs = new HashMap<String, PMTab> ();

	private PMTab (User user, final String message) {
		this.user = user;
		setLayout (new BorderLayout ());
		chat = new PrettyTextArea (false);
		if (message != null) {
			chat.appendBoldAndTextLater ("<" + PMTab.this.user + "> ",
					message + "\n");
		}

		input = new JTextField ();
		input.addKeyListener (new KeyListener () {
			@Override
			public void keyTyped (KeyEvent arg0) {
				SU.assertEdt ();
				if (arg0.getKeyChar () == '\n') {
					try {
						PMTab.this.user.getBot ().SendPrivateMessage (PMTab.this.user.username (),
								input.getText ());
					} catch (IOException e) {
						e.printStackTrace ();
					}

					SwingUtilities.invokeLater (new Runnable () {
						public void run () {
							chat.appendBold ("<" + PMTab.this.user.getBot ().botname ()
									+ "> ");
							chat.appendText (input.getText () + "\n");
							input.setText ("");
						}
					});
					arg0.consume ();
				}

			}

			@Override
			public void keyPressed (KeyEvent e) {

			}

			@Override
			public void keyReleased (KeyEvent e) {

			}
		});
		add (chat.jsp, BorderLayout.CENTER);
		add (input, BorderLayout.SOUTH);


		((EventjDCBot)PMTab.this.user.getBot ()).addListener (this);				
	}
	
	public static PMTab maybeBuild (User user, String message) {
		SU.assertEdt ();
		String uid = user.getHubSignature ()+":"+user;
		PMTab tab = tabs.get (uid);
		if (tab == null) {
			tab = new PMTab (user, message);
			tabs.put (uid, tab);
			return tab;
		}
		return null;
	}

	@Override
	public void on_SearchResult (jDCBot src, String senderNick,
			String senderIP, int senderPort, SearchResultSet result,
			int free_slots, int total_slots, String hubName) {

	}

	@Override
	public void on_Connect (jDCBot src) {

	}

	@Override
	public void on_Connect2Client (jDCBot src) {

	}

	@Override
	public void on_BotQuit (jDCBot src) {

	}

	@Override
	public void on_Disconnect (jDCBot src) {

	}

	@Override
	public void on_PublicMessage (jDCBot src, String user, String message) {

	}

	@Override
	public void on_Join (jDCBot src, String user) {

	}

	@Override
	public void on_Quit (jDCBot src, String user) {

	}

	@Override
	public void on_UpdateMyInfo (jDCBot src, String user) {

	}

	@Override
	public void on_PrivateMessage (jDCBot src, final String user, final String message) {
		if (this.user.username ().equalsIgnoreCase (user))
			chat.appendBoldAndTextLater ("<" + user + "> ", message + "\n");
	}

	@Override
	public void on_ChannelMessage (jDCBot src, String user, String channel,
			String message) {

	}

	@Override
	public void on_PassiveSearch (jDCBot src, String user, SearchSet search) {

	}

	@Override
	public void on_ActiveSearch (jDCBot src, String ip, int port,
			SearchSet search) {

	}

	@Override
	public void on_DownloadComplete (jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {

	}

	@Override
	public void on_DownloadStart (jDCBot src, User user, DUEntity due) {

	}

	@Override
	public void on_UploadComplete (jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {

	}

	@Override
	public void on_UploadStart (jDCBot src, User user, DUEntity due) {

	}
}
