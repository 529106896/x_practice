import request from '@/utils/request'

export default {
    getRoleList(searchModel) {
        return request({
            url: '/role/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                roleName: searchModel.roleName,
            }
        });
    },
    addRole(role) {
        return request({
            url: '/role',
            method: 'post',
            data: role
        });
    },
    updataRole(role) {
        return request({
            url: '/role',
            method: 'put',
            data: role
        });
    },
    saveRole(role) {
        if (role.roleId == null || role.roleId == undefined) {
            return this.addRole(role);
        }
        return this.updataRole(role);
    },
    getRoleById(id) {
        return request({
            url: `/role/${id}`,
            method: 'get'
        });
    },
    deleteRoleById(id) {
        return request({
            url: `/role/${id}`,
            method: 'delete'
        });
    },
}