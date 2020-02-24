package org.learning.netty.http.server.v2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		System.out.println("class:" + msg.getClass().getName());

		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer("test".getBytes()));

		HttpHeaders headers = response.headers();
		headers.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
		headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

		ctx.write(response);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelReadComplete");
		super.channelReadComplete(ctx);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("exceptionCaught");
		if (null != cause)
			cause.printStackTrace();
		if (null != ctx)
			ctx.close();
	}
}
