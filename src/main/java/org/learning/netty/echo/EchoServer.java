package org.learning.netty.echo;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public void start() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(this.port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new EchoServerHandler());
						}
					});

			ChannelFuture f = bootstrap.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listening for connections on "
					+ f.channel().localAddress());
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new EchoServer(9999).start();
	}
}
