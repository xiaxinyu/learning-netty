package org.learning.netty.sticky.packet.problem;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StickyPacketServerHandler extends ChannelInboundHandlerAdapter {
	private int counter;
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");
	private final static Charset DEFFAULT_CHARSET = Charset.forName("UTF-8");
	private final static String MARKER = "QUERY TIME ORDER";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, DEFFAULT_CHARSET).substring(0, req.length - LINE_SEPARATOR.length());
		System.out.println("The time server receive order : " + body.replaceAll(LINE_SEPARATOR, " ") + " ; the counter is :" + (++counter));
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
