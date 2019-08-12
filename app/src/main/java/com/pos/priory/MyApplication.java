package com.pos.priory;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.networks.RetrofitManager;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

/**
 * Created by Lenovo on 2019/1/9.
 */

public class MyApplication extends Application {
    private static MyApplication mContext;
    public static StaffInfoBean staffInfoBean;
    public static String authorization = "";
    public static String storeName = "";
    public static String storeAddress = "";
    public static String storeTel = "";
    public static String region = "";
    public static String storeListJsonString = "";
    public static String hostName = RetrofitManager.BASE_URL;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Utils.init(this);
        //下载库初始化
        PRDownloader.initialize(getApplicationContext(), PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build());
        String sentryDsn = "https://16fbe74588aa409eafdb10e6a94bc827:39bbbc909aec4afba24523c286bbc926@sentry.io/1481689";
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(this));
    }


    public static MyApplication getContext(){
        return mContext;
    }
}
