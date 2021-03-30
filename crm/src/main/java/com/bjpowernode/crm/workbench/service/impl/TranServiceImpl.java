package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/10 11:10
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public Map<String, Object> getEchars() {
        int total = tranDao.getTotal();
        List<Map<String, Object>> dataList = tranDao.getDataList();
        Map<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count1 = tranDao.changeStage(t);
        if (count1 != 1) {
            flag = false;
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setPossibility(t.getPossibility());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setTranId(t.getId());
        th.setExpectedDate(t.getExpectedDate());
        int count2 = tranHistoryDao.save(th);
        if (count2 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> list = tranHistoryDao.getHistoryListByTranId(tranId);
        return list;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public Boolean save(Tran tran, String customerName) {
        boolean flag = true;
        Customer cus = customerDao.getCustomerByName(customerName);
        if (cus == null) {
            cus = new Customer();
            cus.setName(customerName);
            cus.setId(UUIDUtil.getUUID());
            cus.setContactSummary(tran.getContactSummary());
            cus.setOwner(tran.getOwner());
            cus.setDescription(tran.getDescription());
            cus.setNextContactTime(tran.getNextContactTime());
            cus.setCreateBy(tran.getCreateBy());
            cus.setCreateTime(tran.getCreateTime());
            int countCus = customerDao.save(cus);
            if (countCus != 1) {
                flag = false;
            }
        }
        tran.setContactsId(cus.getId());
        //添加交易
        int count = tranDao.save(tran);
        if (count != 1) {
            flag = false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setId(UUIDUtil.getUUID());
        th.setCreateTime(tran.getCreateTime());
        th.setCreateBy(tran.getCreateBy());
        int countTh = tranHistoryDao.save(th);
        if (countTh != 1) {
            flag = false;
        }
        return flag;
    }
}
