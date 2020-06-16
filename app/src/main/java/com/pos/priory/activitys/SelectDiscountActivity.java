package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
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
import com.pos.priory.adapters.DiscountAdapter;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<CouponResultBean.ResultBean> discountList = new ArrayList<>();

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

        titleTv.setText("选择优惠券");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("確定");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DiscountAdapter(R.layout.discount_list_item, discountList);
        recyclerView.setAdapter(adapter);
        getCoupons();
    }

    private void exchangeCoupon() {
        showLoadingDialog("正在兌換...");
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
        Log.e("test", "params:" + gson.toJson(params));
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getCoupons(params), new ModelListener() {
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
                    showToast("優惠券已被使用");
                } else {
                    showToast("未能找到此優惠券");
                }
            }
        });
    }

    private void getCoupons() {
        showLoadingDialog("正在獲取優惠券列表...");
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
        Log.e("test", "params:" + gson.toJson(params));
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getCoupons(params), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                CouponResultBean resultBean = gson.fromJson(result, CouponResultBean.class);
                discountList.clear();
                discountList.addAll(resultBean.getResult());
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
                intent.putExtra("selectDiscountList", gson.toJson(adapter.selectList));
                setResult(2, intent);
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
