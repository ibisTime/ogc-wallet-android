package com.cdkj.token.utils;

import android.content.Context;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.SystemParameterModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * @updateDts 2019/1/23
 */
public class NetWorrkUtils {

    public static void getSystemServer(String cKey, SystemServerListener systemServerListener) {
        getSystemServer(null, cKey, false, systemServerListener);
    }

    public static void getSystemServer(Context context, String cKey, SystemServerListener systemServerListener) {
        getSystemServer(context, cKey, false, systemServerListener);
    }

    public static void getSystemServer(Context context, String cKey, boolean isShowLoading, SystemServerListener systemServerListener) {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", cKey);
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("630047", StringUtils.getRequestJsonString(map));
        if (context instanceof BaseActivity) {
            if (isShowLoading) {
                ((BaseActivity) context).showLoadingDialog();
                ((BaseActivity) context).addCall(call);
            }
        }
        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(null) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (systemServerListener != null) {
                    systemServerListener.onSuccer(data);
                }
            }

            @Override
            protected void onNoNet(String msg) {
                systemServerListener.onError(msg);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                systemServerListener.onError(errorMessage);
            }

            @Override
            protected void onFinish() {
                if (context instanceof BaseActivity) {
                    if (isShowLoading) {
                        ((BaseActivity) context).disMissLoadingDialog();
                    }
                }
            }
        });
    }

    public interface SystemServerListener {
        void onSuccer(SystemParameterModel data);

        void onError(String error);
    }
}
