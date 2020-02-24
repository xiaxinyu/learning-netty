package org.learning.netty.http.snoop;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		
	}
}
