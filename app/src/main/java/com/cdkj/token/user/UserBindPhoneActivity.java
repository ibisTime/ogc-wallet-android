package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

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
import com.cdkj.token.databinding.ActivityUserBindPhoneBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 绑定手机号
 */

public class UserBindPhoneActivity extends AbsActivity implements SendCodeInterface {

    private ActivityUserBindPhoneBinding mBinding;
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
        Intent intent = new Intent(context, UserBindPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_bind_phone, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);
        init();
        initListener();
    }

    private void init() {
        mSendCodePresenter = new SendPhoneCodePresenter(this, this);
        setTopTitle(getStrRes(R.string.user_setting_phone));
    }

    private void initListener() {
        mBinding.edtCode.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.activity_paypwd_mobile_hint));
                return;
            }
            SendVerificationCode sendVerificationCode = new SendVerificationCode(
                    mBinding.edtPhone.getText().toString(), "805060", "C", null);
            mSendCodePresenter.request(sendVerificationCode);

        });

        mBinding.btnConfirm.setOnClickListener(v -> {
            if (check()) {
                modifyPhone();
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_setting_phone));
            return false;
        }
        if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_code_hint));
            return false;
        }
        return true;
    }

    /**
     * 绑定手机号
     */
    public void modifyPhone() {
        Map<String, String> map = new HashMap<>();
        map.put("smsCaptcha", mBinding.edtCode.getText().toString());
        map.put("mobile", mBinding.edtPhone.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("isSendSms", "1");//是否发送密码短息(1=是，0=否)

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805060", StringUtils.getRequestJsonString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    SPUtilHelper.saveUserPhoneNum(mBinding.edtPhone.getText().toString().trim());
                    UITipDialog.showSuccess(UserBindPhoneActivity.this, getStrRes(R.string.user_email_bind_success), dialogInterface -> {
                        finish();
                    });
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg, int req) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white)));
    }

    @Override
    public void CodeFailed(String code, String msg, int req) {
        UITipDialog.showInfoNoIcon(this, msg);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
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
