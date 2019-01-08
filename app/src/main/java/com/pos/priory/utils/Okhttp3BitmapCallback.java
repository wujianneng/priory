package com.pos.priory.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lenovo on 2018/11/6.
 */

public abstract class Okhttp3BitmapCallback implements Callback {
    String sentryTitle = "";

    public abstract void onSuccess(Bitmap results) throws Exception;

    public abstract void onFailed(String erromsg);

    public Okhttp3BitmapCallback() {
    }

    public Okhttp3BitmapCallback(String sentryTitle) {
        this.sentryTitle = sentryTitle;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFailed(e.getMessage());
        if (!sentryTitle.equals("")) {
            Log.e(sentryTitle, "onFailed：" + e.getMessage());
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            InputStream results = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(results);
            if (!sentryTitle.equals(""))
                Log.e(sentryTitle, "response：获取bitmap成功");
            if (response.isSuccessful()) {
                onSuccess(bitmap);
            } else {
                onFailed("获取bitmap失败！");
                if (!sentryTitle.equals("")) {
                    Log.e(sentryTitle, "onFailed：获取bitmap失败！");
                }
            }
        } catch (Exception e) {
            onFailed(e.getMessage());
            if (!sentryTitle.equals("")) {
                Log.e(sentryTitle, "onFailed：" + e.getMessage());
            }
        }

    }

}
