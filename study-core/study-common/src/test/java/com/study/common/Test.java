package com.study.common;

import com.study.common.util.IdUtil;;

public class Test {

	public static void main(String[] args) {
		IdUtil.getLongId();
		for (int i = 0; i < (1 << 12); i++) {
			new Thread(()->{
				System.out.println(IdUtil.getLongId());
				System.out.println(IdUtil.getUuid());
			}).run();
		}

	}
}
