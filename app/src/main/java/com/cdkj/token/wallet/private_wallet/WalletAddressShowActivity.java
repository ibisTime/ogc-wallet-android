package com.cdkj.token.wallet.private_wallet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.GlideApp;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletAddressShowBinding;
import com.cdkj.token.model.CoinAddressShowModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.wallet.account_wallet.WithdrawOrderActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 地址展示  （收款）
 */
public class WalletAddressShowActivity extends AbsLoadActivity {


    private ActivityWalletAddressShowBinding mBinding;

    private PermissionHelper mPermissionHelper;
    private CoinAddressShowModel coinAddressShowModel;


    public static void open(Context context, CoinAddressShowModel coinAddressShowModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletAddressShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, coinAddressShowModel);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_address_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.get_money);
        mBaseBinding.titleView.setRightTitle(getStrRes(R.string.wallet_charge_record));
//        cion_input_message
        if (getIntent() != null) {
            coinAddressShowModel = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        }
        mPermissionHelper = new PermissionHelper(this);
        String coinSymbol = coinAddressShowModel.getCoinSymbol();

        mBinding.tvMsg.setText(getString(R.string.cion_input_message,coinSymbol,coinSymbol,coinSymbol));
        initQRCodeAndAddress();
        initListener();
    }


    private void initQRCodeAndAddress() {
        if (coinAddressShowModel == null) return;

        try {
            String coinLogo = SPUtilHelper.getQiniuUrl() + LocalCoinDBUtils.getCoinIconByCoinSymbol(coinAddressShowModel.getCoinSymbol());
            mBinding.txtAddress.setText(coinAddressShowModel.getAddress());
            GlideApp.with(this).asBitmap().load(coinLogo)
                    .error(R.mipmap.icon_pay_bank_logo)
                    .into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //8.0以上加载logo会崩溃，先做捕获处理
                            try {
                                Bitmap mBitmap = CodeUtils.createImage(coinAddressShowModel.getAddress(), 400, 400, resource);
                                mBinding.imgQRCode.setImageBitmap(mBitmap);
                                mBinding.txtAddress.setText(coinAddressShowModel.getAddress());
                            } catch (Exception e) {

                                try {
                                    Bitmap mBitmap = CodeUtils.createImage(coinAddressShowModel.getAddress(), 400, 400, null);
                                    mBinding.imgQRCode.setImageBitmap(mBitmap);
                                    mBinding.txtAddress.setText(coinAddressShowModel.getAddress());
                                } catch (Exception e2) {

                                }

                            }

                        }

                    });
        } catch (Exception e) {

        }

    }

    private void initListener() {
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            if (coinAddressShowModel == null) return;
            WithdrawOrderActivity.open(this, TextUtils.equals(coinAddressShowModel.getCoinSymbol(), "BTC") ? WithdrawOrderActivity.BTC_INPUT : WithdrawOrderActivity.ETC_INPUT, coinAddressShowModel.getCoinSymbol());
        });
        mBinding.ivCloseMsg.setOnClickListener(view -> mBinding.llMsg.setVisibility(View.GONE));
        mBinding.btnCopy.setOnClickListener(view -> {
            copyAddress();
        });
        mBinding.btnSavePhoto.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
        });
    }

    private void copyAddress() {
        if (!TextUtils.isEmpty(mBinding.txtAddress.getText())) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, mBinding.txtAddress.getText());
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(WalletAddressShowActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        }
    }

    private void permissionRequestAndSaveBitmap() {
        mPermissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                saveBitmapToAlbum();
            }

            @Override
            public void doAfterDenied(String... permission) {
                showSureDialog(getString(R.string.no_file_permission), view -> {
                });
            }

        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    /**
     * 保存图片到相册
     */
    public void saveBitmapToAlbum() {
        mBinding.scrollView.post(() -> {
            showLoadingDialog();
            mSubscription.add(Observable.just("")
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(s -> BitmapUtils.getBitmapByView(mBinding.scrollView))
                    .observeOn(Schedulers.newThread())
                    .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, coinAddressShowModel.getCoinSymbol()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));
                    }, throwable -> {
                        LogUtil.E("a" + throwable);
                        UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));
                        disMissLoadingDialog();
                    }, () -> disMissLoadingDialog()));
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
