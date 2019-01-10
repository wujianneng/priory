package com.pos.priory.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Lenovo on 2018/11/6.
 */

public abstract class Okhttp3StringCallback implements Callback {
    String sentryTitle = "";
    Activity activity;

    public abstract void onSuccess(String results) throws Exception;

    public abstract void onFailed(String erromsg);

    public Okhttp3StringCallback() {
    }

    public Okhttp3StringCallback(String sentryTitle) {
        this.sentryTitle = sentryTitle;
    }

    public Okhttp3StringCallback(Activity activity, String sentryTitle) {
        this.sentryTitle = sentryTitle;
        this.activity = activity;
    }

    public Okhttp3StringCallback(Fragment fragment, String sentryTitle) {
        this.sentryTitle = sentryTitle;
        activity = fragment.getActivity();
    }


    @Override
    public void onFailure(Call call, final IOException e) {
        if (activity != null) {
            new RunOnUiThreadSafe(activity) {
                @Override
                public void runOnUiThread() {
                    onFailed(e.getMessage());
                }
            };
        } else {
            onFailed(e.getMessage());
        }
        if (!sentryTitle.equals("")) {
            Log.e(sentryTitle, "onFailed：" + e.getMessage());
        }
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (activity != null) {
            new RunOnUiThreadSafe(activity) {
                @Override
                public void runOnUiThread() {
                    try {
                        String result = response.body().string();
                        if (response.isSuccessful()) {
                            if (!sentryTitle.equals("")) {
                                Log.e(sentryTitle, "response：" + result);
                                Log.e(sentryTitle, "response：isSuccessful");
                            }
                            onSuccess(result);
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
            };
        } else {
            try {
                String result = response.body().string();
                if (!sentryTitle.equals(""))
                    Log.e(sentryTitle, "response：" + result);
                if (response.isSuccessful()) {
                    Log.e(sentryTitle, "response：isSuccessful");
                    onSuccess(result);
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

}
