package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
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
    @Bind(R.id.location_tv)
    TextView locationTv;
    @Bind(R.id.select_location_btn)
    LinearLayout selectLocationBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
    }

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
        RetrofitManager.changeBaseUrl(sharedPreferences.getString(Constants.LAST_BASE_URL_KEY, Constants.BASE_URL));
        Log.e("test", "MyApplication.hostName:" + RetrofitManager.hostname);
        locationTv.setText(RetrofitManager.hostname.equals(Constants.BASE_URL) ? "中国大陆" : "澳门");
    }

    @OnClick({R.id.btn_cardview, R.id.select_location_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cardview:
                login();
                break;
            case R.id.select_location_btn:
                showSelectLocationPop(v);
                break;
        }
    }

    PopupMenu popupMenu;

    private void showSelectLocationPop(View view) {
        // 这里的view代表popupMenu需要依附的view
        if (popupMenu == null) {
            popupMenu = new PopupMenu(LoginActivity.this, view);
            // 获取布局文件
            popupMenu.getMenuInflater().inflate(R.menu.location_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu popupMenu1) {
                    popupMenu = null;
                }
            });
            // 通过上面这几行代码，就可以把控件显示出来了
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // 控件每一个item的点击事件
                    switch (item.getItemId()) {
                        case R.id.menu0:
                            locationTv.setText(item.getTitle());
                            RetrofitManager.changeBaseUrl(Constants.BASE_URL);
                            break;
                        case R.id.menu1:
                            locationTv.setText(item.getTitle());
                            RetrofitManager.changeBaseUrl(Constants.MACAL_BASE_URL);
                            break;
                    }
                    return true;
                }
            });
        }
    }


    CustomDialog customDialog;

    private void login() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "登录中..");
            customDialog.setOnDismissListener(dialogInterface -> customDialog = null);
            customDialog.show();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("username", edtUsename.getText().toString());
            paramMap.put("password", edtPasswrod.getText().toString());
            Log.e("test", "username:" + edtUsename.getText().toString() + " password:" + edtPasswrod.getText().toString());
            RetrofitManager.excuteTwoOrder(bindToLifecycle(), RetrofitManager.createString(ApiService.class).login(paramMap), new ModelListener() {
                @Override
                public void onSuccess(String result) throws Exception {
                    Log.e("test", "doOnNext:" + result);
                    String token = new JSONObject(result).getString("key");
                    MyApplication.authorization = "Token " + token;
                    RetrofitManager.initHeader(Constants.Authorization_KEY, MyApplication.authorization);
                }

                @Override
                public void onFailed(String erromsg) {
                    customDialog.dismiss();
                    Log.e("test", "请与总部相关人员联络及查询1");
                    Toast.makeText(LoginActivity.this, "请与总部相关人员联络及查询！", Toast.LENGTH_SHORT).show();
                }
            }, RetrofitManager.createString(ApiService.class).getStaffInfo(edtUsename.getText().toString()), new ModelListener() {
                @Override
                public void onSuccess(String result) throws Exception {
                    List<StaffInfoBean> staffInfoBeanList = gson.fromJson(result,
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
                                editor.putString(Constants.LAST_BASE_URL_KEY,
                                        RetrofitManager.hostname);
                            }
                            editor.putString(Constants.CURRENT_STAFF_INFO_KEY, result);
                            editor.commit();
                            if (customDialog != null)
                                customDialog.dismiss();
                            RetrofitManager.initSentry(edtUsename.getText().toString());
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

                @Override
                public void onFailed(String erromsg) {
                    if (customDialog != null)
                        customDialog.dismiss();
                    Log.e("test", "请与总部相关人员联络及查询3");
                    Toast.makeText(LoginActivity.this, "请与总部相关人员联络及查询！", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
