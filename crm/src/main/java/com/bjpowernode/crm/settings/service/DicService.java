package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.service
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 20:10
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();

}
