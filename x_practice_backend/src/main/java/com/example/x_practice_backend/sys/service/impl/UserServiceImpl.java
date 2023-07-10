package com.example.x_practice_backend.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.sys.mapper.UserMapper;
import com.example.x_practice_backend.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    public Map<String, Object> login(User user) {
        // 根据用户名和密码进行查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        User loginUser = this.baseMapper.selectOne(wrapper);

        // 结果不为空，则生成token，并且将用户信息放入redis
        if (loginUser != null) {
            // 暂时使用UUID来生成token，终极方案应该是jwt
            String key = "user:" + UUID.randomUUID();

            // 存入redis
            loginUser.setPassword(null);
            // 设置30分钟有效期
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);

            // 返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", key);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 根据token从redis中获取用户信息
        Object obj = redisTemplate.opsForValue().get(token);
        // 如果拿到了对应信息
        if (obj != null) {
            // 先把Object序列化为Json字符串，再把Json字符串序列化为User对象
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
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
        redisTemplate.delete(token);
    }
}
