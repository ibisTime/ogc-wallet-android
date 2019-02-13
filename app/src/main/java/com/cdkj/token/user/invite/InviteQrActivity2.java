package com.cdkj.token.user.invite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteQr2Binding;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.utils.NetWorrkUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteQrActivity2 extends AbsStatusBarTranslucentActivity {

    private ActivityInviteQr2Binding mBinding;

    private PermissionHelper mPermissionHelper;
    private String shareUrl;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteQrActivity2.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite_qr2, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canEvenFinish() {
        return false;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.fraLayoutTitle.setVisibility(View.GONE);
//        4D30C5  状态栏颜色
        UIStatusBarHelper.translucent(this, Color.parseColor("#4D30C5"));
        mPermissionHelper = new PermissionHelper(this);
        setPageBgRes(R.drawable.invite_bg);

        mBinding.tvName.setText(TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum()) ? SPUtilHelper.getUserEmail() : SPUtilHelper.getUserPhoneNum());
        mBinding.tvMsg.setText(getString(R.string.invite_join,getStrRes(R.string.app_name) ));
//        mBinding.tvMsg.setText(getString(R.string.invite_32, "阿萨德阿萨德"));
//        getStrRes(R.string.app_name)
        setStatusBarWhite();

        setClickListener();

        geShareUrlRequest();

    }

    private void setClickListener() {

        mBinding.tvFinish.setOnClickListener(view -> finish());

        mBinding.btnCopy.setOnClickListener(view -> {
//            copyShareURl();  不用弹窗复制  直复制
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, shareUrl);
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.copy_success));
        });

        mBinding.tvFinish.setOnClickListener(view -> finish());

        // 本地保存
        mBinding.tvSave.setOnClickListener(view -> {
            permissionRequestAndSaveBitmap();
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 申请权限
     */
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
    private void saveBitmapToAlbum() {
        getBitmapByView(mBinding.scrollview);
        mBinding.scrollview.post(() -> {
            showLoadingDialog();
            mBinding.linLayoutCopy.setVisibility(View.GONE);

            mSubscription.add(Observable.just("")
                    .observeOn(AndroidSchedulers.mainThread())  //创建
                    .map(o -> getBitmapByView(mBinding.scrollview))
                    .observeOn(Schedulers.newThread())  //创建
                    .map(bitmap -> BitmapUtils.saveBitmapFile(bitmap, ""))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        disMissLoadingDialog();
                        UITipDialog.showInfoNoIcon(this, getString(R.string.save_success));

                    }, throwable -> {
                        mBinding.linLayoutCopy.setVisibility(View.VISIBLE);
                        disMissLoadingDialog();
                        UITipDialog.showInfoNoIcon(this, getString(R.string.save_fail));

                    }));
        });
    }


    /**
     * 截取scrollview的生产bitmap
     *
     * @param scrollView
     * @return
     */
    public Bitmap getBitmapByView(ScrollView scrollView) {

        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            if (scrollView.getChildAt(i).getVisibility() != View.VISIBLE) {
                continue;
            }
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    public void geShareUrlRequest() {
        NetWorrkUtils.getSystemServer(this, "invite_url", true, new NetWorrkUtils.SystemServerListener() {
            @Override
            public void onSuccer(SystemParameterModel data) {
                String baseShearurl = data.getCvalue();
                shareUrl = baseShearurl + "?inviteCode=" + SPUtilHelper.getUserId();
                try {
                    Bitmap bitmap = CodeUtils.createImage(shareUrl, 500, 500, null);
                    mBinding.ivQr.setImageBitmap(bitmap);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private void copyShareURl() {
        CopyActivity.open(this, shareUrl);
    }
}
