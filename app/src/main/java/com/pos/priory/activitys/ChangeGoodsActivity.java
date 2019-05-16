package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.ChangeGoodsAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class ChangeGoodsActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_unit_tv)
    TextView moneyUnitTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    @Bind(R.id.btn_next)
    MaterialButton btnNext;

    List<OrderBean.ItemsBean> goodList = new ArrayList<>();
    ChangeGoodsAdapter goodsAdapter;

    double sumMoney = 0;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_change_good);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("換貨單");
        rightImg.setVisibility(View.GONE);
        goodList = gson.fromJson(getIntent().getStringExtra("checkedGoodList"),
                new TypeToken<List<OrderBean.ItemsBean>>() {
                }.getType());
        goodsAdapter = new ChangeGoodsAdapter(this, R.layout.change_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        resetSumMoney();
    }



    @OnClick({R.id.btn_next, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (!isAllWeightEdtHasInput()) {
                    Toast.makeText(ChangeGoodsActivity.this, "请输入全部换货商品的重量", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ChangeGoodsActivity.this, AddNewOrderActivity.class);
                intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                intent.putExtra("sumMoney", sumMoney);
                intent.putExtra("checkedGoodList",gson.toJson(goodList));
                startActivity(intent);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }


    private boolean isAllWeightEdtHasInput() {
        for (OrderBean.ItemsBean bean : goodList) {
            if (bean.getWeight().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void resetSumMoney() {
        for (OrderBean.ItemsBean bean : goodList) {
            sumMoney += bean.getPrice() * bean.getOprateCount();
        }
        sumMoney = -1 * sumMoney;
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        goodsAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
