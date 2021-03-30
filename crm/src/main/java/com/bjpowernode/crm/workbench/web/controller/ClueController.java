package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

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
 * @Package: com.bjpowernode.crm.settings.web.controller
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/2/28 16:21
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(servletPath)) {
            getUserList(req, resp);
        } else if ("/workbench/clue/save.do".equals(servletPath)) {
            save(req, resp);
        }
        else if ("/workbench/clue/detail.do".equals(servletPath)) {
            detail(req, resp);
        }
        else if ("/workbench/clue/showActivityListByClueId.do".equals(servletPath)) {
            showActivityListByClueId(req, resp);
        }
        else if ("/workbench/clue/unbund.do".equals(servletPath)) {
            unbund(req, resp);
        }
        else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(servletPath)) {
            getActivityListByNameAndNotByClueId(req, resp);
        }
        else if ("/workbench/clue/bund.do".equals(servletPath)) {
            bund(req, resp);
        }
        else if ("/workbench/clue/getActivityListByName.do".equals(servletPath)) {
            getActivityListByName(req, resp);
        }
        else if ("/workbench/clue/convert.do".equals(servletPath)) {
            convert(req, resp);
        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("线索转换 ");
        String culeId =req.getParameter("culeId");
        String flag =req.getParameter("flag");
        String createBy =((User)req.getSession().getAttribute("user")).getName();
        Tran t= null;
        //判断是否需要创建交易
        if ("a".equals(flag)){
            t=new Tran();
            //接收交易表单中的参数
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime =DateTimeUtil.getSysTime();


           t.setMoney(money);
           t.setName(name);
           t.setExpectedDate(expectedDate);
           t.setStage(stage);
           t.setActivityId(activityId);
           t.setId(id);
           t.setCreateTime(createTime);
           t.setCreateBy(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean successIf=cs.convert(culeId,t,createBy);
       if (successIf){
           resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
       }
    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("查询市场活动列表， 根据名称模糊查询");
        String aname =req.getParameter("aname");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list = activityService.getActivityListByName(aname);
        PrintJson.printJsonObj(resp,list);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行关联市场活动的操作");
        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.bund(cid,aids);
        PrintJson.printJsonFlag(resp,flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("查询市场活动列表， 根据名称模糊查询，排除关联指定线索的列表");
        String aname =req.getParameter("aname");
        String clueId =req.getParameter("clueId");
        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list = activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(resp,list);
    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行解除关联操作");
        String id =req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
         boolean flag = clueService.unbund(id);
         PrintJson.printJsonFlag(resp,flag);
    }

    private void showActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据线索id查询所关联的市场活动列表操作");
        String clueId =req.getParameter("clueId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list  = activityService.showActivityListByClueId(clueId);
        PrintJson.printJsonObj(resp,list);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转至线索详细信息页操作");
        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = clueService.detail(id);
        req.setAttribute("c",c);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);

    }


    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行线索添加操作");
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String fullname = req.getParameter("fullname");
        String appellation=req.getParameter("appellation");
        String company=req.getParameter("company");
        String job=req.getParameter("job");
        String email=req.getParameter("email");
        String phone=req.getParameter("phone");
        String mphone=req.getParameter("mphone");
        String website=req.getParameter("website");
        String state=req.getParameter("state");
        String source=req.getParameter("source");
        String contactSummary=req.getParameter("contactSummary");
        String nextContactTime=req.getParameter("nextContactTime");
        String description = req.getParameter("description");
        String address = req.getParameter("address");
        //创建时间就是当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人就是当前登录的用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        Clue clue = new Clue();
        clue.setId(id);
        clue.setOwner(owner);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setMphone(mphone);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setDescription(description);
        clue.setAddress(address);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(resp, userList);
    }


}


