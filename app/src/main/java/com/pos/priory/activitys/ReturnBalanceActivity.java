package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderDiscountAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.utils.ColseActivityUtils;
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
        moneyTv.setText((int)(sumMoney / 100) + "");
    }

    @OnClick({R.id.btn_finish, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                Intent intent = new Intent(ReturnBalanceActivity.this, BillActivity.class);
                List<OrderItemBean> orderItemBeanList = gson.fromJson(getIntent()
                        .getStringExtra("goodlist"),new TypeToken<List<OrderItemBean>>(){}.getType());
                intent.putExtra("goodlist",gson.toJson(covertOrderItemListToGoodbeanList(orderItemBeanList)));
                intent.putExtra("sumMoney",sumMoney);
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                intent.putExtra("receiveMoney",0);
                intent.putExtra("returnMoney",sumMoney);
                intent.putExtra("ordernumber",getIntent().getStringExtra("ordernumber"));
                startActivity(intent);
                ColseActivityUtils.finishWholeFuntionActivitys();
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private List<GoodBean> covertOrderItemListToGoodbeanList(List<OrderItemBean> orderItemBeanList){
        List<GoodBean> goodBeanList = new ArrayList<>();
        for(OrderItemBean orderItemBean : orderItemBeanList){
            GoodBean goodBean = new GoodBean();
            goodBean.setSaleCount(orderItemBean.getOprateCount());
            goodBean.setQuantity(orderItemBean.getStock().getQuantity());
            goodBean.setId(orderItemBean.getId());
            GoodBean.LocationBean locationBean = new GoodBean.LocationBean();
            locationBean.setName(orderItemBean.getStock().getLocation().getName());
            goodBean.setLocation(locationBean);
            GoodBean.BatchBean batchBean = new GoodBean.BatchBean();
            GoodBean.BatchBean.ProductBean productBean = new GoodBean.BatchBean.ProductBean();
            productBean.setName(orderItemBean.getStock().getBatch().getProduct().getName());
            productBean.setPrice(orderItemBean.getStock().getBatch().getProduct().getRealPrice());
            productBean.setId(orderItemBean.getStock().getBatch().getProduct().getId());
            productBean.setProductcode(orderItemBean.getStock().getBatch().getProduct().getProductcode());
            productBean.setImage(orderItemBean.getStock().getBatch().getProduct().getImage());
            batchBean.setProduct(productBean);
            goodBean.setBatch(batchBean);
            goodBeanList.add(goodBean);
        }
        return goodBeanList;
    }

}
