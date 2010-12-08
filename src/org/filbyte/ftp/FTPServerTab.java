package org.filbyte.ftp;

import java.awt.Component;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

import net.sf.jftp.net.*;

import org.filbyte.*;

// this class download a file via anonymous ftp and shows output.
//
// if you want to use the api in a more complex way, please do at least take a look at the
// FtpConnection, FtpTransfer, ConnectionHandler, DirPanel (blockedTransfer, transfer)
// and ConnectionListener sourcecode.
public class FTPServerTab extends SimpleSplitPane implements ConnectionListener, Stopable {

	// is the connection established?
	private boolean isThere = false;
	JTree tree;

	public static long time = 0;
	UserPassDialog upd;
	PrettyTable pt;
	FtpWrapper con;
	private static final String headers[] = { "Filename" , "Size",  "Last Modified" };


	// creates a FtpConnection and downloads a file
	public void Connect (UserPassDialog upd) {
		SU.assertNotEdt ();
		this.upd = upd;
		// System.out.println("1) "+(System.currentTimeMillis()-current)+"ms.");

		// register app as Logger, debug() and debugRaw below are from now on
		// called by
		// FtpConnection

		// create a FtpConnection - note that it does *not* connect instantly
		
		net.sf.jftp.config.Settings.maxConnections = 1;
		net.sf.jftp.config.Settings.enableResuming = true;
		
		con = new FtpWrapper (upd.ip);
		con.setLocalPath (GlobalProperties.getString ("downloaddir"));

		// System.out.println("2) "+(System.currentTimeMillis()-current)+"ms.");

		// set updatelistener, interface methods are below
		con.addConnectionListener (this);
		// connect and login. this is from where connectionFailed() may be
		// called for example
		con.login (upd.user, upd.pass);

		// System.out.println("3) "+(System.currentTimeMillis()-current)+"ms.");

		// login calls connectionInitialized() below which sets isThere to true
		while (!isThere) {
			try {
				Thread.sleep (10);
			} catch (Exception ex) {
				ex.printStackTrace ();
			}
		}

		// System.out.println("4) "+(System.currentTimeMillis()-current)+"ms.");

		// download the file - this method blocks until the download has
		// finished
		// if you want non-blocking, multithreaded io, just use
		//
		// con.handleDownload(file);
		//
		// which spawns a new thread for the download
		// con.download(file);
	}

	// ------------------ needed by ConnectionListener interface
	// -----------------

	// called if the remote directory has changed
	public void updateRemoteDirectory (BasicConnection con) {
		System.out.println ("new path is: " + con.getPWD ());
	}
	
	private static void emptyTable (DefaultTableModel dtm) {
		for (int len = dtm.getRowCount (); len > 0; --len)
			dtm.removeRow (0);
		
	}

	private void buildUI () {
		SU.assertEdt ();
		DefaultMutableTreeNode root = new FtpNode (con, con.getPWD ());

		tree = new JTree (root);
		tree.setCellRenderer (new DefaultTreeCellRenderer () {
			public Component getTreeCellRendererComponent (JTree tree,
					Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				return super.getTreeCellRendererComponent (tree, value,
						selected, expanded, false, row, hasFocus);
			}
		});
		tree.addTreeSelectionListener (new TreeSelectionListener () {

			@Override
			public void valueChanged (TreeSelectionEvent arg0) {
				@SuppressWarnings ("unchecked")
				TreePath path = arg0.getNewLeadSelectionPath ();
				Object o = null;
				if (path != null) {
					o = path.getLastPathComponent ();
				}
				DefaultTableModel ptm = (DefaultTableModel) pt.jt.getModel ();
				emptyTable (ptm);
				if (o instanceof FtpNode) {
					FtpNode ftpNode = (FtpNode) o;
					
					List<String> names = ftpNode.getFiles ();
					List<String> sizes = ftpNode.getSizes ();
					List<Date> dates = ftpNode.getDates ();
					for (int i = 0; i < names.size (); ++i) {
						Vector<Object> row = new Vector<Object> ();
						row.add (names.get (i));
						row.add (sizes.get (i));
						row.add (dates.get (i));
						ptm.addRow (row);
					}
				}
			}
		});
		// dfsAdd (root, dir);
		pt = new PrettyTable (new DefaultTableModel (headers, 0), new FilePopup (con));
		setLeftComponent (new JScrollPane (tree));
		setRightComponent (pt.jsp);
	}

	// called if a connection has been established
	public void connectionInitialized (BasicConnection con) {
		isThere = true;
		SU.invokeLater (new Runnable () {
			@Override
			public void run () {
				buildUI ();
			}
		});
	}

	// called every few kb by DataConnection during the trnsfer (interval can be
	// changed in Settings)
	public void updateProgress (String file, String type, long bytes) {
	}

	// called if connection fails
	public void connectionFailed (BasicConnection con, String why) {
		System.out.println ("connection failed!");
	}

	// up- or download has finished
	public void actionFinished (BasicConnection con) {
	}

	@Override
	public void stop () {
		con.disconnect ();
	}

}
