package org.learning.netty.uptime;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class UptimeClient {
	public static final int RECONNECT_DELAY = 5;
	private static final int READ_TIMEOUT = 10;

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9999;

	private Bootstrap bootstrap = new Bootstrap();
	private UptimeClientHandler clientHandler = new UptimeClientHandler(this);

	private void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(HOST, PORT)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new IdleStateHandler(READ_TIMEOUT, 0, 0), clientHandler);
					}
				});
		bootstrap.connect();
	}

	public void connect() {
		bootstrap.connect().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future != null) {
					clientHandler.startTime = -1;
					clientHandler.println("Failed to connect: " + future.cause());
				}
			}
		});
	}

	public static void main(String[] args) {
		new UptimeClient().start();
	}
}
