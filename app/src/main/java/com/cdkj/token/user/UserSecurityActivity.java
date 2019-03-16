package com.cdkj.token.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.LoadingMesgDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserSecurityBinding;
import com.cdkj.token.interfaces.IdentifyInterface;
import com.cdkj.token.interfaces.IdentifyPresenter;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.db.NavigationBean;
import com.cdkj.token.user.login.SetLoginPwdActivity;
import com.cdkj.token.user.login.SignInActivity;
import com.cdkj.token.user.pattern_lock.PatternLockSettingActivity;
import com.cdkj.token.utils.NavigationDBUtils;
import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 账户安全
 * Created by lei on 2017/11/1.
 */

public class UserSecurityActivity extends AbsStatusBarTranslucentActivity implements IdentifyInterface, UserInfoInterface {

    private ActivityUserSecurityBinding mBinding;
    private UserInfoPresenter mGetUserInfoPresenter;
    private PermissionHelper mHelper;
    private String parentCode;

    public static void open(Context context, String parentCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserSecurityActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, parentCode);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_security, null, false);
        mHelper = new PermissionHelper(this);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(R.string.accounts_and_security);
//        setPageBgRes(R.drawable.my_bg);
        setPageBgRes(R.color.white);
        if (getIntent() != null) {
            parentCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        initShowHind();
        initListener();
        mGetUserInfoPresenter = new UserInfoPresenter(this, this);

    }

    private void initShowHind() {
        ArrayList<NavigationBean> navigationparentCode = NavigationDBUtils.getNavigationparentCode(parentCode);
        for (NavigationBean bean : navigationparentCode) {
            switch (bean.getName()) {
                case "交易密码":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llTradePwd.setVisibility(View.GONE);
                    break;
                case "手势密码":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llSwitchView.setVisibility(View.GONE);
                    break;
                case "身份认证":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llIdentity.setVisibility(View.GONE);
                    break;
                case "绑定邮箱":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llMail.setVisibility(View.GONE);
                    break;
                case "绑定手机号码":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llPhone.setVisibility(View.GONE);
                    break;
                case "我的收款账号":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llMyBanks.setVisibility(View.GONE);
                    break;
                case "修改邮箱":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llModifyMail.setVisibility(View.GONE);
                    break;
                case "修改手机号码":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llMobile.setVisibility(View.GONE);
                    break;
                case "修改登录密码":
                    if (TextUtils.equals("0", bean.getStatus()))
                        mBinding.llPassword.setVisibility(View.GONE);
                    break;
            }
        }
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
//        mBinding.switchView.setOnClickListener(view -> {
//            if (mBinding.switchView.isChecked()) {
//                PatternLockSettingActivity.open(this);
//            } else {
//                SPUtilHelper.saveUserPatternPwd("");
//            }
//            mBinding.switchView.setChecked(false);
//        });
        mBinding.switchView.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!compoundButton.isPressed()) {
                return;
            }
            if (compoundButton.isChecked()) {
                //没有设置过密码 直接跳转到设置密码界面
                PatternLockSettingActivity.open(UserSecurityActivity.this);

            } else {
                SPUtilHelper.saveUserPatternPwd("");
                SPUtilHelper.saveSetPatternPwd(false);
            }
        });

//        if (compoundButton.isChecked()) {
//            if (TextUtils.isEmpty(SPUtilHelper.getUserPatternPwd())) {
//                //没有设置过密码 直接跳转到设置密码界面
//                PatternLockSettingActivity.open(UserSecurityActivity.this);
//            } else {
//                //设置过密码  提示  是否启用旧密码
//                new CommonDialog(this).builder()
//                        .setTitle("是否启用旧手势密码")
//                        .setPositiveBtn("确定", view -> {
//                            SPUtilHelper.saveSetPatternPwd(true);
//                        }).setNegativeBtn("设置新密码", view -> {
//                    PatternLockSettingActivity.open(UserSecurityActivity.this);
//                    SPUtilHelper.saveSetPatternPwd(false);
//                }).show();
//            }
//        } else {
////                    SPUtilHelper.saveUserPatternPwd("");
//            SPUtilHelper.saveSetPatternPwd(false);
//        }

        //资金密码
        mBinding.llTradePwd.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum())) {
                PayPwdModifyActivity.open(this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
            } else if (!TextUtils.isEmpty(SPUtilHelper.getUserEmail())) {
                PayPwdModifyActivity.open(this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserEmail());
            } else {
                UITipDialog.showInfo(this, "请先绑定手机号或者邮箱");
            }
        });

        //身份认证
        mBinding.llIdentity.setOnClickListener(view -> {

            if (SPUtilHelper.getRealName() == null || SPUtilHelper.getRealName().equals("")) {
                permissionRequest();
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


    /**
     * 权限申请
     */
    private void permissionRequest() {
        LogUtil.E("权限申请");

        mHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                startIdentity();

            }

            @Override
            public void doAfterDenied(String... permission) {
                disMissLoadingDialog();
                UITipDialog.showFail(UserSecurityActivity.this, "请授予权限");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mHelper != null) {
            mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // 身份认证上传回调 ------------------


    LoadingMesgDialog loadingMesgDialog;

    @Override
    public void onIdStart() {
//        showLoadingDialog();
        loadingMesgDialog = new LoadingMesgDialog(this, "正在认证中");
    }


    @Override
    public void onIdEnd(IdResultCallback.IdResult result) {

        loadingMesgDialog.show();
    }

    @Override
    public void onFaceStart() {

    }

    @Override
    public void onFaceEnd(FaceResultCallback.FaceResult result) {
//        loadingMesgDialog.setMsg("正在认证中");
//        loadingMesgDialog.show();
        disMissLoadingDialog();
    }

    @Override
    public void onError(String msg) {
        disMissLoadingDialog();
        loadingMesgDialog.dismiss();
    }

    @Override
    public void onUpLoadStart() {

    }

    @Override
    public void onUpLoadFailure(String info) {
        disMissLoadingDialog();
        loadingMesgDialog.dismiss();
        ToastUtil.show(this, info);
    }

    @Override
    public void onUpLoadSuccess() {
        showLoadingDialog();
        //重新获取用户信息
        mGetUserInfoPresenter.getUserInfoRequest();
    }

    @Override
    public void onUpLoadFinish() {
        loadingMesgDialog.dismiss();
        disMissLoadingDialog();

    }

    /**
     * 用户信息回调
     */
    @Override
    public void onStartGetUserInfo() {
        showLoadingDialog();
    }

    //用户信息  获取成功回调
    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        disMissLoadingDialog();
        if (TextUtils.isEmpty(errorMsg)) {
            new CommonDialog(this).builder()
                    .setTitle("认证成功")
                    .setPositiveBtn("确定", null)
                    .show();
        }
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
