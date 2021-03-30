package com.bjpowernode.crm.settings.domain;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.settings.domain
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/2/28 2:56
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
//  关于字符串中表现的日期及时间
    // yyyy-MM-dd 定长10位 char 效率高
    // yyyy-MM-dd HH:mm:ss 定长19位
public class User {
    private String id;          //主键
    private String loginAct;    //登录账号 -
    private String name;        //用户真实姓名
    private String loginPwd;    //登录密码 -
    private String email;       //邮箱
    private String expireTime;  //到期失效时间 -
    private String lockState;   //锁定状态 0锁定 1启用 -
    private String deptno;      //部门标号
    private String allowIps;    //允许访问的ip地址 系统安全性考虑 -
    private String createTime;  //创建时间
    private String createBy;    //创建人
    private String editTime;    //修改时间
    private String editBy;      //修改人
    // 构造 [表中字段较多时，有参构造难以维护]

    public User() {
    }


    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}
