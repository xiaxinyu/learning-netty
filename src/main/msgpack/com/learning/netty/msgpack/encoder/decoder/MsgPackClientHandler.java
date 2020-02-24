package com.learning.netty.msgpack.encoder.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MsgPackClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserInfo[] userInfos = initUserInfoArray();
		for (UserInfo userInfo : userInfos) {
			//ctx.writeAndFlush(userInfo);
			ctx.write(userInfo);
		}
		ctx.flush();
		System.out.println("-----------------send over-----------------");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Client receives the msgpack message : " + msg);
	}

	private UserInfo[] initUserInfoArray() {
		UserInfo[] userInfos = new UserInfo[100];
		UserInfo userInfo = null;
		for (int i = 0; i < userInfos.length; i++) {
			userInfo = new UserInfo();
			userInfo.setUserID(i);
			userInfo.setUserName(String.format("summer%d", i));
			userInfos[i] = userInfo;
		}
		return userInfos;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
