package com.cdkj.token.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.net.URISyntaxException;
import java.util.List;

public class IsInstallWeChatOrAliPay {
    //跳转支付宝的uri
    private static final String URL_FORMAT =
            "intent://platformapi/startapp?saId=10000007&" +
                    "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
                    "%3Dweb-other&_t=1472443966571#Intent;" + "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";


    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * sina
     * 判断是否安装新浪微博
     */
    public static boolean isSinaInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean startZFBIntentUrl(Activity activity, String intentUrl) {
        try {
            Intent intent = Intent.parseUri(intentUrl, Intent.URI_INTENT_SCHEME);
            activity.startActivity(intent);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 启动支付宝
     *
     * @param mActivity
     * @param imgUrl
     * @return
     */
    public static boolean startAli(Activity mActivity, String imgUrl, StratAliLisenter stratAliLisenter) {


        BitmapUtils.getImage(mActivity, SPUtilHelper.getQiniuUrl() + imgUrl, new BitmapUtils.HttpCallBackListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannedQR(mActivity, bitmap, stratAliLisenter);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                if (stratAliLisenter != null)
                    stratAliLisenter.onError("解析失败");
                return;
            }
        });

        return true;
    }

    /**
     * 扫描获取到的图片
     *
     * @param bitmap
     */
    private static void scannedQR(Activity mActivity, Bitmap bitmap, StratAliLisenter stratAliLisenter) {

        ZxingUtils.analyzeBitmap(bitmap, new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

//                HTTPS://QR.ALIPAY.COM/FKX06440RVTBLYZNHGSHC3?t=1547606011183
//                HTTPS://QR.ALIPAY.COM/FKX06440RVTBLYZNHGSHC3
                //扫描结果可能是  这两种情况

                if (TextUtils.isEmpty(result)) {
                    if (stratAliLisenter != null) {
                        stratAliLisenter.onError("后台参数配置错误,请联系管理员");
                    }
                    return;
                }
                //截取出来支付宝用户的id
                String ailiID = null;
                if (result.contains("HTTPS://QR.ALIPAY.COM/")) {
                    ailiID = result.substring("HTTPS://QR.ALIPAY.COM/".length() - 1, result.contains("?") ? result.indexOf("?") : result.length());

                }
                if (result.contains("https://qr.alipay.com/")) {
                    ailiID = result.substring("https://qr.alipay.com/".length() - 1, result.contains("?") ? result.indexOf("?") : result.length());
                }
                if (TextUtils.isEmpty(ailiID)) {
                    if (stratAliLisenter != null) {
                        stratAliLisenter.onError("后台参数配置错误,请联系管理员");
                    }
                } else {
                    startZFBIntentUrl(mActivity, URL_FORMAT.replace("{urlCode}", ailiID));
                    if (stratAliLisenter != null) {
                        stratAliLisenter.onSuccess();
                    }
                }
            }

            @Override
            public void onAnalyzeFailed() {
                LogUtil.E("扫描失败:");
            }
        });
    }

    public interface StratAliLisenter {
        void onSuccess();

        void onError(String msg);
    }
}