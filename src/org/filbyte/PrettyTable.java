package org.filbyte;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

public class PrettyTable {
	public interface PopupBuilder {
		public void buildPopup (JTable table, MouseEvent e, List<Integer> selected);
	}

	private static final long serialVersionUID = -2702013849513483876L;
	public BetterJTable jt;
	public JScrollPane jsp;
	private final static Dimension min = new Dimension (0, 0);

	public PrettyTable (TableModel tm, final PopupBuilder pb) {
		SU.assertEdt ();
		jt = new BetterJTable (tm);
		jt.setAutoCreateRowSorter (true);
		jt.getColumnModel ().setSelectionModel (new NoSelectionModel ());
		
    	jt.addMouseListener (new MouseListener () {
			@Override
			public void mouseClicked (MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered (MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited (MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed (MouseEvent e) {
				SU.assertEdt ();
				if (e.getButton () == MouseEvent.BUTTON3 &&
						!e.isShiftDown () && !e.isControlDown ()) {
					Point p = e.getPoint ();
					int viewRow = jt.rowAtPoint (p);
					if (viewRow >= 0 && viewRow < jt.getRowCount ()) {
						ListSelectionModel lsm = jt.getSelectionModel ();
						if (!lsm.isSelectedIndex (viewRow)) {
							lsm.setSelectionInterval (viewRow, viewRow);
						}
					}
				}
			}

			@Override
			public void mouseReleased (MouseEvent e) {
				SU.assertEdt ();
				if (e.getButton () == MouseEvent.BUTTON3) {
					int [] selected = jt.getSelectedRows ();
					List<Integer> modelSelected = new ArrayList <Integer> ();
					for (int i = 0; i < selected.length; ++i)
						modelSelected.add(jt.convertRowIndexToModel (selected[i]));
					pb.buildPopup (jt, e, modelSelected);
				}
			}
    	});
		jsp = BetterJTable.createStripedJScrollPane (jt);
		/*TableColumn tc = jt.getColumnModel ().getColumn (jt.getColumnCount () - 1);
		tc.setMinWidth (0);
		tc.setResizable (false);
		jsp.addComponentListener (new ComponentAdapter () {
			public void componentResized (ComponentEvent e) {
				Component c = e.getComponent ();
				int viewWidth = c.getWidth ();
				int tableWidth = jt.getWidth ();
				int diffWidth = viewWidth - tableWidth;
				TableColumn tc = jt.getColumnModel ().getColumn (jt.getColumnCount () - 1);
				int size = tc.getWidth () + diffWidth - 1;
				if (size < 0)
					size = 0;
				tc.setResizable (true);
				tc.setPreferredWidth (size);
				tc.setResizable (false);
			}
		});*/

		jsp.setMinimumSize (min);
		jsp.setPreferredSize (min);
		jsp.setBorder (new EmptyBorder (0, 0, 0, 0));
	}
	private static class NoSelectionModel implements ListSelectionModel {
		@Override public void addListSelectionListener (ListSelectionListener x) {}
		@Override public void addSelectionInterval (int index0, int index1) {}
		@Override public void clearSelection () {}
		@Override public int getAnchorSelectionIndex () {return -1;}
		@Override public int getLeadSelectionIndex () {return -1;}
		@Override public int getMaxSelectionIndex () {return -1;}
		@Override public int getMinSelectionIndex () {return -1;}
		@Override public int getSelectionMode () {return ListSelectionModel.SINGLE_SELECTION;}
		@Override public boolean getValueIsAdjusting () {return false;}
		@Override public void insertIndexInterval (int index, int length, boolean before) {}
		@Override public boolean isSelectedIndex (int index) {return false;}
		@Override public boolean isSelectionEmpty () {return true;}
		@Override public void removeIndexInterval (int index0, int index1) {}
		@Override public void removeListSelectionListener (ListSelectionListener x) {}
		@Override public void removeSelectionInterval (int index0, int index1) {}
		@Override public void setAnchorSelectionIndex (int index) {}
		@Override public void setLeadSelectionIndex (int index) {}
		@Override public void setSelectionInterval (int index0, int index1) {}
		@Override public void setSelectionMode (int selectionMode) {}
		@Override public void setValueIsAdjusting (boolean valueIsAdjusting) {}
	}
}
