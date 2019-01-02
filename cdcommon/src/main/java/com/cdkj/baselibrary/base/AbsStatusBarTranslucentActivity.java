package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityAbsStatusBarBinding;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;


/**
 * 背景沉浸状态栏
 */
public abstract class AbsStatusBarTranslucentActivity extends BaseActivity {

    protected ActivityAbsStatusBarBinding mBaseBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIStatusBarHelper.translucent(this);
        UIStatusBarHelper.setStatusBarLightMode(this);

        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_abs_status_bar);

        View contentView = addContentView();
        if (contentView != null) {
            mBaseBinding.llContent.addView(contentView);
        }

        mBaseBinding.imgBack.setOnClickListener(view -> finish());

        afterCreate(savedInstanceState);
    }

    /**
     * 添加要显示的View
     */
    public abstract View addContentView();

    /**
     * activity的初始化工作
     */
    public abstract void afterCreate(Bundle savedInstanceState);

    /**
     * 设置根布局背景（由于沉浸式采用 fitsSystemWindows 方式实现，设置此背景也就是设置statusbar背景样式）
     * @param imgId
     */
    public void setPageBgRes(@DrawableRes int imgId) {
        mBaseBinding.linLayoutRoot.setBackgroundResource(imgId);
    }

    /**
     * 设置头部导航栏样式，此布局默认无色，浮于跟布局之上，设置此布局背景会导致根布局背景样式被遮挡
     * @param imgId
     */
    public void setTitleBgRes(@DrawableRes int imgId) {
        mBaseBinding.fraLayoutTitle.setBackgroundResource(imgId);
    }

    /**
     * 设置内容布局样式，此布局默认无色，浮于跟布局之上，设置此布局背景会导致根布局背景样式被遮挡
     * @param imgId
     */
    public void setContentBgRes(@DrawableRes int imgId) {
        mBaseBinding.llContent.setBackgroundResource(imgId);
    }

    public void setMidTitle(String titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

    public void setMidTitle(@StringRes int titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

    public void sheShowTitle(boolean isShow) {
        mBaseBinding.fraLayoutTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setWhiteTitle() {
        mBaseBinding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mBaseBinding.imgBack.setImageResource(R.drawable.back_white);
    }

    public void setStatusBarWhite() {
        UIStatusBarHelper.setStatusBarDarkMode(this);
    }


}
