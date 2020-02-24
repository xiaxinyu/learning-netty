package com.learning.netty.msgpack.encoder.decoder;

import org.learning.netty.decoder.delimiter.DelimiterServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class MsgPackServer {

	public static void init() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024, 0, 2,0,2));
					ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
		            ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
					ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
					ch.pipeline().addLast(new MsgPackServerHandler());
				}
			});
			ChannelFuture f = b.bind(9999).sync();
			System.out.println(DelimiterServer.class.getName() + " started and listening for connections on "
					+ f.channel().localAddress());
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		init();
	}
}
