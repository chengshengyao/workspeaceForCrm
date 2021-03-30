package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.web.controller
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/10 11:12
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/workbench/transaction/add.do".equals(servletPath)) {
            add(req, resp);
        } else if ("/workbench/transaction/getCustomerName.do".equals(servletPath)) {
            getCustomerName(req, resp);
        } else if ("/workbench/transaction/save.do".equals(servletPath)) {
            save(req, resp);
        }
        else if ("/workbench/transaction/detail.do".equals(servletPath)) {
            detail(req, resp);
        }
        else if ("/workbench/transaction/getHistoryListByTranId.do".equals(servletPath)) {
            getHistoryListByTranId(req, resp);
        }
        else if ("/workbench/transaction/changeStage.do".equals(servletPath)) {
            changeStage(req, resp);
        }
        else if ("/workbench/transaction/getEchars.do".equals(servletPath)) {
            getEchars(req, resp);
        }
    }

    private void getEchars(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("统计交易阶段数量-交易漏斗图");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
       Map<String,Object> map =tranService.getEchars();
       PrintJson.printJsonObj(resp,map);
    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据交易id获取交易历史列表");
        String stage = req.getParameter("stage");
        String id = req.getParameter("id");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editTime = req.getParameter("editTime");
        String editBy = req.getParameter("editBy");
        Map<String,String> pMap = (Map) req.getServletContext().getAttribute("pMap");
        Tran t = new Tran();
        t.setStage(stage);
        t.setPossibility(pMap.get(stage));
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setExpectedDate(expectedDate);
        t.setMoney(money);
        t.setId(id);
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = tranService.changeStage(t);
        HashMap<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);


        PrintJson.printJsonObj(resp,map);
    }

    private void getHistoryListByTranId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据交易id获取交易历史列表");
        String tranId = req.getParameter("tranId");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> list =tranService.getHistoryListByTranId(tranId);
        Map<String,String> pMap = (Map) req.getServletContext().getAttribute("pMap");
        for (TranHistory th:
             list) {
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }


        PrintJson.printJsonObj(resp,list);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        System.out.println("执行跳转详细信息页操作");
        String id = req.getParameter("id");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = tranService.detail(id);
        //处理可能性
        String stage = tran.getStage();
        Map pMap = (Map) req.getServletContext().getAttribute("pMap");
        String possibility= (String) pMap.get(stage);
        tran.setPossibility(possibility);
        //req.setAttribute("possibility",possibility);
        req.setAttribute("t",tran);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        System.out.println("执行交易添加操作");
        String id = UUIDUtil.getUUID();
        String customerName = req.getParameter("customerName");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String stage = req.getParameter("stage");
        String source = req.getParameter("source");
        String contactSummary = req.getParameter("contactSummary");
        String description = req.getParameter("description");
        String expectedDate = req.getParameter("expectedDate");
        //创建时间就是当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人就是当前登录的用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        Tran tran = new Tran();
        tran.setId(id);
       // tran.setCustomerId(customerName);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setStage(stage);
        tran.setExpectedDate(expectedDate);
        tran.setOwner(owner);
        tran.setName(name);
        tran.setSource(source);
        tran.setContactSummary(contactSummary);
        tran.setDescription(description);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = tranService.save(tran,customerName);
       // PrintJson.printJsonFlag(resp, flag);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("跳转支持模糊查询的 客户名称列表");
        String name = req.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> list = customerService.getCustomerName(name);
        PrintJson.printJsonObj(resp, list);
    }


    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转交易添加页操作");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        req.setAttribute("uList", uList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req, resp);
    }

}
