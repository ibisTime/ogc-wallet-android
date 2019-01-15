package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.adapter.UserFriendsAdapter;
import com.cdkj.token.model.UserFriendsModel;

import java.util.ArrayList;
import java.util.List;

public class UserFriendsActivity extends AbsRefreshListActivity {

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserFriendsActivity.class));
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我的好友");
        initRefreshHelper(RefreshHelper.LIMITE);

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return new UserFriendsAdapter(listData);
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        ArrayList<UserFriendsModel> data = new ArrayList<>();
        data.add(new UserFriendsModel());
        data.add(new UserFriendsModel());
        data.add(new UserFriendsModel());
        mRefreshHelper.setData(data, "暂无好友", 0);
    }
}
