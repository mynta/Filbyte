package org.filbyte.ftp;

import java.io.IOException;
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.jftp.net.FtpConnection;

public class FtpNode extends DefaultMutableTreeNode {
	private boolean areChildrenDefined = false;
	private String path;
	private ArrayList<String> files = new ArrayList<String> ();
	private ArrayList<String> sizes = new ArrayList<String> ();
	private ArrayList<Date> dates = new ArrayList<Date> ();
	private FtpWrapper ftp;

	public FtpNode(FtpWrapper ftp, String path) {
		super (path);
		System.out.println ("path: " + path);
		this.ftp = ftp;
		this.path = path;
	}

	public boolean isLeaf() {
		return(false);
	}

	public int getChildCount() {
		synchronized (files) {
			if (!areChildrenDefined)
				defineChildNodes();
			return(super.getChildCount());
		}
	}
	
	public java.util.List<String> getFiles () {
		synchronized (files) {
			if (!areChildrenDefined)
				defineChildNodes();
			return files;
		}
	}

	public ArrayList<String> getSizes () {
		return sizes;
	}

	public ArrayList<Date> getDates () {
		return dates;
	}

	private void defineChildNodes() {
		// You must set the flag before defining children if you
		// use "add" for the new children. Otherwise you get an infinite
		// recursive loop, since add results in a call to getChildCount.
		// However, you could use "insert" in such a case.
		try {
			areChildrenDefined = true;
			if (!ftp.getPWD ().equals (path))
				ftp.chdir (path);

			ftp.list ();
			String [] names = ftp.sortLs();
			String[] allSizes = ftp.sortSize ();
			Date [] allDates = ftp.sortDates ();
			files = new ArrayList<String> ();
			for(int i=0; i<names.length; i++) {
				String name = names[i];
				if (name.length () == 0)
					continue;
				if (name.charAt (name.length ()-1) == '/')
					add(new FtpNode(ftp,path+name));
				else {
					files.add (name);
					sizes.add (allSizes[i]);
					if (allDates != null)
						dates.add (allDates[i]);
					else
						dates.add (null);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
