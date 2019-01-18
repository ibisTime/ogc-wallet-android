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
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogPayMessageBinding;
import com.cdkj.token.databinding.FragmentTradeBinding;
import com.cdkj.token.model.UserBankCardModel;
import com.cdkj.token.user.UserBackCardActivity;
import com.cdkj.token.utils.MPChartUtils;
import com.cdkj.token.utils.StringUtil;
import com.cdkj.token.views.dialogs.PasswordInputDialog;
import com.github.mikephil.charting.data.Entry;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_trade, null, false);

        initLineChart();
        initClickListener();
        return mBinding.getRoot();

    }

    /**
     * 初始化图表  设置图表的样式
     */
    private void initLineChart() {
        MPChartUtils.setLineChartStyle(mBinding.lineChart);
        initLineChartData();
    }

    private void initLineChartData() {

        /**
         * Entry 坐标点对象  构造函数 第一个参数为x点坐标 第二个为y点
         *
         * 以为此图x轴的坐标不显示  所以 只要按照后一个数值比前一个大的顺序即可 例如x轴的数据可为0,1,2,3  但不可为  0,1,2,5,3,2不可为这样的
         * 可以设置多条折线
         */
        ArrayList<Entry> values1 = new ArrayList<>();
        values1.add(new Entry(0, 20));
        values1.add(new Entry(6, 15));
        values1.add(new Entry(9, 50));
        values1.add(new Entry(12, 5));
        values1.add(new Entry(15, 100));

        MPChartUtils.setLineChartData(mBinding.lineChart, values1);
    }

    private void initClickListener() {
        //账单
        mBinding.tvBill.setOnClickListener(view -> OrderListActivity.open(mActivity));
        mBinding.llSelectBank.setOnClickListener(v -> UserBackCardActivity.open(mActivity, true));
//        mBinding.llSlectBank.setOnClickListener(view -> OrderListActivity.open(mActivity));
        //提交按钮
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
                    PasswordInputDialog.build(mActivity)
                            .setView("买入BTC", "200BTC")
                            .setOnNegativeListener(null)
                            .setOnPositiveListener((view2, psaaword) -> {
                                ToastUtil.show(mActivity, "密码为:" + psaaword);
                            }).show();
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

    /**
     * 选择银行卡的回调
     *
     * @param userBankCardModel
     */
    @Subscribe
    public void selectBank(UserBankCardModel.ListBean userBankCardModel) {
        if (userBankCardModel == null)
            return;
        mBinding.tvPayName.setText(userBankCardModel.getBankName());
    }

}