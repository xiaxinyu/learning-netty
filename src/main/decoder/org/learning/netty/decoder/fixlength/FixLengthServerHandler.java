package org.learning.netty.decoder.fixlength;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class FixLengthServerHandler extends ChannelInboundHandlerAdapter {
	private int counter = 0;

	/**
	 * 每次传入的消息都要调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive client : [" + msg + "], counter : [" + (++counter) + "]");
	}

	/**
	 * 异常捕获
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
