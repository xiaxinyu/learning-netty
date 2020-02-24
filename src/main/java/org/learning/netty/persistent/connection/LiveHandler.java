package org.learning.netty.persistent.connection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

public class LiveHandler extends SimpleChannelInboundHandler<LiveMessage> {
	private static Map<Integer, LiveChannelCache> channelCache = new HashMap<>();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LiveMessage msg) throws Exception {
		Channel channel = ctx.channel();
		final int hashCode = channel.hashCode();
		System.out.println("channel hashCode:" + hashCode + " msg:" + msg + " cache:" + channelCache.size());

		if (!channelCache.containsKey(hashCode)) {
			System.out.println("channelCache.containsKey(hashCode), put key:" + hashCode);
			channel.closeFuture().addListener(future -> {
				System.out.println("channel close, remove key:" + hashCode);
				channelCache.remove(hashCode);
			});
			ScheduledFuture scheduledFuture = ctx.executor().schedule(() -> {
				System.out.println("schedule runs, close channel:" + hashCode);
				channel.close();
			}, 10, TimeUnit.SECONDS);
			channelCache.put(hashCode, new LiveChannelCache(channel, scheduledFuture));
		}

		switch (msg.getType()) {
		case LiveMessage.TYPE_HEART: {
			LiveChannelCache cache = channelCache.get(hashCode);
			ScheduledFuture scheduledFuture = ctx.executor().schedule(() -> channel.close(), 5, TimeUnit.SECONDS);
			cache.getScheduledFuture().cancel(true);
			cache.setScheduledFuture(scheduledFuture);
			ctx.channel().writeAndFlush(msg);
			break;
		}
		case LiveMessage.TYPE_MESSAGE: {
			channelCache.entrySet().stream().forEach(entry -> {
				Channel otherChannel = entry.getValue().getChannel();
				otherChannel.writeAndFlush(msg);
			});
			break;
		}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (null != cause)
			cause.printStackTrace();
		if (null != ctx)
			ctx.close();
	}
}
