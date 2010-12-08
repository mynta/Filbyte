package org.filbyte.ftp;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.filbyte.SU;


public class UserPassDialog extends JDialog{
	private static final long serialVersionUID = -4031941406291102675L;
	private final JTextField serverField;
	private final JTextField userField;
	private final JTextField passField;
	public String ip;
	public int port = 411;
	public String user;
	public String pass;
	private final JButton connect;
	private final JButton cancel;
	public UserPassDialog (JFrame frame, String defaultServer) {
		super (frame, "Connect to server", true);
		SU.assertEdt ();

		setLayout (new FlowLayout ());

		add (new JLabel ("Server"));
		serverField = new JTextField (defaultServer);
		serverField.setColumns (15);
		add (serverField);
		add (new JLabel ("Username"));
		userField = new JTextField ("anonymous");
		userField.setColumns (15);
		add (userField);
		add (new JLabel ("Password"));
		passField = new JTextField ("");
		passField.setColumns (15);
		add ( passField);
		connect = new JButton ("Connect!");
		connect.addActionListener (new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SU.assertEdt ();
				try {
					String server = serverField.getText ();
					int cutOff = server.indexOf (':');
					if (cutOff != -1) {
						port = Integer.parseInt (server.substring (cutOff));
						ip = server.substring (0, cutOff);
					} else {
						ip = server;
					}
					user = userField.getText ();
					pass = passField.getText ();
					dispose ();
				}
				catch (NumberFormatException e) {
				}

			}
		});
		add (connect);
		cancel = new JButton ("Cancel");
		cancel.addActionListener (new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose ();
			}
		});
		add (cancel);
		pack ();
	}
}
