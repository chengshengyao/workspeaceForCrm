package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.web.filter
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 16:27
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        //放行路径 资源
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        //其他路径进行验证
        else {
            User user = (User) request.getSession().getAttribute("user");
            //如果user！=null 已登录 放行
            if (user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            //用户没登录重定向至登录页
            //请求转发与重定向
            //转发使用特殊的绝对路径 前面不加 /项目名 【内部路径】 转发后路径保留原路径
            //重定向  传统绝对路径的写法 前面加 /项目名           定向后更新路径
            else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }

    }
}
