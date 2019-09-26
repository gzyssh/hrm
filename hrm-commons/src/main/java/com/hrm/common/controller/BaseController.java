package com.hrm.common.controller;

import com.hrm.entity.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;

    /**
     * 使用JWT方式获取
     */
    /*@ModelAttribute
    public void setReqAndRes(HttpServletRequest request,HttpServletResponse response){
        Object obj = request.getAttribute("user_claims");
        if(obj!=null){
            this.claims= (Claims) obj;
            this.companyId=(String) claims.get("companyId");
            this.companyName=(String) claims.get("companyName");
        }
        this.request=request;
        this.response=response;

    }*/
    /**
     * 使用shiro方式获取
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,HttpServletResponse response){
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        if(principals!=null&&!principals.isEmpty()){
            //获取安全数据
            ProfileResult profileResult=(ProfileResult) principals.getPrimaryPrincipal();
            this.companyId=profileResult.getCompanyId();
            this.companyName=profileResult.getMobile();
        }
        this.request=request;
        this.response=response;
    }
}
