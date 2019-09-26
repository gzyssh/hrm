package com.hrm.system.shiro.realm;

import com.hrm.common.shiro.realm.HrmRealm;
import com.hrm.entity.system.Permission;
import com.hrm.entity.system.User;
import com.hrm.entity.system.response.ProfileResult;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/09/26
 */
public class UserRealm extends HrmRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户的手机号/密码
        UsernamePasswordToken upToken=(UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //根据手机号判断用户是否存在
        User user = userService.findByMobile(mobile);
        //判断密码是否与数据库一致
        if(user!=null){
            //构造返回数据
            ProfileResult profileResult=null;
            if("user".equals(user.getLevel())){
                profileResult=new ProfileResult(user);
            }else{
                Map<String,Object> map=new HashMap<>();
                if("coAdmin".equals(user.getLevel())){
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                profileResult=new ProfileResult(user,list);
            }
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(profileResult,user.getPassword(),getName());
            return info;
        }
        //返回null 会抛出异常，表示用户名或密码错误
        return null;
    }
}
