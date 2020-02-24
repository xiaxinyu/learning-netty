package org.learning.netty.ftp.v1;

import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FileUploadClientHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
	private volatile int start = 0;
	private volatile int lastLength = 0;
	private RandomAccessFile randomAccessFile;
	private UploadFile uploadFile;

	public FileUploadClientHandler(UploadFile file) {
		if (file.getFile().exists()) {
			if (!file.getFile().isFile()) {
				System.out.println("Not a file :" + file.getFile());
				return;
			}
		}
		this.uploadFile = file;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		randomAccessFile = new RandomAccessFile(uploadFile.getFile(), "r");
		randomAccessFile.seek(uploadFile.getStartPos());
		lastLength = (int) randomAccessFile.length() / 10;
		byte[] bytes = new byte[lastLength];
		if ((byteRead = randomAccessFile.read(bytes)) != -1) {
			uploadFile.setEndPos(byteRead);
			uploadFile.setBytes(bytes);
			ctx.writeAndFlush(uploadFile);
		} else {
			System.out.println("文件已经读完");
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead");
		if (msg instanceof Integer) {
			start = (Integer) msg;
			if (start != -1) {
				randomAccessFile = new RandomAccessFile(uploadFile.getFile(), "r");
				randomAccessFile.seek(start);
				System.out.println("块儿长度：" + (randomAccessFile.length() / 10));
				System.out.println("长度：" + (randomAccessFile.length() - start));
				int a = (int) (randomAccessFile.length() - start);
				int b = (int) (randomAccessFile.length() / 10);
				if (a < b) {
					lastLength = a;
				}
				byte[] bytes = new byte[lastLength];
				System.out.println("-----------------------------" + bytes.length);
				if ((byteRead = randomAccessFile.read(bytes)) != -1 && (randomAccessFile.length() - start) > 0) {
					System.out.println("byte 长度：" + bytes.length);
					uploadFile.setEndPos(byteRead);
					uploadFile.setBytes(bytes);
					try {
						ctx.writeAndFlush(uploadFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					randomAccessFile.close();
					ctx.close();
					System.out.println("文件已经读完--------" + byteRead);
				}
			}
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
