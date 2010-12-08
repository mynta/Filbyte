package org.filbyte;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.filbyte.dc.*;
import org.filbyte.ftp.FTPTab;


import java.util.*;
import java.util.List;


public class UI extends javax.swing.JFrame {
	private static final long serialVersionUID = -1230351475515089542L;
	TabManager tm;
	JTextField ipField;
	JTextField portField;
	List<String> tabs = new ArrayList <String> ();
	
    public UI() {
		SU.assertEdt ();
    	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Filebyte");
        buildMenu();
        Container cpane = getContentPane();
        tm = new TabManager ();
        cpane.add(tm.getJTP ());
        tm.addTab ("Main", buildMainTab (), false);
        pack();
        setSize (500, 500);
    }
    
    private JPanel buildMainTab() {
		SU.assertEdt ();
    	JPanel mainTab = new JPanel ();
        mainTab.setLayout(new FlowLayout ());
        
        mainTab.add (new JLabel ("Your ip: "));
        ipField = new JTextField (GlobalProperties.getString ("ourIP"));
        ipField.setColumns (20);
        mainTab.add (ipField);
        mainTab.add (new JLabel ("Your port: "));
        portField = new JTextField (Integer.toString (GlobalProperties.getInt ("ourport")));
        portField.setColumns (20);
        mainTab.add (portField);
        
    	return mainTab;
    }

	private void buildMenu() {
		SU.assertEdt ();
		JMenuBar jmb = new JMenuBar ();
        
        jmb.add (getFileMenu ());
        jmb.add (getHelpMenu ());
        this.setJMenuBar(jmb);
	}

	private JMenu getHelpMenu () {
		SU.assertEdt ();
		JMenu helpMenu = new JMenu("Help"); 
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showAboutDialog ();
				
			}});
        helpMenu.add (aboutItem);
		return helpMenu;
	}
	
	private void showAboutDialog () {
		SU.assertEdt ();
		JOptionPane.showMessageDialog(this,
			    "Filebyte is a multiprotocol filesharing program");
	}

	private JMenu getFileMenu () {
		SU.assertEdt ();
		JMenu menu = new JMenu("File"); 
        JMenu newMenu = new JMenu("New Tab");
        menu.add(newMenu);
        
        JMenuItem newDC = new JMenuItem("DC");
        newDC.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addDcTab ();
				
			}});
        newMenu.add (newDC);
        
        JMenuItem newFTP = new JMenuItem("FTP");
        newFTP.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addFTPTab ();
				
			}});
        newMenu.add (newFTP);
        
		return menu;
	}
	private void addDcTab () {
		SU.assertEdt ();
		DCTab dct = new DCTab (this, ipField.getText (), Integer.parseInt (portField.getText ()));
		if (tm.addTab ("DC", dct, true))
			dct.start ();
	}
	
	private void addFTPTab () {
		SU.assertEdt ();
		FTPTab dct = new FTPTab (this, ipField.getText (), Integer.parseInt (portField.getText ()));
		tm.addTab ("FTP", dct, true);
	}
    
    public static void start() {
        SU.invokeLater(new Runnable() {
            public void run() {
            	UI ui = new UI ();
                ui.setVisible(true);
            }
        });
    }
}
