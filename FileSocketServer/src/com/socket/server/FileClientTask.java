package com.socket.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Map;

import com.socket.server.bean.SocketHeader;
import com.socket.server.util.FileUtils;
import com.socket.server.util.Log;
import com.socket.server.util.StreamUtils;
import com.socket.server.util.Uitity;

public class FileClientTask implements Runnable {
	
	private Socket socket;
	private Map<String, Long> cache;
	private String path;
	
	public FileClientTask(Socket socket, Map<String, Long> cache, String path) {
		this.socket = socket;
		this.cache = cache;
		this.path = path;
	}
	
	public void run() {
		if (Log.debug()) {
			final String from = "host:" + socket.getInetAddress()  + ", port:" + socket.getPort();
			Log.print(from);
		}
		try {
			PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
			// 处理客户端请求头
			String head = StreamUtils.readLine(inStream);
			if (!Uitity.isEmpty(head)) {
				SocketHeader header = SocketHeader.toHeader(head);
				// 获取文件上传位置、及资源ID
				if (!Uitity.isEmpty(header.getId())) {
					Long size = cache.get(header.getId());
					header.setPosition(size == null ? 0 : size.longValue());
				} else {
					header.setId(System.nanoTime() + "");
					header.setPosition(0);
				}
				// 假如文件存放目录不存在，则创建其目录
				File file = new File(path, header.getName());
				FileUtils.createDirectory(file);
				// 告知客户端从文件哪个位置，开始上传
				OutputStream outStream = socket.getOutputStream();
				outStream.write(header.toResponse().getBytes());
				
				// 写文件
				RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
				if (header.getPosition() == 0) {
					// 首次接收文件，指定文件大小
					fileOutStream.setLength(header.getLength());
				}
				// 跳转写文件的起始位置
				fileOutStream.seek(header.getPosition());
				
				// 写文件
				byte[] buffer = new byte[1024];
				int len = -1;
				long length = header.getPosition();
				final String id = header.getId();
				final long totalSize = header.getLength();
				while((len = inStream.read(buffer)) != -1) { //从输入流中读取数据写入到文件中
					fileOutStream.write(buffer, 0, len);
					length += len;
					cache.put(id, length);
					if(length == totalSize) {
						// 文件接收完毕，移除缓存文件接收位置记录、及告知客户端文件上传完毕
						cache.remove(id);
						outStream.write("ok\r\n".getBytes());
					}
				}
				fileOutStream.close();
				outStream.close();
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					socket = null;
				}
			}
		}
	}
}
