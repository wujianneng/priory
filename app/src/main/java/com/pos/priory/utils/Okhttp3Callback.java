package com.pos.priory.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lenovo on 2018/11/6.
 */

public abstract class Okhttp3Callback<T> implements Callback {
    Gson gson = new Gson();
    Class<T> mClass;
    String sentryTitle = "";

    public abstract void onSuccess(T body);

    public abstract void onFailed(String erromsg);

    public Okhttp3Callback(Class<T> mClass) {
        this.mClass = mClass;
    }

    public Okhttp3Callback(Class<T> mClass, String sentryTitle) {
        this.sentryTitle = sentryTitle;
        this.mClass = mClass;
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
            String result = response.body().string();
            if (!sentryTitle.equals(""))
                Log.e(sentryTitle, "response：" + result);
            if (response.isSuccessful()) {
                T obj = (T) gson.fromJson(result, mClass);
                onSuccess(obj);
            } else {
                onFailed(result);
                if (!sentryTitle.equals("")) {
                    Log.e(sentryTitle, "onFailed：" + result);
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
