package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.dao
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 20:06
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface DicTypeDao {
    List<DicType> getTypeList();
}
