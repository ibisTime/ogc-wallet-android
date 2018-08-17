package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.NumberPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityManageMoneyDetailsBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.StringUtil;
import com.cdkj.token.views.dialogs.MoneyProductBuyStep1Dialog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 理财产品详情
 * Created by cdkj on 2018/8/9.
 */

public class ManagementMoneyDetailsActivity extends AbsLoadActivity {

    private ActivityManageMoneyDetailsBinding mBinding;

    private String mProductCode;//产品编号

    private ManagementMoney managementMoney;

    private NumberPwdInputDialog inputDialog;

    /**
     * @param context
     * @param productCode
     */
    public static void open(Context context, String productCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ManagementMoneyDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, productCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_manage_money_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBinding.getRoot().setVisibility(View.GONE);
        setStatusBarBlue();
        setTitleBgBlue();
        mBaseBinding.titleView.setMidTitle(R.string.money_product_details);

        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        initClickListener();

        getProductDetailsRequest();
    }

    private void initClickListener() {

        mBinding.btnBuy.setOnClickListener(view -> {
            if (managementMoney == null) return;

            getUserBalance(managementMoney.getSymbol());

        });

    }

    /**
     * 获取产品详情请求
     */
    private void getProductDetailsRequest() {

        if (TextUtils.isEmpty(mProductCode)) {
            return;
        }

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("code", mProductCode);

        Call<BaseResponseModel<ManagementMoney>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductDetails("625511", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ManagementMoney>(this) {
            @Override
            protected void onSuccess(ManagementMoney data, String SucMessage) {
                managementMoney = data;
                showData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void showData(ManagementMoney data) {

        if (data == null) {
            return;
        }

        BigDecimal coinUnit = LocalCoinDBUtils.getLocalCoinUnit(data.getSymbol());//币种最小单位

        mBinding.tvBuyTotlaRatio.setText(StringUtils.showformatPercentage(data.getExpectYield()));//年化率

        mBinding.tvTime2.setText(getString(R.string.product_days, data.getLimitDays() + ""));//产品期限

        mBinding.tvTotalAmount.setText(getCoinAmountText(data, coinUnit, data.getAmount()));//产品总额

        mBinding.tvLastAmunt.setText(getCoinAmountText(data, coinUnit, data.getAvilAmount()));//剩余

        mBinding.tvMinMoney.setText(Html.fromHtml(getCoinAmountText(data, coinUnit, data.getMinAmount()) +
                getString(R.string.limit_amount, getCoinAmountText(data, coinUnit, data.getLimitAmount()))));//起购 +限购


        BigDecimal sale = BigDecimalUtils.div(data.getSaleAmount(), data.getAmount(), 2);
        mBinding.progressbar.setProgress((int) (sale.floatValue() * 100));
        mBinding.tvState.setText(getString(R.string.product_buy, StringUtils.showformatPercentage(sale.floatValue())));


        /*产品详情*/
        mBinding.tvProductName.setText(data.getName());//产品名称
        mBinding.tvCoinType.setText(data.getSymbol());//认购币种
        if (TextUtils.equals("0", data.getPaymentType())) {
            mBinding.tvBackMoneyType.setText(R.string.payment_type);
        }
        //产品介绍
        mBinding.webviewProductIntroduction.loadData(data.getDescription(), "text/html;charset=UTF-8", "UTF-8");

        mBinding.getRoot().setVisibility(View.VISIBLE);
    }


    /**
     * 获取币种金额显示+币种名称显示文本
     *
     * @param data
     * @param coinUnit
     * @param amount
     * @return
     */
    private String getCoinAmountText(ManagementMoney data, BigDecimal coinUnit, BigDecimal amount) {
        return AmountUtil.amountFormatUnitForShow(amount, coinUnit, AmountUtil.ALLSCALE) + data.getSymbol();
    }

    /**
     * 获取余额
     */
    private void getUserBalance(String coin) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()) || TextUtils.isEmpty(coin))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", coin);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));
        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                if (data.getAccountList() == null || data.getAccountList().size() == 0) {
                    return;
                }

                showBuyStp1Dialog(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 显示购买第一步dialog
     *
     * @param data
     */
    private void showBuyStp1Dialog(CoinModel data) {
        BigDecimal amount = new BigDecimal(data.getAccountList().get(0).getAmountString());//可用余额
        MoneyProductBuyStep1Dialog moneyProductBuyStep1Dialog = new MoneyProductBuyStep1Dialog(ManagementMoneyDetailsActivity.this);
        moneyProductBuyStep1Dialog.
                setShowData(amount, managementMoney)
                .setToBuyListener(money -> {
                    moneyProductBuyStep1Dialog.dismiss();
                    showPasswordInputDialog(money);
                })
                .show();
    }


    /**
     * 显示密码输入框
     */
    private void showPasswordInputDialog(String money) {
        if (inputDialog == null) {
            inputDialog = new NumberPwdInputDialog(this).builder().setTitle(getString(R.string.please_input_transaction_pwd))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                        String tradePwd = inputDialog.getContentView().getText().toString().trim();
                        if (TextUtils.isEmpty(tradePwd)) {
                            UITipDialog.showInfoNoIcon(ManagementMoneyDetailsActivity.this, getString(R.string.please_input_transaction_pwd));
                            return;
                        }

                        buyRequest(money, tradePwd);

                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
        }
        inputDialog.getContentView().setText("");
        inputDialog.getContentView().setHint(getStrRes(R.string.please_input_transaction_pwd));
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }


    /**
     * 购买请求
     *
     * @param money
     * @param pwd
     */
    public void buyRequest(String money, String pwd) {

        if (TextUtils.isEmpty(mProductCode) || TextUtils.isEmpty(pwd)) return;

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("code", mProductCode);
        map.put("investAmount", AmountUtil.bigDecimalFormat(new BigDecimal(money), managementMoney.getSymbol()).toPlainString());
        map.put("tradePwd", pwd);
        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<IsSuccessModes>> call = RetrofitUtils.getBaseAPiService().successRequest("625520", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    @Override
    protected void onDestroy() {
        if (inputDialog != null) {
            inputDialog.dismiss();
        }
        super.onDestroy();
    }
}
