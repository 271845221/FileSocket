package com.socket.server.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class UIUtils {

	private UIUtils() {
	}
	
	public static int[] getScreenDisplay() {
		int[] display = new int[2];
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		display[0] = d.width;
		display[1] = d.height;
		return display;
	}
	
	public static void toast(JFrame frame, String message) {
		JOptionPane.showMessageDialog(frame, message);
	}
	
}
