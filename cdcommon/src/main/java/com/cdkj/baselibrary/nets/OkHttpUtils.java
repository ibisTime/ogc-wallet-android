package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.utils.LogUtil;

import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Okhttp OkHttpClient 使用封装
 * Created by Administrator on 2016-09-05.
 */
public class OkHttpUtils {

    private final static int CONNECT_TIMEOUT = 60;//连接超时
    private final static int READ_TIMEOUT = 60;//数据返回超时
    private final static int WRITE_TIMEOUT = 60;//请求超时

    public OkHttpUtils() {
    }

    private static OkHttpClient client;

    /**
     * 获取 OkHttpClient 对象
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getInstance() {
        if (client == null) {

//            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                    .tlsVersions(TlsVersion.TLS_1_1)
//                    .tlsVersions(TlsVersion.TLS_1_2)
//                    .cipherSuites(
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                    .build();

            client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)//允许失败重试
                    .cookieJar(new CookiesManager())  //cookie 管理
                    .addInterceptor(getInterceptor(LogUtil.isLog))    //网络日志
//                    .addNetworkInterceptor(new StethoInterceptor())
//                    .connectionSpecs(Collections.singletonList(spec))
                    .build();

        }

        return client;
    }


    /**
     * 网络请求拦截器
     *
     * @param isShow 控制请求日志的显示
     * @return interceptor
     */
    private static HttpLoggingInterceptor getInterceptor(boolean isShow) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    LogUtil.I("okhttp: " + URLDecoder.decode(message, "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.I("okhttp日志错误: " + message);
                }

            }
        });

        if (isShow) {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor = interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return interceptor;
    }
}
