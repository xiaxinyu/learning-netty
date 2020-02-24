package org.learning.netty.http.server.v2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer {
	private final int port;

	public HttpServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		new HttpServer(8080).start();
	}

	public void start() throws InterruptedException {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup workers = new NioEventLoopGroup();

		ServerBootstrap server = new ServerBootstrap();
		server
		.group(boss, workers)
		.channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				System.out.println("initChannel ch:" + ch);
				ch.pipeline().addLast("decoder", new HttpRequestDecoder())
						.addLast("encoder", new HttpResponseEncoder())
						.addLast("aggregator", new HttpObjectAggregator(512 * 1024))
						.addLast("handler", new HttpHandler());
			}
		})
		.option(ChannelOption.SO_BACKLOG, 128)
		.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
		
		server.bind(port).sync();
	}
}
