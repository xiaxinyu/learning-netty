package org.learning.netty.persistent.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.net.Socket;

public class Test {
	String host = "localhost";
	int port = 8080;

	public void testLongConn() throws Exception {
		final Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port));
		new Thread(() -> {
			while (true) {
				try {
					byte[] input = new byte[64];
					int readByte = socket.getInputStream().read(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		Scanner scanner = new Scanner(System.in);
		int code;
		while (true) {
			code = scanner.nextInt();
			if (code == 0) {
				break;
			} else if (code == 1) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(5);
				byteBuffer.put((byte) 1);
				byteBuffer.putInt(0);
				socket.getOutputStream().write(byteBuffer.array());
			} else if (code == 2) {
				byte[] content = ("hello, I'm" + hashCode()).getBytes();
				ByteBuffer byteBuffer = ByteBuffer.allocate(content.length + 5);
				byteBuffer.put((byte) 2);
				byteBuffer.putInt(content.length);
				byteBuffer.put(content);
				socket.getOutputStream().write(byteBuffer.array());
			}
		}
		socket.close();
	}

	//运行main方法之后，输入1表示发心跳包，输入2表示发content，5秒内不输入1则服务端会自动断开连接。
	public static void main(String[] args) throws Exception {
		new Test().testLongConn();
	}
}
