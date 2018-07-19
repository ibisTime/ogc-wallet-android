package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserLanguageBinding;

import org.greenrobot.eventbus.EventBus;

import static com.cdkj.baselibrary.appmanager.MyConfig.ENGLISH;
import static com.cdkj.baselibrary.appmanager.MyConfig.KOREA;
import static com.cdkj.baselibrary.appmanager.MyConfig.SIMPLIFIED;
import static com.cdkj.baselibrary.appmanager.MyConfig.getUserLanguageLocal;

/**
 * 语言设置
 * Created by lei on 2017/12/7.
 */

public class UserLanguageActivity extends AbsStatusBarTranslucentActivity {

    private ActivityUserLanguageBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserLanguageActivity.class));
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_language, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(getStrRes(R.string.user_title_language));
        setPageBgImage(R.drawable.my_bg);
        init();
        initListener();
    }

    private void init() {
        setView(SPUtilHelper.getLanguage());
    }

    private void initListener() {
        //简体中午
        mBinding.llSimple.setOnClickListener(view -> {
            initView();
            mBinding.ivSimple.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(SIMPLIFIED);
        });

        mBinding.llEnglish.setOnClickListener(view -> {
            initView();
            mBinding.ivEnglish.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(ENGLISH);
        });

        mBinding.llKorea.setOnClickListener(v -> {
            initView();
            mBinding.ivKorea.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(KOREA);
        });

    }

    private void sendEventBusAndFinishAll(String language) {
        SPUtilHelper.saveLanguage(language);
        EventBus.getDefault().post(new AllFinishEvent());
        AppUtils.setAppLanguage(this, getUserLanguageLocal());   //设置语言
        MainActivity.open(this);
        finish();
    }

    private void initView() {
        mBinding.ivSimple.setVisibility(View.GONE);
        mBinding.ivEnglish.setVisibility(View.GONE);
        mBinding.ivKorea.setVisibility(View.GONE);
    }

    private void setView(String language) {
        initView();
        switch (language) {
            case SIMPLIFIED:
                mBinding.ivSimple.setVisibility(View.VISIBLE);
                break;
            case ENGLISH:
                mBinding.ivEnglish.setVisibility(View.VISIBLE);
                break;
            case KOREA:
                mBinding.ivKorea.setVisibility(View.VISIBLE);
                break;
        }
    }

}
