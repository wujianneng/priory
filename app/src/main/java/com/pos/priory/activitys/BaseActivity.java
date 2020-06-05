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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.zxinglib.utils.SystemBarTintManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    Handler handler = new Handler();
    CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (Build.VERSION.SDK_INT >= 19) initNocationBar(android.R.color.transparent);
        ColseActivityUtils.addActivity(this);
        EventBus.getDefault().register(this);
    }

    public void initNocationBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) setTranslucentStatus(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) winParams.flags |= bits;
        else winParams.flags &= ~bits;
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
    public void handleEventBus(String event) {
    }

    public void showLoadingDialog(String msg) {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, msg);
            customDialog.setOnDismissListener(customDialog1 -> customDialog = null);
            customDialog.show();
        } else {
            if (!customDialog.isShowing()) customDialog.show();
        }
    }

    public void hideLoadingDialog() {
        if (customDialog != null) customDialog.dismiss();
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show());
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
        fragmentTransaction.commitNowAllowingStateLoss();
        return sf;
    }

}
