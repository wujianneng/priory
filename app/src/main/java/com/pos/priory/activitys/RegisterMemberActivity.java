package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
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
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/30.
 */

public class RegisterMemberActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_first_name)
    EditText edtFirstName;
    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.btn_sex)
    TextView btnSex;
    @Bind(R.id.edt_phone)
    EditText edtPhone;
    @Bind(R.id.btn_commit)
    MaterialButton btnCommit;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_register_member);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("注册会员");
        rightImg.setVisibility(View.GONE);
    }

    int yourChoice;
    AlertDialog choiceSexDialog;

    private void showChoiceSexDialog() {
        if (choiceSexDialog == null) {
            final String[] items = {"男", "女"};
            yourChoice = 0;
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("请选择性别");
            // 第二个参数是默认选项，此处设置为0
            singleChoiceDialog.setSingleChoiceItems(items, yourChoice,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yourChoice = which;
                        }
                    });
            singleChoiceDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yourChoice != -1) {
                                btnSex.setText(items[yourChoice]);
                            }
                            choiceSexDialog.dismiss();
                        }
                    });
            choiceSexDialog = singleChoiceDialog.create();
            choiceSexDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    choiceSexDialog = null;
                }
            });
            choiceSexDialog.show();
        }
    }

    @OnClick({R.id.btn_sex, R.id.back_btn, R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sex:
                showChoiceSexDialog();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_commit:
                if (edtFirstName.getText().toString().equals("")
                        || edtPhone.getText().toString().equals("")) {
                    Toast.makeText(RegisterMemberActivity.this, "姓和电话都不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (btnSex.getText().toString().equals("请点击选择性别")) {
                    Toast.makeText(RegisterMemberActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerMember();
                break;
        }
    }

    CustomDialog customDialog;

    private void registerMember() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在注册会员..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("last_name", edtFirstName.getText().toString());
        paramMap.put("first_name", edtName.getText().toString());
        paramMap.put("mobile", edtPhone.getText().toString());
        paramMap.put("sex", btnSex.getText().toString());
        RetrofitManager.createString(ApiService.class)
                .registerMember(paramMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        JSONObject jsonObject = new JSONObject(results);
                        Toast.makeText(RegisterMemberActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();
                        finish();
                        Intent intent = new Intent(RegisterMemberActivity.this, AddNewOrderActivity.class);
                        intent.putExtra("memberId", jsonObject.getInt("id"));
                        intent.putExtra("memberMobile", jsonObject.getString("mobile"));
                        intent.putExtra("memberReward", jsonObject.getInt("reward"));
                        intent.putExtra("memberName", jsonObject.getString("last_name") +
                                jsonObject.getString("first_name"));
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        if (throwable.getMessage().contains("400")) {
                            Toast.makeText(RegisterMemberActivity.this, "该手机号已经注册", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(RegisterMemberActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
