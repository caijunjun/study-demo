package com.study.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void connect(String host, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)//
				.channel(NioSocketChannel.class)//
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ClientHandler());
					}
				});

		try {

			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			logger.error("启动netty客户端异常", e);
			Thread.currentThread().interrupt();
		} finally {
			group.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new Client().connect("127.0.0.1", 8888);
	}
}
