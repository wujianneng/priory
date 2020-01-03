package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.adapters.DiscountAdapter;
import com.pos.priory.beans.DiscountBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectDiscountActivity extends BaseActivity {

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

    DiscountAdapter adapter;
    List<DiscountBean> discountList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_discount);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleTv.setText("选择优惠券");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("確定");
        discountList.add(new DiscountBean(0, "满100减10", "满100减10", false, false));
        discountList.add(new DiscountBean(0, "8折", "黄金商品打8折", false, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DiscountAdapter(R.layout.discount_list_item, discountList);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.next_tv,R.id.exchange_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectDiscountList", gson.toJson(adapter.selectList));
                setResult(2, intent);
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
            adapter.addData(new DiscountBean(0, "5折", "全场商品打5折", false, false));
        }
    }

}
