package com.learning.netty.msgpack.encoder.decoder;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		final byte[] array;
		int length = msg.readableBytes();
		array = new byte[length];
		msg.getBytes(msg.readerIndex(), array, 0, length);

		MessagePack msgPack = new MessagePack();
		out.add(msgPack.read(array));
	}
}
