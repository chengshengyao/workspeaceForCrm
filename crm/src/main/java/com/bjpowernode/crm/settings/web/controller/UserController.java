package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.web.controller
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/2/28 16:21
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/settings/user/login.do".equals(servletPath)) {
            login(req, resp);
        } else if ("/settings/user/xxx.do".equals(servletPath)) {

        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        //验证登录操作
        //接收前端登录数据
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");
        //将密码 明文 转为 MD5密文
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收IP地址 [获取偏僻的地址  远离后端的前端登录地址]
        String ip = req.getRemoteAddr();
        //业务层开发统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = us.login(loginAct, loginPwd, ip);
            req.getSession().setAttribute("user", user);
            //若程序执行至此处 说明 控制层无异常抛出 登录成功
            //String str = "{\"success\":true}";
            //resp.getWriter().println(str);    //手动
            //使用工具类
            PrintJson.printJsonFlag(resp, true);
        } catch (Exception e) {
            //若程序执行至此处 说明 控制层异常抛出 登录失败
            e.printStackTrace();
            String msg = e.getMessage();
            /**
             * 控制层 为 异步对象提供多项信息时
             * 1.将多项信息存入map，将map解析成json串
             * 2.创建一个vo
             * 对于展示的信息 将来还会使用 使用vo
             * 对于展示的信息 只是本需求中使用 使用map
             */
            Map map = new HashMap<String, Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(resp,map);
        }
    }
}

