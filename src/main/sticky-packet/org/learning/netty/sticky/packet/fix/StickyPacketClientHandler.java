package org.learning.netty.sticky.packet.fix;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StickyPacketClientHandler extends ChannelInboundHandlerAdapter {
	private int counter;
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static Charset DEFFAULT_CHARSET = Charset.forName("UTF-8");
	private byte[] reqData = ("QUERY TIME ORDER" + LINE_SEPARATOR).getBytes(DEFFAULT_CHARSET);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(reqData.length);
			message.writeBytes(reqData);
			ctx.writeAndFlush(message);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("Now is : " + body.replaceAll(LINE_SEPARATOR, " ") + " : the counter is : " + (++counter));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
