package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.service
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/10 17:39
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public interface CustomerService {

    List<Customer> getCustomerName(String name);
}
