package com.cdkj.token.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/25.
 */

public class WalletBalanceAdapter extends BaseQuickAdapter<WalletBalanceModel, BaseViewHolder> {

    public WalletBalanceAdapter(@Nullable List<WalletBalanceModel> data) {
        super(R.layout.item_coin_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBalanceModel item) {


        if (TextUtils.equals(item.getAddress(), SPUtilHelper.getPastBtcInfo().split("\\+")[0])) {
            // 老BTC地址需要加上老版本标示
            helper.setText(R.id.tv_coin_name, item.getCoinSymbol() + "(old version)");
        } else {
            helper.setText(R.id.tv_coin_name, item.getCoinSymbol());
        }

        String availablemountString = AmountUtil.transformFormatToString(item.getAvailableAmount(), item.getCoinSymbol(), 8) + " " + item.getCoinSymbol();

        helper.setText(R.id.tv_amount, availablemountString);

        ImgUtils.loadImage(mContext, item.getCoinImgUrl(), helper.getView(R.id.img_coin_logo));

        helper.setText(R.id.tv_market_price, getMarketPriceString(item));

        helper.setText(R.id.tv_amount_cny, getAmountString(item));
        helper.addOnClickListener(R.id.ll_item);
//        ll_item

    }

    public String getMarketPriceString(WalletBalanceModel item) {
        String priceString = item.getLocalMarketPrice();
        return priceString + SPUtilHelper.getLocalMarketSymbol();
    }

    public String getAmountString(WalletBalanceModel item) {
        String priceString = item.getLocalAmount();
        return priceString + SPUtilHelper.getLocalMarketSymbol();
    }

}
