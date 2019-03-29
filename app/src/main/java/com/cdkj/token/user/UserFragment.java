package com.cdkj.token.user;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.activitys.NickModifyActivity;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.TextPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.NickNameUpdate;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentUser2Binding;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.jinmi.PrivateJinMinMoneyActivity;
import com.cdkj.token.model.db.NavigationBean;
import com.cdkj.token.user.invite.InviteQrActivity2;
import com.cdkj.token.user.setting.UserSettingActivity;
import com.cdkj.token.utils.NavigationDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.create_guide.CreateWalletStartActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2018/6/28.
 */

public class UserFragment extends BaseLazyFragment implements UserInfoInterface {

    private FragmentUser2Binding mBinding;

    public final int PHOTOFLAG = 110;

    private CommonDialog commonDialog;

    private UserInfoPresenter mGetUserInfoPresenter;//获取用户信息
    private ArrayList<NavigationBean> userNavigationList;
    private TextPwdInputDialog inputDialog;
    private final int DIALOG_DELETE = 0;//dialog 类型 删除
    private final int DIALOG_BACKUP = 1; //dialog类型备份


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user2, null, false);

        initClickListener();

        initShowHind();
//        getFufenData();
        mGetUserInfoPresenter = new UserInfoPresenter(this, mActivity);
        mGetUserInfoPresenter.getUserInfoRequest();
        return mBinding.getRoot();
    }

//    /**
//     * 获取福分数据
//     */
//    private void getFufenData() {
//        Map<String, String> map = new HashMap<>();
//
//        map.put("userId", SPUtilHelper.getUserId());
//
//        Call<BaseResponseModel<FufenBean>> call = RetrofitUtils.createApi(MyApi.class).getFufen("805913", StringUtils.getRequestJsonString(map));
//
//        call.enqueue(new BaseResponseModelCallBack<FufenBean>(mActivity) {
//            @Override
//            protected void onSuccess(FufenBean data, String SucMessage) {
//
//                mBinding.tvFufen.setText(data.getRegAcount() + "");
//            }
//
//            @Override
//            protected void onReqFailure(String errorCode, String errorMessage) {
//                super.onReqFailure(errorCode, errorMessage);
//
//            }
//
//            @Override
//            protected void onFinish() {
//
//            }
//        });
//    }

    private void initShowHind() {
        NavigationBean navigationBean = ((MainActivity) mActivity).navigationList.get(MainActivity.USER);
        userNavigationList = NavigationDBUtils.getNavigationparentCode(navigationBean.getCode());

        for (NavigationBean bean : userNavigationList) {
            switch (bean.getName()) {
                case "账户与安全":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.linLayoutUserAccount.setVisibility(View.GONE);
                    } else {
                        mBinding.linLayoutUserAccount.setVisibility(View.VISIBLE);
                    }
                    break;
                case "我的好友":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.llFriends.setVisibility(View.GONE);
                        mBinding.lineFriends.setVisibility(View.GONE);
                    } else {
                        mBinding.llFriends.setVisibility(View.VISIBLE);
                        mBinding.lineFriends.setVisibility(View.VISIBLE);
                    }
                    break;
                case "邀请有礼":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.linLayoutInvite.setVisibility(View.GONE);
                    } else {
                        mBinding.linLayoutInvite.setVisibility(View.VISIBLE);
                    }
                    break;
                case "加入社群":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.joinUs.setVisibility(View.GONE);
                        mBinding.lineJoinUs.setVisibility(View.GONE);
                    } else {
                        mBinding.joinUs.setVisibility(View.VISIBLE);
                        mBinding.lineJoinUs.setVisibility(View.VISIBLE);
                    }
                    break;
                case "帮助中心":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.helper.setVisibility(View.GONE);
                        mBinding.lineHelper.setVisibility(View.GONE);
                    } else {
                        mBinding.helper.setVisibility(View.VISIBLE);
                        mBinding.lineHelper.setVisibility(View.VISIBLE);
                    }
                    break;
                case "设置":
                    if (TextUtils.equals("0", bean.getStatus())) {
                        mBinding.setting.setVisibility(View.GONE);
                    } else {
                        mBinding.setting.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

    }

    @Override
    public void onDestroy() {
        if (commonDialog != null) {
            commonDialog.closeDialog();
        }
        if (mGetUserInfoPresenter != null) {
            mGetUserInfoPresenter.clear();
        }
        super.onDestroy();
    }

    public static UserFragment getInstance() {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initClickListener() {
        //更换头像
        mBinding.imgLogo.setOnClickListener(view -> {
            ImageSelectActivity.launchFragment(this, PHOTOFLAG);
        });

        //修改昵称
        mBinding.tvNickName.setOnClickListener(view -> {
            NickModifyActivity.open(mActivity, SPUtilHelper.getUserName());
        });

        //账户与安全
        mBinding.linLayoutUserAccount.setOnClickListener(view -> {
            UserSecurityActivity.open(mActivity, getFunctionBean("账户与安全"));
        });

        //金米钱包
        mBinding.llJinmiMoney.setOnClickListener(view -> {
            //如果没有创建钱包就 进入创建界面  如果已经创建了  就直接进入私有钱包界面
            boolean isHasWallet = WalletHelper.isUserAddedWallet(SPUtilHelper.getUserId());
            if (isHasWallet) {
                PrivateJinMinMoneyActivity.open(mActivity);
            } else {
                CreateWalletStartActivity.open(mActivity);
            }
        });


        //我的好友
        mBinding.llFriends.setOnClickListener(view -> UserFriendsActivity.open(mActivity));

        //邀请有礼
        mBinding.linLayoutInvite.setOnClickListener(view -> {
            InviteQrActivity2.open(mActivity);
        });

        //金米福分
        mBinding.llJinmiFraction.setOnClickListener(view -> {
            JinmiTeamActivity.open(mActivity);
        });

        //加入社群
        mBinding.joinUs.setOnClickListener(view -> UserJoinActivity.open(mActivity));

        //帮助中心
        mBinding.helper.setOnClickListener(view -> {
            OtherLibManager.openZendeskHelpCenter(mActivity);
        });
        //设置
        mBinding.setting.setOnClickListener(view -> {
            UserSettingActivity.open(mActivity, getFunctionBean("设置"));
        });

    }

    @Override
    protected void lazyLoad() {
//        if (mBinding != null && mGetUserInfoPresenter != null) {
//            mGetUserInfoPresenter.getUserInfoRequest();
//        }
    }

    @Override
    protected void onInvisible() {

    }


    private String getFunctionBean(String name) {
        for (NavigationBean navigationBean : userNavigationList) {
            if (TextUtils.equals(name, navigationBean.getName())) {
                return navigationBean.getCode();
            }
        }
        return "";
    }

    /**
     * 设置用户数据显示
     *
     * @param data
     */
    private void setShowData(UserInfoModel data) {
        if (data == null) {
            mBinding.tvNickName.setText("");
            mBinding.tvPhoneNumber.setText("");
            mBinding.imgLogo.setImageResource(R.drawable.photo_default);
            return;
        }


        mBinding.tvNickName.setText(data.getNickname());
        if (TextUtils.isEmpty(data.getMobile())) {
            if (!TextUtils.isEmpty(data.getEmail())) {
                mBinding.tvPhoneNumber.setText(data.getEmail());
            }
        } else {
            mBinding.tvPhoneNumber.setText(StringUtils.ttransformShowPhone(data.getMobile()));
        }

        ImgUtils.loadLogo(mActivity, data.getPhoto(), mBinding.imgLogo);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            showLoadingDialog();
            String path = data.getStringExtra(CameraHelper.staticPath);
            new QiNiuHelper(mActivity).uploadSinglePic(new QiNiuHelper.QiNiuCallBack() {
                @Override
                public void onSuccess(String key) {
                    updateUserPhoto(key);
                }


                @Override
                public void onFal(String info) {
                    disMissLoading();
                    ToastUtil.show(mActivity, info);
                }
            }, path);

        }
    }

    /**
     * 更新用户头像
     *
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<>();
        map.put("photo", key);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getRequestJsonString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess() && mGetUserInfoPresenter != null) {

                    mGetUserInfoPresenter.getUserInfoRequest();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 显示密码输入框
     *
     * @param type 调起dialog类型
     */
    private void showPasswordInputDialog(int type) {
        inputDialog = new TextPwdInputDialog(mActivity).builder().setTitle(getString(R.string.please_input_transaction_pwd))
                .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                    if (TextUtils.isEmpty(inputMsg)) {
                        UITipDialog.showInfoNoIcon(mActivity, getString(R.string.please_input_transaction_pwd));
                        return;
                    }

                    checkPassword(inputMsg, type);

                })
                .setNegativeBtn(getStrRes(R.string.cancel), null)
                .setContentMsg("");
        inputDialog.getContentView().setText("");
        inputDialog.getContentView().setHint(getStrRes(R.string.please_input_transaction_pwd));
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

    /**
     * 用户密码是否输入正确 （钱包密码）
     *
     * @param tradePwd
     */
    private void checkPassword(String tradePwd, int type) {

        if (!WalletHelper.checkPasswordByUserId(tradePwd, WalletHelper.WALLET_USER)) {  //用户密码输入错误
            UITipDialog.showInfoNoIcon(mActivity, getString(R.string.transaction_password_error));
            return;
        }

        switch (type) {
            case DIALOG_BACKUP:
//                BackupWalletActivity.open(this, true);
                break;

            case DIALOG_DELETE:
                deleteWallet();
                break;
        }
    }

    private void deleteWallet() {
        WalletHelper.deleteUserWallet(WalletHelper.WALLET_USER);
        UITipDialog.showSuccess(mActivity, getString(R.string.wallet_delete_success), dialogInterface -> {

            if (SPUtilHelper.isLoginNoStart()) {
                EventBus.getDefault().post(new AllFinishEvent());
                MainActivity.open(mActivity);
//                finish();
            } else {
                EventBus.getDefault().post(new AllFinishEvent());
//                GuideActivity.open(this);
//                finish();
            }
        });
    }

    /**
     * 显示确认取消dialog
     *
     * @param str
     * @param onPositiveListener
     */
    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        if (commonDialog == null) {
            commonDialog = new CommonDialog(mActivity).builder()
                    .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(str)
                    .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                    .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);
        }

        commonDialog.show();
    }


    /**
     * 更新昵称
     *
     * @param nickNameUpdate
     */
    @Subscribe
    public void nickNameUpdate(NickNameUpdate nickNameUpdate) {
        if (mBinding != null) {
            mBinding.tvNickName.setText(SPUtilHelper.getUserName());
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public void onStartGetUserInfo() {

    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        setShowData(userInfo);
    }

    @Subscribe
    public void refreshView(String msg) {
        if (TextUtils.equals(msg, "更新布局")) {
            initShowHind();
        }
    }
}
