package com.example.x_practice_backend;

import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setUsername("newUser123");
        newUser.setPassword("465321");

        userService.save(newUser);
    }
}
