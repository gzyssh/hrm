package com.hrm.common.shiro.realm;

import com.hrm.entity.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * <p>
 *  公共Realm：获取安全数据，构造权限信息
 * </p>
 *
 * @author guozy
 * @create 2019/09/25
 */
public class HrmRealm extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("hrmRealm");
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取安全数据
        ProfileResult profileResult= (ProfileResult) principalCollection.getPrimaryPrincipal();
        //获取权限信息
        Set<String> apis = (Set<String>) profileResult.getRoles().get("apis");
        //构造权限数据
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.setStringPermissions(apis);
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
