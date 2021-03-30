package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.service.impl
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 20:10
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao =SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<>();
        //将数据字典中的字典类型取出
        List<DicType> dtList = dicTypeDao.getTypeList();
        //将字典类型进行遍历
        for (DicType dt :
                dtList) {
            //取得7种字典类型
            String code = dt.getCode();
            //可根据字典类型取得相对应的字典值List
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code,dvList);
        }

        return map;
    }
}
