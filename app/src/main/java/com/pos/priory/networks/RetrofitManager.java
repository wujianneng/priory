package com.pos.priory.networks;

import com.pos.priory.MyApplication;
import com.pos.priory.utils.SSLSocketClient;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by kaka on 2018/3/14.
 * email:375120706@qq.com
 */

public class RetrofitManager {
    public static final String BASE_URL = "https://pos.annabellaip.com";
    private static volatile OkHttpClient mOkHttpClient;
    private static Cache cache = new Cache(MyApplication.getContext().getCacheDir(), 1024 * 1024 * 10);//缓存10mib

    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new OkHttpInterceptor())
                            .connectionPool(new ConnectionPool(4, 60, TimeUnit.SECONDS))
                            .connectTimeout(60, TimeUnit.SECONDS)//连接超时时间
                            .readTimeout(10, TimeUnit.SECONDS)//读取超时时间
                            .writeTimeout(10, TimeUnit.SECONDS)//写入超时时间
                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                            .cache(cache)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }


    public static <T> T createGson(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(RetrofitManager.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(clazz);
    }

    public static <T> T createString(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(RetrofitManager.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        return retrofit.create(clazz);
    }

}
