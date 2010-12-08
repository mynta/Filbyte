package org.filbyte;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class AskForServerDialog extends JDialog{
	private static final long serialVersionUID = -4031941406291102675L;
	private final JTextField serverField;
	public String ip;
	public int port = 411;
	private final JButton connect;
	private final JButton cancel;
	public AskForServerDialog (JFrame frame, String defaultServer) {
		super (frame, "Connect to server", true);
		SU.assertEdt ();

		setLayout (new FlowLayout ());

		add (new JLabel ("Server"));
		serverField = new JTextField (defaultServer);
		serverField.setColumns (15);
		add (serverField);
		connect = new JButton ("Connect!");
		connect.addActionListener (new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String server = serverField.getText ();
					int cutOff = server.indexOf (':');
					if (cutOff != -1) {
						port = Integer.parseInt (server.substring (cutOff+1));
						ip = server.substring (0, cutOff);
					} else {
						ip = server;
					}
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
