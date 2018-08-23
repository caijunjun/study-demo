package com.study.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class TestPool {
	
	//最佳线程数目 = （线程等待时间（io操作）与线程CPU时间之比 + 1）* CPU数目
	static ExecutorService executorService = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1024),
			new BasicThreadFactory.Builder().namingPattern("pool-%d").daemon(true).build(),
			new ThreadPoolExecutor.CallerRunsPolicy());

	static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
			new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
			new ThreadPoolExecutor.AbortPolicy());

	public static void main(String[] args) {
		executorService.execute(() -> System.out.println(Thread.currentThread().getName()));
		try {
			executorService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
