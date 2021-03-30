package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.dao
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 20:28
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition();

    List<Activity> getDataListByCondition();

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
