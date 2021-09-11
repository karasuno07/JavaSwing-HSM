package utils;

import javax.swing.JOptionPane;

public class MessageBox {
	public static void notif(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	public static boolean confirm(String message) {
		return JOptionPane.showConfirmDialog(null, message, "Confirmation",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	public static void alert(String message) {
		JOptionPane.showMessageDialog(null, message, "Error occupied", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String prompt(String message, String title) {
		return JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
	}
}
