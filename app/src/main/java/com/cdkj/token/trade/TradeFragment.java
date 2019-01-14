package com.cdkj.token.trade;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogPayMessageBinding;
import com.cdkj.token.databinding.FragmentTradeBinding;
import com.cdkj.token.utils.StringUtil;

/**
 * Created by cdkj on 2018/12/27.
 */

public class TradeFragment extends BaseLazyFragment {

    private FragmentTradeBinding mBinding;
    private int position;//0是  买入  1是卖出

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
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_trade, null, false);
        initClickListener();
        return mBinding.getRoot();

    }

    private void initClickListener() {
        mBinding.btnConfirm.setOnClickListener(view -> {

            if (position == 0) {
                DialogPayMessageBinding dialogView = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.dialog_pay_message, null, false);
                AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
                dialog.setView(dialogView.getRoot());
                dialog.show();
                //设置弹窗里面的 文字高亮显示
                dialogView.tvMessage1.setText(StringUtil.highlight(mActivity, "请在10分钟内完成付款", "10分钟", "#4064E6", 0, 0));
                dialogView.tvMessage2.setText(StringUtil.highlight(mActivity, "转账请务必填写转账附信", "转账附信", "#4064E6", 0, 0));
                dialogView.tvConfirm.setOnClickListener(view1 -> {
                    dialog.dismiss();
                    OrderListActivity.open(mActivity);
                });
            }
        });

        mBinding.tlWay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 0) {
                    mBinding.tvAvailableAssetsTitle.setVisibility(View.GONE);
                    mBinding.tvAvailableAssets.setVisibility(View.GONE);
                    mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mBinding.btnConfirm.setText("买入BTC");
                } else {
                    mBinding.tvAvailableAssetsTitle.setVisibility(View.VISIBLE);
                    mBinding.tvAvailableAssets.setVisibility(View.VISIBLE);
                    mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.indicator_yealo_color));
                    mBinding.btnConfirm.setText("卖出BTC");

                }
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
