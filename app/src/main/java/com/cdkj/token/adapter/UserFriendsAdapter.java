package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.UserFriendsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class UserFriendsAdapter extends BaseQuickAdapter<UserFriendsModel.ListBean, BaseViewHolder> {


    public UserFriendsAdapter(@Nullable List<UserFriendsModel.ListBean> data) {
        super(R.layout.item_user_friends, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserFriendsModel.ListBean item) {
        helper.setText(R.id.tv_name, item.getLoginName());
        helper.setText(R.id.tv_time, DateUtil.format(item.getCreateDatetime(),DateUtil.DEFAULT_DATE_FMT));
        ImageView ivHead = helper.getView(R.id.iv_head);
        if (TextUtils.isEmpty(item.getPhoto())) {
            ivHead.setImageResource(R.drawable.photo_default);
        } else {
            ImgUtils.loadLogo(mContext, item.getPhoto(), ivHead);
        }
    }
}
