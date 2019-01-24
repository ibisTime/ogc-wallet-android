package com.cdkj.baselibrary.interfaces;

/**
 * Created by cdkj on 2017/8/8.
 */

public interface SendCodeInterface {

    void CodeSuccess(String msg,int requestCode);    //成功

    void CodeFailed(String code, String msg,int requestCode);   //失败

    void StartSend();   //开始

    void EndSend();   //结束


}
