package org.learning.netty.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {
	static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpServerInitializer());

			ChannelFuture f = b.bind(PORT).sync();
			System.out.println("Open your web browser and navigate to " + PORT);

			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		public void initChannel(SocketChannel ch) {
			ChannelPipeline p = ch.pipeline();
			p.addLast(new HttpServerCodec());
			p.addLast(new HttpServerExpectContinueHandler());
			p.addLast(new HttpServerHandler());
		}
	}
}