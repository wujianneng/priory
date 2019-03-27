package com.pos.priory.activitys;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.priory.R;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/30.
 */

public class RegisterMemberActivity extends BaseActivity {

    @Bind(R.id.padding_laout)
    View paddingLaout;
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
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_commit)
    CardView btnCommit;

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

    }

    int yourChoice;
    AlertDialog choiceSexDialog;
    private void showChoiceSexDialog(){
        if(choiceSexDialog == null) {
            final String[] items = {"男", "女"};
            yourChoice = 0;
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("請選擇性別");
            // 第二个参数是默认选项，此处设置为0
            singleChoiceDialog.setSingleChoiceItems(items,yourChoice,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yourChoice = which;
                        }
                    });
            singleChoiceDialog.setPositiveButton("確定",
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

    @OnClick({R.id.btn_sex,R.id.back_btn,R.id.btn_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sex:
                showChoiceSexDialog();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_commit:
                if(edtFirstName.getText().toString().equals("")
                        || edtPhone.getText().toString().equals("")){
                    Toast.makeText(RegisterMemberActivity.this,"姓和电话都不可为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(btnSex.getText().toString().equals("請點擊選擇性別")){
                    Toast.makeText(RegisterMemberActivity.this,"請選擇性別",Toast.LENGTH_SHORT).show();
                    return;
                }

                registerMember();
                break;
        }
    }

    CustomDialog customDialog;
    private void registerMember() {
        if(customDialog == null)
            customDialog = new CustomDialog(this,"正在注册会员..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("last_name",edtFirstName.getText().toString());
        paramMap.put("first_name",edtName.getText().toString());
        paramMap.put("mobile",edtPhone.getText().toString());
        paramMap.put("sex",btnSex.getText().toString());
        OkHttp3Util.doPostWithToken(Constants.GET_MEMBERS_URL + "/", gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(RegisterMemberActivity.this,"registerMember") {
            @Override
            public void onSuccess(String results) throws Exception {
                Toast.makeText(RegisterMemberActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
                finish();
                Intent intent = new Intent(RegisterMemberActivity.this, AddNewOrderActivity.class);
                JSONObject jsonObject = new JSONObject(results);
                intent.putExtra("memberId", jsonObject.getInt("id"));
                intent.putExtra("memberName", jsonObject.getString("last_name") +
                        jsonObject.getString("first_name"));
                startActivity(intent);
            }

            @Override
            public void onFailed(String erromsg) {
                customDialog.dismiss();
                Toast.makeText(RegisterMemberActivity.this,erromsg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
