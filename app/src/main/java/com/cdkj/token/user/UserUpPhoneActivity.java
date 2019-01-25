package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserUpPhoneBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 更换手机号码
 * Created by cdkj on 2017/6/16.
 */

public class UserUpPhoneActivity extends AbsActivity implements SendCodeInterface {
    private ActivityUserUpPhoneBinding mBinding;
    private SendPhoneCodePresenter mSendCodePresenter;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserUpPhoneActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_up_phone, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_mobile_title));
        setSubLeftImgState(true);

        mBinding.edtPhoneOld.setDownImgVisibilityGone();
        mBinding.edtPhoneOld.getEditText().setEnabled(false);
        mBinding.edtPhoneOld.getEditText().setText(SPUtilHelper.getUserPhoneNum());

        mBinding.edtPhoneOld.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtPhoneNew.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mSendCodePresenter = new SendPhoneCodePresenter(this, this);
        initListener();
    }

    private void initListener() {
        //发送验证码  老手机号
        mBinding.edtCodeOld.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneOld.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpPhoneActivity.this, getString(R.string.activity_paypwd_mobile_hint));
                    return;
                }

                String phone = mBinding.edtPhoneOld.getText().toString().trim();

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        phone, "805061", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode(), 100);
                mSendCodePresenter.request(sendVerificationCode);
            }
        });

        //发送验证码  新手机号
        mBinding.edtCodeNew.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpPhoneActivity.this, getString(R.string.activity_paypwd_mobile_hint));
                    return;
                }
                String phone = mBinding.edtPhoneNew.getText().toString().trim();

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        phone, "805061", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode(), 200);
                mSendCodePresenter.request(sendVerificationCode);
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_mobile_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCodeNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtPhoneOld.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_mobile_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCodeOld.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }
                updatePhone();
            }
        });
    }


    /**
     * 更换手机号
     */
    private void updatePhone() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("newMobile", mBinding.edtPhoneNew.getText().toString());
        map.put("newSmsCaptcha", mBinding.edtCodeNew.getText().toString());
        map.put("oldMobile", mBinding.edtPhoneOld.getText().toString());
        map.put("oldSmsCaptcha", mBinding.edtCodeOld.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805061", StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.activity_mobile_modify_success));
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtPhoneNew.getText().toString().trim());
                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @Override
    public void CodeSuccess(String msg, int req) {
        if (req == 100) {
            mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCodeOld.getSendCodeBtn()));
        } else if (req == 200) {
            mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCodeNew.getSendCodeBtn()));
        }
    }

    //
    @Override
    public void CodeFailed(String code, String msg, int req) {
        showToast(msg);
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
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
            mSendCodePresenter = null;
        }
    }

}
