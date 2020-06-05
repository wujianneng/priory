package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.adapters.ExchangeCashCouponAdapter;
import com.pos.priory.beans.ExchangeCashCouponBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeCashCouponActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.next_tv)
    TextView nextTv;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.usable_integal_tv)
    TextView usableIntegalTv;
    @Bind(R.id.used_integal_tv)
    TextView usedIntegalTv;
    @Bind(R.id.own_integal_tv)
    TextView ownIntegalTv;

    ExchangeCashCouponAdapter adapter;
    List<ExchangeCashCouponBean> datalist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_cash_coupon);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleTv.setText("积分换现金券");
        nextTv.setText("確定");
        nextTv.setVisibility(View.VISIBLE);
        datalist.add(new ExchangeCashCouponBean("$300",-30000));
        datalist.add(new ExchangeCashCouponBean("$100",-10000));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new ExchangeCashCouponAdapter(R.layout.exchange_coupon_list_item,datalist);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.next_tv, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectCashCouponList", gson.toJson(adapter.selectList));
                setResult(2, intent);
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
