package com.socket.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.socket.server.util.FileUtils;

public class FileServer {

	private static final int PORT = 7878;
	
	private ServerSocket mServerSocket;
	/** {@link FileServer}的运行端口 */
	private int mPort;
	/** 接收到的文件的保存路径 */
	private String mPath;
	/** 线程池 */
	private ExecutorService mExecutorService;
	/** 标志{@link FileServer} 运行状态 */
	private boolean isQuit = true;
	/** 缓存文件上传位置 */
	private Map<String, Long> mCache = new ConcurrentHashMap<String, Long>();
	private Thread mServerThread;
	
	public FileServer(String savePath) {
		this(PORT, savePath);
	}
	
	/**
	 * Constructor
	 * @param port 程序运行的端口号
	 * @param savePath 文件保存路径
	 */
	public FileServer(int port, String savePath) {
		this.mPort = port;
		this.mPath = savePath;
		File dir = new File(mPath);
		FileUtils.createDirectory(dir);
		// 创建线程池：池数量 = CPU核数 * 50
		mExecutorService = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors() * 50
				);
	}
	
	/**
	 * 启动Server
	 */
	public void start() {
		if (!isQuit) {
			return;
		}
		try {
			mServerSocket = new ServerSocket(mPort);
			isQuit = false;
		} catch (IOException e) {
			isQuit = true;
			e.printStackTrace();
		}
		mServerThread = new Thread(new Runnable() {
			
			public void run() {
				// 不断接收Socket请求
				while (!isQuit) {
					Socket socket;
					try {
						// 接受客户端请求
						socket = mServerSocket.accept();
						// 将请求放入线程池处理
						mExecutorService.execute(new FileClientTask(socket, mCache, mPath));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				shutdownNow();
			}
		});
		mServerThread.start();
	}
	
	public void quit() {
		if (isQuit) {
			return;
		}
		isQuit = true;
		shutdownNow();
		if (mServerThread != null) {
			try {
				mServerThread.stop();
			} catch (Exception e) {
			}
		}
	}
	
	private void shutdownNow() {
		if (!mExecutorService.isShutdown()) {
			mExecutorService.shutdownNow();
		}
	}
}
