package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.service.impl
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 19:43
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean convert(String culeId, Tran t, String createBy) {
        //线索转换
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        Clue c = clueDao.getById(culeId);
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        if (cus == null) {
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setName(company);
            cus.setContactSummary(c.getContactSummary());
            cus.setOwner(c.getOwner());
            cus.setDescription(c.getDescription());
            cus.setPhone(c.getPhone());
            cus.setWebsite(c.getWebsite());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            int countCus = customerDao.save(cus);
            if (countCus != 1) {
                flag = false;
            }
        }
        Contacts con = new Contacts();
        con.setCustomerId(cus.getId());
        con.setJob(c.getJob());
        con.setId(UUIDUtil.getUUID());
        con.setAddress(c.getAddress());
        con.setSource(c.getSource());
        con.setContactSummary(c.getContactSummary());
        con.setOwner(c.getOwner());
        con.setDescription(c.getDescription());
        con.setAppellation(c.getAppellation());
        con.setBirth(DateTimeUtil.getSysTime());
        con.setEmail(c.getEmail());
        con.setFullname(c.getFullname());
        con.setMphone(c.getMphone());
        con.setNextContactTime(c.getNextContactTime());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        int countCon = contactsDao.save(con);
        if (countCon != 1) {
            flag = false;
        }
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(culeId);
        for (ClueRemark clueRemark :
                clueRemarkList) {
            String noteContent = clueRemark.getNoteContent();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            int countCRD = customerRemarkDao.save(customerRemark);
            if (countCRD != 1) {
                flag = false;
            }
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setContactsId(cus.getId());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(cus.getId());
            int countCORD = contactsRemarkDao.save(contactsRemark);
            if (countCORD != 1) {
                flag = false;
            }
        }
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getListByClueId(culeId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations
        ) {
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            int countCLRD = contactsActivityRelationDao.save(clueActivityRelation);
            if (countCLRD != 1) {
                flag = false;
            }
        }
        if (t != null) {
            t.setOwner(c.getOwner());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setSource(c.getSource());
            t.setContactsId(con.getId());
            int countT = tranDao.save(t);
            if (countT != 1) {
                flag = false;
            }
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setMoney(t.getMoney());
            th.setExpectedDate(t.getExpectedDate());
            th.setStage(t.getStage());
            th.setTranId(t.getId());
            int countTH = tranHistoryDao.save(th);
            if (countTH != 1) {
                flag = false;
            }
        }
        for (ClueRemark clueRemark : clueRemarkList) {
            int countCRDT = clueRemarkDao.delete(clueRemark);
            if (countCRDT != 1) {
                flag = false;
            }
        }
        for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
            int countCARDDT = clueActivityRelationDao.delete(clueActivityRelation);
            if (countCARDDT != 1) {
                flag = false;
            }
        }
        int countCLD =clueDao.delete(culeId);
        if (countCLD != 1) {
            flag = false;
        }
            return flag;
        }





    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for (String aid :
                aids) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1) {
            flag = false;

        }
        return flag;
    }

    @Override
    public Boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1) {
            flag = false;

        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }
}
