package com.hrm.system.service;


import com.hrm.common.service.BaseService;
import com.hrm.common.utils.IdWorker;
import com.hrm.entity.company.Department;
import com.hrm.entity.system.Role;
import com.hrm.entity.system.User;
import com.hrm.system.dao.RoleDao;
import com.hrm.system.dao.UserDao;
import com.hrm.system.feign.DepartmentFeign;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private DepartmentFeign departmentFeign;

    /**
     * 添加用户
     */
    public void save(User user) {
        user.setId(String.valueOf(idWorker.nextId()));
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        user.setPassword(password);//设置初始密码
        user.setLevel("user");//设置用户级别：普通用户
        user.setEnableState(1);
        userDao.save(user);
    }


    @Transactional
    public void saveAll(List<User> list ,String companyId,String companyName){
        for (User user : list) {
            user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
            user.setId(String.valueOf(idWorker.nextId()));
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");
            Department department = departmentFeign.findByCode(user.getDepartmentId(), companyId);
            if(department!=null){
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            userDao.save(user);
        }
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
     * 根据公司ID获取员工列表
     * @param companyId
     * @return
     */
    public List<User> findAll(String companyId) {
        return userDao.findAll(super.getSpec(companyId));
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

    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }
}
