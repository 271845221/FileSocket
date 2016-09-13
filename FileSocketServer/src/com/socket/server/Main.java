package com.socket.server;

import java.awt.EventQueue;

import com.socket.server.ui.ServerWindow;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				new ServerWindow().setVisible(true);
			}
		});
		
	}

}
