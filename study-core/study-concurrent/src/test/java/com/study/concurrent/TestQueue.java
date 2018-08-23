package com.study.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestQueue {

	
	
	static BlockingQueue<Integer> queue=new LinkedBlockingQueue<>();
	
	
	public static void main(String[] args) {
		try {
			queue.put(1);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(queue.poll());
		
	}
}
