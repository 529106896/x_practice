package com.example.x_practice_backend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.x_practice_backend.sys.entity.Menu;
import com.example.x_practice_backend.sys.mapper.MenuMapper;
import com.example.x_practice_backend.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getAllMenu() {
        // 先查所有的一级菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        List<Menu> menuList = this.list(wrapper);
        // 填充子菜单
        setSubMenu(menuList);
        return menuList;
    }

    private void setSubMenu(List<Menu> menuList) {
        if (menuList != null) {
            // 遍历一级菜单
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper = new LambdaQueryWrapper<>();
                // 看谁把一级菜单作为父菜单
                subWrapper.eq(Menu::getParentId, menu.getMenuId());
                List<Menu> subMenuList = this.list(subWrapper);
                menu.setChildren(subMenuList);
                // 递归调用，继续查询子菜单
                setSubMenu(subMenuList);
            }
        }
    }
}
