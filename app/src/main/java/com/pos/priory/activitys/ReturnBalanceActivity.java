package com.pos.priory.activitys;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.utils.ColseActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class ReturnBalanceActivity extends BaseActivity {

    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.index_tv)
    TextView indexTv;
    @Bind(R.id.tip_tv)
    TextView tipTv;
    @Bind(R.id.tip_layout)
    FrameLayout tipLayout;
    @Bind(R.id.edt_cas_money)
    TextView edtCasMoney;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_finish)
    CardView btnFinish;
    double sumMoney = 0;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_return_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        sumMoney = -1 * getIntent().getDoubleExtra("sumMoney", 0);
        edtCasMoney.setText(sumMoney + "");
        moneyTv.setText((int)sumMoney + "");
    }

    @OnClick({R.id.btn_finish, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                sharedPreferences.edit().putBoolean("isRefreshOrderFragment",true).commit();
                ColseActivityUtils.finishWholeFuntionActivitys();
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }


}
