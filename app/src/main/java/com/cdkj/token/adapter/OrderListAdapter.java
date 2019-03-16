package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.model.OrderListModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by cdkj on 2018/5/25.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderListModel.ListBean, BaseViewHolder> {

    ArrayList<Disposable> disposableList = new ArrayList<>();

    public OrderListAdapter(@Nullable List<OrderListModel.ListBean> data) {
        super(R.layout.item_order_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListModel.ListBean item) {
        helper.setText(R.id.tv_date, DateUtil.format(item.getCreateDatetime(), DateUtil.DEFAULT_DATE_FMT));
        if (TextUtils.equals("0", item.getType())) {
            helper.setText(R.id.tv_name, "买入-"+item.getTradeCoin());
        } else {
            helper.setText(R.id.tv_name, "卖出-"+item.getTradeCoin());
        }
        setTypeView(helper.getView(R.id.tv_remaining_time), helper.getView(R.id.tv_type), helper.getView(R.id.iv_type), item);
    }

    //   待支付   已完成   已取消    超时
    //0=待支付 1=已支付 2=已释放 3=已取消  4=平台取消  5=超时

    private void setTypeView(TextView tvTime, TextView tvType, ImageView iv, OrderListModel.ListBean item) {
        switch (item.getStatus()) {
            case "0":
//                Date startTime = new Date();
//                Date endTime = new Date(item.getInvalidDatetime());
//                long startLong = startTime.getTime();
//                long endLong = endTime.getTime();
//                int remainingTime = (int) ((endLong - startLong) / 1000);
//                //因为是取的本地时间,可能会出现本地日期  小于开始的如期
//                if (remainingTime <= 0) {
//                    tvTime.setText("本地日期不正确");
//                } else {
//
//                }
                Disposable disposable = AppUtils.startTimeDown(item.getRemainTime(), tvTime, view -> {
                    item.setStatus("5");
                    stopTimeDow();
                    this.notifyDataSetChanged();
                });
                disposableList.add(disposable);
                //买的话是 待支付,  卖的话是 待确定
                if (TextUtils.equals("0", item.getType())) {
                    tvType.setText(R.string.to_be_paid);
                    iv.setImageResource(R.mipmap.icon_pay_loding);
                } else {
                    tvType.setText(R.string.to_be_confirmed);
                    iv.setImageResource(R.mipmap.icon_pay_to_be_confirmed);
                }
                tvType.setTextColor(Color.parseColor("#0EC55B"));
                tvTime.setTextColor(Color.parseColor("#0EC55B"));
                break;
            case "1":

                BigDecimal btcNumber1 = BigDecimalUtils.div(item.getCount(), LocalCoinDBUtils.getLocalCoinUnit(item.getTradeCoin()), 8);
                tvTime.setText(TextUtils.equals("0", item.getType()) ? ("+" + btcNumber1)+"-"+item.getTradeCoin() : ("-" + btcNumber1)+"-"+item.getTradeCoin());
                tvType.setText("已支付");
                tvType.setTextColor(Color.parseColor("#D53D3D"));
                tvTime.setTextColor(Color.parseColor("#333333"));
                iv.setImageResource(R.mipmap.icon_pay_success);
                break;
            case "2":
                //买入是增加  卖出是减少
                BigDecimal btcNumber = BigDecimalUtils.div(item.getCount(), LocalCoinDBUtils.getLocalCoinUnit(item.getTradeCoin()), 8);
                tvTime.setText(TextUtils.equals("0", item.getType()) ? ("+" + btcNumber+"-"+item.getTradeCoin()) : ("-" + btcNumber+"-"+item.getTradeCoin()));
                tvType.setText(R.string.completed);
                tvType.setTextColor(Color.parseColor("#D53D3D"));
                tvTime.setTextColor(Color.parseColor("#333333"));
                iv.setImageResource(R.mipmap.icon_pay_success);
                break;
            case "3":
            case "4":
                tvTime.setText(R.string.user_order_cancel);
                tvType.setText(R.string.order_cancel);
                tvType.setTextColor(Color.parseColor("#999999"));
                tvTime.setTextColor(Color.parseColor("#999999"));
                iv.setImageResource(R.mipmap.icon_pay_cancel);
                break;
            case "5":
                tvTime.setText(R.string.order_time_out);
                tvType.setText(R.string.order_cancel);
                tvType.setTextColor(Color.parseColor("#999999"));
                tvTime.setTextColor(Color.parseColor("#999999"));
                iv.setImageResource(R.mipmap.icon_pay_timeout);
                break;
            default:
                tvTime.setText("");
                tvType.setText("");
                tvType.setTextColor(Color.parseColor("#999999"));
                tvTime.setTextColor(Color.parseColor("#999999"));
                iv.setImageResource(R.mipmap.icon_pay_cancel);
        }
    }

    /**
     * 刷新数据前一定要调用
     * 停止所有的倒计时
     */
    public void stopTimeDow() {
        if (disposableList != null && disposableList.size() > 0) {
            for (int i = 0; i < disposableList.size(); i++) {
                Disposable disposable = disposableList.get(0);
                disposable.dispose();
                disposable = null;
                disposableList.remove(i);
            }
        }
    }
}
