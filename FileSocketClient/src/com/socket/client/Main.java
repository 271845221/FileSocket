package com.socket.client;

import java.awt.EventQueue;

import com.socket.client.ui.ClientWindow;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new ClientWindow().setVisible(true);
			}
		});
	}

}
