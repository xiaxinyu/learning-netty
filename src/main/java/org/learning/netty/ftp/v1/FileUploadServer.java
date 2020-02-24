package org.learning.netty.ftp.v1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class FileUploadServer {
	private final int port;

	public FileUploadServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
		new FileUploadServer(8080).start();
	}

	public void start() throws InterruptedException {
		NioEventLoopGroup boss = new NioEventLoopGroup(3);
		NioEventLoopGroup workers = new NioEventLoopGroup(10);

		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(boss, workers)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline()
					.addLast(new ObjectEncoder())
					.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)))
					.addLast(new FileUploadServerHandler());
				}
			})
			.option(ChannelOption.SO_BACKLOG, 1024);

			ChannelFuture future = server.bind(port).sync();
			System.out.println("Start FileUploadServer, port is " + port);
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			workers.shutdownGracefully();
		}
	}
}
