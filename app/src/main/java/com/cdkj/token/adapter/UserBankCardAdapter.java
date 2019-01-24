package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.UserBankCardModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class UserBankCardAdapter extends BaseQuickAdapter<UserBankCardModel.ListBean, BaseViewHolder> {


    public UserBankCardAdapter(@Nullable List<UserBankCardModel.ListBean> data) {
        super(R.layout.item_my_bank_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBankCardModel.ListBean item) {


        if (TextUtils.equals("1", item.getIsDefault())) {
            //默认银行卡
            helper.setChecked(R.id.cb_default, true);
        } else {
            helper.setChecked(R.id.cb_default, false);
        }


        helper.setText(R.id.txt_name, item.getBankName());
        helper.setText(R.id.txt_number, item.getBankName());
        if (!TextUtils.isEmpty(item.getBankcardNumber()) && item.getBankcardNumber().length() > 5) {
            helper.setText(R.id.txt_number, item.getBankcardNumber().substring(item.getBankcardNumber().length() - 4, item.getBankcardNumber().length()));
        } else {
            helper.setText(R.id.txt_number, item.getBankcardNumber());
        }

        if (TextUtils.isEmpty(item.getBackground())) {
            ImgUtils.loadImage(mContext, R.mipmap.back_default, helper.getView(R.id.img_back_bg));
        } else {
            ImgUtils.loadImage(mContext, item.getBackground(), helper.getView(R.id.img_back_bg));
        }
        if (TextUtils.isEmpty(item.getIcon())) {
            ImgUtils.loadCircleImg(mContext, R.mipmap.logo_defalut, helper.getView(R.id.img_bankCart));
        } else {
            ImgUtils.loadImage(mContext, item.getIcon(), helper.getView(R.id.img_bankCart));
        }

        helper.addOnClickListener(R.id.cb_default);
        helper.addOnClickListener(R.id.tv_untying);
    }
}
