package com.socket.client.callback;

public interface ProcessListener {

	void onProcess(long total, long length);
	
	void onResult(boolean isSuccess);
	
}
