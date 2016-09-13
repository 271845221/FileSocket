package com.socket.server.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class SocketHeader implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6842294227148567062L;
	/** 文件唯一性ID */
	private String id;
	/** 文件大小 */
	private long length;
	/** 文件名称 */
	private String name;
	/** 文件上传位置 */
	private long position;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public static SocketHeader toHeader(String json) {
		return JSON.parseObject(json, SocketHeader.class);
	}
	
	public String toJSONString() {
		return JSON.toJSONString(this);
	}
	
	public String toResponse() {
		return toJSONString() + "\r\n";
	}
}
