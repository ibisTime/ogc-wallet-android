package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.api.BaseApiServer;

import retrofit2.Retrofit;

import static com.cdkj.baselibrary.appmanager.AppConfig.getBaseURL;
import static com.cdkj.baselibrary.appmanager.AppConfig.getOtherBaseURL;

/**
 * 服务器api
 * Created by Administrator on 2016/9/1.
 */
public class RetrofitUtils {

    private static Retrofit retrofitInstance = null;
    private static Retrofit otherRetrofitInstance = null;

    private RetrofitUtils() {
    }

    /**
     * 获取Retrofit实例
     *
     * @return Retrofit
     */
    private static Retrofit getInstance() {

        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(getBaseURL())
                    .client(OkHttpUtils.getInstance())
                    .addConverterFactory(FastJsonConVerter.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    /**
     * 获取Retrofit实例
     *
     * @return Retrofit
     */
    public static Retrofit getOtherInstance() {

        if (otherRetrofitInstance == null) {
            otherRetrofitInstance = new Retrofit.Builder()
                    .baseUrl(getOtherBaseURL())
                    .client(OkHttpUtils.getInstance())
                    .addConverterFactory(FastJsonConVerter.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return otherRetrofitInstance;
    }

    public static void reSetInstance() {
        retrofitInstance = null;
    }


    /**
     * 创建Retrofit请求Api
     *
     * @param clazz Retrofit Api接口
     * @return api实例
     */
    public static <T> T createApi(Class<T> clazz) {
        return getInstance().create(clazz);
    }

    public static BaseApiServer getBaseAPiService() {
        return createApi(BaseApiServer.class);
    }

}
