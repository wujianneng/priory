package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.priory.utils.RunOnUiThreadSafe;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.edt_usename)
    EditText edtUsename;
    @Bind(R.id.edt_passwrod)
    EditText edtPasswrod;
    @Bind(R.id.btn_cardview)
    MaterialButton btnCardview;
    @Bind(R.id.checkbox)
    CheckBox checkbox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (sharedPreferences.getBoolean(Constants.IS_SAVE_PASSWORD_KEY, false)) {
            edtUsename.setText(sharedPreferences.getString(Constants.LAST_USERNAME_KEY, ""));
            edtPasswrod.setText(sharedPreferences.getString(Constants.LAST_PASSWORD_KEY, ""));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogicUtils.openKeybord(edtUsename, LoginActivity.this);
                }
            }, 300);
        }
        checkbox.setChecked(sharedPreferences.getBoolean(Constants.IS_SAVE_PASSWORD_KEY, false));
    }

    @OnClick({R.id.btn_cardview})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cardview:
                login();
                break;
        }
    }


    CustomDialog customDialog;

    private void login() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "登录中..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("username", edtUsename.getText().toString());
            paramMap.put("password", edtPasswrod.getText().toString());
            RetrofitManager
                    .createString(ApiService.class)
                    .login(paramMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            String token = new JSONObject(s).getString("key");
                            MyApplication.authorization = "Token " + token;
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            customDialog.dismiss();
                            Log.e("test", "请与总部相关人员联络及查询1");
                            Toast.makeText(LoginActivity.this, "请与总部相关人员联络及查询！", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<String, Observable<String>>() {
                        @Override
                        public Observable<String> apply(String s) throws Exception {
                            return RetrofitManager.createString(ApiService.class).getStaffInfo(edtUsename.getText().toString());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            List<StaffInfoBean> staffInfoBeanList = gson.fromJson(s,
                                    new TypeToken<List<StaffInfoBean>>() {
                                    }.getType());

                            if (staffInfoBeanList != null && staffInfoBeanList.size() != 0) {
                                StaffInfoBean staffInfoBean = staffInfoBeanList.get(0);
                                MyApplication.staffInfoBean = staffInfoBean;
                                if (staffInfoBean.getUser().equals(edtUsename.getText().toString())) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(Constants.IS_SAVE_PASSWORD_KEY, checkbox.isChecked());
                                    if (checkbox.isChecked()) {
                                        editor.putString(Constants.LAST_USERNAME_KEY,
                                                edtUsename.getText().toString());
                                        editor.putString(Constants.LAST_PASSWORD_KEY,
                                                edtPasswrod.getText().toString());
                                    }
                                    editor.putString(Constants.CURRENT_STAFF_INFO_KEY, s);
                                    editor.commit();
                                    if (customDialog != null)
                                        customDialog.dismiss();

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            } else {
                                if (customDialog != null)
                                    customDialog.dismiss();
                                Log.e("test", "请与总部相关人员联络及查询2");
                                Toast.makeText(LoginActivity.this, "请与总部相关人员联络及查询！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (customDialog != null)
                                customDialog.dismiss();
                            Log.e("test", "请与总部相关人员联络及查询3");
                            Toast.makeText(LoginActivity.this, "请与总部相关人员联络及查询！", Toast.LENGTH_SHORT).show();
                        }
                    });


        }

    }

}
