package com.cdkj.token.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.OrderListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by cdkj on 2018/5/25.
 */

public class OrderListAdapter extends BaseQuickAdapter<OrderListModel, BaseViewHolder> {

    ArrayList<Disposable> disposableList = new ArrayList<>();

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
//                tvTime.setText("剩余付款时间：9分30秒");
                Disposable disposable = AppUtils.startTimeDown(10, tvTime, view -> {
                    item.setType(3);
                    stopTimeDow();
                    this.notifyDataSetChanged();
                });
                disposableList.add(disposable);
                tvType.setText(R.string.to_be_paid);
                tvType.setTextColor(Color.parseColor("#0EC55B"));
                tvTime.setTextColor(Color.parseColor("#0EC55B"));
                iv.setImageResource(R.mipmap.icon_pay_loding);
                break;
            case 1:
                tvTime.setText(R.string.user_order_cancel);
                tvType.setText(R.string.order_cancel);
                tvType.setTextColor(Color.parseColor("#999999"));
                tvTime.setTextColor(Color.parseColor("#999999"));
                iv.setImageResource(R.mipmap.icon_pay_cancel);
                break;
            case 2:
                tvTime.setText("-0.089874");
                tvType.setText(R.string.completed);
                tvType.setTextColor(Color.parseColor("#D53D3D"));
                tvTime.setTextColor(Color.parseColor("#333333"));
                iv.setImageResource(R.mipmap.icon_pay_success);
                break;
            case 3:
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
            for (Disposable disposable : disposableList) {
                disposable.dispose();
            }
        }
    }
}
