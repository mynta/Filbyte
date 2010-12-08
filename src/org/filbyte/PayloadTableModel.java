package org.filbyte;

import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;

public class PayloadTableModel<T> implements TableModel {
	private static final long serialVersionUID = 1101239769437904279L;
	public String [] headers;
	List<List<Object>> rows = new ArrayList<List<Object>> ();
	List<T> payloads = new ArrayList<T> ();
	List<TableModelListener> tmls = new ArrayList<TableModelListener> ();

	public PayloadTableModel (String [] headers) {
		this.headers = headers;
	}
	
	public void addRow (List<Object> row, T payload) {
		SU.assertEdt ();
		int rowIndex = rows.size ();
		rows.add (row);
		payloads.add (payload);
		TableModelEvent tme = new TableModelEvent (this, rowIndex, rowIndex,
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		for (TableModelListener tml : tmls) {
			tml.tableChanged (tme);
		}
	}

	@Override public void addTableModelListener (TableModelListener l) {
		SU.assertEdt ();
		tmls.add (l);
	}

	@Override public Class<?> getColumnClass (int columnIndex) {
		SU.assertEdt ();
		if (columnIndex < headers.length)
			return Object.class;
		throw new IndexOutOfBoundsException ("Index out of bounds"); 
	}

	@Override public int getColumnCount () {
		SU.assertEdt ();
		return headers.length;
	}

	@Override public String getColumnName (int columnIndex) {
		SU.assertEdt ();
		return headers[columnIndex];
	}

	@Override public int getRowCount () {
		SU.assertEdt ();
		return rows.size ();
	}

	@Override public Object getValueAt (int rowIndex, int columnIndex) {
		SU.assertEdt ();
		return rows.get (rowIndex).get (columnIndex);
	}

	@Override public boolean isCellEditable (int rowIndex, int columnIndex) {
		SU.assertEdt ();
		return false;
	}

	@Override public void removeTableModelListener (TableModelListener l) {
		SU.assertEdt ();
		tmls.remove (l);
	}

	@Override public void setValueAt (Object aValue, int rowIndex,
			int columnIndex) {
		SU.assertEdt ();
		throw new RuntimeException ();
	}

	public void clear () {
		SU.assertEdt ();
		int nrows = rows.size ();
		if (nrows != 0) {
			rows.clear ();
			payloads.clear ();
			TableModelEvent tme = new TableModelEvent (this, 0, nrows - 1,
					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
			for (TableModelListener tml : tmls) {
				tml.tableChanged (tme);
			}
		}
	}
}
