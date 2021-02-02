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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.CashCouponAdapter;
import com.pos.priory.beans.CashCouponParamsBean;
import com.pos.priory.beans.CashCouponResultBean;
import com.pos.priory.beans.CouponParamBean;
import com.pos.priory.beans.ExchangeCashCouponParamBean;
import com.pos.priory.beans.ExchangeCashCouponReslutBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;

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
    List<CashCouponResultBean> couponBeanList = new ArrayList<>();

    List<FittingBean.ResultsBean> goodList = new ArrayList<>();

    MemberBean.ResultsBean memberBean;

    double sumAmount = 0;

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
        if(exchangeEdt.getText().toString().isEmpty()) {
            Toast.makeText(this, "請輸入兌換碼", Toast.LENGTH_SHORT).show();
            return;
        }
        ExchangeCashCouponParamBean cashCouponParamsBean = new ExchangeCashCouponParamBean();
        cashCouponParamsBean.setCode(exchangeEdt.getText().toString());
        cashCouponParamsBean.setMember_id(memberBean.getId());
        Log.e("test","params:" + gson.toJson(cashCouponParamsBean));
        RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class).exchangeCashCoupons(cashCouponParamsBean),
                new ModelGsonListener<ExchangeCashCouponReslutBean>() {
            @Override
            public void onSuccess(ExchangeCashCouponReslutBean result) throws Exception {
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
                    showToast("現金券已被使用或已失效");
                } else {
                    showToast("系統沒有相關現金券");
                }
            }
        });
    }

    private void getCoupons() {
        CashCouponParamsBean cashCouponParamsBean = new CashCouponParamsBean();
        cashCouponParamsBean.setMember_id(memberBean.getId());
        List<CashCouponParamsBean.ProductItemBean> items = new ArrayList<>();
        try {
            for (FittingBean.ResultsBean resultsBean : goodList) {
                CashCouponParamsBean.ProductItemBean itemsBean = new CashCouponParamsBean.ProductItemBean();
                itemsBean.setId(resultsBean.getId());
                itemsBean.setQuantity(resultsBean.getBuyCount());
                items.add(itemsBean);
            }
        } catch (Exception e) {

        }
        cashCouponParamsBean.setProduct_item(items);
        Log.e("test", "params:" + gson.toJson(cashCouponParamsBean));
        RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class).getCashCoupons(cashCouponParamsBean),
                new ModelGsonListener<List<CashCouponResultBean>>() {
            @Override
            public void onSuccess(List<CashCouponResultBean> result) throws Exception {
                hideLoadingDialog();
                couponBeanList.clear();
                couponBeanList.addAll(result);

                adapter.selectList.clear();


                List<CashCouponResultBean> selectedDiscountBeans = gson.fromJson(getIntent().
                        getStringExtra("selectCashCouponList"), new TypeToken<List<CashCouponResultBean>>() {
                }.getType());
                if(selectedDiscountBeans != null){
                    for(CashCouponResultBean oldresultBean : selectedDiscountBeans){
                        for(CashCouponResultBean resultBean : couponBeanList){
                            if(oldresultBean.getId() == resultBean.getId()){
                                resultBean.setSelected(true);
                                adapter.selectList.add(resultBean);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String erromsg) {
                Log.e("test",erromsg);
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
