package com.hrm.entity.system.response;

import com.hrm.entity.system.Permission;
import com.hrm.entity.system.Role;
import com.hrm.entity.system.User;
import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class ProfileResult implements Serializable , AuthCachePrincipal {
    private String mobile;
    private String username;
    private String companyId;
    private Map<String,Object> roles = new HashMap<>();

    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.companyId = user.getCompanyName();

        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : roles) {
            Set<Permission> perms = role.getPermissions();
            for (Permission perm : perms) {
                String code = perm.getCode();
                if(perm.getType() == 1) {
                    menus.add(code);
                }else if(perm.getType() == 2) {
                    points.add(code);
                }else {
                    apis.add(code);
                }
            }
        }

        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }
    public ProfileResult(User user, List<Permission> list) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.companyId = user.getCompanyName();

        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Permission perm : list) {
            String code = perm.getCode();
            if(perm.getType() == 1) {
                menus.add(code);
            }else if(perm.getType() == 2) {
                points.add(code);
            }else {
                apis.add(code);
            }
        }

        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
