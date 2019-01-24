package com.cdkj.token.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/29.
 */

public class WithdrawOrderModel implements Serializable {
    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 1
     * totalPage : 1
     * list : [{"code":"QX20190123155406464374589","accountNumber":"A201901141132375336142","accountType":"C","currency":"BTC","bizType":"transfer","amount":0,"fee":0,"actualAmount":0,"channelType":"1","channelBank":"BTC","payCardInfo":"BTC","payCardNo":"mhz7F7fL4L7zYmH3osSPL8yLFoaai8cagD","status":"1","applyUser":"U20190114113239790410882","applyNote":"BTC提现","applyDatetime":"Jan 23, 2019 3:54:06 PM","accountInfo":{"accountNumber":"A201901141132375336142","userId":"U20190114113239790410882","currency":"BTC","address":"mhz7F7fL4L7zYmH3osSPL8yLFoaai8cagD","type":"C","status":"0","amount":200000001,"frozenAmount":0,"md5":"e429e5c5d49a9b0dc371392c44a9b75b","inAmount":200000000,"outAmount":0,"createDatetime":"Jan 14, 2019 11:32:41 AM","lastOrder":"AJ20190123165025141439470"},"applyUserInfo":{"userId":"U20190114113239790410882","loginName":"13282838237","mobile":"13282838237","kind":"C","loginPwdStrength":"1","level":"1","tradePwdStrength":"1","status":"0","tradeRate":0.001,"createDatetime":"Jan 14, 2019 11:32:39 AM","lastLogin":"Jan 21, 2019 12:02:35 AM","loginPwdFlag":false,"tradepwdFlag":false,"googleAuthFlag":false,"emailBindFlag":false}}]
     */

    private int pageNO;
    private int start;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private List<ListBean> list;

    public int getPageNO() {
        return pageNO;
    }

    public void setPageNO(int pageNO) {
        this.pageNO = pageNO;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * code : QX20190123155406464374589
         * accountNumber : A201901141132375336142
         * accountType : C
         * currency : BTC
         * bizType : transfer
         * amount : 0
         * fee : 0
         * actualAmount : 0
         * channelType : 1
         * channelBank : BTC
         * payCardInfo : BTC
         * payCardNo : mhz7F7fL4L7zYmH3osSPL8yLFoaai8cagD
         * status : 1
         * applyUser : U20190114113239790410882
         * applyNote : BTC提现
         * applyDatetime : Jan 23, 2019 3:54:06 PM
         * accountInfo : {"accountNumber":"A201901141132375336142","userId":"U20190114113239790410882","currency":"BTC","address":"mhz7F7fL4L7zYmH3osSPL8yLFoaai8cagD","type":"C","status":"0","amount":200000001,"frozenAmount":0,"md5":"e429e5c5d49a9b0dc371392c44a9b75b","inAmount":200000000,"outAmount":0,"createDatetime":"Jan 14, 2019 11:32:41 AM","lastOrder":"AJ20190123165025141439470"}
         * applyUserInfo : {"userId":"U20190114113239790410882","loginName":"13282838237","mobile":"13282838237","kind":"C","loginPwdStrength":"1","level":"1","tradePwdStrength":"1","status":"0","tradeRate":0.001,"createDatetime":"Jan 14, 2019 11:32:39 AM","lastLogin":"Jan 21, 2019 12:02:35 AM","loginPwdFlag":false,"tradepwdFlag":false,"googleAuthFlag":false,"emailBindFlag":false}
         */

        private String code;
        private String accountNumber;
        private String accountType;
        private String currency;
        private String bizType;
        private BigDecimal amount;
        private BigDecimal fee;
        private BigDecimal actualAmount;
        private String channelType;
        private String channelBank;
        private String payCardInfo;
        private String payCardNo;
        private String status;
        private String applyUser;
        private String applyNote;
        private String applyDatetime;
        private AccountInfoBean accountInfo;
        private ApplyUserInfoBean applyUserInfo;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getFee() {
            return fee;
        }

        public void setFee(BigDecimal fee) {
            this.fee = fee;
        }

        public BigDecimal getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(BigDecimal actualAmount) {
            this.actualAmount = actualAmount;
        }

        public String getChannelType() {
            return channelType;
        }

        public void setChannelType(String channelType) {
            this.channelType = channelType;
        }

        public String getChannelBank() {
            return channelBank;
        }

        public void setChannelBank(String channelBank) {
            this.channelBank = channelBank;
        }

        public String getPayCardInfo() {
            return payCardInfo;
        }

        public void setPayCardInfo(String payCardInfo) {
            this.payCardInfo = payCardInfo;
        }

        public String getPayCardNo() {
            return payCardNo;
        }

        public void setPayCardNo(String payCardNo) {
            this.payCardNo = payCardNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getApplyUser() {
            return applyUser;
        }

        public void setApplyUser(String applyUser) {
            this.applyUser = applyUser;
        }

        public String getApplyNote() {
            return applyNote;
        }

        public void setApplyNote(String applyNote) {
            this.applyNote = applyNote;
        }

        public String getApplyDatetime() {
            return applyDatetime;
        }

        public void setApplyDatetime(String applyDatetime) {
            this.applyDatetime = applyDatetime;
        }

        public AccountInfoBean getAccountInfo() {
            return accountInfo;
        }

        public void setAccountInfo(AccountInfoBean accountInfo) {
            this.accountInfo = accountInfo;
        }

        public ApplyUserInfoBean getApplyUserInfo() {
            return applyUserInfo;
        }

        public void setApplyUserInfo(ApplyUserInfoBean applyUserInfo) {
            this.applyUserInfo = applyUserInfo;
        }

        public static class AccountInfoBean {
            /**
             * accountNumber : A201901141132375336142
             * userId : U20190114113239790410882
             * currency : BTC
             * address : mhz7F7fL4L7zYmH3osSPL8yLFoaai8cagD
             * type : C
             * status : 0
             * amount : 200000001
             * frozenAmount : 0
             * md5 : e429e5c5d49a9b0dc371392c44a9b75b
             * inAmount : 200000000
             * outAmount : 0
             * createDatetime : Jan 14, 2019 11:32:41 AM
             * lastOrder : AJ20190123165025141439470
             */

            private String accountNumber;
            private String userId;
            private String currency;
            private String address;
            private String type;
            private String status;
            private int amount;
            private int frozenAmount;
            private String md5;
            private int inAmount;
            private int outAmount;
            private String createDatetime;
            private String lastOrder;

            public String getAccountNumber() {
                return accountNumber;
            }

            public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getFrozenAmount() {
                return frozenAmount;
            }

            public void setFrozenAmount(int frozenAmount) {
                this.frozenAmount = frozenAmount;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public int getInAmount() {
                return inAmount;
            }

            public void setInAmount(int inAmount) {
                this.inAmount = inAmount;
            }

            public int getOutAmount() {
                return outAmount;
            }

            public void setOutAmount(int outAmount) {
                this.outAmount = outAmount;
            }

            public String getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(String createDatetime) {
                this.createDatetime = createDatetime;
            }

            public String getLastOrder() {
                return lastOrder;
            }

            public void setLastOrder(String lastOrder) {
                this.lastOrder = lastOrder;
            }
        }

        public static class ApplyUserInfoBean {
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


//    /**
//     * pageNO : 1
//     * start : 0
//     * pageSize : 10
//     * totalCount : 2
//     * totalPage : 1
//     * list : [{"code":"QX201801250140368335312","accountNumber":"A201801220432517624658","accountName":"15700092384","type":"C","amountString":"1000000000000000000","feeString":"10000000000000000","channelType":"ETH","payCardInfo":"ETH","payCardNo":"0x39f881b9f6c53369c84ac9954918275c7a873c5f","status":"2","applyUser":"U201801220432517597250","applyNote":"","applyDatetime":"Jan 25, 2018 1:40:36 AM","approveUser":"cs001","approveNote":"2","approveDatetime":"Jan 25, 2018 1:41:08 AM","systemCode":"CD-COIN000017","companyCode":"CD-COIN000017","user":{"userId":"U201801220432517597250","secretUserId":"71d36b2de0f394896ea6981edc2d666cd965cdd7773f74ef3b254b5d02c9b7ac","loginName":"15700092384","mobile":"15700092384","nickname":"Junxi","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","divRate1":0.001,"divRate2":0.001,"tradeRate":0.01,"status":"0","email":"596365494@qq.com","createDatetime":"Jan 22, 2018 4:32:51 AM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","lastLogin":"Jan 16, 2018 10:43:55 AM","tradepwdFlag":false,"googleAuthFlag":false}},{"code":"QX201801171508233584517","accountNumber":"A201712201616209124990","accountName":"13868074590","type":"C","amountString":"15000000000000000","feeString":"10000000000000000","channelType":"ETH","payCardInfo":"ETH","payCardNo":"0xed73fef7246f408617ac7b0f4db0188352df7818","status":"3","applyUser":"U201712201616209096580","applyNote":"C端提现","applyDatetime":"Jan 17, 2018 3:08:23 PM","approveUser":"admin","approveNote":"tg","approveDatetime":"Jan 17, 2018 3:22:54 PM","systemCode":"CD-COIN000017","companyCode":"CD-COIN000017","user":{"userId":"U201712201616209096580","secretUserId":"06ef8e38faaf94c4847c5e89864bea8b285a3961b8bd1ac486768e9e35833ae0","loginName":"13868074590","mobile":"13868074590","nickname":"To","loginPwdStrength":"1","kind":"C","level":"1","idKind":"1","idNo":"412321199305120634","realName":"田磊","tradePwdStrength":"1","divRate1":0.001,"divRate2":0.001,"tradeRate":0.007,"status":"0","createDatetime":"Dec 20, 2017 4:16:20 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","lastLogin":"Jan 17, 2018 6:17:18 PM","tradepwdFlag":false,"googleAuthFlag":false}}]
//     */
//
//    private int pageNO;
//    private int start;
//    private int pageSize;
//    private int totalCount;
//    private int totalPage;
//    private List<ListBean> list;
//
//    public int getPageNO() {
//        return pageNO;
//    }
//
//    public void setPageNO(int pageNO) {
//        this.pageNO = pageNO;
//    }
//
//    public int getStart() {
//        return start;
//    }
//
//    public void setStart(int start) {
//        this.start = start;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }
//
//    public int getTotalPage() {
//        return totalPage;
//    }
//
//    public void setTotalPage(int totalPage) {
//        this.totalPage = totalPage;
//    }
//
//    public List<ListBean> getList() {
//        return list;
//    }
//
//    public void setList(List<ListBean> list) {
//        this.list = list;
//    }
//
//    public static class ListBean {
//        /**
//         * code : QX201801250140368335312
//         * accountNumber : A201801220432517624658
//         * accountName : 15700092384
//         * type : C
//         * amountString : 1000000000000000000
//         * feeString : 10000000000000000
//         * channelType : ETH
//         * payCardInfo : ETH
//         * payCardNo : 0x39f881b9f6c53369c84ac9954918275c7a873c5f
//         * status : 2
//         * applyUser : U201801220432517597250
//         * applyNote :
//         * applyDatetime : Jan 25, 2018 1:40:36 AM
//         * approveUser : cs001
//         * approveNote : 2
//         * approveDatetime : Jan 25, 2018 1:41:08 AM
//         * systemCode : CD-COIN000017
//         * companyCode : CD-COIN000017
//         * user : {"userId":"U201801220432517597250","secretUserId":"71d36b2de0f394896ea6981edc2d666cd965cdd7773f74ef3b254b5d02c9b7ac","loginName":"15700092384","mobile":"15700092384","nickname":"Junxi","loginPwdStrength":"1","kind":"C","level":"1","tradePwdStrength":"1","divRate1":0.001,"divRate2":0.001,"tradeRate":0.01,"status":"0","email":"596365494@qq.com","createDatetime":"Jan 22, 2018 4:32:51 AM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","lastLogin":"Jan 16, 2018 10:43:55 AM","tradepwdFlag":false,"googleAuthFlag":false}
//         */
//
//        private String code;
//        private String accountNumber;
//        private String accountName;
//        private String type;
//        private String currency;
//        private String amountString;
//        private String feeString;
//        private String channelType;
//        private String payCardInfo;
//        private String payCardNo;
//        private String status;
//        private String applyUser;
//        private String applyNote;
//        private String applyDatetime;
//        private String approveUser;
//        private String approveNote;
//        private String approveDatetime;
//        private String systemCode;
//        private String companyCode;
//        private UserBean user;
//
//        public String getCurrency() {
//            return currency;
//        }
//
//        public void setCurrency(String currency) {
//            this.currency = currency;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//
//        public String getAccountNumber() {
//            return accountNumber;
//        }
//
//        public void setAccountNumber(String accountNumber) {
//            this.accountNumber = accountNumber;
//        }
//
//        public String getAccountName() {
//            return accountName;
//        }
//
//        public void setAccountName(String accountName) {
//            this.accountName = accountName;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getAmountString() {
//            return amountString;
//        }
//
//        public void setAmountString(String amountString) {
//            this.amountString = amountString;
//        }
//
//        public String getFeeString() {
//            return feeString;
//        }
//
//        public void setFeeString(String feeString) {
//            this.feeString = feeString;
//        }
//
//        public String getChannelType() {
//            return channelType;
//        }
//
//        public void setChannelType(String channelType) {
//            this.channelType = channelType;
//        }
//
//        public String getPayCardInfo() {
//            return payCardInfo;
//        }
//
//        public void setPayCardInfo(String payCardInfo) {
//            this.payCardInfo = payCardInfo;
//        }
//
//        public String getPayCardNo() {
//            return payCardNo;
//        }
//
//        public void setPayCardNo(String payCardNo) {
//            this.payCardNo = payCardNo;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public String getApplyUser() {
//            return applyUser;
//        }
//
//        public void setApplyUser(String applyUser) {
//            this.applyUser = applyUser;
//        }
//
//        public String getApplyNote() {
//            return applyNote;
//        }
//
//        public void setApplyNote(String applyNote) {
//            this.applyNote = applyNote;
//        }
//
//        public String getApplyDatetime() {
//            return applyDatetime;
//        }
//
//        public void setApplyDatetime(String applyDatetime) {
//            this.applyDatetime = applyDatetime;
//        }
//
//        public String getApproveUser() {
//            return approveUser;
//        }
//
//        public void setApproveUser(String approveUser) {
//            this.approveUser = approveUser;
//        }
//
//        public String getApproveNote() {
//            return approveNote;
//        }
//
//        public void setApproveNote(String approveNote) {
//            this.approveNote = approveNote;
//        }
//
//        public String getApproveDatetime() {
//            return approveDatetime;
//        }
//
//        public void setApproveDatetime(String approveDatetime) {
//            this.approveDatetime = approveDatetime;
//        }
//
//        public String getSystemCode() {
//            return systemCode;
//        }
//
//        public void setSystemCode(String systemCode) {
//            this.systemCode = systemCode;
//        }
//
//        public String getCompanyCode() {
//            return companyCode;
//        }
//
//        public void setCompanyCode(String companyCode) {
//            this.companyCode = companyCode;
//        }
//
//        public UserBean getUser() {
//            return user;
//        }
//
//        public void setUser(UserBean user) {
//            this.user = user;
//        }
//
//        public static class UserBean {
//            /**
//             * userId : U201801220432517597250
//             * secretUserId : 71d36b2de0f394896ea6981edc2d666cd965cdd7773f74ef3b254b5d02c9b7ac
//             * loginName : 15700092384
//             * mobile : 15700092384
//             * nickname : Junxi
//             * loginPwdStrength : 1
//             * kind : C
//             * level : 1
//             * tradePwdStrength : 1
//             * divRate1 : 0.001
//             * divRate2 : 0.001
//             * tradeRate : 0.01
//             * status : 0
//             * email : 596365494@qq.com
//             * createDatetime : Jan 22, 2018 4:32:51 AM
//             * companyCode : CD-COIN000017
//             * systemCode : CD-COIN000017
//             * lastLogin : Jan 16, 2018 10:43:55 AM
//             * tradepwdFlag : false
//             * googleAuthFlag : false
//             */
//
//            private String userId;
//            private String secretUserId;
//            private String loginName;
//            private String mobile;
//            private String nickname;
//            private String loginPwdStrength;
//            private String kind;
//            private String level;
//            private String tradePwdStrength;
//            private double divRate1;
//            private double divRate2;
//            private double tradeRate;
//            private String status;
//            private String email;
//            private String createDatetime;
//            private String companyCode;
//            private String systemCode;
//            private String lastLogin;
//            private boolean tradepwdFlag;
//            private boolean googleAuthFlag;
//
//            public String getUserId() {
//                return userId;
//            }
//
//            public void setUserId(String userId) {
//                this.userId = userId;
//            }
//
//            public String getSecretUserId() {
//                return secretUserId;
//            }
//
//            public void setSecretUserId(String secretUserId) {
//                this.secretUserId = secretUserId;
//            }
//
//            public String getLoginName() {
//                return loginName;
//            }
//
//            public void setLoginName(String loginName) {
//                this.loginName = loginName;
//            }
//
//            public String getMobile() {
//                return mobile;
//            }
//
//            public void setMobile(String mobile) {
//                this.mobile = mobile;
//            }
//
//            public String getNickname() {
//                return nickname;
//            }
//
//            public void setNickname(String nickname) {
//                this.nickname = nickname;
//            }
//
//            public String getLoginPwdStrength() {
//                return loginPwdStrength;
//            }
//
//            public void setLoginPwdStrength(String loginPwdStrength) {
//                this.loginPwdStrength = loginPwdStrength;
//            }
//
//            public String getKind() {
//                return kind;
//            }
//
//            public void setKind(String kind) {
//                this.kind = kind;
//            }
//
//            public String getLevel() {
//                return level;
//            }
//
//            public void setLevel(String level) {
//                this.level = level;
//            }
//
//            public String getTradePwdStrength() {
//                return tradePwdStrength;
//            }
//
//            public void setTradePwdStrength(String tradePwdStrength) {
//                this.tradePwdStrength = tradePwdStrength;
//            }
//
//            public double getDivRate1() {
//                return divRate1;
//            }
//
//            public void setDivRate1(double divRate1) {
//                this.divRate1 = divRate1;
//            }
//
//            public double getDivRate2() {
//                return divRate2;
//            }
//
//            public void setDivRate2(double divRate2) {
//                this.divRate2 = divRate2;
//            }
//
//            public double getTradeRate() {
//                return tradeRate;
//            }
//
//            public void setTradeRate(double tradeRate) {
//                this.tradeRate = tradeRate;
//            }
//
//            public String getStatus() {
//                return status;
//            }
//
//            public void setStatus(String status) {
//                this.status = status;
//            }
//
//            public String getEmail() {
//                return email;
//            }
//
//            public void setEmail(String email) {
//                this.email = email;
//            }
//
//            public String getCreateDatetime() {
//                return createDatetime;
//            }
//
//            public void setCreateDatetime(String createDatetime) {
//                this.createDatetime = createDatetime;
//            }
//
//            public String getCompanyCode() {
//                return companyCode;
//            }
//
//            public void setCompanyCode(String companyCode) {
//                this.companyCode = companyCode;
//            }
//
//            public String getSystemCode() {
//                return systemCode;
//            }
//
//            public void setSystemCode(String systemCode) {
//                this.systemCode = systemCode;
//            }
//
//            public String getLastLogin() {
//                return lastLogin;
//            }
//
//            public void setLastLogin(String lastLogin) {
//                this.lastLogin = lastLogin;
//            }
//
//            public boolean isTradepwdFlag() {
//                return tradepwdFlag;
//            }
//
//            public void setTradepwdFlag(boolean tradepwdFlag) {
//                this.tradepwdFlag = tradepwdFlag;
//            }
//
//            public boolean isGoogleAuthFlag() {
//                return googleAuthFlag;
//            }
//
//            public void setGoogleAuthFlag(boolean googleAuthFlag) {
//                this.googleAuthFlag = googleAuthFlag;
//            }
//        }
//    }
}
