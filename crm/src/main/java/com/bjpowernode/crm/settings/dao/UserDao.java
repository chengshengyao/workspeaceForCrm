package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.dao
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/2/28 16:05
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface UserDao {
    User login(Map<String, String> map);

    List<User> getUserList();
}
