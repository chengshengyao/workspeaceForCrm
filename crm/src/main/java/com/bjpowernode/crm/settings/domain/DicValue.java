package com.bjpowernode.crm.settings.domain;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.domain
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 19:53
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class DicValue {
    private String id;
    private String value;
    private String text;
    private String orderNo;
    private String typeCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
