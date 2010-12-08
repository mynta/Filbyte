package org.filbyte.dc;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import org.elite.jdcbot.framework.*;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.*;

public class SearchTab extends JSplitPane implements Stopable {
	private static final long serialVersionUID = 298511149058311708L;
	JButton searchButton;
	private JPanel optionsPanel;
	private PrettyTable fileList;
	private FileTableModel ftm;
	private JTextField query;
	DCManager manager;
	private Searcher activeSearcher = null;
	
	public SearchTab (final DCManager manager) {
		SU.assertEdt ();
		this.manager = manager;
    	optionsPanel = new JPanel ();
    	query = new JTextField ();
    	optionsPanel.add (query);
    	query.setColumns (15);
    	JButton searchButton = new JButton ("Search");
    	searchButton.addActionListener (new ActionListener ()  {
			@Override
			public void actionPerformed (ActionEvent e) {
				addSearcher ();
			}
    	});
    	
    	optionsPanel.add (searchButton);
    	optionsPanel.setPreferredSize (new Dimension (200, 200));

    	ftm = new FileTableModel ();
    	fileList = new PrettyTable (ftm, new FilePopup (manager));
    	
    	setLeftComponent (optionsPanel);
    	setRightComponent (fileList.jsp);
    	setResizeWeight (0);
    	this.setDoubleBuffered(true);
    	this.setContinuousLayout (true);
    	resetToPreferredSizes ();
    	setDividerSize (1);
	}

	@Override
	public synchronized void stop () {
		synchronized (this) {
			if (activeSearcher != null)
				manager.StopSearch (activeSearcher);
		}
	}

	private synchronized void addSearcher () {
		// TODO Auto-generated method stub

		SearchSet ss = new SearchSet ();
		ss.string = query.getText ();
		
		if (activeSearcher != null)
			manager.StopSearch (activeSearcher);
		activeSearcher = new Searcher (ss) {
			@Override
			public void dispatch (final jDCBot src, final String senderNick,
					final String senderIP, final int senderPort,
					final SearchResultSet result, final int free_slots,
					final int total_slots, final String hubName) {
				SU.invokeLater (new Runnable () {
					@Override
					public void run () {
						ftm.addData (src, senderNick, senderIP, senderPort, result,
								free_slots, total_slots, hubName);
					}});
			}
		};
		
		try {
			SU.invokeAndWait (new Runnable () {
				@Override
				public void run () {
					ftm.clear ();
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			manager.StartSearch (activeSearcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
