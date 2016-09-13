package test;

import com.FileServer;

public class ServerTest {

	public static void main(String[] args) {
		FileServer server = new FileServer(7878, "/Users/kejunyao/file_server/");
		server.start();
	}

}
