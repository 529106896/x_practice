package com.example.x_practice_backend.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.mapper.UserMapper;
import com.example.x_practice_backend.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.x_practice_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yjz
 * @since 2023-07-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 使用Jwt代替传统token
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(User user) {
        // 先根据用户名进行查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);

        // 结果不为空，并且传入的密码和数据库中的密码是匹配的，则生成token，并且将用户信息放入redis
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            // 暂时使用UUID来生成token，终极方案应该是jwt
//            String key = "user:" + UUID.randomUUID();

            // 存入redis
            loginUser.setPassword(null);
            // 设置30分钟有效期
//            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);

            // 使用jwt来代替UUID的token
            String token = jwtUtil.createToken(loginUser);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }

    // 旧版登录逻辑（未使用密码加密）
//    @Override
//    public Map<String, Object> login(User user) {
//        // 根据用户名和密码进行查询
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//
//        // 结果不为空，则生成token，并且将用户信息放入redis
//        if (loginUser != null) {
//            // 暂时使用UUID来生成token，终极方案应该是jwt
//            String key = "user:" + UUID.randomUUID();
//
//            // 存入redis
//            loginUser.setPassword(null);
//            // 设置30分钟有效期
//            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
//
//            // 返回数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", key);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token从redis中获取用户信息
//        Object obj = redisTemplate.opsForValue().get(token);

        // 因为jwt不再存在redis中，所以不需要从redis中获取token，直接解析jwt即可
        User loginUser = null;
        // 和原本的逻辑不同，如果解析出错，会抛异常，所以加上try-catch
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果拿到了对应信息
        if (loginUser != null) {
            // 把用户信息放到map中返回
            Map<String, Object> data = new HashMap<>();
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());
            // 根据用户id获取角色信息时需要做关联查询
            List<String> userRoleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", userRoleList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        // 因为不再使用redis，所以不需要从redis删除token了
//        redisTemplate.delete(token);

    }
}
