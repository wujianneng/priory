package com.pos.priory.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Lenovo on 2018/11/7.
 */

public abstract class RunOnUiThreadSafe {

    public abstract void runOnUiThread();

    public RunOnUiThreadSafe(Activity activity) {
        if (activity != null && !activity.isDestroyed())
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread();
                }
            });
    }

    public RunOnUiThreadSafe(Fragment fragment) {
        if(fragment != null) {
            Activity activity = fragment.getActivity();
            if (activity != null && !activity.isDestroyed())
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread();
                    }
                });
        }
    }


}
