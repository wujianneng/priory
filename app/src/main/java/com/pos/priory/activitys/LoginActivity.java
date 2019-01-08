package com.pos.priory.activitys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
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
import okhttp3.Headers;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.input_cardview)
    CardView inputCardview;
    @Bind(R.id.btn_cardview)
    CardView btnCardview;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.edt_usename)
    EditText edtUsename;
    @Bind(R.id.edt_passwrod)
    EditText edtPasswrod;

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
        edtUsename.setText(sharedPreferences.getString(Constants.LAST_USERNAME_KEY, ""));
        edtPasswrod.setText(sharedPreferences.getString(Constants.LAST_PASSWORD_KEY, ""));
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
            OkHttp3Util.doPost(Constants.LOGIN_URL, gson.toJson(paramMap), new Okhttp3StringCallback("login") {
                @Override
                public void onSuccess(String results) throws Exception {
                    String token = new JSONObject(results).getString("key");
                    sharedPreferences.edit().putString(Constants.Authorization_KEY, token).commit();
                    getStaffInfo();
                }

                @Override
                public void onFailed(String erromsg) {
                    new RunOnUiThreadSafe(LoginActivity.this) {
                        @Override
                        public void runOnUiThread() {
                            customDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        }
                    };
                }
            });
        }

    }

    private void getStaffInfo() {
        OkHttp3Util.doGetWithToken(Constants.STAFF_INFO_URL + "?user=" + edtUsename.getText().toString(),
                sharedPreferences, new Okhttp3StringCallback("staffInfo") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        sharedPreferences.edit().putBoolean(Constants.IS_SAVE_PASSWORD_KEY, checkbox.isChecked()).commit();
                        if (checkbox.isChecked()) {
                            sharedPreferences.edit().putString(Constants.LAST_USERNAME_KEY,
                                    edtUsename.getText().toString()).commit();
                            sharedPreferences.edit().putString(Constants.LAST_PASSWORD_KEY,
                                    edtPasswrod.getText().toString()).commit();
                            sharedPreferences.edit().putString(Constants.CURRENT_STAFF_INFO_KEY, results).commit();
                        }
                        new RunOnUiThreadSafe(LoginActivity.this) {
                            @Override
                            public void runOnUiThread() {
                                customDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }
}
