package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.adapter.UserBankCardAdapter;
import com.cdkj.token.model.UserBankCardModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行卡列表
 * Created by 李先俊 on 2017/6/29.
 */
//TODO 签约界面也使用了获取银行卡列表接口 SigningSureActivity
public class UserBackCardActivity extends AbsRefreshListActivity {

    private boolean mIsselect;//用户打开类型是否是选择银行卡

    /**
     * @param context
     */
    public static void open(Context context, boolean isSelect) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserBackCardActivity.class);
        intent.putExtra("isSelect", isSelect);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initRefreshHelper(RefreshHelper.LIMITE);
        init();
        initOnClickListener();
    }

    private void initOnClickListener() {
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            AddBackCardActivity.open(this);
        });
    }

    private void init() {
        mBaseBinding.titleView.setMidTitle("我的银行卡");
        mBaseBinding.titleView.setRightTitle("绑定");
        if (getIntent() != null) {
            mIsselect = getIntent().getBooleanExtra("isSelect", true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserBankCardAdapter mAdapter = new UserBankCardAdapter(listData);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mIsselect) {//如果是选择银行卡
                EventBus.getDefault().post(listData.get(position));
                finish();
            } else {
                //修改银行卡
//                UpdateBackCardActivity.open(mContext, bankCardModel);
            }
        });
        return mAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        ArrayList<UserBankCardModel> list = new ArrayList<>();
        list.add(new UserBankCardModel());
        list.add(new UserBankCardModel());
        list.add(new UserBankCardModel());
        mRefreshHelper.setData(list, "暂无银行卡", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshHelper.onDefaluteMRefresh(true);
    }


}
