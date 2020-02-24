package com.learning.netty.msgpack.encoder.decoder;

import java.io.Serializable;
import java.nio.ByteBuffer;

import org.msgpack.annotation.Message;

@Message
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userName;
	private int userID;
	
	public UserInfo buildUserName(String userName) {
		this.userName = userName;
		return this;
	}
	
	public UserInfo buildUserInfo(int userID) {
		this.userID = userID;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public byte[] getDataByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.userID);
		buffer.flip();
		
		value = null;
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}

	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", userID=" + userID + "]";
	}
}
