package com.pos.priory;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.utils.OkHttp3Util;

import java.io.InputStream;

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
    }


    public static MyApplication getContext(){
        return mContext;
    }
}
