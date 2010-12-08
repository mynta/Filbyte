package org.filbyte;

public class HTMLUtil {
	private static String replaceTags (String text) {
		return text.replaceAll ("\\<", "\\&lt\\;").replaceAll ("\\>", "\\&gt\\;");
	}
	private static String replaceNewlines (String text) {
		return text.replaceAll ("\\n", "<br>");
	}
	
	public static String textToHtml (String text) {
		return replaceNewlines (replaceTags (text));
	}
}
