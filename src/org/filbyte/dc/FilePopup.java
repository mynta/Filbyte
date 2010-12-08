package org.filbyte.dc;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.elite.jdcbot.framework.*;
import org.filbyte.*;
import org.filbyte.dc.FileTableModel.Result;

public class FilePopup implements PrettyTable.PopupBuilder {
	DCManager man;

	public FilePopup (DCManager man) {
		this.man = man;
	}

	@Override
	public void buildPopup (JTable table, MouseEvent e, final List<Integer> selected) {
		SU.assertEdt ();
		JPopupMenu menu = new JPopupMenu ();
		JMenuItem download = new JMenuItem ("download");
		FileTableModel ftm = (FileTableModel)table.getModel ();
		final List<Result> results = new ArrayList<Result> ();
		for (Integer result : selected)
			results.add (ftm.results.get (result));

		download.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed (ActionEvent arg0) {
				for (Result result : results) {
					User u = result.src.getUser (result.senderNick);
					String downloadDir = GlobalProperties.getString ("downloaddir");
					File saveto = new File (downloadDir, result.result.name);
					File parent = saveto.getParentFile ();
					if (parent != null)
						parent.mkdirs ();
					
					long len = result.result.size;
					try {
						if (result.result.TTH.isEmpty ())
							man.download (result.result.name, false, len, saveto, u);
						else
							man.download (result.result.TTH, true, len, saveto, u);
					} catch (FileNotFoundException e) {
						e.printStackTrace ();
					} catch (BotException e) {
						e.printStackTrace ();
					}
				}
			}
		});
		menu.add (download);
		menu.show (e.getComponent (), e.getX (), e.getY ());
	}
}
