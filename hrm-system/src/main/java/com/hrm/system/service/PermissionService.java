package com.hrm.system.service;


import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.service.BaseService;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.common.utils.IdWorker;
import com.hrm.common.utils.PermissionConstants;
import com.hrm.entity.system.*;
import com.hrm.system.dao.PermissionApiDao;
import com.hrm.system.dao.PermissionDao;
import com.hrm.system.dao.PermissionMenuDao;
import com.hrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@Service
@Transactional
public class PermissionService extends BaseService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private PermissionApiDao permissionApiDao;

    /**
     * 添加权限
     */
    public void save(Map<String,Object> map) throws Exception {
        //通过map构造Permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(String.valueOf(idWorker.nextId()));
        //根据类型构造不同的资源对象（菜单、按钮、api）
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //保存
        permissionDao.save(permission);
    }
    /**
     * 更新权限
     */
    public void update(Map<String,Object> map) throws Exception {
        Permission perm = BeanMapUtils.mapToBean(map, Permission.class);
        //1.通过传递的权限id查询权限
        Permission permission = permissionDao.findById(perm.getId()).get();
        permission.setName(perm.getName());
        permission.setCode(perm.getCode());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());
        //2.根据类型构造不同的资源
        Integer type = perm.getType();
        switch (type){
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(perm.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(perm.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(perm.getId());
                permissionApiDao.save(permissionApi);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
       //3.保存
        permissionDao.save(perm);
    }
    /**
     * 根据ID获取权限信息
     * @param id 权限ID
     * @return 权限信息
     */
    public Map<String,Object> findById(String id) throws Exception {
        //查询权限
        //根据权限的类型查询资源
        //构造map集合
        Permission permission = permissionDao.findById(id).get();
        Integer type = permission.getType();
        Object object=null;
        if(type==PermissionConstants.PY_MENU){
            object=permissionMenuDao.findById(id).get();
        }else if(type==PermissionConstants.PY_POINT){
            object=permissionPointDao.findById(id).get();
        }else if(type==PermissionConstants.PY_API){
            object=permissionApiDao.findById(id).get();
        }else{
            throw new CommonException(ResultCode.FAIL);
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("enVisible",permission.getEnVisible());
        map.put("pid",permission.getPid());
        return map;
    }
    /**
     * 删除权限
     * @param id 权限ID
     */
    public void delete(String id) throws Exception {
        //1.通过传递的权限id查询权限
        Permission permission = permissionDao.findById(id).get();
        //2.根据类型构造不同的资源
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.delete(permission);
    }
    /**
     * 获取权限列表
     * 参数：map集合的形式
     * type：0（菜单+按钮）,1(菜单)，2(按钮)，3(ApI接口)
     * pid：父ID
     * enVisible：0查询SaaS平台的最高权限；1查询企业的权限
     */
    public List<Permission> findAll(Map<String,Object> map) {
        Specification<Permission> spec=new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                //根据父ID查询
                if(!StringUtils.isEmpty(map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据enVisible查询
                if(!StringUtils.isEmpty(map.get("enVisible"))){
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //根据类型查询
                if(!StringUtils.isEmpty(map.get("type"))){
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals((String) map.get("type"))){
                        in.value("1").value("2");
                    }else{
                       in.value(Integer.parseInt((String) map.get("type")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(spec);
    }
}
