package com.infitack.rxretorfit2library;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
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
    public static String hostname = "";
    private static volatile OkHttpClient mOkHttpClient;
    private static Cache cache;//缓存10mib
    private static Interceptor interceptor;
    public static String sentryUsername = "";
    public static String headerKey = "";
    public static String headerValue = "";

    public static void init(String baseUrl, Cache okhttp3Cache) {
        hostname = baseUrl;
        cache = okhttp3Cache;
    }

    public static void initHeader(String headerkey,String headervalue) {
        headerKey = headerkey;
        headerValue = headervalue;
        Log.e("test", "1headerValue:" + headerValue);
    }

    public static void initSentry(String username) {
        sentryUsername = username;
    }

    public static void init(Context context,String sentryDsn, String baseUrl, Cache okhttp3Cache, String username) {
        hostname = baseUrl;
        cache = okhttp3Cache;
        sentryUsername = username;
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(context));

    }

    public static void init(Context context,String sentryDsn, String baseUrl, Cache okhttp3Cache,
                            String username,String headerkey,String headervalue) {
        hostname = baseUrl;
        cache = okhttp3Cache;
        sentryUsername = username;
        headerKey = headerkey;
        headerValue = headervalue;
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(context));
    }

    public static void init(Context context,String sentryDsn,String baseUrl,OkHttpClient okHttpClient) {
        hostname = baseUrl;
        mOkHttpClient = okHttpClient;
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(context));
    }

    public static void init(Context context,String sentryDsn,String baseUrl, Cache okhttp3Cache, Interceptor okhttp3Interceptor) {
        hostname = baseUrl;
        cache = okhttp3Cache;
        interceptor = okhttp3Interceptor;
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(context));
    }


    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    if (interceptor == null) {
                        mOkHttpClient = new OkHttpClient.Builder()
                                .addInterceptor(new OkHttpInterceptor())
                                .connectionPool(new ConnectionPool(4, 600, TimeUnit.SECONDS))
                                .connectTimeout(600, TimeUnit.SECONDS)//连接超时时间
                                .readTimeout(600, TimeUnit.SECONDS)//读取超时时间
                                .writeTimeout(600, TimeUnit.SECONDS)//写入超时时间
                                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                                .cache(cache)
                                .build();
                    } else {
                        mOkHttpClient = new OkHttpClient.Builder()
                                .addInterceptor(interceptor)
                                .connectionPool(new ConnectionPool(4, 600, TimeUnit.SECONDS))
                                .connectTimeout(600, TimeUnit.SECONDS)//连接超时时间
                                .readTimeout(600, TimeUnit.SECONDS)//读取超时时间
                                .writeTimeout(600, TimeUnit.SECONDS)//写入超时时间
                                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                                .cache(cache)
                                .build();
                    }
                }
            }
        }
        return mOkHttpClient;
    }


    public static <T> T createGson(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(hostname)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(clazz);
    }

    public static <T> T createString(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(hostname)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


        return retrofit.create(clazz);
    }

}
