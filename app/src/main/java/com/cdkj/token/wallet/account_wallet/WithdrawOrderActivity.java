package com.cdkj.token.wallet.account_wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.WithdrawOrderAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.WithdrawOrderModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2018/1/17.
 */

public class WithdrawOrderActivity extends BaseRefreshActivity<WithdrawOrderModel.ListBean> {
    public static final String ETC_INPUT = "1";//ETC 的转入
    public static final String ETC_OUTPUT = "2";//ETC 的转出
    public static final String BTC_INPUT = "3";//BTC 的转入
    public static final String BTC_OUTPUT = "4";//BTC 的转出
    String code;
    private String currency;
    private String type;

    public static void open(Context context, String type, String currency) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WithdrawOrderActivity.class)
                .putExtra("currency", currency)
                .putExtra(CdRouteHelper.DATASIGN, type));
    }


    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {

        setTopLineState(true);
        setSubLeftImgState(true);
        if (getIntent() == null)
            return;
        currency = getIntent().getStringExtra("currency");
        type = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        if (TextUtils.equals(ETC_INPUT, type) || TextUtils.equals(BTC_INPUT, type)) {
            code = "802345";
            setTopTitle(getString(R.string.wallet_input_order_title));
        } else {
            code = "802355";
            setTopTitle(getString(R.string.wallet_withdraw_order_title));
        }


        getListData(pageIndex, limit, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("companyCode", AppConfig.COMPANYCODE);
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("currency", currency);
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("limit", limit + "");
        map.put("start", pageIndex + "");

        Call call = RetrofitUtils.createApi(MyApi.class).getWithdrawOrder(code, StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<WithdrawOrderModel>(this) {

            @Override
            protected void onSuccess(WithdrawOrderModel data, String SucMessage) {
                if (data == null)
                    return;
                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<WithdrawOrderModel.ListBean> mDataList) {
        return new WithdrawOrderAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.wallet_withdraw_order_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}
