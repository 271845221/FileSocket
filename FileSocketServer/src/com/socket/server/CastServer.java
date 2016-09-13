package com.socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.socket.server.util.SingleTaskUitls;

public class CastServer {

	private int mPort = 9999;
	DatagramSocket mDatagramSocket;
	
	public CastServer() {
	}
	
	public CastServer(int port) {
		this();
		this.mPort = port;
	}
	
	public void sendTimerCast(final byte[] message) {
		SingleTaskUitls.scheduleAtFixedRate(new Runnable() {
			
			public void run() {
				sendCast(message);
			}
		}, 0, 2000, TimeUnit.MILLISECONDS);
	}
	
	public void sendCast(byte[] message) {
		try {
			if (mDatagramSocket == null) {
				mDatagramSocket = new DatagramSocket();
			}
			DatagramPacket dgPacket = new DatagramPacket(message, message.length, InetAddress.getByName("255.255.255.255"), mPort); 
			mDatagramSocket.send(dgPacket);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void quit() {
		if (mDatagramSocket != null) {
			mDatagramSocket.close();
		}
		SingleTaskUitls.shutdownNow();
	}
}
