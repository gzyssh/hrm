package com.hrm.common.controller;

import io.jsonwebtoken.Claims;
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

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,HttpServletResponse response){
        Object obj = request.getAttribute("user_claims");
        if(obj!=null){
            this.claims= (Claims) obj;
        }
        this.request=request;
        this.response=response;
        this.companyId=(String) claims.get("companyId");
        this.companyName=(String) claims.get("companyName");
    }
}
