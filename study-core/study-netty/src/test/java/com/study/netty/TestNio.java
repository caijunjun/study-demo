package com.study.netty;

import java.nio.ByteBuffer;

public class TestNio {

	public static void main(String[] args) {

		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		ByteBuffer src = ByteBuffer.wrap("哈喽".getBytes());
		byteBuffer.put(src);
		src.flip();

		// for (int i = 0; i < src.remaining(); i++) {
		// System.out.println(src.get());
		// }
		//
		while (src.hasRemaining()) {
			System.out.println(src.get());
		}
		
		src.flip();
		
		while (src.hasRemaining()) {
			System.out.println(src.get());
		}
		System.out.println();

	}
}
