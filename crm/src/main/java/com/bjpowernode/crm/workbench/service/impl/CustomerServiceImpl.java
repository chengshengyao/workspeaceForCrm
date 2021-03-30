package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/10 17:40
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = (CustomerDao) ServiceFactory.getService(CustomerService.class);
    @Override
    public List<Customer> getCustomerName(String name) {
        List<Customer> list= customerDao.getCustomerName(name);
        return list;
    }
}
