package org.learning.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoderAdventurer extends MessageToByteEncoder<UnixTime> {
	@Override
	protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
		out.writeInt((int) msg.value());
	}
}