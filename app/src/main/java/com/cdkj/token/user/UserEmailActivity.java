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
import com.cdkj.token.databinding.ActivityUserEmailBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.StringUtils.isEmail;

/**
 * Created by lei on 2017/11/25.
 */

public class UserEmailActivity extends AbsActivity implements SendCodeInterface {

    private ActivityUserEmailBinding mBinding;

    private String email;

    private SendPhoneCodePresenter mSendCodePresenter;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String email) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserEmailActivity.class);
        intent.putExtra("email", email);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_email, null, false);
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

        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");

            if (TextUtils.isEmpty(email)) {
                setTopTitle(getStrRes(R.string.user_title_email_bind));
            } else {
                setTopTitle(getStrRes(R.string.user_title_email_modify));
//                mBinding.edtEmail.getEditText().setText(email);
                mBinding.edtEmail.getEditText().setHint(email);
            }
        }
    }

    private void initListener() {
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {

            if (check("code")){

                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        mBinding.edtEmail.getText().toString(), "805131", "C", SPUtilHelper.getCountryInterCode());

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

        if (isEmail(mBinding.edtEmail.getText().toString().trim())) {
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
     * 修改邮箱
     */
    public void modifyEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("captcha", mBinding.edtCode.getText().toString());
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805131", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    SPUtilHelper.saveUserEmail(mBinding.edtEmail.getText().toString().trim());

                    if (TextUtils.isEmpty(email)) {

                        UITipDialog.showSuccess(UserEmailActivity.this, getStrRes(R.string.user_email_bind_success), dialogInterface -> {
                            finish();
                        });

                    } else {
                        UITipDialog.showSuccess(UserEmailActivity.this, getStrRes(R.string.user_email_modify_success), dialogInterface -> {
                            finish();
                        });
                    }
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
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white)));

    }

    @Override
    public void CodeFailed(String code, String msg) {
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
