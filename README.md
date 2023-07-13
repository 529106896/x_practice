# 日志
## 2023.07.13 20:35:04

#### COMPLETE

+ 添加Swagger页面，并将Swagger适配Jwt
+ 添加角色管理页面

#### TODO

+ 完善角色校验

## 2023.07.13 14:00:54

#### COMPLETE

+ 将原本的token改为使用Jwt，并且为所有请求添加Jwt拦截器

#### TODO

+ 在Jwt无效时自动注销，并返回登陆页面

## 2023.07.12

#### COMPLETE

* 为用户列表添加创建日期、修改日期字段，在创建或修改后自动更新

#### TODO

* 想办法防止添加用户时因添加重复信息导致登录出错
  * 目前想到的办法：
    * UNIQUE索引
    * 布隆过滤器
    * 把需要查重的字段放到一个新表里，添加PK索引

## 2023.07.12

#### COMPLETE

+ 完成基础的用户模块：增删改查，成功对接前端与后端

#### TODO

+ 可加入权限校验、角色校验、jwt等

