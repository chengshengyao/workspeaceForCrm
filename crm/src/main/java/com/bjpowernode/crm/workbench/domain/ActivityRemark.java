package com.bjpowernode.crm.workbench.domain;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.workbench.domain
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/1 18:44
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class ActivityRemark {
    private String id;      //
    private String noteContent; //备注信息
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String editFlag; //是否修改过的标记
    private String activityId; //外键

    public ActivityRemark() {
    }

    //g & s
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
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

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
