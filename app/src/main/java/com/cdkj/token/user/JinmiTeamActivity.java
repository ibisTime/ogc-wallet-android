package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityJinmiTeamBinding;
import com.cdkj.token.model.FufenBean;
import com.cdkj.token.model.JinmiTeamBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class JinmiTeamActivity extends AbsLoadActivity {

    private ActivityJinmiTeamBinding mBinding;

    public static void open(Context context) {
        Intent intent = new Intent(context, JinmiTeamActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_jinmi_team, null, false);
        mBaseBinding.titleView.setMidTitle("金米福分");
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        getData();
        getFufenData();
        getRule();
    }


    /**
     * 获取福分数据
     */
    private void getFufenData() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        showLoadingDialog();
        Call<BaseResponseModel<FufenBean>> call = RetrofitUtils.createApi(MyApi.class).getFufen("805913", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<FufenBean>(this) {
            @Override
            protected void onSuccess(FufenBean data, String SucMessage) {

                mBinding.tvMyFufen.setText(BigDecimalUtils.div(data.getTotalAward(), new BigDecimal(Math.pow(10, 8)), 2).toString());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    /**
     * 获取团队人数
     */
    private void getData() {
        showLoadingDialog();
        Map<String, String> map = new HashMap();
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseModel<JinmiTeamBean>> jinmiTeam = RetrofitUtils.createApi(MyApi.class).getJinmiTeam("805123", StringUtils.getRequestJsonString(map));
        jinmiTeam.enqueue(new BaseResponseModelCallBack<JinmiTeamBean>(this) {
            @Override
            protected void onSuccess(JinmiTeamBean data, String SucMessage) {
                mBinding.tvMyTeamNumber.setText(data.getRefrereeCount() + "");
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    /**
     * 获取积分规则
     */
    private void getRule() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "activety_notice");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("630047", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                showContent(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    private void showContent(String content) {
        mBinding.webview.loadData(content, "text/html;charset=UTF-8", "UTF-8");
    }
}
