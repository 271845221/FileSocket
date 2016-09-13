package com.socket.client.util;

public class Log {

	private Log() {
	}
	
	public static final boolean debug() {
		return true;
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}
	
	public static void print(Exception ex) {
		System.out.println(ex);
	}
}
