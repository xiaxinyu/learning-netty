package org.learning.netty.ftp.v1;

import java.io.File;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class FileUploadClient {
	public void connect(int port, String host, final UploadFile uploadFile) throws Exception {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap client = new Bootstrap();
			client
			.group(worker)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					 ch.pipeline()
					 .addLast(new ObjectEncoder())
	                 .addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)))
	                 .addLast(new FileUploadClientHandler(uploadFile));
				}
			})
			.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
			ChannelFuture future = client.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		UploadFile uploadFile = new UploadFile();
		File file = new File("c:/1.txt");
		String fileMd5 = file.getName();
		uploadFile.setFile(file);
		uploadFile.setFile_md5(fileMd5);
		uploadFile.setStartPos(0);
		new FileUploadClient().connect(8080, "127.0.0.1", uploadFile);
	}
}