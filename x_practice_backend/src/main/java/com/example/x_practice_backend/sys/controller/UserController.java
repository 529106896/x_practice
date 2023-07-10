package com.example.x_practice_backend.sys.controller;

import com.example.x_practice_backend.commmon.Result;
import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yjz
 * @since 2023-07-08
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin  可以使用这种方式来跨域，但不推荐
public class UserController {
    @Autowired
    private IUserService userService;

    // 获取所有用户信息
    @GetMapping("/all")
    public Result<List<User>> getAllUser() {
        return Result.success(userService.list(), "查询成功");
    }

    // 用户登录，登录成功返回用户token
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);

        // 登录成功
        if (data != null) {
            return Result.success(data);
        }
        // 登陆失败
        return Result.fail(20002, "用户名或密码不存在");
    }

    // 根据token获取当前用户信息，token存在则返回用户信息
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
        // 根据用户token来从redis中获取用户信息
        Map<String, Object> data = userService.getUserInfo(token);

        // 获取成功
        if (data != null) {
            return Result.success(data);
        }
        // 登陆失败
        return Result.fail(20003, "登录信息无效，请重新登录");
    }

    // 登出
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }

    // 查询用户
    @GetMapping
    public Result<Map<String, Object>> searchUser(@RequestParam(value = "username", required = false) String username,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        return null;
    }
}
