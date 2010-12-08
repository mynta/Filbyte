package org.filbyte;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class SU {
	public static void invokeLater (Runnable r) {
		if (SwingUtilities.isEventDispatchThread ())
			r.run ();
		else
			SwingUtilities.invokeLater (r);
	}
	public static void invokeAndWait (Runnable r) throws InterruptedException, InvocationTargetException {
		if (SwingUtilities.isEventDispatchThread ())
			r.run ();
		else
			SwingUtilities.invokeAndWait (r);
	}
	public static void assertNotEdt () {
		if (SwingUtilities.isEventDispatchThread ())
			System.err.println ("Slow method running on EDT");
	}
	public static void assertEdt () {
		if (!SwingUtilities.isEventDispatchThread ())
			throw new Error ("Assert failed");
	}
}
