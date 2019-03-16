package com.cdkj.token.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.interfaces.StartPagePresenter;
import com.cdkj.token.interfaces.StartPageView;
import com.cdkj.token.model.VersionModel;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

import static com.cdkj.token.utils.UpdateUtil.isForceUpload;
import static com.cdkj.token.utils.UpdateUtil.startWeb;

/**
 * 启动页
 */
@Route(path = CdRouteHelper.APPSTART)
public class StartActivity extends BaseActivity implements StartPageView {


    public StartPagePresenter pagePresenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity
        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_start);

//        WalletHelper.getPastBtcAddress();

        pagePresenter = new StartPagePresenter(this);
        pagePresenter.start();

        int width = DisplayHelper.getScreenWidth(this);
        Log.e("width", width + "px");
        Log.e("width", DisplayHelper.px2dp(this, width) + "dp");

    }


    /**
     * 获取导航配置失败
     */
    @Override
    public void onNavigationError() {

//        ToastUtil.show(this,"获取配置失败");

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle("获取配置失败是否重新获取")
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), view -> {
                    //点击确定重新获取
                    if (pagePresenter != null)
                        pagePresenter.start();
                })
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), view -> {
                    //点击取消  退出app
//                    CdRouteHelper.openLogin(true);
                    EventBus.getDefault().post(new AllFinishEvent());
                }, false);

        commonDialog.show();

    }

    @Override
    public void addNetCall(Call call) {
        addCall(call);
    }

    @Override
    public void versionUpdateDialog(VersionModel versionModel) {
        if (isForceUpload(versionModel.getForceUpdate())) { // 强制更新
            showSureDialog(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                finish();
            });

        } else {
            showDoubleWarnListen(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
            }, view -> {
                if (pagePresenter != null) {
                    pagePresenter.refuseUpdate();
                }
            });
        }
    }

    @Override
    public void startMain() {
        MainActivity.open(this);
        finish();
    }

    @Override
    public void startLogin() {
        SignInActivity.open(this, true);
        finish();
    }


    @Override
    protected void onDestroy() {
        if (pagePresenter != null) {
            pagePresenter.clear();
            pagePresenter = null;
        }
        super.onDestroy();
    }

    /**
     * 显示确认取消弹框
     *
     * @param title
     * @param content
     * @param onPositiveListener
     * @param onNegativeListener
     */
    protected void showDoubleWarnListen(String title, String content, CommonDialog.OnPositiveListener onPositiveListener, CommonDialog.OnNegativeListener onNegativeListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title).setContentMsg(content)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), onNegativeListener, false);

        commonDialog.show();
    }

    /**
     * 只显示确认弹框的按钮
     *
     * @param title
     * @param str
     * @param onPositiveListener
     */
    protected void showSureDialog(String title, String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (this == null || isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title)
                .setContentMsg(str)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener);

        commonDialog.show();
    }

}
