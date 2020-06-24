package com.pos.priory.activitys;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.CashCouponAdapter;
import com.pos.priory.beans.CashCouponBean;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<CouponResultBean.ResultBean> couponBeanList = new ArrayList<>();

    List<FittingBean.ResultsBean> goodList = new ArrayList<>();
    MemberBean.ResultsBean memberBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_discount);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);
        goodList = gson.fromJson(getIntent().getStringExtra("goodlist"),
                new TypeToken<List<FittingBean.ResultsBean>>() {
                }.getType());

        titleTv.setText("选择現金券");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("確定");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CashCouponAdapter(R.layout.cash_coupon_list_item, couponBeanList);
        recyclerView.setAdapter(adapter);

        getCoupons();
    }

    private void exchangeCoupon() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);
        params.put("member_id", memberBean.getId());
        params.put("code", exchangeEdt.getText().toString());
        JsonArray jsonArray = new JsonArray();
        try {
            for (FittingBean.ResultsBean resultsBean : goodList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("itemid", resultsBean.getId());
                jsonObject.addProperty("qty", resultsBean.getBuyCount());
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {

        }
        params.put("items", jsonArray);
        Log.e("test","params:" + gson.toJson(params));
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getCashCoupons(params), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                exchangeEdt.setText("");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("兌換成功");
                        getCoupons();
                    }
                }, 100);

            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
                if (erromsg.contains("302")) {
                    showToast("優惠券已被使用或已失效");
                } else {
                    showToast("未能找到此優惠券");
                }
            }
        });
    }

    private void getCoupons() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 2);
        params.put("member_id", memberBean.getId());
        JsonArray jsonArray = new JsonArray();
        try {
            for (FittingBean.ResultsBean resultsBean : goodList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("itemid", resultsBean.getId());
                jsonObject.addProperty("qty", resultsBean.getBuyCount());
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {

        }
        params.put("items", jsonArray);
        Log.e("test","params:" + gson.toJson(params));
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getCashCoupons(params), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                CouponResultBean resultBean = gson.fromJson(result, CouponResultBean.class);
                couponBeanList.clear();
                couponBeanList.addAll(resultBean.getResult());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
            }
        });
    }


    @OnClick({R.id.back_btn,R.id.next_tv, R.id.exchange_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectCashCouponList", gson.toJson(adapter.selectList));
                setResult(1, intent);
                finish();
                break;
            case R.id.exchange_btn:
                exchangeCoupon();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

}
