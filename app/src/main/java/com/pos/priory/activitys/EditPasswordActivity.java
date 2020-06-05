package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;

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
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.btn_commit)
    MaterialButton btnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);
        initViews() ;
    }

    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("修改密码");
        setEditTextHintTextSize(edtNewPassword,12,"请输入新密码(长度大于8位，并且必须包含数字和字母)");
        setEditTextHintTextSize(edtRepeatNewPassword,12,"请再次输入新密码(长度大于8位，并且必须包含数字和字母)");
    }

    private void setEditTextHintTextSize(EditText editText,int spSize,String hintContent){
        SpannableString ss = new SpannableString(hintContent);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(spSize, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    CustomDialog customDialog;

    private void editPassword() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在修改密码..");
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
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(EditPasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(EditPasswordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.btn_commit, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (edtNewPassword.getText().toString().equals("") ||
                        edtRepeatNewPassword.getText().toString().equals("")) {
                    Toast.makeText(EditPasswordActivity.this, "新密码和确认密码输入框不能爲空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtNewPassword.getText().toString().length() < 8 ||
                        edtRepeatNewPassword.getText().length() < 8) {
                    Toast.makeText(EditPasswordActivity.this, "新密码和确认密码长度必须大于8位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (LogicUtils.isNumeric(edtNewPassword.getText().toString()) ||
                        LogicUtils.isNumeric(edtRepeatNewPassword.getText().toString())) {
                    Toast.makeText(EditPasswordActivity.this, "新密码和确认密码都不能为纯数字", Toast.LENGTH_SHORT).show();
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
