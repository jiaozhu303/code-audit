package com.dj.tool.model;

import org.apache.commons.lang3.StringUtils;

public class CodeAuditSettingModel {

    private String url;
    private String userName;
    private String password;

    private String spaceKey;
    private String parentId;


    public CodeAuditSettingModel(String url, String userName, String password, String spaceKey, String parentId) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.spaceKey = spaceKey;
        this.parentId = parentId;
    }


    public String getSpaceKey() {
        return spaceKey;
    }

    public void setSpaceKey(String spaceKey) {
        this.spaceKey = spaceKey;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return StringUtils.isNoneBlank(this.url) &&
            StringUtils.isNoneBlank(this.userName) &&
            StringUtils.isNoneBlank(this.password) &&
            StringUtils.isNoneBlank(this.spaceKey) &&
            StringUtils.isNoneBlank(this.password);
    }


}
