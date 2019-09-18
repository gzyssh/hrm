package com.hrm.system.service;


import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.common.utils.PermissionConstants;
import com.hrm.entity.system.Permission;
import com.hrm.entity.system.Role;
import com.hrm.entity.system.User;
import com.hrm.system.dao.PermissionDao;
import com.hrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@Service
public class RoleService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 添加角色
     */
    public void save(Role role) {
        role.setId(String.valueOf(idWorker.nextId()));
        roleDao.save(role);
    }
    /**
     * 更新角色
     */
    public void update(Role role) {
        //1.根据id查询角色
        Role target = roleDao.getOne(role.getId());
        target.setName(role.getName());
        target.setDescription(role.getDescription());
        //2.设置角色属性
        roleDao.save(target);
    }
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }
    /**
     * 删除角色
     * @param id 角色ID
     */
    public void delete(String id) {
        roleDao.deleteById(id);
    }
    /**
     * 获取角色列表（根据企业ID）
     * @param companyId
     * @param page
     * @param size
     * @return
     */
    public Page<Role> findSearch(String companyId, int page, int size) {
        Specification<Role> spec=new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        Page<Role> pageRole = roleDao.findAll(spec, new PageRequest(page-1, size));
        return pageRole;
    }

    public List<Role> findAll(String companyId) {
        return roleDao.findAll(getSpec(companyId));
    }

    /**
     * 分配权限
     * @param roleId
     * @param permIds
     */
    public void assignRoles(String roleId, List<String> permIds) {
        //根据ID查询角色
        Role role = roleDao.findById(roleId).get();
        //设置角色的权限集合
        Set<Permission> perms=new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            //需要根据类型和父ID查询API权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            perms.addAll(apiList);//自动赋予APi权限
            perms.add(permission);//当前菜单或按钮的权限
        }
        //设置角色和权限集合的关系
        role.setPermissions(perms);
        //更新用户
        roleDao.save(role);
    }
}
