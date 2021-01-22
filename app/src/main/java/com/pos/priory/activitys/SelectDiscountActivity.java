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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.DiscountAdapter;
import com.pos.priory.beans.CouponParamBean;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.ExchangeCouponParamBean;
import com.pos.priory.beans.ExchangeCouponReslutBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;

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
    List<CouponResultBean> discountList = new ArrayList<>();

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
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter ad, View view, int position) {
                if (view.getId() == R.id.checkbox) {
                    if (((CheckBox) view).isChecked())
                        adapter.selectList.add(discountList.get(position));
                    else adapter.selectList.remove(discountList.get(position));
                    discountList.get(position).setSelected(((CheckBox) view).isChecked());
                    try {
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                    Log.e("test", "isChecked：" + ((CheckBox) view).isChecked() + " size:" + adapter.selectList.size());
                }
            }
        });
        getCoupons();
    }

    private void exchangeCoupon() {
        if(exchangeEdt.getText().toString().isEmpty())
            return;
        showLoadingDialog("正在兌換...");
        ExchangeCouponParamBean exchangeCouponParamBean = new ExchangeCouponParamBean();
        exchangeCouponParamBean.setCode(exchangeEdt.getText().toString());
        exchangeCouponParamBean.setMember(memberBean.getId());
        Log.e("test", "params:" + gson.toJson(exchangeCouponParamBean));
        RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class).exchangeCoupon(exchangeCouponParamBean), new ModelGsonListener<ExchangeCouponReslutBean>() {
            @Override
            public void onSuccess(ExchangeCouponReslutBean result) throws Exception {
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
        showLoadingDialog("正在獲取優惠券列表...");
        CouponParamBean cashCouponParamsBean = new CouponParamBean();
        cashCouponParamsBean.setMember(memberBean.getId());
        List<CouponParamBean.ProductsItemsBean> items = new ArrayList<>();
        try {
            for (FittingBean.ResultsBean resultsBean : goodList) {
                CouponParamBean.ProductsItemsBean itemsBean = new CouponParamBean.ProductsItemsBean();
                itemsBean.setId(resultsBean.getId());
                itemsBean.setQuantity(resultsBean.getBuyCount());
                itemsBean.setCategory(resultsBean.getCategoryId());
                itemsBean.setAmount((int) (resultsBean.getBuyCount() * resultsBean.getPrice().get(0).getPrice()));
                items.add(itemsBean);
            }
        } catch (Exception e) {

        }
        cashCouponParamsBean.setProducts_items(items);
        Log.e("test", "params:" + gson.toJson(cashCouponParamsBean));
        RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class).getCoupons(cashCouponParamsBean), new ModelGsonListener<List<CouponResultBean>>() {
            @Override
            public void onSuccess(List<CouponResultBean> result) throws Exception {
                hideLoadingDialog();
                discountList.clear();
                discountList.addAll(result);
                adapter.selectList.clear();


                List<CouponResultBean> selectedDiscountBeans = gson.fromJson(getIntent().
                        getStringExtra("couponList"), new TypeToken<List<CouponResultBean>>() {
                }.getType());
                if(selectedDiscountBeans != null){
                    for(CouponResultBean oldresultBean : selectedDiscountBeans){
                        for(CouponResultBean resultBean : discountList){
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
                hideLoadingDialog();
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.next_tv, R.id.exchange_btn})
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
