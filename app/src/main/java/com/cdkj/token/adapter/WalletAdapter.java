package com.cdkj.token.adapter;

import android.support.annotation.Nullable;

import com.cdkj.token.R;
import com.cdkj.token.model.WalletModel;
import com.cdkj.token.utils.AmountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/12/29.
 */

public class WalletAdapter extends BaseQuickAdapter<WalletModel.AccountListBean, BaseViewHolder> {

    public WalletAdapter(@Nullable List<WalletModel.AccountListBean> data) {
        super(R.layout.item_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletModel.AccountListBean item) {

//        ImgUtils.loadImage(mContext, item.ge(), helper.getView(R.id.img_coin_logo));
        if (item.getCurrency()!=null) {

            if (item.getCurrency().toUpperCase().equals("BTC")){
                helper.setBackgroundRes(R.id.iv_symbol, R.mipmap.wallet_icon_btc);
            }

            if (item.getCurrency().toUpperCase().equals("ETH")){
                helper.setBackgroundRes(R.id.iv_symbol, R.mipmap.wallet_icon_eth);
            }
            helper.setText(R.id.tv_symbol, item.getCurrency().toUpperCase());
        }




        String froStr = mContext.getString(R.string.freeze);
        String frozenAmount = AmountUtil.toMinWithUnit(item.getFrozenAmount(), item.getCurrency(), 8);
        helper.setText(R.id.tv_frozen, froStr + frozenAmount);

        String unFroStr = mContext.getString(R.string.un_freeze);
        String unFrozenAmount = AmountUtil.toMinWithUnit(item.getAmount().subtract(item.getFrozenAmount()), item.getCurrency(), 8);
        helper.setText(R.id.tv_un_frozen, unFroStr + unFrozenAmount);

        String amount = AmountUtil.toMinWithUnit(item.getAmount(), item.getCurrency(), 8);
        helper.setText(R.id.tv_balance, amount);
    }
}
