package com.cdkj.token.trade;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentTradeManageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TradeFragment extends BaseLazyFragment {

    private FragmentTradeManageBinding mBinding;
    private int position;
    List<Fragment> mList = new ArrayList<>();
    private List<String> titleString;

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


        mList.add(TradeFragmentBTC.getInstance());
        mList.add(TradeFragmentUSDT.getInstance());
        initTabAndViewpager();

        initListener();

        return mBinding.getRoot();
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
        titleString = new ArrayList<>();
        titleString.add("BTC");
        titleString.add("USDT");
        mBinding.tlTab.addTab(mBinding.tlTab.newTab().setText("BTC"));
        mBinding.tlTab.addTab(mBinding.tlTab.newTab().setText("USDT"));

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


//    public TradeFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_trade_manage, container, false);
//    }

}
