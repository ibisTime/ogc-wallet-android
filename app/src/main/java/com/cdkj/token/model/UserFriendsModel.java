package com.cdkj.token.model;

import java.util.List;

/**
 * @updateDts 2019/1/15
 */
public class UserFriendsModel {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 15
     * totalCount : 1
     * totalPage : 1
     * list : [{"userId":"U20190123185742571183206","loginName":"15939255831","photo":"ANDROID_1548241200494_4608_3456.jpg","loginPwdFlag":false,"tradepwdFlag":false,"googleAuthFlag":false,"emailBindFlag":false}]
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
         * userId : U20190123185742571183206
         * loginName : 15939255831
         * photo : ANDROID_1548241200494_4608_3456.jpg
         * loginPwdFlag : false
         * tradepwdFlag : false
         * googleAuthFlag : false
         * emailBindFlag : false
         */

        private String userId;
        private String loginName;
        private String photo;
        private String createDatetime;
        private boolean loginPwdFlag;
        private boolean tradepwdFlag;
        private boolean googleAuthFlag;
        private boolean emailBindFlag;

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
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
