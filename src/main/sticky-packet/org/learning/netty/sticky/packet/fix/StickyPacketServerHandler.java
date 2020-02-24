package org.learning.netty.sticky.packet.fix;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StickyPacketServerHandler extends ChannelInboundHandlerAdapter {
	private int counter;
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static String MARKER = "QUERY TIME ORDER";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		body = body.replaceAll(LINE_SEPARATOR, " ");
		System.out.println("The time server receive order : " + body + " ; the counter is :" + (++counter));
		String currentTime = MARKER.equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
		currentTime += LINE_SEPARATOR;
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
