package com.socket.server.util;

import java.net.InetAddress;

public final class Uitity {

	private Uitity() {
	}
	
	public static final boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static String gitLocalIP() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
}
