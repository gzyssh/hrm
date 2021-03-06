package com.hrm.common.interceptor;

import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * preHandle：进入到控制器方法之前执行的内容
 *          boolean:true  可以继续执行控制器方法
 *                  false 拦截
 * postHandle：执行控制器方法之后执行的内容
 * afterCompletion：响应结束之前执行的内容
 *
 * 作用：
 * 1.简化获取token数据的代码编写
 * 完成统一的用户权限校验（是否登录）
 * 2.判断用户是否有访问当前接口的权限
 *
 * @author guozy
 * @create 2019/09/24
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //简化获取token数据的代码编写（判断是否登录）
        //1.通过request获取token信息
        String authorization = request.getHeader("Authorization");
        //判断请求头信息是否为空、是否以Bearer开头
        if(!StringUtils.isEmpty(authorization)&&authorization.startsWith("Bearer")){
            //替换Bearer+空格
            String token = authorization.replace("Bearer ", "");
            //解析token
            Claims claims = jwtUtils.parseJwt(token);
            if(claims!=null){
                //通过claims获取当前用户可访问的Api权限字符串
                String apis= (String) claims.get("apis");
                //通过handler
                HandlerMethod h=(HandlerMethod)handler;
                //获取接口上的RequestMapping注解
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前请求接口的name属性
                String name = annotation.name();
                //判断当前用户是否具有响应的权限
                if(apis.contains(name)){
                    request.setAttribute("user_claims",claims);
                    return true;
                }else{
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }
}
