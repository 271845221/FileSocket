package com.socket.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;

import com.socket.client.callback.OnResultListener;
import com.socket.client.util.SingleTaskUitls;

public class CastClient {

	private int mPort = 9999;
	private DatagramSocket mDatagramSocket;
	
	public CastClient() {
	}
	
	public CastClient(int port) {
		this();
		this.mPort = port;
	}
	
	public void receiveTimerCast(final OnResultListener<String> listener) {
		SingleTaskUitls.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				receiveCast(listener);
			}
		}, 0, 2000, TimeUnit.MILLISECONDS);
	}
	
	public void receiveCast(OnResultListener<String> listener) {
		try {
			if (mDatagramSocket == null) {
				mDatagramSocket = new DatagramSocket(mPort);
			}
			byte[] by = new byte[1024];
			DatagramPacket packet = new DatagramPacket(by,by.length);  
			mDatagramSocket.receive(packet);
            String str = new String(packet.getData(), 0, packet.getLength());  
            if (listener != null) {
            	listener.onResult(str);
            }
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
