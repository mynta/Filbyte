package org.filbyte.ftp;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.sf.jftp.net.FtpConnection;

import org.elite.jdcbot.framework.*;
import org.filbyte.*;

public class FilePopup implements PrettyTable.PopupBuilder {
	FtpWrapper ftp;
	
	public FilePopup (FtpWrapper ftp) {
		this.ftp = ftp;
	}
	@Override
	public void buildPopup (JTable table, MouseEvent e, final List<Integer> selected) {
		SU.assertEdt ();
		JPopupMenu menu = new JPopupMenu ();
		JMenuItem download = new JMenuItem ("download");
		DefaultTableModel ftm = (DefaultTableModel)table.getModel ();
		final List<String> selectedFiles = new ArrayList<String> ();
		for (Integer rowNum : selected) {
			Vector<Object> row = (Vector<Object>) ftm.getDataVector ().elementAt (rowNum);
			selectedFiles.add ((String)row.get (0));
		}

		download.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent arg0) {
				for (String name : selectedFiles) {
					ftp.handleDownload (name);
				}
			}
		});
		menu.add (download);
		menu.show (e.getComponent (), e.getX (), e.getY ());
	}
}
