package com.study.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Iterator;

public class SocketServer {

	public static void main(String[] args) {
		run();
	}

	private static Selector selector;

	private static void run() {
		try {
			// 新建NIO通道
			ServerSocketChannel ssc = ServerSocketChannel.open();
			// 使通道为非阻塞
			ssc.configureBlocking(false);

			ssc.socket().bind(new InetSocketAddress("127.0.0.1", 9393));

			selector = Selector.open();

			// 将NIO通道选绑定到择器,当然绑定后分配的主键为skey
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {

				// 阻塞
				if (selector.select() == 0) {
					continue;
				}

				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					// 先删除还是后删除？
					iterator.remove();
					handleSelectedKey(selectionKey);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleSelectedKey(SelectionKey selectionKey) {
		try {
			if (selectionKey.isAcceptable()) {
				// 切换注册模式

				// 从key中获取关联的通道（此处是ServerSocketChannel，因为需要将服务器的检测模式注册到选择器中）
				ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
				// 获得和客户端连接的通道，并准备使用此通道注册事件
				SocketChannel channel = serverChannel.accept();
				// 设置成非阻塞
				channel.configureBlocking(false);
				// 在这里可以发送消息给客户端
				channel.write(ByteBuffer.wrap(new String("hello client").getBytes()));
				// 在客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限
				channel.register(selector, SelectionKey.OP_READ);

			} else if (selectionKey.isReadable()) {

				ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
				// 刚才在Acceptable 注册的
				SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
				int count = -1;
				while ((count = socketChannel.read(byteBuffer)) > 0) {

					// 解码框架 从客户端读取

					// 处理消息 处理
					System.out.println(new String(byteBuffer.array(), "UTF-8"));
					// 编码框架 写入客户端

					// 清空
					byteBuffer.clear();
				}

				// 远程已经关闭了
				if (count == -1) {
					System.out.println(MessageFormat.format("Close channel {}", socketChannel.getRemoteAddress()));
					socketChannel.close();
				} else {

				}

				System.out.println("isReadable");
			} else if (selectionKey.isWritable()) {
				System.out.println("isWritable");
				// 取消写入事件
				selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
			}
		} catch (IOException e) {
			selectionKey.cancel();
			e.printStackTrace();
		}

	}
}
