package com.pos.priory.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetialListActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_detial_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("訂貨清單");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
