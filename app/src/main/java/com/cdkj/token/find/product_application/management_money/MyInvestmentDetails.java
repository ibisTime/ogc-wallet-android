package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MyManagementMoneyAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityMyInvestmentDetailsBinding;
import com.cdkj.token.model.InvestmentAmountModel;
import com.cdkj.token.model.MyManamentMoneyProduct;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的投资
 * Created by cdkj on 2018/9/27.
 */

public class MyInvestmentDetails extends BaseActivity {

    private ActivityMyInvestmentDetailsBinding mBinding;

    private RefreshHelper mRefreshHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyInvestmentDetails.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_investment_details);

        UIStatusBarHelper.setStatusBarDarkMode(this);

        initClickListener();

        initStatusChangeListener();

        initRefreshHelper();

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void initClickListener() {

        mBinding.linLayoutBillBTC.setOnClickListener(view -> InvestmentBillListActivity.open(this, "BTC"));

        mBinding.linLayoutBillUSDT.setOnClickListener(view -> InvestmentBillListActivity.open(this, "USDT"));

        mBinding.imgFinish.setOnClickListener(view -> finish());
    }

    /**
     * 筛选状态改变
     */
    private void initStatusChangeListener() {
        //已申购
        mBinding.checkboxBuy.setOnCheckedChangeListener((compoundButton, b) -> {
            mRefreshHelper.onDefaluteMRefresh(true);
        });
        //已持有
        mBinding.checkboxOwn.setOnCheckedChangeListener((compoundButton, b) -> {
            mRefreshHelper.onDefaluteMRefresh(true);
        });
        //已回款
        mBinding.checkboxBackMoney.setOnCheckedChangeListener((compoundButton, b) -> {
            mRefreshHelper.onDefaluteMRefresh(true);
        });
    }


    /**
     * 刷新
     */
    void initRefreshHelper() {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getUsrInvestAmount();
                if (isShowDialog) showLoadingDialog();

                Map<String, Object> map = new HashMap<>();

                map.put("userId", SPUtilHelper.getUserId());
                map.put("start", pageindex + "");
                map.put("limit", limit + "");
                map.put("statusList", getStatus());
                map.put("language", SPUtilHelper.getLanguage());

                Call<BaseResponseModel<ResponseInListModel<MyManamentMoneyProduct>>> call = RetrofitUtils.createApi(MyApi.class).getMyMoneyManageProductList("625526", StringUtils.getRequestJsonString(map));

                addCall(call);

                call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyManamentMoneyProduct>>(MyInvestmentDetails.this) {
                    @Override
                    protected void onSuccess(ResponseInListModel<MyManamentMoneyProduct> data, String SucMessage) {
                        mRefreshHelper.setData(data.getList(), getString(R.string.no_product), 0);
                    }

                    @Override
                    protected void onFinish() {
                        disMissLoadingDialog();
                    }
                });
            }
        });

        mRefreshHelper.init(RefreshHelper.LIMITE);

    }


    /**
     * 获取用户投资总额
     */
    public void getUsrInvestAmount() {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<InvestmentAmountModel>> call = RetrofitUtils.createApi(MyApi.class).getUserInvestAmount("625527", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<InvestmentAmountModel>(this) {
            @Override
            protected void onSuccess(InvestmentAmountModel data, String SucMessage) {
                mBinding.tvTotalInvestBTC.setText(AmountUtil.transformFormatToString2(data.getBtcTotalInvest(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
                mBinding.tvTotalIncomeBTC.setText(AmountUtil.transformFormatToString2(data.getBtcTotalIncome(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
                mBinding.tvTotalInvestUSDT.setText(AmountUtil.transformFormatToString2(data.getUsdtTotalInvest(), WalletHelper.COIN_USDT, AmountUtil.SCALE_4));
                mBinding.tvTotalIncomeUSDT.setText(AmountUtil.transformFormatToString2(data.getUsdtTotalIncome(), WalletHelper.COIN_USDT, AmountUtil.SCALE_4));
            }

            @Override
            protected void onFinish() {

            }
        });


    }

    /**
     * (0申购中，1持有中，2已到期，3募集失败)
     *
     * @return
     */
    private List<String> getStatus() {

        List<String> strings = new ArrayList<>();

        if (mBinding.checkboxBuy.isChecked()) {
            strings.add("0");
        }
        if (mBinding.checkboxOwn.isChecked()) {
            strings.add("1");
        }
        if (mBinding.checkboxBackMoney.isChecked()) {
            strings.add("2");
        }
        return strings;
    }


    /**
     * 获取数据适配器
     *
     * @param listData
     * @return
     */
    public RecyclerView.Adapter getListAdapter(List listData) {

        MyManagementMoneyAdapter adapter = new MyManagementMoneyAdapter(listData);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MyManagementMoneyDetailsActivity.open(this, adapter.getItem(position));
        });

        return adapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }

}
