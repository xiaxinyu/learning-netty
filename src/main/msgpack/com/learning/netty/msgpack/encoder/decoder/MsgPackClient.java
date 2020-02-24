package com.learning.netty.msgpack.encoder.decoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class MsgPackClient {

	public static void init() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							//这里设置通过增加包头表示报文长度来避免粘包
							ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(1024, 0, 2,0,2));
							ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
							//这里设置读取报文的包头长度来避免粘包
				            ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
							ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
							ch.pipeline().addLast(new MsgPackClientHandler());
						}
					});
			ChannelFuture f = b.connect("localhost", 9999).sync();
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
