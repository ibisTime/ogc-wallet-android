package com.cdkj.baselibrary.appmanager;


import android.text.TextUtils;

import com.cdkj.baselibrary.utils.LogUtil;

import static com.cdkj.baselibrary.base.BaseActivity.ENGLISH;

public class MyConfig {

    public final static String COMPANYCODE = "CD-TOKEN00018";
    public final static String SYSTEMCODE = "CD-TOKEN00018";
    public final static String USERTYPE = "C";//用户类型

    public final static int NODE_DEV = 0; //研发测试环境
    public final static int NODE_REALSE = 1;//真实环境


    // app运行环境

    public static final String BUILD_TYPE_TEST = "0";
    public static final String BUILD_TYPE_DEBUG = "1";
    public static final String BUILD_TYPE_RELEASE = "2";


    //默认七牛url
    public static String IMGURL = "http://pajvine9a.bkt.clouddn.com/";

    // 拍照文件保存路径
    public static final String CACHDIR = "tha_photo";

    /**
     * 获取转账节点类型
     *
     * @return
     */
    public static int getThisNodeType() {
        return NODE_REALSE;
    }

    // 环境访问地址
    public static final String BASE_URL_DEV = "http://120.26.6.213:2101/forward-service/"; // 研发
    public static final String BASE_URL_TEST = "http://120.26.6.213:2101/forward-service/"; // 测试
    //    public static final String BASE_URL_ONLINE = "http://120.26.6.213:2101/forward-service/"; // 测试
    public static final String BASE_URL_ONLINE = "http://47.75.165.70:2101/forward-service/"; // 线上


    /**
     * 获取网络请求URL
     *
     * @return
     */
    public static String getBaseURL() {
        if (false) {
            switch (SPUtilHelper.getAPPBuildType()) {
                case BUILD_TYPE_TEST: // 测试
                    return MyConfig.BASE_URL_TEST;
                default: // 研发
                    return MyConfig.BASE_URL_DEV;
            }
        } else {
            // 线上
            return MyConfig.BASE_URL_ONLINE;
        }
    }


    /**
     * 获取红包分享路径
     *
     * @param redPackageCode
     * @return
     */
    public static String getRedPacketShareUrl(String redPackageCode, String inviteCode) {

        StringBuffer stringBuffer = new StringBuffer();

        if (LogUtil.isLog) {
            stringBuffer.append("http://m.thadev.hichengdai.com/redPacket/receive.html");//研发
        } else {
            stringBuffer.append("http://m.thachain.org/redPacket/receive.html");//正式
        }

        stringBuffer.append("?code=" + redPackageCode);//红包码

        stringBuffer.append("&inviteCode=" + inviteCode);// 邀请码

        if (TextUtils.equals(SPUtilHelper.getLanguage(), ENGLISH)) { //国际化
            stringBuffer.append("&lang=en");
        } else {
            stringBuffer.append("&lang=cn");
        }

        return stringBuffer.toString();
    }

}
