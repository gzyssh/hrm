package com.hrm.system.service;


import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.entity.system.Role;
import com.hrm.entity.system.User;
import com.hrm.system.dao.RoleDao;
import com.hrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@Service
public class UserService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 添加用户
     */
    public void save(User user) {
        user.setId(String.valueOf(idWorker.nextId()));
        user.setPassword("123456");//设置初始密码
        user.setEnableState(1);
        userDao.save(user);
    }
    /**
     * 更新用户
     */
    public void update(User user) {
        //1.根据id查询用户
        User target = userDao.findById(user.getId()).get();
        //2.设置用户属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.save(target);
    }
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }
    /**
     * 删除用户
     * @param id 用户ID
     */
    public void delete(String id) {
        userDao.deleteById(id);
    }
    /**
     * 获取用户列表
     * 参数：map集合的形式
     * hasDept 是否分配部门
     * departmentId
     * companyId
     */
    public Page findAll(Map<String,Object> map, int page, int size) {

        Specification<User> spec=new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if(!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                if(!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                //根据请求的hasDept判断   0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                if(!StringUtils.isEmpty(map.get("hasDept"))){
                    if("0".equals((String) map.get("hasDept"))){
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else{
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page-1, size));
        return pageUser;
    }

    /**
     * 分配角色
     * @param userId
     * @param roleIds
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //根据ID查询用户
        User user = userDao.findById(userId).get();
        //设置用户的角色集合
        Set<Role> roles=new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //更新用户
        userDao.save(user);
    }
}
