package test;

import java.io.File;

import com.FileClient;

public class ClientTest {

	public static void main(String[] args) {
		final String host = "192.168.1.100";
		final int port = 7878;
		FileClient client = new FileClient(host, port);
		File f = new File("/Users/kejunyao/Desktop/Timer-TimerTask.m4v");
		client.upload(f.getName(), f.getAbsolutePath());
		client.quit();
	}
	
}
