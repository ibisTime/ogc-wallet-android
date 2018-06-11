package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityTransferBinding;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.pop.GasTypeChoosePop;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.WalletHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包转账
 * Created by cdkj on 2018/6/8.
 */

public class WalletTransferActivity extends AbsBaseLoadActivity {

    private ActivityTransferBinding mBinding;

    private final int CODEPERSE = 101;

    private int chooseGasType = GasTypeChoosePop.ORDINARY;//矿工费类型 默认普通

    private BigInteger gasPrice;//矿工费用
    private BigInteger transferGasPrice;//计算后转账矿工费用

    private BalanceListModel.AccountListBean accountListBean;

    public static void open(Context context, BalanceListModel.AccountListBean accountListBean) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletTransferActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, accountListBean);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (accountListBean != null) {
            mBinding.tvCurrency.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getBalance()), 8) + " " + accountListBean.getSymbol());
            mBaseBinding.titleView.setMidTitle(accountListBean.getSymbol());
        }

        gasPrice = WalletHelper.getGasLimit();
        transferGasPrice = WalletHelper.getGasLimit();

        mBaseBinding.titleView.setMidTitle(R.string.transfer);
        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 15, 8));

        initClickListener();

        getGasPriceValue();

        mBinding.btnNext.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mBinding.editToAddress.getText().toString().trim())) {
                UITipDialog.showInfo(this, "请输入接收地址");
                return;
            }

            if (!WalletUtils.isValidAddress(mBinding.editToAddress.getText().toString().trim())) {
                UITipDialog.showInfo(this, "无效的接收地址");
                return;
            }

            if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())) {
                UITipDialog.showInfo(this, "请输入转账数量");
                return;
            }

            try {
                BigInteger amountBigInteger = new BigDecimal(mBinding.edtAmount.getText().toString().trim()).toBigInteger();
                if (transferGasPrice.add(amountBigInteger).compareTo(AccountUtil.amountFormatUnit(new BigDecimal(accountListBean.getBalance()), 8)) == 1) {
                    UITipDialog.showInfo(this, "可用余额不足");
                    return;
                }
            } catch (Exception e) {
                UITipDialog.showInfo(this, "请输入正确的金额");
                return;
            }

            transfer();

        });
    }

    /**
     * 转账操作
     */
    private void transfer() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> {
                    WalletDBModel w = WalletHelper.getPrivateKeyAndAddressByCoinType(WalletHelper.COIN_ETH);
                    if (TextUtils.equals(accountListBean.getSymbol(), WalletHelper.COIN_WAN)) {
                        return WalletHelper.transferWan(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), transferGasPrice);
                    }
                    return WalletHelper.transfer(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), transferGasPrice);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getError() != null) {
                        LogUtil.E("has————" + s.getError().getMessage());
                        UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                        return;
                    }

                    if (!TextUtils.isEmpty(s.getTransactionHash())) {
                        UITipDialog.showSuccess(WalletTransferActivity.this, "转账成功，正在同步中", dialogInterface -> finish());
                    }

                }, throwable -> {
                    UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                    LogUtil.E("has————" + throwable);
                }, () -> disMissLoading()));
    }

    /**
     * 获取燃料费用
     */
    private void getGasPriceValue() {
        showLoadingDialog();

        mSubscription.add(
                Observable.just("")
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> WalletHelper.getGasValue())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(gasPrice -> {
                            this.gasPrice = gasPrice;
                            setShowGasPrice(gasPrice);

                        }, throwable -> {

                        }, () -> disMissLoading())
        );
    }

    /**
     * 设置矿工费显示
     *
     * @param gasPrice
     */
    private void setShowGasPrice(BigInteger gasPrice) {
        mBinding.tvGas.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(gasPrice), 8) + " " + accountListBean.getSymbol());
    }

    private void initClickListener() {
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, CODEPERSE);
        });

        mBinding.linLayoutGasChoose.setOnClickListener(view -> {
            new GasTypeChoosePop(this, chooseGasType).setItemClickListener((chooseType, typeString) -> {
                chooseGasType = chooseType;
                mBinding.tvChooseType.setText(typeString);

                switch (chooseType) {
                    case GasTypeChoosePop.FIRST:
                        transferGasPrice = gasPrice.multiply(new BigDecimal(2).toBigInteger());
                        setShowGasPrice(transferGasPrice);
                        break;
                    case GasTypeChoosePop.ORDINARY:
                        transferGasPrice = gasPrice;
                        break;

                    case GasTypeChoosePop.ECONOMICS:
                        transferGasPrice = gasPrice.divide(new BigDecimal(2).toBigInteger());
                        break;

                }
                setShowGasPrice(transferGasPrice);

            }).showPopupWindow();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == CODEPERSE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (WalletUtils.isValidAddress(result)) {
                        mBinding.editToAddress.setText(result);
                    } else {
                        Toast.makeText(WalletTransferActivity.this, "错误的地址", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WalletTransferActivity.this, "解析地址失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
