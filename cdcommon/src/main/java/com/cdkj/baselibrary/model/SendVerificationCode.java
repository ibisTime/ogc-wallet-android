package com.cdkj.baselibrary.model;

/**
 * 用于发送验证码
 * Created by cdkj on 2018/9/20.
 */

public class SendVerificationCode {

    private String loginName;
    private String sessionID;
    private String bizType;
    private String kind;
    private String countryCode;
    private int requestCode;//跳转的请求码,用于  一个界面需要多个发送多个验证码的回调判断,可以不传




    public SendVerificationCode(String loginName, String bizType, String kind, String countryCode) {
        this.loginName = loginName;
        this.bizType = bizType;
        this.kind = kind;
        this.countryCode = countryCode;
    }

    public SendVerificationCode(String loginName, String bizType, String kind, String countryCode, int requestCode) {
        this.loginName = loginName;
        this.bizType = bizType;
        this.kind = kind;
        this.countryCode = countryCode;
        this.requestCode = requestCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
