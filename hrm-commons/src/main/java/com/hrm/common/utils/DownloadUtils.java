package com.hrm.common.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

public class DownloadUtils {
    public void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletRequest request,HttpServletResponse response, String returnName) throws IOException {
        response.setContentType("application/octet-stream");
        String userAgent = request.getHeader("user-agent").toLowerCase();
        //保存的文件名,必须和页面编码一致,否则乱码
        if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
            returnName = URLEncoder.encode(returnName, "UTF-8");
        } else {
            returnName = new String(returnName.getBytes("utf-8"), "ISO-8859-1");
        }
        response.addHeader("content-disposition","attachment;filename="+returnName);
        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream out = response.getOutputStream();	  //取得输出流
        byteArrayOutputStream.writeTo(out);					      //写到输出流
        byteArrayOutputStream.close();						      //关闭
        out.flush();										      //刷数据
    }
}
