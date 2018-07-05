package com.cdkj.token.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.model.BillModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_DAY;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_HM;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_M;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_YM;
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by lei on 2017/8/22.
 */

public class BillListAdapter extends BaseQuickAdapter<BillModel.ListBean, BaseViewHolder> {

    List<BillModel.ListBean> list;

    public BillListAdapter(@Nullable List<BillModel.ListBean> data) {
        super(R.layout.item_bill, data);
    }

    @NonNull
    @Override
    public List<BillModel.ListBean> getData() {
        return super.getData();
    }

    @Override
    protected void convert(BaseViewHolder helper, BillModel.ListBean item) {
        if (list == null) {
            list = getData();
        }

        // 当itemPosition为0时，展示日期
        if (helper.getLayoutPosition() == 1) {
            helper.setGone(R.id.ll_ym, true);
            helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(), DATE_YM));
        } else { // 当itemPosition不为0但当前item的日期与上一个item不相同时，展示日期，否则不展示
            String month_now = DateUtil.formatStringData(item.getCreateDatetime(), DATE_M);
            String month_last = DateUtil.formatStringData(list.get(helper.getLayoutPosition() - 1).getCreateDatetime(), DATE_M);

            if (!month_now.equals(month_last)) {
                helper.setGone(R.id.ll_ym, true);
                helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(), DATE_YM));
            } else {
                helper.setGone(R.id.ll_ym, false);
            }
        }

        helper.setText(R.id.tv_day, DateUtil.formatStringData(item.getCreateDatetime(), DATE_DAY));
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(), DATE_HM));

        helper.setText(R.id.tv_remark, item.getBizNote());
        helper.setText(R.id.tv_currency, item.getCurrency());

        BigDecimal tas = new BigDecimal(item.getTransAmountString());
        int i = tas.compareTo(BigDecimal.ZERO);
        if (i == 1) {
            helper.setText(R.id.tv_amount, "+" + AccountUtil.amountFormatUnit(tas, item.getCurrency(), 8));
        } else {
            helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(tas, item.getCurrency(), 8));
        }

        ImageView ivType = helper.getView(R.id.iv_type);
        if (item.getKind().equals("0")) { // 非冻结流水

            switch (item.getBizType()) {
                case "charge": // 充值
                case "o2o_in": // o2o店铺消费收入
                case "invite": // 推荐好友分成
                    ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 2), ivType);
                    break;

                case "withdraw": // 取现
                case "tradefee": // 手续费
                case "withdrawfee": // 手续费
                case "o2o_out": // o2o店铺消费支出
                    ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 3), ivType);
                    break;
                case "redpacket_back": // 红包退回
                    ivType.setImageResource(R.drawable.money_in);
                    break;
                case "sendredpacket_out": // 发红包
                    ivType.setImageResource(R.drawable.money_out);
                    break;
                case "sendredpacket_in": // 抢红包
                    ivType.setImageResource(R.drawable.money_in);
                    break;
            }

        } else { // 冻结流水

            if (item.getTransAmountString().contains("-")) { // 金额是负数
                ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 3), ivType);
            } else {
                ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 2), ivType);
            }

        }

    }


}
