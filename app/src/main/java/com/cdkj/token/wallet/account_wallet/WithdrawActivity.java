package com.cdkj.token.wallet.account_wallet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.TextPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWithdrawBinding;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.WalletModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.NetWorrkUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 提币
 * <p>
 * Created by lei on 2017/10/18.
 */

public class WithdrawActivity extends AbsLoadActivity {

    private WalletModel.AccountListBean model;
    private PermissionHelper permissionHelper;
    private TextPwdInputDialog inputDialog;
    private ActivityWithdrawBinding mBinding;
//    private String serviceCharge;


    public static void open(Context context, WalletModel.AccountListBean model) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WithdrawActivity.class).putExtra(CdRouteHelper.DATASIGN, model));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_withdraw, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        if (model == null) return;
        WithdrawOrderActivity.open(this, TextUtils.equals(model.getCurrency(), "BTC") ? WithdrawOrderActivity.BTC_OUTPUT : WithdrawOrderActivity.ETC_OUTPUT, model.getCurrency());
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("转出");
        mBaseBinding.titleView.setRightTitle(getString(R.string.wallet_charge_recode));
        init();
        initListener();
        getUserInfoRequest();
        getWithdrawFee();
    }

    private void init() {
        if (getIntent() == null) {
            return;
        }
        model = (WalletModel.AccountListBean) getIntent().getSerializableExtra(CdRouteHelper.DATASIGN);
        if (model == null) {
            return;
        }
        String availablemountString = AmountUtil.transformFormatToString(model.getAmount(), model.getCurrency(), 8) + " " + model.getCurrency();
        mBinding.tvAmount.setText(availablemountString);
        mBinding.tvFee.setText("0" + model.getCurrency());

    }

    private void initListener() {
        //二维码
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            QRscan();
        });
        mBinding.btnWithdraw.setOnClickListener(view -> {
            if (!check()) {
                return;
            }
            if (SPUtilHelper.getTradePwdFlag()) {
                showInputDialog();
            } else {
                UITipDialog.showInfo(WithdrawActivity.this, getString(R.string.please_set_account_money_password), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        PayPwdModifyActivity.open(WithdrawActivity.this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
                    }
                });
            }
        });
        mBinding.edtNumber.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtNumber, 15, 8));
//        mBinding.edtNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (TextUtils.isEmpty(serviceCharge))
//                    return;
//                String number = mBinding.edtNumber.getText().toString().trim();
//                if (TextUtils.isEmpty(number) || !(Double.parseDouble(number) > 0)) {
//                    mBinding.tvFee.setText("0" + model.getCurrency());
//                    return;
//                }
//                String serviceChargeText = BigDecimalUtils.multiply(new BigDecimal(number), new BigDecimal(serviceCharge)).toString();
//                mBinding.tvFee.setText(AmountUtil.scale(serviceChargeText, 8) + model.getCurrency());
//            }
//        });
    }

    //权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.user_address_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtNumber.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_amount_hint));
            return false;
        }

//        if (SPUtilHelper.getGoogleAuthFlag() && TextUtils.isEmpty(mBinding.editGoogleCode.getText().toString())) {
//            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.google_code_hint));
//            mBinding.linLayoutGoogle.setVisibility(View.VISIBLE);
////            mBinding.viewGoogle.setVisibility(View.VISIBLE);
//            return false;
//        }

        return true;
    }

    private void showInputDialog() {
        if (inputDialog == null) {
            inputDialog = new TextPwdInputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        if (TextUtils.isEmpty(inputMsg)) {
                            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.trade_code_hint));
                        } else {
                            withdrawal(inputMsg);
                            inputDialog.dismiss();
                        }

                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
            inputDialog.getContentView().setText("");
            inputDialog.getContentView().setHint(getStrRes(R.string.trade_code_hint));
        }
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!TextUtils.isEmpty(result))
                        mBinding.editToAddress.setText(result);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WithdrawActivity.this, getStrRes(R.string.qrcode_parsing_failure), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 获取手续费
     */
    private void getWithdrawFee() {
        String cKey;
        if (TextUtils.equals("BTC", model.getCurrency())) {
            cKey = "btc_withdraw_fee";
        } else {
            cKey = "usdt_withdraw_fee";
        }
        NetWorrkUtils.getSystemServer(this, cKey, true, new NetWorrkUtils.SystemServerListener() {
            @Override
            public void onSuccer(SystemParameterModel data) {
                mBinding.tvFee.setText(data.getCvalue() + model.getCurrency());
            }

            @Override
            public void onError(String error) {
                LogUtil.E("手续费获取失败" + error);

            }
        });

//        if (TextUtils.isEmpty(coin)) {
//            return;
//        }
//
//        Map<String, String> map = new HashMap<>();
//
//        map.put("symbol", coin);
//        map.put("systemCode", AppConfig.SYSTEMCODE);
//        map.put("companyCode", AppConfig.COMPANYCODE);
//
//        Call call = RetrofitUtils.createApi(MyApi.class).getCoinFees("802266", StringUtils.getRequestJsonString(map));
//
//        addCall(call);
//
//        showLoadingDialog();
//
//        call.enqueue(new BaseResponseModelCallBack<LocalCoinDbModel>(this) {
//
//            @Override
//            protected void onSuccess(LocalCoinDbModel data, String SucMessage) {
//                if (data == null)
//                    return;
//
////                mBinding.tvFee.setText(AmountUtil.transformFormatToString(data.getWithdrawFee(), coin, AmountUtil.ALLSCALE));
//            }
//
//            @Override
//            protected void onFinish() {
//                disMissLoadingDialog();
//            }
//        });
    }

    /**
     * 提现
     *
     * @param tradePwd
     */
    private void withdrawal(String tradePwd) {
//        BigDecimal bigDecimal = new BigDecimal(mBinding.edtNumber.getText().toString().trim());
        Map<String, String> map = new HashMap<>();
        map.put("accountNumber", model.getAccountNumber());
        map.put("amount", BigDecimalUtils.multiply(new BigDecimal(mBinding.edtNumber.getText().toString().trim()), LocalCoinDBUtils.getLocalCoinUnit(WalletHelper.COIN_BTC)).toString());
        map.put("applyNote", "");
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("applyNote", model.getCurrency() + getString(R.string.bill_type_withdraw));
        map.put("payCardInfo", model.getCurrency());
        map.put("payCardNo", mBinding.editToAddress.getText().toString().trim());
        map.put("tradePwd", tradePwd);

        Call call = RetrofitUtils.getBaseAPiService().codeRequest("802350", StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {

            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                UITipDialog.showSuccess(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_success), dialogInterface -> finish());
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    /**
     * 二维码扫描
     */
    private void QRscan() {
        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(WithdrawActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 100);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.camera_refused));
            }
        }, Manifest.permission.CAMERA);
    }


    /**
     * 获取用户信息 获取用户是否设置了支付密码
     */
    public void getUserInfoRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {

            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                if (data == null)
                    return;
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

}
