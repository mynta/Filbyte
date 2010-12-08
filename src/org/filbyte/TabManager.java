package org.filbyte;

import java.awt.Component;
import java.util.*;

import javax.swing.JTabbedPane;

public class TabManager {
	private static final long serialVersionUID = 685319670979139276L;

	public JTabbedPane jtp;
	private List<Component> tabs;
	private List<Boolean> closeable;

	public TabManager () {
		SU.assertEdt ();
		jtp = new JTabbedPane ();
		tabs = new ArrayList<Component> ();
		closeable = new ArrayList<Boolean> ();
		jtp.addMouseListener (new PopupTabListener (this));
	}

	public boolean addTab (String name, Component c, boolean closeable) {
		SU.assertEdt ();
		int index = tabs.indexOf (c);
		if (index == -1) {
			this.closeable.add (closeable);
			tabs.add (c);
			jtp.addTab (name, c);
			index = tabs.size () - 1;
			jtp.setSelectedIndex (index);
			return true;
		}
		jtp.setSelectedIndex (index);
		return false;
	}

	public boolean getIsCloseable (int index) {
		SU.assertEdt ();
		return closeable.get (index);
	}

	public boolean removeTab (Component c) {
		SU.assertEdt ();
		int index = tabs.indexOf (c);
		if (index == -1 || !closeable.get (index))
			return false;
		if (c instanceof Stopable)
			((Stopable)c).stop ();
		closeable.remove (index);
		jtp.remove (index);
		tabs.remove (index);
		return true;
	}

	public boolean removeTab (int index) {
		SU.assertEdt ();
		if (index == -1 || index >= tabs.size () || !closeable.get (index))
			return false;
		Component c = tabs.get (index);
		if (c instanceof Stopable)
			((Stopable)c).stop ();
		else
			System.out.println ("Forcefully shutting tab down");
		closeable.remove (index);
		jtp.remove (index);
		tabs.remove (index);
		return true;
	}

	public JTabbedPane getJTP () {
		SU.assertEdt ();
		return jtp;
	}
}
