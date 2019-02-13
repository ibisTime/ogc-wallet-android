package com.cdkj.token.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.OrderListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.OrderListModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

public class OrderListActivity extends AbsRefreshListActivity<OrderListModel.ListBean> {

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
        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.OrderListTitle));
        initRefreshHelper(RefreshHelper.LIMITE);


        mRefreshHelper.setOnRefreshLoadmoreListener(new RefreshHelper.OnMyRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //刷新前 先停止所有的倒计时
                OrderListAdapter adapter = (OrderListAdapter) mRefreshHelper.getmAdapter();
                adapter.stopTimeDow();
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                OrderListAdapter adapter = (OrderListAdapter) mRefreshHelper.getmAdapter();
                adapter.stopTimeDow();
            }
        });

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List<OrderListModel.ListBean> listData) {
        OrderListAdapter adapter = new OrderListAdapter(listData);
        TextView textView = new TextView(this);
        textView.setWidth(DisplayHelper.getScreenWidth(this));
        textView.setHeight(DisplayHelper.dpToPx(10));
        textView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        //添加一个分割线再头部
        adapter.addHeaderView(textView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            OrderListModel.ListBean orderListModel = listData.get(position);
            OrderDetailsActivity.open(this, orderListModel.getCode());
        });
        return adapter;
    }


    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        getData(pageindex, limit, isShowDialog);
    }

    private void getData(int pageindex, int limit, boolean isShowDialog) {
        HashMap<String, String> map = new HashMap<>();
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());
        Call<BaseResponseModel<OrderListModel>> ordetList = RetrofitUtils.createApi(MyApi.class).getOrdetList("625287", StringUtils.getRequestJsonString(map));
        addCall(ordetList);
        if (isShowDialog) {
            showLoadingDialog();
        }
        ordetList.enqueue(new BaseResponseModelCallBack<OrderListModel>(this) {
            @Override
            protected void onSuccess(OrderListModel data, String SucMessage) {
                if (data == null)
                    return;
                mRefreshHelper.setData(data.getList(), getString(R.string.OrderListTitleEmpty), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    @Subscribe
    public void refreshList(String msg) {
        if (TextUtils.equals(msg, "刷新订单")) {
            OrderListAdapter adapter = (OrderListAdapter) mRefreshHelper.getmAdapter();
            adapter.stopTimeDow();
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }
}
