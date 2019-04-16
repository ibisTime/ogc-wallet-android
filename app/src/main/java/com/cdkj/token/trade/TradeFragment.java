package com.cdkj.token.trade;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentTradeManageBinding;
import com.cdkj.token.model.TradeTypeBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeFragment extends BaseLazyFragment {

    private FragmentTradeManageBinding mBinding;
    private int position;
    List<Fragment> mList = new ArrayList<>();
    private List<String> titleString = new ArrayList<>();

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static TradeFragment getInstance() {
        TradeFragment fragment = new TradeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_trade_manage, null, false);

        getData();
        initListener();
        return mBinding.getRoot();
    }

    private void getData() {
        showLoadingDialog();
        Call<BaseResponseListModel<TradeTypeBean>> tradeType = RetrofitUtils.createApi(MyApi.class).getTradeType("802007", "{}");
        tradeType.enqueue(new BaseResponseListCallBack<TradeTypeBean>(mActivity) {
            @Override
            protected void onSuccess(List<TradeTypeBean> data, String SucMessage) {
                initFrragment(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void initFrragment(List<TradeTypeBean> data) {
        for (TradeTypeBean bean : data) {
            if (TextUtils.equals("1", bean.getIsAccept())) {
                titleString.add(bean.getSymbol());
                mBinding.tlTab.addTab(mBinding.tlTab.newTab().setText(bean.getSymbol()));
                mList.add(TradeFragmentCommon.getInstance(bean.getSymbol()));
            }
        }
        initTabAndViewpager();
    }

    private void initListener() {

        mBinding.tvBill.setOnClickListener(view -> {
            if (position == 0) {
                //btc 账单
                OrderListActivity.open(mActivity);
            } else {
                //usdt  账单
                OrderListActivity.open(mActivity);
            }
        });

    }

    private void initTabAndViewpager() {


        ViewPagerAdapter mAdapter = new ViewPagerAdapter(getFragmentManager(), mList, titleString);
        mBinding.viewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mBinding.tlTab.setupWithViewPager(mBinding.viewPager);
        mBinding.tlTab.setTabsFromPagerAdapter(mAdapter);

        mBinding.tlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                LogUtil.E("当前索引为" + position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
