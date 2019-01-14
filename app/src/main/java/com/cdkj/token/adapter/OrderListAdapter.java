package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.token.R;
import com.cdkj.token.model.OrderListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/25.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderListModel, BaseViewHolder> {


    public OrderListAdapter(@Nullable List<OrderListModel> data) {
        super(R.layout.item_order_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListModel item) {
//        helper
        helper.setText(R.id.tv_date, "2018-12-12 12:30");
        helper.setText(R.id.tv_name, "ETC");
        setTypeView(helper.getView(R.id.tv_remaining_time), helper.getView(R.id.tv_type), helper.getView(R.id.iv_type), item);
    }

    //0  待支付 1 已取消 2 已完成  3 超时
    //
    private void setTypeView(TextView tvTime, TextView tvType, ImageView iv, OrderListModel item) {
        switch (item.getType()) {
            case 0:
                tvTime.setText("剩余付款时间：9分30秒");
                tvType.setText("待支付");
                tvType.setTextColor(Color.parseColor("#0EC55B"));
                tvTime.setTextColor(Color.parseColor("#0EC55B"));
                iv.setImageResource(R.mipmap.icon_pay_loding);
                break;
            case 1:
                tvTime.setText("用户取消订单");
                tvType.setText("已取消");
                tvType.setTextColor(Color.parseColor("#999999"));
                tvTime.setTextColor(Color.parseColor("#999999"));
                iv.setImageResource(R.mipmap.icon_pay_cancel);
                break;
            case 2:
                tvTime.setText("-0.089874");
                tvType.setText("已完成");
                tvType.setTextColor(Color.parseColor("#D53D3D"));
                tvTime.setTextColor(Color.parseColor("#333333"));
                iv.setImageResource(R.mipmap.icon_pay_success);
                break;
            case 3:
                tvTime.setText("订单超时");
                tvType.setText("已取消");
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
}
