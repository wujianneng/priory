package com.pos.priory;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.utils.Constants;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

import okhttp3.Cache;

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

        RetrofitManager.init(this,Constants.SENTRY_DNS,Constants.MACAL_BASE_URL,new Cache(getCacheDir(),
                1024 * 1024 * 10),"",Constants.Authorization_KEY,authorization);
    }


    public static MyApplication getContext(){
        return mContext;
    }
}
