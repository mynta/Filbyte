package org.filbyte;
import javax.swing.*;
import javax.swing.text.html.*;


public class PrettyTextArea {
	public JTextPane jtp;
	public JScrollPane jsp;
	StringBuffer content = new StringBuffer ();


	public PrettyTextArea (boolean editable) {
		SU.assertEdt ();
		jtp = new JTextPane ();
		jtp.setEditorKit (new HTMLEditorKit ());
		jtp.setEditable (editable);

		jsp = new JScrollPane (jtp);
		jsp.setHorizontalScrollBarPolicy (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	private void appendHTML (String str) {
		SU.assertEdt ();
		content.append (str);
		jtp.setText (content.toString ());
	}
	public void appendText (String str) {
		SU.assertEdt ();
		content.append (HTMLUtil.textToHtml (str));
		jtp.setText (content.toString ());
	}
	public void appendBold (String str) {
		SU.assertEdt ();
		content.append ("<b>");
		content.append (HTMLUtil.textToHtml (str));
		content.append ("</b>");
		jtp.setText (content.toString ());
	}
	public void appendBoldAndTextLater (final String bold, final String text) {
		SwingUtilities.invokeLater (new Runnable () {
			@Override
			public void run () {
				appendBold (bold);
				appendText (text);
			}
		});
	}
	public void appendTextLater(final String str) {
		SwingUtilities.invokeLater (new Runnable () {
			@Override
			public void run () {
				appendText (str);
			}
		});
	}
	public void appendHTMLLater(final String str) {
		SwingUtilities.invokeLater (new Runnable () {
			@Override
			public void run () {
				appendHTML (str);
			}
		});
	}
	public void appendBoldLater (final String str) {
		SwingUtilities.invokeLater (new Runnable () {
			@Override
			public void run () {
				appendBold (str);
			}
		});
	}
}
