package com.socket.client.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SingleThreadUtils {

	private static ExecutorService sPool = Executors.newSingleThreadExecutor();
	
	private SingleThreadUtils() {
	}
	
	public static void execute(Runnable r) {
		if (r == null) {
			return;
		}
		sPool.execute(r);
	}
}
