package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.web.filter
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 2:31
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //过滤请求post请求中文乱码的问题
        servletRequest.setCharacterEncoding("UTF-8");
        //过滤响应流中文乱码的问题
        servletResponse.setContentType("text/html;charset=utf-8");
        //放行请求
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
