package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.UserBankCardModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class UserBankCardAdapter extends BaseQuickAdapter<UserBankCardModel, BaseViewHolder> {


    public UserBankCardAdapter(@Nullable List<UserBankCardModel> data) {
        super(R.layout.item_my_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBankCardModel item) {
        int logoId = R.mipmap.logo_defalut;
        int backId = R.mipmap.back_default;

//        logoId = mContext.getResources().getIdentifier("logo_" + "ABC", "mipmap", mContext.getPackageName());
//        backId = mContext.getResources().getIdentifier("back_" + "ABC", "mipmap", mContext.getPackageName());

        ImgUtils.loadCircleImg(mContext, logoId, helper.getView(R.id.img_bankCart));
        ImgUtils.loadImage(mContext, backId, helper.getView(R.id.img_back_bg));

    }
}
