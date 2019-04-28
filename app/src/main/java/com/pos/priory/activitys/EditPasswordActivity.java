package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.priory.R;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class EditPasswordActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_new_password)
    EditText edtNewPassword;
    @Bind(R.id.edt_repeat_new_password)
    EditText edtRepeatNewPassword;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.btn_commit)
    CardView btnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("修改密碼");
    }

    CustomDialog customDialog;

    private void editPassword() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在修改密碼..");
        }
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("new_password1", edtNewPassword.getText().toString());
        map.put("new_password2", edtRepeatNewPassword.getText().toString());
        RetrofitManager.createString(ApiService.class)
                .resetPassword(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(EditPasswordActivity.this, "修改密碼成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(EditPasswordActivity.this, "修改密碼失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.btn_commit, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (edtNewPassword.getText().toString().equals("") ||
                        edtRepeatNewPassword.getText().toString().equals("")) {
                    Toast.makeText(EditPasswordActivity.this, "新密碼和確認密碼輸入框不能爲空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtNewPassword.getText().toString().length() < 8 ||
                        edtRepeatNewPassword.getText().length() < 8) {
                    Toast.makeText(EditPasswordActivity.this, "新密碼和確認密碼長度必須大於8位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (LogicUtils.isNumeric(edtNewPassword.getText().toString()) ||
                        LogicUtils.isNumeric(edtRepeatNewPassword.getText().toString())) {
                    Toast.makeText(EditPasswordActivity.this, "新密碼和確認密碼都不能為純數字", Toast.LENGTH_SHORT).show();
                    return;
                }
                editPassword();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
