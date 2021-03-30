package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 20:37
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class ActivityServiceImpl implements ActivityService {
    //反射动态代理 生成dao对象
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> list  = activityDao.getActivityListByName(aname);
        return list;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> list  = activityDao.getActivityListByNameAndNotByClueId(map);
        return list;
    }

    @Override
    public List<Activity> showActivityListByClueId(String clueId) {
        List<Activity> list = activityDao.showActivityListByClueId(clueId);
        return list;
    }

    @Override
    public Boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1) {
            flag = false;
        }
        return false;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        //取得记录总条数  total 与
        int total = activityDao.getTotalByCondition();
        //  dataList 数据集合
        List<Activity> dataList = activityDao.getDataListByCondition();
        PaginationVO<Activity> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(dataList);
        return paginationVO;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //先查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids();
        //删除备注返回影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2) {
            flag = false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList = userDao.getUserList();
        //取a
        Activity a = activityDao.getById(id);
        //将uList和a打包到map中
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);
        //返回map就可以了
        return map;
    }

    @Override
    public Boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count != 1) {
            flag = false;
        }
        return false;
    }

    @Override
    public Activity detail(String id) {
        Activity activityDetail = activityDao.detail(id);
        return activityDetail;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> remarkList = activityRemarkDao.getRemarkListByAid(activityId);
        return remarkList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag =true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag =true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag =true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag=false;
        }
        return flag;
    }
}
