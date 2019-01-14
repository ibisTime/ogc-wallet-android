package com.cdkj.token.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.adapter.OrderListAdapter;
import com.cdkj.token.model.OrderListModel;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AbsRefreshListActivity<OrderListModel> {

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, OrderListActivity.class));
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("订单记录");
        initRefreshHelper(RefreshHelper.LIMITE);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List<OrderListModel> listData) {
        OrderListAdapter adapter = new OrderListAdapter(listData);
        TextView textView = new TextView(this);
        textView.setWidth(DisplayHelper.getScreenWidth(this));
        textView.setHeight(DisplayHelper.dpToPx(10));
        textView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        //添加一个分割线再头部
        adapter.addHeaderView(textView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            OrderListModel orderListModel = listData.get(position);
            OrderDetailsActivity.open(this, orderListModel);
        });
        return adapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        ArrayList dataList = new ArrayList<OrderListModel>();
        OrderListModel orderListModel0 = new OrderListModel();
        OrderListModel orderListModel1 = new OrderListModel();
        OrderListModel orderListModel2 = new OrderListModel();
        OrderListModel orderListModel3 = new OrderListModel();
        orderListModel0.setType(0);
        orderListModel1.setType(1);
        orderListModel2.setType(2);
        orderListModel3.setType(3);
        dataList.add(orderListModel0);
        dataList.add(orderListModel1);
        dataList.add(orderListModel2);
        dataList.add(orderListModel3);
        mRefreshHelper.setData(dataList, "暂无订单数据", 0);
    }

}
