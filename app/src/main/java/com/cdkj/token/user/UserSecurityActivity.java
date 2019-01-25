package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserSecurityBinding;
import com.cdkj.token.interfaces.IdentifyInterface;
import com.cdkj.token.interfaces.IdentifyPresenter;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.user.login.SetLoginPwdActivity;
import com.cdkj.token.user.login.SignInActivity;
import com.cdkj.token.user.pattern_lock.PatternLockSettingActivity;
import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户安全
 * Created by lei on 2017/11/1.
 */

public class UserSecurityActivity extends AbsStatusBarTranslucentActivity implements IdentifyInterface, UserInfoInterface {

    private ActivityUserSecurityBinding mBinding;
    private UserInfoPresenter mGetUserInfoPresenter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserSecurityActivity.class));
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_security, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(R.string.accounts_and_security);
//        setPageBgRes(R.drawable.my_bg);
        setPageBgRes(R.color.white);
        initListener();
        mGetUserInfoPresenter = new UserInfoPresenter(this, this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }


    private void init() {

        mBinding.switchView.setChecked(SPUtilHelper.isSetPatternPwd());

        mBinding.tvMail.setText(SPUtilHelper.getUserEmail());

        if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_close));
        } else {
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_open));
        }


        if (SPUtilHelper.getLoginPwdFlag()) { // 未打开谷歌验证
            mBinding.tvPwdState.setText(getStrRes(R.string.user_setting_password));
        } else {
            mBinding.tvPwdState.setText(R.string.set_login_pwd);
        }

        if (!TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum())) {
            mBinding.tvPhone.setText(SPUtilHelper.getUserPhoneNum());
            mBinding.ivPhone.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(SPUtilHelper.getUserEmail())) {
            mBinding.tvMail.setText(SPUtilHelper.getUserEmail());
            mBinding.ivMail.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(SPUtilHelper.getRealName())) {
            mBinding.tvIdentity.setText(SPUtilHelper.getRealName());
            mBinding.ivIdentity.setVisibility(View.GONE);
        }

    }

    private void initListener() {

        //开启关闭手势密码
        mBinding.switchView.setOnClickListener(view -> {
            if (mBinding.switchView.isChecked()) {
                PatternLockSettingActivity.open(this);
            } else {
                SPUtilHelper.saveUserPatternPwd("");
            }
            mBinding.switchView.setChecked(false);
        });

        //资金密码
        mBinding.llTradePwd.setOnClickListener(view -> {
            PayPwdModifyActivity.open(this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
        });

        //身份认证
        mBinding.llIdentity.setOnClickListener(view -> {
            if (SPUtilHelper.getRealName() == null || SPUtilHelper.getRealName().equals("")) {
                startIdentity();
            } else {
                showToast(getStrRes(R.string.user_identity_success));
            }
        });

        //绑定手机号
        mBinding.llPhone.setOnClickListener(v -> {
            if (!SPUtilHelper.getUserPhoneNum().isEmpty()) {
                return;
            }
            UserBindPhoneActivity.open(this);
        });
        //修改手机号
        mBinding.llMobile.setOnClickListener(view -> {
            if (SPUtilHelper.getUserPhoneNum().isEmpty()) {
                UITipDialog.showFail(this, "请先绑定手机号");
                return;
            }
            UserUpPhoneActivity.open(this);
        });

        //我的银行卡
        mBinding.llMyBanks.setOnClickListener(view -> UserBackCardActivity.open(this, false));

        //绑定邮箱
        mBinding.llMail.setOnClickListener(view -> {
            if (!SPUtilHelper.getUserEmail().isEmpty()) {
                return;
            }
            UserBindEmailActivity.open(this);
        });

        //修改邮箱
        mBinding.llModifyMail.setOnClickListener(view -> {
            if (SPUtilHelper.getUserEmail().isEmpty()) {
                UITipDialog.showFail(this, "请先绑定邮箱");
                return;
            }
            UserUpEmailActivity.open(this);
        });


        //登录密码
        mBinding.llPassword.setOnClickListener(view -> {
            SetLoginPwdActivity.open(this);
        });

        mBinding.llGoogle.setOnClickListener(view -> {
            if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
                UserGoogleActivity.open(this, "open");
            } else {
                GoogleCodeSetActivity.open(this);
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            showDoubleWarnListen(getStrRes(R.string.user_setting_sign_out) + "?", view1 -> {
                SPUtilHelper.logOutClear();
                OtherLibManager.uemProfileSignOff();
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                SignInActivity.open(UserSecurityActivity.this, true);
                finish();
            });
        });
    }

    private void startIdentity() {
        IdentifyPresenter mIdentifyPresenter = new IdentifyPresenter(this, this);

        if (TextUtils.isEmpty(SPUtilHelper.getRealName())) {
            mIdentifyPresenter.startCardIndentify();
        } else {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_iden_ok));
        }
    }


    // 身份认证上传回调 ------------------

    @Override
    public void onError(String msg) {
        disMissLoadingDialog();
    }


    @Override
    public void onIdStart() {
        showLoadingDialog();
    }

    @Override
    public void onIdEnd(IdResultCallback.IdResult result) {
        disMissLoadingDialog();
    }

    @Override
    public void onFaceStart() {
        showLoadingDialog();
    }

    @Override
    public void onFaceEnd(FaceResultCallback.FaceResult result) {
    }

    @Override
    public void onUpLoadStart() {
        showLoadingDialog();
    }

    @Override
    public void onUpLoadFailure(String info) {
        ToastUtil.show(this, info);
    }

    @Override
    public void onUpLoadSuccess() {
        //重新获取用户信息
        mGetUserInfoPresenter.getUserInfoRequest();
    }

    @Override
    public void onUpLoadFinish() {
        disMissLoadingDialog();
    }


    /**
     * 用户信息回调
     */
    @Override
    public void onStartGetUserInfo() {

    }

    //用户信息  获取成功回调
    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        init();
    }

    // ------------------ 身份认证上传回调
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetUserInfoPresenter != null) {
            mGetUserInfoPresenter.clear();
        }
    }
}
