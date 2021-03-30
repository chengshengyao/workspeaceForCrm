package com.bjpowernode.crm.settings.domain;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.domain
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/7 19:52
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class DicType {
    private String code;
    private String name;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
