package com.study.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class SocketServer {

	public static void main(String[] args) {
		try {

			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(9393));
			ssc.configureBlocking(false);

			Selector selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {

				//阻塞
				int readyChannels = selector.select();
				if (readyChannels == 0) {
					continue;
				}
	
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				keyIterator.forEachRemaining(key -> {
					keyIterator.remove();
					if (key.isAcceptable()) {
//						System.out.println("isAcceptable");
					} else if (key.isConnectable()) {
						System.out.println("isConnectable");
					} else if (key.isReadable()) {
						System.out.println("isReadable");
					} else if (key.isWritable()) {
						System.out.println("isWritable");
					}
					
				});
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
