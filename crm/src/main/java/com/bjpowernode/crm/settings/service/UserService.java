package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.service
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/2/28 16:12
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
