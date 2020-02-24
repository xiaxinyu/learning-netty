package org.learning.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		try {
			System.out.print(in.toString(CharsetUtil.UTF_8));
		} finally {
			in.retain();
			ReferenceCountUtil.release(in);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
