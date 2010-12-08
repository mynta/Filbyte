package org.filbyte.dc;


import java.io.*;
import java.util.*;

import org.elite.jdcbot.framework.*;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.GlobalProperties;
import org.filbyte.SU;

public class DCManager implements EventjDCBotListener {
	private MultiHubsAdapter mha;
	public ShareManager shareManager;
	public DownloadCentral dc;
	private List<Searcher> searchers = new ArrayList<Searcher> ();
	DCContext context;
	
	public DCManager (DCContext context, String ip, int port) {
		SU.assertEdt ();
		try {
			String nick = GlobalProperties.getString ("dc.nick");
			String pass = GlobalProperties.getString ("dc.pass");
			String desc = GlobalProperties.getString ("dc.description");
			String email = GlobalProperties.getString ("dc.email");
			String connection = GlobalProperties.getString ("dc.connection");
			int uSlots = GlobalProperties.getInt ("dc.upslots");
			int dSlots = GlobalProperties.getInt ("dc.downslots");
			boolean passive = GlobalProperties.getBool ("dc.passive");
			mha = new MultiHubsAdapter(nick, ip, port, port, pass, desc,
					connection, email, "0", uSlots, dSlots, passive);
			
			String mDir = GlobalProperties.getString ("dc.miscdir");
			String iDir = GlobalProperties.getString ("dc.incompleteDir");
			String sDir = GlobalProperties.getString ("dc.sharedir");
			
			mha.setDirs(mDir, iDir);
			shareManager = new ShareManager(mha);
			dc = new DownloadCentral(mha);

			mha.setShareManager(shareManager); //CORRECT
			mha.setDownloadCentral(dc); //CORRECT
			
			File sDirFile = new File (sDir);
			List<File> a = Collections.singletonList(sDirFile);
			List<File> b = new ArrayList<File> ();
			FilenameFilter c = new AcceptingFilter ();
			String d = null;
			shareManager.addShare(a, b, c, d);
			this.context = context;
			shareManager.addListener (new FileListTabManager (context));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HashException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShareException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public EventjDCBot connect (String ip, int port) {
		SU.assertEdt ();
		EventjDCBot bot = null;
		try {
			bot = new EventjDCBot (mha) {};
			bot.addListener (this);
			mha.connect (ip, port, bot);
			return bot;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BotException e) {
			e.printStackTrace();
		}
		if (bot != null)
			bot.quit();
		bot.terminate ();
		return null;
	}
	private static class AcceptingFilter implements FilenameFilter{
		@Override
		public boolean accept(File arg0, String arg1) {
			return true;
		}
	}
	public void StartSearch (Searcher searcher) throws IOException {
		synchronized (searchers) {
			searchers.add (searcher);
			mha.Search (searcher.ss);
		}
	}
	public void StopSearch (Searcher searcher) {
		synchronized (searchers) {
			searchers.remove (searcher);
		}
	}
	
	@Override
	public void on_SearchResult (jDCBot src, String senderNick,
			String senderIP, int senderPort, SearchResultSet result,
			int free_slots, int total_slots, String hubName) {
		synchronized (searchers) {
			for (Searcher searcher : searchers)
				searcher.onSearch (src, senderNick, senderIP, senderPort, result,
						free_slots, total_slots, hubName);
		}
	}
	public void download (String file, boolean isHash, long len, File saveto, User u) throws BotException, FileNotFoundException {
		SU.assertEdt ();
		dc.download (file, isHash, len, saveto, u);
	}
	
	@Override
	public void on_Connect (jDCBot src) {

		
	}
	@Override
	public void on_Connect2Client (jDCBot src) {

		
	}
	@Override
	public void on_BotQuit (jDCBot src) {

		
	}
	@Override
	public void on_Disconnect (jDCBot src) {

		
	}
	@Override
	public void on_PublicMessage (jDCBot src, String user, String message) {

		
	}
	@Override
	public void on_Join (jDCBot src, String user) {

		
	}
	@Override
	public void on_Quit (jDCBot src, String user) {

		
	}
	@Override
	public void on_UpdateMyInfo (jDCBot src, String user) {

		
	}
	@Override
	public void on_PrivateMessage (jDCBot src, String user, String message) {

		
	}
	@Override
	public void on_ChannelMessage (jDCBot src, String user, String channel,
			String message) {

		
	}
	@Override
	public void on_PassiveSearch (jDCBot src, String user, SearchSet search) {

		
	}
	@Override
	public void on_ActiveSearch (jDCBot src, String ip, int port,
			SearchSet search) {

		
	}
	@Override
	public void on_DownloadComplete (jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {

		
	}
	@Override
	public void on_DownloadStart (jDCBot src, User user, DUEntity due) {

		
	}
	@Override
	public void on_UploadComplete (jDCBot src, User user, DUEntity due,
			boolean success, BotException e) {

		
	}
	@Override
	public void on_UploadStart (jDCBot src, User user, DUEntity due) {

		
	}
	public void shutdown () {
		SU.assertEdt ();
		mha.terminate ();
	}
}
