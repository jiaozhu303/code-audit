package com.dj.tool.model;

public class CodeAuditSettingModel {

    public static final String URL_KEY = "code_audit_key_url";
    public static final String USER_KEY = "code_audit_key_user_name";
    public static final String PASSWORD_KEY = "code_audit_key_password";

    private String url;
    private String userName;
    private String password;

    public CodeAuditSettingModel(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
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


}
