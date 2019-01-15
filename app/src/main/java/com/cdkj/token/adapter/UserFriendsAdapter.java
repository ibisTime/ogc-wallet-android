package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.UserFriendsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class UserFriendsAdapter extends BaseQuickAdapter<UserFriendsModel, BaseViewHolder> {


    public UserFriendsAdapter(@Nullable List<UserFriendsModel> data) {
        super(R.layout.item_user_friends, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFriendsModel item) {


    }
}
