package org.filbyte;

import javax.swing.*;

public class SettingsDialog extends JFrame {
	private static final long serialVersionUID = -6289761030764084187L;

	public SettingsDialog () {
		SU.assertEdt ();
		this.add (new JLabel ("Test"));
		this.add (new JLabel ("Test2"));
	}
}
