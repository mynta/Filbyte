package org.filbyte.dc;

import java.util.ArrayList;

import javax.swing.table.*;

import org.elite.jdcbot.framework.jDCBot;
import org.elite.jdcbot.shareframework.SearchResultSet;
import org.filbyte.SU;
import org.filbyte.StringUtils;

public class FileTableModel extends DefaultTableModel {
	private static final long serialVersionUID = -8531539471335682329L;

	final private static String[] headers = { "Filename", "Type", "Size",
			"Nickname", "Directory", "Slots"};

	public ArrayList<Result> results = new ArrayList<Result> ();

	public FileTableModel () {
		super (headers, 0);
		SU.assertEdt ();
	}
	
	@Override public boolean isCellEditable (int row, int column) {
		return false;
	}

	private Object[] resultToRow (Result res) {
		SU.assertEdt ();
		String[] ret = new String[7];
		SearchResultSet srs = res.result;
		String path = srs.name;
		ret[0] = StringUtils.pathToName (path);
		ret[1] = StringUtils.nameToExt (ret[0]);
		ret[2] = StringUtils.prettySize (srs.size);
		ret[3] = res.senderNick;
		ret[4] = StringUtils.pathToDir (path);
		ret[5] = res.free_slots + " / " + res.total_slots;

		return ret;
	}

	public void addData (jDCBot src, String senderNick, String senderIP,
			int senderPort, SearchResultSet result, int free_slots,
			int total_slots, String hubName) {
		SU.assertEdt ();
		Result res = new Result ();
		res.src = src;
		res.senderNick = senderNick;
		res.senderIP = senderIP;
		res.senderPort = senderPort;
		res.result = result;
		res.free_slots = free_slots;
		res.total_slots = total_slots;
		res.hubName = hubName;
		results.add (res);
		super.addRow (resultToRow (res));
	}
	
	public void clear () {
		SU.assertEdt ();
		for (int i=0;i<results.size ();++i)
			super.removeRow (0);
		results = new ArrayList<Result> ();
	}

	static class Result {
		jDCBot src;
		String senderNick;
		String senderIP;
		int senderPort;
		SearchResultSet result;
		int free_slots;
		int total_slots;
		String hubName;
	}
}
