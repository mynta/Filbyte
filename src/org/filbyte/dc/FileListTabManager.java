package org.filbyte.dc;


import org.elite.jdcbot.framework.User;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.SU;



public class FileListTabManager implements ShareManagerListener {
	DCContext context;
	public FileListTabManager (DCContext context) {
		this.context = context;
	}
	@Override
	public void onFilelistDownloadFinished (final User u, boolean success, Exception e) {
		final FLDir dir = context.manager.shareManager.getOthersFileListManager (u).getFilelist ();
		SU.invokeLater (new Runnable () {
			@Override
			public void run () {
				FileListTab flt = FileListTab.build (u, context, dir);
				context.tabs.addTab ("Fillista", flt, true);
				
			}
		});
	}
	@Override
	public void hashingOfFileStarting (String file) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hashingJobFinished () {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hashingOfFileComplete (String f, boolean success,
			HashException e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hashingOfFileSkipped (String f, String reason) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMiscMsg (String msg) {
		// TODO Auto-generated method stub
		
	}
}
