package com.pos.priory.activitys;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 日盤點
 */
public class DayInventoryActivity extends BaseActivity {

    @Bind(R.id.gold_count_tv)
    TextView goldCountTv;
    @Bind(R.id.gold_count_edt)
    EditText goldCountEdt;
    @Bind(R.id.spar_count_tv)
    TextView sparCountTv;
    @Bind(R.id.spar_count_edt)
    EditText sparCountEdt;
    @Bind(R.id.else_count_tv)
    TextView elseCountTv;
    @Bind(R.id.else_count_edt)
    EditText elseCountEdt;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_finish)
    CardView btnFinish;
    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.hand_count_tv)
    TextView handCountTv;
    @Bind(R.id.hand_count_edt)
    EditText handCountEdt;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_day_inventory);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
