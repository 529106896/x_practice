package com.example.x_practice_backend;

import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class XPracticeBackendApplicationTests {

	@Resource
	private UserMapper userMapper;

	@Test
	void testMapper() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
	}

}
