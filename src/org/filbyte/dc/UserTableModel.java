package org.filbyte.dc;
import java.util.*;

import javax.swing.table.AbstractTableModel;

import org.elite.jdcbot.framework.User;
import org.filbyte.SU;
import org.filbyte.StringUtils;

public class UserTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 651160118509750123L;
	private ArrayList<User> users;

	public UserTableModel (User[] users) {
		SU.assertEdt ();
		this.users = new ArrayList<User> ();
		this.users.addAll (Arrays.asList (users));
	}
	
	@Override public boolean isCellEditable (int row, int column) {
		return false;
	}

	@Override
	public int getColumnCount () {
		return 6;
	}

	@Override
	public int getRowCount () {
		SU.assertEdt ();
		return users.size ();
	}
	
	public User getUser (int row) {
		SU.assertEdt ();
		return users.get (row);
	}

	@Override
	public Object getValueAt (int arg0, int arg1) {
		SU.assertEdt ();
		switch (arg1) {
			case 0:
				return users.get (arg0).username ();
			case 1:
				return StringUtils.prettySize (Long.parseLong (users.get (arg0).sharesize ()));
			case 2:
				return users.get (arg0).real_description ();
			case 3:
				return users.get (arg0).tag ();
			case 4:
				return users.get (arg0).connection_type ();
			case 5:
				return users.get (arg0).mail ();
				
		}
		return null;
	}

	@Override
	public String getColumnName (int arg0) {
		SU.assertEdt ();
		switch (arg0) {
			case 0:
				return "Nickname";
			case 1:
				return "Sharesize";
			case 2:
				return "Decription";
			case 3:
				return "Tag";
			case 4:
				return "Connection";
			case 5:
				return "Email";
		}
		return null;
	}

	public void removeUser (String user) {
		SU.assertEdt ();
		synchronized (this) {
			for (int i = 0; i < users.size (); ++i) {
				User u = users.get (i);
				if (u.username ().equalsIgnoreCase (user)) {
					users.remove (i);
					fireTableRowsDeleted (i, i);
					return;
				}
			}
		}
	}

	public void addUser (User user) {
		SU.assertEdt ();
		synchronized (this) {
			users.add (user);
			fireTableRowsInserted (users.size () - 1, users.size () - 1);
		}
	}

	public void updateInfo (User user) {
		SU.assertEdt ();
		synchronized (this) {
			String nick = user.username ();
			for (int i = 0; i < users.size (); ++i) {
				User u = users.get (i);
				if (u.username ().equalsIgnoreCase (nick)) {
					users.set (i, user);
					fireTableRowsUpdated (i, i);
					return;
				}
			}
		}
	}
}
