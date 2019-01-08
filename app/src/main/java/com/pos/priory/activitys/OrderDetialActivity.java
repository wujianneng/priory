package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderDetialDiscountAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class OrderDetialActivity extends BaseActivity {
    List<OrderItemBean> goodList = new ArrayList<>();
    List<OrderItemBean> checkedGoodList = new ArrayList<>();
    List<String> discountList = new ArrayList<>();
    OrderDetialDiscountAdapter discountAdapter;
    OrderDetialGoodsAdapter goodsAdapter;
    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_unit_tv)
    TextView moneyUnitTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.card_money_tv)
    TextView cardMoneyTv;
    @Bind(R.id.cash_money_tv)
    TextView cashMoneyTv;
    @Bind(R.id.small_change_tv)
    TextView smallChangeTv;
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.discount_recycler_view)
    RecyclerView discountRecyclerView;
    @Bind(R.id.icon_change)
    ImageView iconChange;
    @Bind(R.id.text_scan)
    TextView textScan;
    @Bind(R.id.btn_change)
    CardView btnChange;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_return)
    CardView btnReturn;
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    OrderBean orderBean;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.discount_layout)
    CardView discountLayout;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_order_detial);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        orderBean = gson.fromJson(getIntent().getStringExtra("order"), OrderBean.class);
        orderNumberTv.setText(orderBean.getOrdernumber());
        dateTv.setText(orderBean.getCreated());
        moneyTv.setText(orderBean.getTotalprice() + "");
        memberNameTv.setText(orderBean.getMember().getLast_name() + orderBean.getMember().getFirst_name());

        discountList.add("0");
        discountList.add("0");
        discountList.add("0");
        discountList.add("0");
        discountList.add("0");
        discountAdapter = new OrderDetialDiscountAdapter(R.layout.order_detial_discount_list_item, discountList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        discountRecyclerView.setLayoutManager(gridLayoutManager);
        discountRecyclerView.setAdapter(discountAdapter);

        goodsAdapter = new OrderDetialGoodsAdapter(R.layout.order_detial_good_list_item, goodList);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderItemBean orderItemBean = goodList.get(position);
                if(view.getId() == R.id.decrease_btn){
                    if(orderItemBean.getOprateCount() > 1){
                        orderItemBean.setOprateCount(orderItemBean.getOprateCount() - 1);
                        goodsAdapter.notifyItemChanged(position);
                    }
                }else if(view.getId() == R.id.increase_btn){
                    if(orderItemBean.getOprateCount() < orderItemBean.getQuantity()){
                        orderItemBean.setOprateCount(orderItemBean.getOprateCount() + 1);
                        goodsAdapter.notifyItemChanged(position);
                    }else {
                        Toast.makeText(OrderDetialActivity.this,"已达到最大操作数量",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);

        getOrderDetialData();
    }

    private void getOrderDetialData() {
        OkHttp3Util.doGetWithToken(Constants.GET_ORDER_ITEM_URL + "?ordernumber=" + orderBean.getOrdernumber(), sharedPreferences,
                new Okhttp3StringCallback(OrderDetialActivity.this, "getOrderDetialData") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        List<OrderItemBean> orderItemBeanList = gson.fromJson(results, new TypeToken<List<OrderItemBean>>
                                () {
                        }.getType());
                        if (orderItemBeanList != null) {
                            goodList.addAll(orderItemBeanList);
                            goodsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }


    @OnClick({R.id.btn_change, R.id.btn_return, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change:
                resetCheckedGoodList();
                if(checkedGoodList.size() == 0){
                    Toast.makeText(OrderDetialActivity.this,"请选择换货商品",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(OrderDetialActivity.this, ChangeGoodsActivity.class);
                intent.putExtra("checkedGoodList",gson.toJson(checkedGoodList));
                intent.putExtra("memberId",orderBean.getMember().getId());
                intent.putExtra("memberName", orderBean.getMember().getLast_name() +
                        orderBean.getMember().getLast_name());
                intent.putExtra("ordernumber",orderBean.getOrdernumber());
                startActivity(intent);
                break;
            case R.id.btn_return:
                resetCheckedGoodList();
                if(checkedGoodList.size() == 0){
                    Toast.makeText(OrderDetialActivity.this,"请选择退货商品",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(OrderDetialActivity.this, ReturnGoodsActivity.class);
                intent2.putExtra("checkedGoodList",gson.toJson(checkedGoodList));
                intent2.putExtra("memberId",orderBean.getMember().getId());
                intent2.putExtra("memberName", orderBean.getMember().getLast_name() +
                        orderBean.getMember().getLast_name());
                intent2.putExtra("ordernumber",orderBean.getOrdernumber());
                startActivity(intent2);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void resetCheckedGoodList(){
        checkedGoodList.clear();
        for(OrderItemBean bean : goodList){
            if(bean.isSelected()){
                checkedGoodList.add(bean);
            }
        }
    }
}
