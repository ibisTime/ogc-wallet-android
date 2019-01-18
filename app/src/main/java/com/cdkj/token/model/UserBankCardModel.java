package com.cdkj.token.model;

import java.util.List;

/**
 * @updateDts 2019/1/15
 */
public class UserBankCardModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 15
     * totalCount : 2
     * totalPage : 1
     * list : [{"code":"BC20190117104411941149291","bankcardNumber":"1234567891011112","bankCode":"ZJTLCB","bankName":"浙江泰隆商业银行","subbranch":"1234567891011112","userId":"U20181228133231683443414","type":"0","status":"1","systemCode":"CD-OGC000019","isDefault":"0"},{"code":"BC20190116192900461604378","bankcardNumber":"3303271996194648","bankCode":"PSBC","bankName":"中国邮政储蓄银行","subbranch":"3303271996194648","userId":"U20181228133231683443414","type":"0","status":"1","systemCode":"CD-OGC000019","isDefault":"0"}]
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
         * code : BC20190117104411941149291
         * bankcardNumber : 1234567891011112
         * bankCode : ZJTLCB
         * bankName : 浙江泰隆商业银行
         * subbranch : 1234567891011112
         * userId : U20181228133231683443414
         * type : 0
         * status : 1
         * systemCode : CD-OGC000019
         * isDefault : 0
         */

        private String code;
        private String bankcardNumber;
        private String bankCode;
        private String bankName;
        private String subbranch;
        private String userId;
        private String type;
        private String status;
        private String systemCode;
        private String isDefault;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBankcardNumber() {
            return bankcardNumber;
        }

        public void setBankcardNumber(String bankcardNumber) {
            this.bankcardNumber = bankcardNumber;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getSubbranch() {
            return subbranch;
        }

        public void setSubbranch(String subbranch) {
            this.subbranch = subbranch;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }
    }
}
