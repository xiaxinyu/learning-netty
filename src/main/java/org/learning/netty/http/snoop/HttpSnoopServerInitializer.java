package org.learning.netty.http.snoop;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
	private final SslContext sslCtx;

	public HttpSnoopServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

	}
}
