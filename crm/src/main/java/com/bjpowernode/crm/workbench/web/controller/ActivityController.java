package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

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
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("workbench/activity/getUserList.do".equals(servletPath)) {
            getUserList(req, resp);
        } else if ("/workbench/activity/save.do".equals(servletPath)) {
            save(req, resp);
        } else if ("/workbench/activity/pageList.do".equals(servletPath)) {
            pageList(req, resp);
        } else if ("/workbench/activity/delete.do".equals(servletPath)) {
            delete(req, resp);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(servletPath)) {
            getUserListAndActivity(req, resp);
        } else if ("/workbench/activity/update.do".equals(servletPath)) {
            update(req, resp);
        } else if ("/workbench/activity/detail.do".equals(servletPath)) {
            detail(req, resp);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(servletPath)) {
            getRemarkListByAid(req, resp);
        }
        else if ("/workbench/activity/deleteRemark.do".equals(servletPath)) {
            deleteRemark(req, resp);
        }
        else if ("/workbench/activity/saveRemark.do".equals(servletPath)) {
            saveRemark(req, resp);
        }
        else if ("/workbench/activity/updateRemark.do".equals(servletPath)) {
            updateRemark(req, resp);
        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行更新备注的操作");
        String noteContent = req.getParameter("noteContent");
        String id = req.getParameter("id");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);

    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行添加备注的操作");
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag ="0";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.saveRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据市场活动id取得备注信息列表");
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.deleteRemark(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据市场活动id取得备注信息列表");
        String activityId = req.getParameter("activityId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarkList = activityService.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(resp,remarkList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到跳转详细信息页的操作");
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = activityService.detail(id);
        req.setAttribute("a", a);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动修改的操作");
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startTime");
        String endDate = req.getParameter("endTime");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建时间就是当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人就是当前登录的用户
        String editBy = ((User) req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.update(activity);
        PrintJson.printJsonFlag(resp, flag);

    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动查询用户信息列表与根据id查询市场活动单条记录的操作");
        String id = req.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Object> map = activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp, map);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动删除操作");
        String ids[] = req.getParameterValues("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.delete(ids);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动分页查询 及 条件查询");
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String starTime = req.getParameter("starTime");
        String entTime = req.getParameter("entTime");
        String pageNo = req.getParameter("pageNo");
        //每页展现的记录条数
        String pageSize = req.getParameter("pageSize");
        // select * from tbl_student limit(所略过的记录条数, 每页展现的记录条数)
        //计算 所略过的记录条数
        int skipCount = (Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize);
        //将所获取的前端数据保存至 map
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("starTime", starTime);
        map.put("entTime", entTime);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //前端需要数据库中市场活动表记录总条数，市场信息列表 使用率高返回VO对象  paginationVO
        PaginationVO<Activity> paginationVO = activityService.pageList(map);
        PrintJson.printJsonObj(resp, paginationVO);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动添加操作");
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startTime");
        String endDate = req.getParameter("endTime");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建时间就是当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人就是当前登录的用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.save(activity);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(resp, userList);
    }


}


