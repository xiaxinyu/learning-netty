package org.learning.netty.decoder.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class DelimiterServerHandler extends ChannelInboundHandlerAdapter {
	int counter = 0;

	/**
	 * 每次传入的消息都要调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("This is " + (++counter) + " times receive client: [" + body + "]");
		body += "$_";
		ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(echo);
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
