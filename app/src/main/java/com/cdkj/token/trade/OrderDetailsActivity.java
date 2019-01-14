package com.cdkj.token.trade;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityOrderDetailsBinding;
import com.cdkj.token.model.OrderListModel;

public class OrderDetailsActivity extends AbsLoadActivity {
    private ActivityOrderDetailsBinding mBinding;
    private OrderListModel orderListModel;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, OrderListModel data) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, data);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_order_details, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        init();
        setViewData();

        initOnClickListener();
    }

    private void initOnClickListener() {
        mBinding.ivBack.setOnClickListener(v -> finish());
        mBinding.tvFuyanCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvFuyan.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvOnekeyCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, "一键复制");
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvPayeeCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvPayee.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankNumberCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankNumber.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankNameCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankName.getText().toString());
            ToastUtil.show(this, "复制成功");
        });
        mBinding.tvBankBranchCopy.setOnClickListener(v -> {
            SystemUtils.copy(this, mBinding.tvBankBranch.getText().toString());
            ToastUtil.show(this, "复制成功");
        });

        mBinding.tvTitleRight.setOnClickListener(v -> {
            ToastUtil.show(this, "取消订单");
        });

        mBinding.btnConfirm.setOnClickListener(v -> {
//            orderListModel
            if (orderListModel.getPayType() == 0) {
                orderListModel.setPayType(1);
            } else {
                orderListModel.setPayType(0);
            }
            setViewData();
        });


    }

    private void init() {
        if (getIntent() != null) {
            orderListModel = (OrderListModel) getIntent().getSerializableExtra(CdRouteHelper.DATASIGN);
        }
    }

    private void setViewData() {

        //0  待支付 1 已取消 2 已完成  3 超时
        switch (orderListModel.getType()) {
            case 0:
//                mBinding.ivType.setImageResource(R.mipmap.icon_pay_loding);
//                mBinding.tvType.setText("待支付");

                //待支付是支付宝  还是银行卡
                mBinding.llType0.setVisibility(View.VISIBLE);
                mBinding.llType3.setVisibility(View.GONE);
                mBinding.tvTitleRight.setVisibility(View.VISIBLE);
                //0  是支付宝  1是银行卡
                if (orderListModel.getPayType() == 0) {
                    mBinding.ivQr.setVisibility(View.VISIBLE);
                    mBinding.llBack.setVisibility(View.GONE);
                    mBinding.ivPayLogo.setImageResource(R.mipmap.icon_ali_logo);
                    mBinding.tvPayName.setText("支付宝");
                } else {
                    mBinding.ivQr.setVisibility(View.GONE);
                    mBinding.llBack.setVisibility(View.VISIBLE);
                    mBinding.ivPayLogo.setImageResource(R.mipmap.icon_pay_bank_logo);
                    mBinding.tvPayName.setText("银行卡转账");
                }

                break;
            case 1:
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_cancel);
                mBinding.tvType.setText("已取消");

                break;
            case 2:
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_success);
                mBinding.tvType.setText("已完成");

                break;
            case 3:
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_timeout);
                mBinding.tvType.setText("已超时");

                break;
            default:
                mBinding.ivType.setImageResource(R.mipmap.icon_pay_cancel);
                mBinding.tvType.setText("");
        }

    }


}
