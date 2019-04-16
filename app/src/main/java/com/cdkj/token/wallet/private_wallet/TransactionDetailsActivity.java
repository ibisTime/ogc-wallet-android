package com.cdkj.token.wallet.private_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransctionDetailsBinding;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.token.utils.AmountUtil.ALLSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getMoneyStateByState;

/**
 * 交易详情
 * Created by cdkj on 2018/6/12.
 */

public class TransactionDetailsActivity extends AbsLoadActivity {

    private ActivityTransctionDetailsBinding mBinding;

    private String coinType;

    /**
     * @param context
     * @param localCoinBill localCoinBill
     * @param coinType      币类型
     */
    public static void open(Context context, LocalCoinBill localCoinBill, String coinType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, localCoinBill);
        intent.putExtra(CdRouteHelper.DATASIGN2, coinType);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transction_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(getString(R.string.transaction_details));

        LocalCoinBill localCoinBill = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        coinType = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);

        setShowData(localCoinBill);

        mBinding.tvViewMore.setOnClickListener(view -> {

            if (localCoinBill != null) {
                if (TextUtils.equals(coinType, WalletHelper.COIN_TRX)) {
                    WebViewActivity.openURL(this, getString(R.string.transaction_details), "https://tronscan.org/#/transaction/" + localCoinBill.getTxHash());
                } else {
                    WebViewActivity.openURL(this, getString(R.string.transaction_details), WalletHelper.getBrowserUrlByCoinType(coinType) + localCoinBill.getTxHash());
                }
            }
        });
    }

    /**
     * 设置页面显示数据
     *
     * @param localCoinBill
     */
    private void setShowData(LocalCoinBill localCoinBill) {

        if (localCoinBill == null) return;

//        mBinding.imgStateIcon.setImageResource(CoinUtil.getStataIconByState(localCoinBill.getDirection()));

        if (LocalCoinDBUtils.isInState(localCoinBill.getDirection())) {
            mBinding.tvStateString.setText(R.string.withdraw);
        } else {
            mBinding.tvStateString.setText(R.string.transfer);
        }

        mBinding.tvMoney.setText(getMoneyStateByState(localCoinBill.getDirection()) + AmountUtil.transformFormatToString(localCoinBill.getValue(), coinType, ALLSCALE) + " " + coinType);

        mBinding.tvToAddress.setText(localCoinBill.getTo());
        mBinding.tvFromAddress.setText(localCoinBill.getFrom());

        if (localCoinBill.getHeight() < 0) {
            mBinding.tvBlockHeight.setText(R.string.transaction_confirm_state);
        } else {
            mBinding.tvBlockHeight.setText(localCoinBill.getHeight() + "");
        }

        if (TextUtils.equals(WalletHelper.COIN_TRX, coinType)) {
            mBinding.tvDate.setText(DateUtil.formatIntegerData(Long.parseLong(localCoinBill.getTransDatetime()), DEFAULT_DATE_FMT));
        } else {
            mBinding.tvDate.setText(DateUtil.formatStringData(localCoinBill.getTransDatetime(), DEFAULT_DATE_FMT));
        }
        mBinding.tvGas.setText(AmountUtil.transformFormatToString(localCoinBill.getTxFee(), this.coinType, ALLSCALE));
        mBinding.tvTransctionCode.setText(localCoinBill.getTxHash());


    }
}
