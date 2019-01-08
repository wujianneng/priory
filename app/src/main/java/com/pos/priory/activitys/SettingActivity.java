package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.utils.ColseActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edit_passwrod_btn)
    CardView editPasswrodBtn;
    @Bind(R.id.login_out_btn)
    CardView loginOutBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.edit_passwrod_btn, R.id.login_out_btn,R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_passwrod_btn:
                startActivity(new Intent(SettingActivity.this,EditPasswordActivity.class));
                break;
            case R.id.login_out_btn:
                Log.e("test","cliklogout");
                ColseActivityUtils.closeAllAcitivty();
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
