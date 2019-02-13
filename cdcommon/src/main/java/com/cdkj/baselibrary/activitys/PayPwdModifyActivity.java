package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPayPasswordBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 修改 设置 资金密码
 * Created by cdkj on 2017/6/29.
 */
public class PayPwdModifyActivity extends AbsActivity implements SendCodeInterface {

    private ActivityModifyPayPasswordBinding mBinding;

    private boolean mIsSetPwd;//是否设置过密码

    private SendPhoneCodePresenter mSendCoodePresenter;

    private String bizType;


    /**
     * @param context
     * @param isSetPwd//是否设置过资金密码
     */
    public static void open(Context context, boolean isSetPwd, String mobile) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PayPwdModifyActivity.class);
        intent.putExtra("isSetPwd", isSetPwd);
        intent.putExtra("mobile", mobile);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_pay_password, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
//        selectCountryCode = SPUtilHelper.getCountryInterCode();

        setSubLeftImgState(true);


        if (getIntent() != null) {
            mIsSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
            mBinding.edtPhone.setText(getIntent().getStringExtra("mobile"));
            mBinding.edtPhone.setSelection(mBinding.edtPhone.getText().length());
        }

        if (mIsSetPwd) {
            setTopTitle(getString(R.string.activity_paypwd_title));
        } else {
            setTopTitle(getString(R.string.activity_paypwd_title_set));
        }
        mSendCoodePresenter = new SendPhoneCodePresenter(this, this);
        setListener();
    }

    /**
     * 设置事件
     */
    private void setListener() {


//发送验证码
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
                    UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_find_mobile_hint));
                    return;
                }
                if (mIsSetPwd) {
                    bizType = "805067";//修改
                } else {
                    bizType = "805066";

                }
                String phone = mBinding.edtPhone.getText().toString().trim();
                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        phone, bizType, AppConfig.USERTYPE, null);
                mSendCoodePresenter.request(sendVerificationCode);


            }
        });
//确认
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputState()) return;

                setPwd();

            }
        });
    }

    /**
     * 验证输入状态
     *
     * @return 是否拦截
     */
    public boolean checkInputState() {
        if (TextUtils.isEmpty(mBinding.edtPhone.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_mobile_hint));
            return true;
        }
        if (TextUtils.isEmpty(mBinding.edtCode.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_code_hint));
            return true;
        }
        if (TextUtils.isEmpty(mBinding.edtPassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_pwd_hint));
            return true;
        }
        if (mBinding.edtPassword.getText().length() < 6) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_pwd_format_hint));
            return true;
        }


        if (TextUtils.isEmpty(mBinding.edtRepassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_find_repassword_hint));
            return true;
        }

        if (!TextUtils.equals(mBinding.edtPassword.getText().toString().trim(), mBinding.edtRepassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_find_repassword_format_hint));
            return true;
        }
        return false;
    }


    private void setPwd() {

        Map<String, String> object = new HashMap<>();

        object.put("userId", SPUtilHelper.getUserId());
        object.put("token", SPUtilHelper.getUserToken());
        if (mIsSetPwd) {
            object.put("newTradePwd", mBinding.edtPassword.getText().toString().trim());
        } else {
            object.put("tradePwd", mBinding.edtPassword.getText().toString().trim());
        }

        object.put("smsCaptcha", mBinding.edtCode.getText().toString().toString());

        String code = "";
        if (mIsSetPwd) {  //修改
            code = "805067";
        } else {
            code = "805066";
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getRequestJsonString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (!data.isSuccess()) {
                    return;
                }

                SPUtilHelper.saveTradePwdFlag(true);

                String dialotString = "";

                if (mIsSetPwd) {
                    dialotString = getString(R.string.activity_paypwd_modify_sucess);
                } else {
                    dialotString = getString(R.string.activity_paypwd_set_success);
                }

                UITipDialog.showSuccess(PayPwdModifyActivity.this, dialotString, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg, int req) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
    }

    @Override
    public void CodeFailed(String code, String msg, int req) {
        UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCoodePresenter != null) {
            mSendCoodePresenter.clear();
            mSendCoodePresenter = null;
        }
    }
}
