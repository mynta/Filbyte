package org.filbyte.dc;
import org.elite.jdcbot.framework.jDCBot;
import org.elite.jdcbot.shareframework.*;


public abstract class Searcher {
	public final SearchSet ss;
	
	public Searcher (SearchSet ss) {
		this.ss = ss;
	}
	public void onSearch (jDCBot src, String senderNick,
			String senderIP, int senderPort, SearchResultSet result,
			int free_slots, int total_slots, String hubName) {
		if (result.name.toLowerCase ().contains (ss.string.toLowerCase ())) {
			dispatch (src, senderNick, senderIP, senderPort, result,
					free_slots, total_slots, hubName);
		}
	}
	public abstract void dispatch (jDCBot src, String senderNick,
			String senderIP, int senderPort, SearchResultSet result,
			int free_slots, int total_slots, String hubName);
}
