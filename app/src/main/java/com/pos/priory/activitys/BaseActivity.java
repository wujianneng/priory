package com.pos.priory.activitys;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.zxinglib.utils.SystemBarTintManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Lenovo on 2018/12/29.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    Handler handler = new Handler();

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
        EventBus.getDefault().register(this);
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
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        ColseActivityUtils.removeActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(String event){

    }

    public static <F extends Fragment> F handleTabFragments(AppCompatActivity activity, int containerId, Class<F> sfClass,
                                                            F sf, Fragment... fragments) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (sf == null) {
            sf = (F) Fragment.instantiate(activity, sfClass.getName());
            fragmentTransaction.add(containerId, sf);
        } else {
            fragmentTransaction.show(sf);
        }
        for (Fragment fragment : fragments) {
            if (fragment != null) fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
        return sf;
    }

    protected abstract void beForeInitViews();
    protected abstract void initViews();

}
