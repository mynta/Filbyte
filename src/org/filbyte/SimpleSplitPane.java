package org.filbyte;

import javax.swing.JSplitPane;

public class SimpleSplitPane extends JSplitPane {
	private static final long serialVersionUID = -5293662011269613591L;
	private double weight;

	public SimpleSplitPane () {
		this (0.5);
		SU.assertEdt ();
	}
	public SimpleSplitPane (double weight) {
		SU.assertEdt ();
		this.weight = weight;
	}
	
	@Override
	public int getDividerLocation () {
		SU.assertEdt ();
		double width = getWidth ();
		return (int)(weight * width);
	}

	@Override
	public int getDividerSize () {
		SU.assertEdt ();
		return super.getDividerSize ();
	}

	@Override
	public int getLastDividerLocation () {
		SU.assertEdt ();
		return super.getLastDividerLocation ();
	}

	@Override
	public int getMaximumDividerLocation () {
		SU.assertEdt ();
		return getWidth ();
	}

	@Override
	public int getMinimumDividerLocation () {
		SU.assertEdt ();
		return 0;
	}

	@Override
	public double getResizeWeight () {
		SU.assertEdt ();
		return weight;
	}

	@Override
	public void setResizeWeight (double weight) {
	}

	@Override
	public void setDividerLocation (double proportionalLocation) {
	}

	@Override
	public void setDividerLocation (int location) {
	}
}
