package com.cdkj.token.user.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.activitys.AppBuildTypeActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignIn2Binding;

@Route(path = CdRouteHelper.APPLOGIN)
public class SignInActivity extends AbsStatusBarTranslucentActivity implements LoginInterface, SendCodeInterface {

    private boolean canOpenMain;

    private LoginPresenter mPresenter;
    private SendPhoneCodePresenter mSendPhoneCodePresenter;
    private ActivitySignIn2Binding mBinding;

    private final String CODE_LOGIN_CODE = "805050";//验证码登录接口编号

    private int changeDevCount = 0;//用于记录研发或测试环境切换条件

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, boolean canOpenMain) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, canOpenMain);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in2, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        sheShowTitle(false);
        setPageBgRes(R.color.colorPrimary);
        setContentBgRes(R.color.white);
        setStatusBarWhite();

        mPresenter = new LoginPresenter(this);
        mSendPhoneCodePresenter = new SendPhoneCodePresenter(this, this);
        init();
        initEditInputType();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDevCount = 0;
    }

    private void initEditInputType() {
        mBinding.edtUsername.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void init() {
        if (getIntent() == null)
            return;

        canOpenMain = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);
    }

    private void initListener() {

        //发送验证码
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
                return;
            }
            String phone = mBinding.edtUsername.getText().toString().trim();

            SendVerificationCode sendVerificationCode = new SendVerificationCode(
                    phone, CODE_LOGIN_CODE, "C", SPUtilHelper.getCountryInterCode());

            mSendPhoneCodePresenter.openVerificationActivity(sendVerificationCode);

        });

        //登录
        mBinding.btnConfirm.setOnClickListener(v -> {
            checkInputAndLogin();
        });

        //找回密码
        mBinding.tvForget.setOnClickListener(v -> {
            FindLoginPwdActivity.open(this, mBinding.edtUsername.getText().toString().trim());
        });

        //注册
        mBinding.tvToSignUp.setOnClickListener(view -> {
            SignUpActivity.open(this);
        });

        /**
         * 切换环境
         */
        if (LogUtil.isLog) {
            mBinding.imgTha.setOnClickListener(view -> {
                changeDevCount++;
                if (changeDevCount > 8) {           //连续点击8次以上才可以切换环境
                    AppBuildTypeActivity.open(this);
                }
            });
        }
    }

    /**
     * 检测输入并登录
     */
    private void checkInputAndLogin() {

        if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
            return;
        }

        if (mBinding.edtPassword.getText().toString().trim().length() < 6) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_format_hint));
            return;
        }

        mPresenter.login(mBinding.edtUsername.getText().toString(), mBinding.edtPassword.getText().toString(), SPUtilHelper.getCountryInterCode(), this);
    }

    /**
     * 登录成功下一步
     *
     * @param user
     */
    public void loginSuccessNext(UserLoginModel user) {
        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtUsername.getText().toString().trim());
        OtherLibManager.uemProfileSignIn(user.getUserId());

        if (canOpenMain) {
            MainActivity.open(this);
        }
        finish();
    }

    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {
        loginSuccessNext(user);
    }


    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoadingDialog();
        showToast(msg);
    }


    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        disMissLoadingDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
        if (mSendPhoneCodePresenter != null) {
            mSendPhoneCodePresenter.clear();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void CodeSuccess(String msg, int req) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.btn_blue), ContextCompat.getColor(this, R.color.white)));

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
        if (mSendPhoneCodePresenter != null) {
            mSendPhoneCodePresenter.onActivityResult(requestCode, resultCode, data);
        }

    }


}
