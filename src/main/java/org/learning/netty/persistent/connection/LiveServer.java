package org.learning.netty.persistent.connection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class LiveServer {
	private final int port;

	public LiveServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		int port;  
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new LiveServer(port).start();
	}

	public void start() throws Exception {
		ServerBootstrap b = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		b.group(group).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("decoder", new LiveDecoder()) // 1
						.addLast("encoder", new LiveEncoder()) // 2
						.addLast("aggregator", new HttpObjectAggregator(256 * 1024)) // 3
						.addLast("handler", new LiveHandler()); // 4
			}
		}).option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
				.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

		b.bind(port).sync();
	}
}
