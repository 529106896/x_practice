package com.example.x_practice_backend;

import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.service.IUserService;
import com.example.x_practice_backend.util.JwtValidateInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private JwtValidateInterceptor interceptor;

    @Test
    public void getUserByIdTest() throws Exception {
        User user = new User(2, "yjz", "123456", "123@cscec.com", "110", 1, "www.baidu.com", 0, null, null, null);
        // 模拟Service
        when(userService.getUserById(anyInt())).thenReturn(user);
        // 绕过Jwt验证
        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);

        String resp = mvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        System.out.println(resp);
    }
}
