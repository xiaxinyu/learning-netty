package org.learning.netty.decoder.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DelimiterClientHandler extends ChannelInboundHandlerAdapter {
	private int counter = 0;
	private final static String ECHO_REQ = "Hi, Summer. Welcome to Netty.$_";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("This is " + (++counter) + " times received server:	[" + msg + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
