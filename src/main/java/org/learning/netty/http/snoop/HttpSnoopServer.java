package org.learning.netty.http.snoop;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HttpSnoopServer {
	public void start(int port, boolean isSSL) {
		SslContext sslCtx = null;
		if (isSSL) {
			try {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} catch (SSLException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			}
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpSnoopServerInitializer(sslCtx));
			Channel channel = bootstrap.bind(port).sync().channel();
			System.out.println("Open your web browser and navigate to " + (isSSL ? "https" : "http") + "://127.0.0.1:"
					+ port + '/');
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new HttpSnoopServer().start(9999, false);
	}
}
