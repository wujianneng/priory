package com.pos.priory.activitys;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.adapters.CashCouponAdapter;
import com.pos.priory.adapters.DiscountAdapter;
import com.pos.priory.beans.CashCouponBean;
import com.pos.priory.beans.DiscountBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCashCouponActivity extends BaseActivity {

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
    @Bind(R.id.exchange_edt)
    EditText exchangeEdt;
    @Bind(R.id.exchange_btn)
    MaterialButton exchangeBtn;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    CashCouponAdapter adapter;
    List<CashCouponBean> couponBeanList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_discount);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleTv.setText("选择現金券");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("確定");
        couponBeanList.add(new CashCouponBean("xx活動現金券", "#139843984394830", "20", "2019-12-01", "2019-12-21"));
        couponBeanList.add(new CashCouponBean("客服贈送", "#47574584908540", "100", "2019-12-01", "2019-12-11"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CashCouponAdapter(R.layout.cash_coupon_list_item, couponBeanList);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.next_tv, R.id.exchange_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectCashCouponList", gson.toJson(adapter.selectList));
                setResult(1, intent);
                finish();
                break;
            case R.id.exchange_btn:
                doExchange();
                break;
        }
    }

    private void doExchange() {
        if (exchangeEdt.getText().toString().equals("123456")) {
            exchangeEdt.setText("");
            adapter.addData(new CashCouponBean("兌換碼兌換", "#557574584954540", "50", "2019-12-01", "2019-12-11"));
        }
    }
}
