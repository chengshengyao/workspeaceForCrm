package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.dao
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 23:04
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface ActivityRemarkDao {
    int getCountByAids();

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
