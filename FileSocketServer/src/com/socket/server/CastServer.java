package com.socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class CastServer {

	private Timer mTimer;
	private int mPort = 9999;
	DatagramSocket mDatagramSocket;
	
	public CastServer() {
	}
	
	public CastServer(int port) {
		this();
		this.mPort = port;
	}
	
	public void sendTimerCast(final byte[] message) {
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					sendCast(message);
				}
			}, 0, 2000);
		}
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
		if (mTimer != null) {
			mTimer.cancel();
		}
	}
}
