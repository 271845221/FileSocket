package com.socket.server;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import com.socket.server.bean.SocketHeader;
import com.socket.server.util.Log;
import com.socket.server.util.StreamUtils;

public class FileClient {

	private String host;
	private int port;
	private Socket socket;
	
	public FileClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public boolean upload(String sourceId, String path) {
		boolean is = false;
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
			if (socket == null) {
				socket = new Socket(host, port);
			}
			SocketHeader header = new SocketHeader();
			header.setId(sourceId);
			header.setLength(f.length());
			header.setName(f.getName());
			header.setPosition(0);
			OutputStream outStream = socket.getOutputStream();
			outStream.write(header.toResponse().getBytes());
			PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
			String response = StreamUtils.readLine(inStream);
			if (Log.debug()) {
				Log.print(response);
			}
			header = SocketHeader.toHeader(response);
			RandomAccessFile fileOutStream = new RandomAccessFile(f, "r");
			fileOutStream.seek(header.getPosition());
			int len = 0;
			byte[] buffer = new byte[1024];
			long length = header.getPosition();
			while ((len = fileOutStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				length += len;
				
//				if (Log.debug()) {
//					Log.print("upload length: " + length);
//				}
			}
			is = true;
			response = StreamUtils.readLine(inStream);
			if (Log.debug()) {
				Log.print("last response: " + response);
			}
			fileOutStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public void quit() {
		if (socket != null) {
			try {
				if (socket.getOutputStream() != null) {
					socket.getOutputStream().close();
				}
				if (socket.getInputStream() != null) {
					socket.getInputStream().close();
				}
				socket.close();
			} catch (Exception e) {
				// e.printStackTrace();
				socket = null;
			}
		}
	}
}
