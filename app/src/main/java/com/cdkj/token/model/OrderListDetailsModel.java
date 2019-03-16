package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * @updateDts 2019/1/22
 */
public class OrderListDetailsModel {


    /**
     * code : AO20190121190028148586704
     * orderUid : 100
     * type : 0
     * userId : U20190114113239790410882
     * acceptUserId : SYS_USER_INCOME
     * tradeCurrency : BTC
     * tradeCoin : BTC
     * tradePrice : 24179
     * count : 1
     * tradeAmount : 24179
     * fee : 0
     * invalidDatetime : Jan 21, 2019 7:10:28 PM
     * receiveType : 0
     * receiveInfo : 余杭支行
     * receiveBank : 中国民生银行
     * receiveCardNo : 12345678901112
     * receiveSubbranch : 余杭支行
     * receiveName : Fun1 BTC
     * postscript : E84Z
     * status : 0
     * createDatetime : Jan 21, 2019 7:00:28 PM
     * pic : Fnk8QUjkUxmvnvH0cpIjDsZhG5Au
     * user : {"userId":"U20190114113239790410882","loginName":"13282838237","mobile":"13282838237","kind":"C","loginPwdStrength":"1","level":"1","tradePwdStrength":"1","status":"0","tradeRate":0.001,"createDatetime":"Jan 14, 2019 11:32:39 AM","lastLogin":"Jan 21, 2019 12:02:35 AM","loginPwdFlag":false,"tradepwdFlag":false,"googleAuthFlag":false,"emailBindFlag":false}
     */

    private String code;
    private int orderUid;
    private String type;
    private String userId;
    private String acceptUserId;
    private String tradeCurrency;
    private String tradeCoin;
    private BigDecimal tradePrice;
    private BigDecimal count;
    private BigDecimal tradeAmount;
    private int fee;
    private String invalidDatetime;
    private String receiveType;
    private String receiveInfo;
    private String receiveBank;
    private String receiveCardNo;
    private String receiveSubbranch;
    private String receiveName;
    private String postscript;
    private String status;
    private String createDatetime;
    private String pic;
    private UserBean user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrderUid() {
        return orderUid;
    }

    public void setOrderUid(int orderUid) {
        this.orderUid = orderUid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getTradeCoin() {
        return tradeCoin;
    }

    public void setTradeCoin(String tradeCoin) {
        this.tradeCoin = tradeCoin;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getInvalidDatetime() {
        return invalidDatetime;
    }

    public void setInvalidDatetime(String invalidDatetime) {
        this.invalidDatetime = invalidDatetime;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getReceiveInfo() {
        return receiveInfo;
    }

    public void setReceiveInfo(String receiveInfo) {
        this.receiveInfo = receiveInfo;
    }

    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }

    public String getReceiveCardNo() {
        return receiveCardNo;
    }

    public void setReceiveCardNo(String receiveCardNo) {
        this.receiveCardNo = receiveCardNo;
    }

    public String getReceiveSubbranch() {
        return receiveSubbranch;
    }

    public void setReceiveSubbranch(String receiveSubbranch) {
        this.receiveSubbranch = receiveSubbranch;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * userId : U20190114113239790410882
         * loginName : 13282838237
         * mobile : 13282838237
         * kind : C
         * loginPwdStrength : 1
         * level : 1
         * tradePwdStrength : 1
         * status : 0
         * tradeRate : 0.001
         * createDatetime : Jan 14, 2019 11:32:39 AM
         * lastLogin : Jan 21, 2019 12:02:35 AM
         * loginPwdFlag : false
         * tradepwdFlag : false
         * googleAuthFlag : false
         * emailBindFlag : false
         */

        private String userId;
        private String loginName;
        private String mobile;
        private String kind;
        private String loginPwdStrength;
        private String level;
        private String tradePwdStrength;
        private String status;
        private double tradeRate;
        private String createDatetime;
        private String lastLogin;
        private boolean loginPwdFlag;
        private boolean tradepwdFlag;
        private boolean googleAuthFlag;
        private boolean emailBindFlag;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLoginPwdStrength() {
            return loginPwdStrength;
        }

        public void setLoginPwdStrength(String loginPwdStrength) {
            this.loginPwdStrength = loginPwdStrength;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getTradePwdStrength() {
            return tradePwdStrength;
        }

        public void setTradePwdStrength(String tradePwdStrength) {
            this.tradePwdStrength = tradePwdStrength;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTradeRate() {
            return tradeRate;
        }

        public void setTradeRate(double tradeRate) {
            this.tradeRate = tradeRate;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        public boolean isLoginPwdFlag() {
            return loginPwdFlag;
        }

        public void setLoginPwdFlag(boolean loginPwdFlag) {
            this.loginPwdFlag = loginPwdFlag;
        }

        public boolean isTradepwdFlag() {
            return tradepwdFlag;
        }

        public void setTradepwdFlag(boolean tradepwdFlag) {
            this.tradepwdFlag = tradepwdFlag;
        }

        public boolean isGoogleAuthFlag() {
            return googleAuthFlag;
        }

        public void setGoogleAuthFlag(boolean googleAuthFlag) {
            this.googleAuthFlag = googleAuthFlag;
        }

        public boolean isEmailBindFlag() {
            return emailBindFlag;
        }

        public void setEmailBindFlag(boolean emailBindFlag) {
            this.emailBindFlag = emailBindFlag;
        }
    }
}
