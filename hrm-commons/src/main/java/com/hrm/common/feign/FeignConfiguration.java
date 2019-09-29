package com.hrm.common.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * <p>
 *  Feign拦截器
 * </p>
 *
 * @author guozy
 * @create 2019/09/29
 */
@Configuration
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            /**
             * 获取所有浏览器发送的请求属性，请求头赋值到Feign
             * @param requestTemplate
             */
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //请求属性
                ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                if(attributes!=null){
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headers = request.getHeaderNames();
                    while(headers.hasMoreElements()){
                        String name = headers.nextElement();//请求头名称
                        String value = request.getHeader(name);//请求头数据
                        requestTemplate.header(name,value);
                    }
                }
            }
        };
    }
}
