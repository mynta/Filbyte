package org.filbyte;
import java.awt.Component;
import java.awt.event.*;

import javax.swing.*;



public class PopupTabListener implements MouseListener {
	TabManager tm;
	
	public PopupTabListener (TabManager manager) {
		this.tm = manager;
	}
	public void mouseReleased(MouseEvent e) {
		SU.assertEdt ();
		if (!e.isPopupTrigger ())
			return;
		Component c = e.getComponent();
		if (c instanceof JTabbedPane){
			final JTabbedPane jtp = (JTabbedPane)c;
			final int index = jtp.getUI ().tabForCoordinate (jtp, e.getX(), e.getY ());
			if (index != -1 && tm.getIsCloseable (index)) {
				JPopupMenu menu = new JPopupMenu ();
				JMenuItem close = new JMenuItem ("Close");
				close.addActionListener(new ActionListener () {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						tm.removeTab (index);
					}
				});
				menu.add(close);
				menu.show(e.getComponent(), e.getX(), e.getY ());
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
