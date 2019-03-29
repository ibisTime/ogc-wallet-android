package com.cdkj.token.utils;

import android.Manifest;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.LoadingMesgDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.interfaces.IdentifyInterface;
import com.cdkj.token.interfaces.IdentifyPresenter;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.zqzn.idauth.sdk.FaceResultCallback;
import com.zqzn.idauth.sdk.IdResultCallback;

/**
 * @updateDts 2019/3/20
 */
public class IdentityVerificationUtils implements IdentifyInterface, UserInfoInterface {

    private static PermissionHelper mHelper;
    private static BaseActivity mContext;
    private UserInfoPresenter mGetUserInfoPresenter;
//    public IdentityVerificationUtils(){
//
//    }

    /**
     * 开始身份认证,第一步申请权限
     */
    public PermissionHelper start(BaseActivity context) {
        mContext = context;

//        if(mContext instanceof BaseActivity){
//
//        }else if(mContext instanceof BaseLazyFragment){
//
//        }else{
//            return null;
//        }

        mHelper = new PermissionHelper(mContext);
        mHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                LogUtil.E("身份认证权限申请成功");
                startIdentity();
            }

            @Override
            public void doAfterDenied(String... permission) {

                UITipDialog.showFail(mContext, "请授予权限");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        return mHelper;
    }

    private void startIdentity() {
        IdentifyPresenter mIdentifyPresenter = new IdentifyPresenter(mContext, this);

        if (TextUtils.isEmpty(SPUtilHelper.getRealName())) {
            mIdentifyPresenter.startCardIndentify();
        } else {
            UITipDialog.showInfoNoIcon(mContext, mContext.getStrRes(R.string.user_iden_ok));
        }
    }
    //************身份证实名认证回调

    LoadingMesgDialog loadingMesgDialog;

    @Override
    public void onIdStart() {
        loadingMesgDialog = new LoadingMesgDialog(mContext, "正在认证中");
        mGetUserInfoPresenter = new UserInfoPresenter(this, mContext);
//        mContext.showLoadingDialog();
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
        mContext.disMissLoadingDialog();
    }


    @Override
    public void onError(String msg) {
        mContext.disMissLoadingDialog();
        loadingMesgDialog.dismiss();
    }

    @Override
    public void onUpLoadStart() {
//        showLoadingDialog();
//        loadingMesgDialog.setMsg("正在验证");
    }

    @Override
    public void onUpLoadFailure(String info) {
        mContext.disMissLoadingDialog();
        loadingMesgDialog.dismiss();
        ToastUtil.show(mContext, info);
    }

    @Override
    public void onUpLoadSuccess() {
        //重新获取用户信息
        mContext.showLoadingDialog();
        mGetUserInfoPresenter.getUserInfoRequest();
    }

    //身份认证上传回调完成  无论成功失败
    @Override
    public void onUpLoadFinish() {
        mContext.disMissLoadingDialog();
        loadingMesgDialog.dismiss();

    }
    //=======*********身份认证*********==============

    //=========***********获取用户信息**********==========
    @Override
    public void onStartGetUserInfo() {
        mContext.showLoadingDialog();
    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        mContext.disMissLoadingDialog();
        if (TextUtils.isEmpty(errorMsg)) {
            new CommonDialog(mContext).builder()
                    .setTitle("认证成功")
                    .setPositiveBtn("确定", null)
                    .show();
        }
    }
    //=========***********获取用户信息**********==========
}
