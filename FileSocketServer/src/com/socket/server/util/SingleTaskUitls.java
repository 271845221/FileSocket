package com.socket.server.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SingleTaskUitls {

	private SingleTaskUitls() {
	}
	
	private static ScheduledExecutorService sPool = Executors.newSingleThreadScheduledExecutor();
	
	public static void scheduleAtFixedRate(Runnable command,
            long initialDelay,
            long period,
            TimeUnit unit) {
		sPool.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	
	public static void shutdownNow() {
		sPool.shutdownNow();
	}
}
