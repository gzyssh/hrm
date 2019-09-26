package com.hrm.employee;
import com.hrm.common.shiro.realm.HrmRealm;
import com.hrm.common.shiro.session.CustomSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    /**
     * 1.创建realm
     * @return
     */
    @Bean
    public HrmRealm getRealm() {
        return new HrmRealm();
    }

    /**
     * 2.创建安全管理器
     * @param realm
     * @return
     */
    @Bean
    public SecurityManager getSecurityManager(HrmRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    /**
     * 3.配置shiro的过滤器工厂
     * 在web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactory.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，未授权跳转的页面）
        filterFactory.setLoginUrl("/auth-error?code=1");//跳转url地址
        filterFactory.setUnauthorizedUrl("/auth-error?code=2");//未授权的url
        //4.设置过滤器集合
        Map<String,String> filterMap = new LinkedHashMap<>();
        //anon:可以匿名访问
        filterMap.put("/sys/login","anon");
        filterMap.put("/auth-error","anon");
        //authc:认证后方可访问(登录)
        filterMap.put("/**","authc");
        //perms:某种权限方可访问
        //roles:某种角色可以访问
        //具有某中权限才能访问
        filterFactory.setFilterChainDefinitionMap(filterMap);
        return filterFactory;
    }


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    /**
     * 1.redis的控制器，操作redis
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        return redisManager;
    }

    /**
     * 2.sessionDao
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    /**
     * 3.会话管理器
     */
    public DefaultWebSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookie
        sessionManager.setSessionIdCookieEnabled(false);
        //禁用URL重写
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 4.缓存管理器
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    //开启对shior注解的支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
