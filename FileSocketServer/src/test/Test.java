package test;

import java.net.InetAddress;

public class Test {

	public static void main(String[] args) {
//		String ip = "";
//		try {
//			InetAddress add = InetAddress.getLocalHost();
//			ip = add.getHostAddress();
//			ip = ip.substring(0, ip.lastIndexOf(".") +  1);
//			System.out.println(add.getHostAddress() + ", " + add.getCanonicalHostName());
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		for (int i = 1; i < 256; i++) {
//			String host = ip + i;
//			try {
//				InetAddress ia = InetAddress.getByName(host);
//				if (ia.isReachable(1200)) {
//					System.out.println(ia);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
		CastServer server = new CastServer();
		server.sendCast("发送广播！".getBytes());
	}
	
}
