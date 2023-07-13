package com.example.x_practice_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.x_practice_backend.commmon.Result;
import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.service.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
@Api(tags = {"用户模块控制器"}) // 对整个控制器的说明
@RestController
@RequestMapping("/user")
//@CrossOrigin  可以使用这种方式来跨域，但不推荐
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取所有用户信息
    @GetMapping("/all")
    public Result<List<User>> getAllUser() {
        return Result.success(userService.list(), "查询成功");
    }

    // 用户登录，登录成功返回用户token
    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "com.example.x_practice_backend.sys.entity.User", name = "user", value = "包含用户名和密码的User对象", required = true),
    })
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
    @GetMapping("/list")
    /*
        username: 用户名
        phone: 电话号码
        pageNo: 要查看第几页
        pageSize: 一页显示多少个
     */
    public Result<Map<String, Object>> getUserList(@RequestParam(value = "username", required = false) String username,
                                                  @RequestParam(value = "phone", required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 当用户传入的username不为空时，将条件拼接，下同
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);
        wrapper.orderByDesc(User::getId);
        // 分页，记得在配置文件中引入分页拦截器
        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        // 总共有多少条
        data.put("total", page.getTotal());
        // 查询结果
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    // 新增用户
    @PostMapping
    public Result<?> addUser(@RequestBody User user) {
        // 用户密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return Result.success("新增用户成功");
    }

    // 修改用户
    @PutMapping
    public Result<?> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return Result.success("修改用户成功");
    }

    // 根据用户ID查询用户信息
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<?> deleteUserById(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return Result.success("删除用户成功");
    }
}
