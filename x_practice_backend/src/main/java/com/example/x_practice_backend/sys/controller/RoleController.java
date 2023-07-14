package com.example.x_practice_backend.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.x_practice_backend.commmon.Result;
import com.example.x_practice_backend.sys.entity.Role;
import com.example.x_practice_backend.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    // 查询角色
    @GetMapping("/list")
    public Result<Map<String, Object>> getRoleList(@RequestParam(value = "roleName", required = false) String roleName,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);
        wrapper.orderByDesc(Role::getRoleId);

        Page<Role> page = new Page<>(pageNo, pageSize);
        roleService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    // 新增角色
    @PostMapping
    public Result<?> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("新增角色成功");
    }

    // 修改角色
    @PutMapping
    public Result<?> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    // 根据角色ID查询角色信息
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteRoleById(@PathVariable("id") Integer id) {
        roleService.deleteRoleById(id);
        return Result.success("删除角色成功");
    }

    @GetMapping("/all")
    public Result<List<Role>> getAllRole() {
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }
}
