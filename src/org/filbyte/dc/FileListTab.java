package org.filbyte.dc;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.elite.jdcbot.framework.User;
import org.elite.jdcbot.shareframework.*;
import org.filbyte.*;
import org.filbyte.PrettyTable.PopupBuilder;

public class FileListTab extends SimpleSplitPane {
	private static final long serialVersionUID = 4061427591631584968L;
	User user;
	DCContext context;
	FLDir dir;
	JTree tree;
	PrettyTable pt;

	private static final String headers[] = { "Filename", "Size" };

	public FileListTab (User user, DCContext context, FLDir dir) {
		SU.assertEdt ();
		this.user = user;
		this.context = context;
		this.dir = dir;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode (
				user.username ());
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
				PayloadTableModel<FLFile> ptm = ((PayloadTableModel<FLFile>) pt.jt
						.getModel ());
				TreePath path = arg0.getNewLeadSelectionPath ();
				Object o = null;
				if (path != null) {
					o = path.getLastPathComponent ();
				}
				ptm.clear ();
				if (o instanceof DirNode) {
					for (FLFile file : ((DirNode) o).dir.getFiles ()) {
						List<Object> row = new ArrayList<Object> ();
						row.add (file.name);
						row.add (StringUtils.prettySize (file.size));
						ptm.addRow (row, file);
					}
				}
			}
		});
		dfsAdd (root, dir);
		pt = new PrettyTable (new PayloadTableModel<FLFile> (headers),
				new PopupBuilder () {
					@Override
					public void buildPopup (JTable table, MouseEvent e,
							List<Integer> selected) {
						// TODO Auto-generated method stub

					}
				});
		setLeftComponent (new JScrollPane (tree));
		setRightComponent (pt.jsp);
	}

	private void dfsAdd (DefaultMutableTreeNode parent, FLDir dir) {
		SU.assertEdt ();
		for (FLDir subdir : dir.getSubDirs ()) {
			DefaultMutableTreeNode subnode = new DirNode (subdir);
			parent.add (subnode);
			dfsAdd (subnode, subdir);
		}
	}

	public static FileListTab build (User user, DCContext context, FLDir dir) {
		SU.assertEdt ();
		return new FileListTab (user, context, dir);
	}

	public class DirNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = -1469806468935991001L;
		FLDir dir;

		public DirNode (FLDir dir) {
			super (dir.getName ());
			this.dir = dir;
		}
	}
}
