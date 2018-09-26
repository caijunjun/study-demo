package com.study.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.study.web.dao.UserInfoMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	Logger logger=LoggerFactory.getLogger(ApplicationTest.class);
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Test
	public void test() {
		System.out.println(userInfoMapper.selectByPrimaryKey(1L));
	}
}
