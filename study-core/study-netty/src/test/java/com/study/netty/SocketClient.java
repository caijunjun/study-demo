package com.study.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketClient {

	public static void main(String[] args) {
		try {
			SocketChannel socketChannel = SocketChannel.open();
			SocketAddress remote = new InetSocketAddress("127.0.0.1", 9393);
			// 设置非阻塞模式
			socketChannel.configureBlocking(false);
			socketChannel.connect(remote);

			Selector selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			// 获取就绪的 socket 个数
			while (selector.select() > 0) {

				// 获取符合的 socket 在选择器中对应的事件句柄 key
				Set<SelectionKey> keys = selector.selectedKeys();

				// 遍历所有的key
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {

					// 获取对应的 key，并从已选择的集合中移除
					SelectionKey key = (SelectionKey) it.next();
					
					if (key.isConnectable()) {
						// 进行连接操作
						connect(key);
					} else if (key.isWritable()) {
						// 进行写操作
						write(key);
					} else if (key.isReadable()) {
						// 进行读操作
						receive(key);
					}
					it.remove();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void receive(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		System.out.println(new String(buffer.array(),"UTF-8"));

	}

	private static void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		// 向 SocketChannel 写入事件
		channel.write(ByteBuffer.wrap("你好".getBytes()));
		// 修改 SocketChannel 所关心的事件
		key.interestOps(SelectionKey.OP_READ);
	}

	private static void connect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		channel.configureBlocking(false);
		channel.finishConnect();
		// 打印连接信息
		InetSocketAddress remote = (InetSocketAddress) channel.socket().getRemoteSocketAddress();
		String host = remote.getHostName();
		int port = remote.getPort();
		String connectStr = String.format("访问地址: %s:%s 连接成功!", host, port);
		System.out.println(connectStr);
	}
}
