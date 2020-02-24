package org.learning.netty.uptime;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class UptimeServerHandler extends SimpleChannelInboundHandler<Object> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// discard
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
