package com.bjpowernode.crm.vo;

import java.util.List;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.vo
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/2 12:19
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class PaginationVO<T> {
    private int total;
    private List<T> dataList;

    public PaginationVO() {
    }

    //g & s
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
