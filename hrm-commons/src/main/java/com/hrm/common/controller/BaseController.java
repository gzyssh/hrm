package com.hrm.common.controller;

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

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,HttpServletResponse response){
        this.request=request;
        this.response=response;
        this.companyId="1";
        this.companyName="河南欣宜嘉医疗科技有限公司";
    }
}
