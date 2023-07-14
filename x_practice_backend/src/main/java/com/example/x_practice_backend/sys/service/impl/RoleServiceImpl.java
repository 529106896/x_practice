package com.example.x_practice_backend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.x_practice_backend.sys.entity.Role;
import com.example.x_practice_backend.sys.entity.RoleMenu;
import com.example.x_practice_backend.sys.mapper.RoleMapper;
import com.example.x_practice_backend.sys.mapper.RoleMenuMapper;
import com.example.x_practice_backend.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yjz
 * @since 2023-07-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        // 先将新增角色写入角色Role表
        this.baseMapper.insert(role);

        // 再将角色对应权限写入角色菜单Role-Menu表
        if (null != role.getMenuIdList()) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer id) {
        // 先根据id选出对应角色
        Role role = this.baseMapper.selectById(id);

        // 再根据角色id查出对应的菜单项，注意要只选出子菜单
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        // 修改角色表
        this.baseMapper.updateById(role);

        // 删除角色在role_menu表中对应的数据
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);

        // 把修改后的数据再插入role_menu表
        if (null != role.getMenuIdList()) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        // 先删除角色表中的数据
        this.baseMapper.deleteById(id);

        // 再删除角色在role_menu表中对应的数据
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
    }
}
