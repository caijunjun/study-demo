package com.example.demo;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.dao.UserRepository;
import com.example.demo.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void contextLoads() {
		BsonDocument bson=new BsonDocument();
		mongoTemplate.execute("user", collection->{
			
			return null;
		});
		
		User user=new User();
		user.setId(123457);
		user.setName("测试名称");
		user.setPhone("101");
		userRepository.findAll();
		userRepository.count();
		userRepository.insert(user);
	}

}
