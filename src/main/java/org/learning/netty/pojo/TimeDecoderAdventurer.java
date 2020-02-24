package org.learning.netty.pojo;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class TimeDecoderAdventurer extends ReplayingDecoder<Void> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		out.add(in.readBytes(4));
	}
}
