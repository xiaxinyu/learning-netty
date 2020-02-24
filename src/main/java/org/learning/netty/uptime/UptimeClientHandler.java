package org.learning.netty.uptime;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Sharable
public class UptimeClientHandler extends SimpleChannelInboundHandler<Object> {
	public long startTime = -1;
	private UptimeClient uptimeClient;

	public UptimeClientHandler(UptimeClient uptimeClient) {
		this.uptimeClient = uptimeClient;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (this.startTime < 0) {
			startTime = System.currentTimeMillis();
		}
		println("Connected to: " + ctx.channel().remoteAddress());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// Discard received data
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (!(evt instanceof IdleStateEvent)) {
			return;
		}

		IdleStateEvent event = (IdleStateEvent) evt;
		if (event.state() == IdleState.READER_IDLE) {
			println("Disconnecting due to no inbound traffic");
			ctx.close();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		println("Disconnected from: " + ctx.channel().remoteAddress());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		println("Sleeping for: " + UptimeClient.RECONNECT_DELAY + "s");

		ctx.channel().eventLoop().schedule(new Runnable() {
			@Override
			public void run() {
				println("Reconnecting to: " + UptimeClient.HOST + ':' + UptimeClient.PORT);
				uptimeClient.connect();
			}
		}, UptimeClient.RECONNECT_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	public void println(String msg) {
		if (this.startTime < 0) {
			System.err.format("[SERVER IS DOWN] %s%n", msg);
		} else {
			System.err.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - this.startTime) / 1000, msg);
		}
	}
}
