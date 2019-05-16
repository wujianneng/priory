package com.pos.priory;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pos.priory.beans.StaffInfoBean;

/**
 * Created by Lenovo on 2019/1/9.
 */

public class MyApplication extends Application {
    private static MyApplication mContext;
    public static StaffInfoBean staffInfoBean;
    public static String authorization = "";

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
    }


    public static MyApplication getContext(){
        return mContext;
    }
}
