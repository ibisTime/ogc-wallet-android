package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.cdkj.token.databinding.ActivityUserBindEmailBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.StringUtils.isEmail;

/**
 * 绑定邮箱
 */

public class UserBindEmailActivity extends AbsActivity implements SendCodeInterface {

    private ActivityUserBindEmailBinding mBinding;
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
        Intent intent = new Intent(context, UserBindEmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_bind_email, null, false);
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
        setTopTitle(getStrRes(R.string.user_title_email_bind));
    }

    private void initListener() {
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {

            if (check("code")) {

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        mBinding.edtEmail.getText().toString(), "805086", "C", SPUtilHelper.getCountryInterCode());

                mSendCodePresenter.openVerificationActivity(sendVerificationCode);
            }

        });

        mBinding.btnConfirm.setOnClickListener(v -> {

            if (check("all")) {
                modifyEmail();
            }
        });
    }

    private boolean check(String type) {
        if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_hint));
            return false;
        }

        if (!isEmail(mBinding.edtEmail.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.signup_email_format_wrong));
            return false;
        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_code_hint));
                return false;
            }
        }
        return true;
    }

    /**
     * 绑定邮箱
     */
    public void modifyEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("captcha", mBinding.edtCode.getText().toString());
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805086", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    SPUtilHelper.saveUserEmail(mBinding.edtEmail.getText().toString().trim());
                    UITipDialog.showSuccess(UserBindEmailActivity.this, getStrRes(R.string.user_email_bind_success), dialogInterface -> {
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
