package com.pos.priory.activitys;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.zxinglib.utils.SystemBarTintManager;

/**
 * Created by Lenovo on 2018/12/29.
 */

public abstract class BaseActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(Build.VERSION.SDK_INT >= 19){
            initNocationBar(android.R.color.transparent);
        }
        ColseActivityUtils.addActivity(this);
        beForeInitViews();
        initViews();
    }

    public void initNocationBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ColseActivityUtils.removeActivity(this);
    }

    protected abstract void beForeInitViews();
    protected abstract void initViews();

}
