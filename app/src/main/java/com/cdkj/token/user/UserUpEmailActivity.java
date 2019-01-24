package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.cdkj.token.databinding.ActivityUserUpEmailBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 更换手机号码
 * Created by cdkj on 2017/6/16.
 */

public class UserUpEmailActivity extends AbsActivity implements SendCodeInterface {
    private ActivityUserUpEmailBinding mBinding;
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
        Intent intent = new Intent(context, UserUpEmailActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_up_email, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_eamil_title));//activity_eamil_title
        setSubLeftImgState(true);

        mBinding.edtEmailOld.setDownImgVisibilityGone();
        mBinding.edtEmailOld.getEditText().setEnabled(false);
        mBinding.edtEmailOld.getEditText().setText(SPUtilHelper.getUserEmail());
        mSendCodePresenter = new SendPhoneCodePresenter(this, this);
        initListener();
    }

    private void initListener() {
        //发送验证码  老邮箱
        mBinding.edtCodeOld.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mBinding.edtEmailOld.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpEmailActivity.this, getString(R.string.input_email));
                    return;
                }
                if (!StringUtils.isEmail(mBinding.edtEmailOld.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpEmailActivity.this, getString(R.string.signup_email_format_wrong));
                    return;
                }

                String eamil = mBinding.edtEmailOld.getText().toString().trim();

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        eamil, "805070", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode(), 100);
                mSendCodePresenter.openVerificationActivity(sendVerificationCode);
            }
        });

        //发送验证码  新手机号
        mBinding.edtCodeNew.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtEmailNew.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpEmailActivity.this, getString(R.string.input_email));
                    return;
                }
                if (!StringUtils.isEmail(mBinding.edtEmailNew.getText())) {
                    UITipDialog.showInfoNoIcon(UserUpEmailActivity.this, getString(R.string.signup_email_format_wrong));
                    return;
                }
                String eamil = mBinding.edtEmailNew.getText().toString().trim();
                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        eamil, "805070", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode(), 200);
                mSendCodePresenter.openVerificationActivity(sendVerificationCode);
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtEmailNew.getText().toString())) {
                    showToast(getString(R.string.input_email));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCodeNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtEmailOld.getText().toString())) {
                    showToast(getString(R.string.input_email));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCodeOld.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }
                updateEamil();
            }
        });
    }


    /**
     * 更换邮箱
     */
    private void updateEamil() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("newEmail", mBinding.edtEmailNew.getText().toString());
        map.put("newSmsCaptcha", mBinding.edtCodeNew.getText().toString());
        map.put("oldEmail", mBinding.edtEmailOld.getText().toString());
        map.put("oldSmsCaptcha", mBinding.edtCodeOld.getText().toString());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805070", StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.activity_email_modify_success));
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtEmailNew.getText().toString().trim());
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mBinding.edtPhoneOld.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
//        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.edtPhoneOld.getLeftImage());
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
