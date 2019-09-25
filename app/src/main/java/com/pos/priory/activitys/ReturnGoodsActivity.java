package com.pos.priory.activitys;

import android.content.DialogInterface;
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
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.LogicUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class ReturnGoodsActivity extends BaseActivity {
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
    double sumMoney = 0;

    List<OrderBean.ItemsBean> goodList = new ArrayList<>();
    ChangeGoodsAdapter goodsAdapter;
    String currentGoldPrice = "";
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_return_good);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("回收单");
        rightImg.setVisibility(View.GONE);
        goodList = gson.fromJson(getIntent().getStringExtra("checkedGoodList"),
                new TypeToken<List<OrderBean.ItemsBean>>() {
                }.getType());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        getCurrentGoldPrice();
    }

    private void getCurrentGoldPrice() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在新增回收单..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        RetrofitManager.createString(ApiService.class)
                .getCurrentGoldPrice()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        currentGoldPrice = new JSONObject(s).getString("price");
                        goldPriceTv.setText("当前金价(MOP)：" + (int) (Double.parseDouble(currentGoldPrice)) + "/g" + "  " + (int) (Double.parseDouble(currentGoldPrice) * 37.5) + "/两");
                        goodsAdapter = new ChangeGoodsAdapter(ReturnGoodsActivity.this, R.layout.change_good_list_item, goodList);
                        goodRecyclerView.setAdapter(goodsAdapter);
                        resetSumMoney(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (customDialog != null) {
                            customDialog.dismiss();
                            Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }


    CustomDialog customDialog;


    private boolean isAllWeightEdtHasInput() {
        for (OrderBean.ItemsBean bean : goodList) {
            if (bean.getWeight().equals("")) {
                return false;
            }
        }
        return true;
    }


    @OnClick({R.id.btn_next, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (!isAllWeightEdtHasInput()) {
                    Toast.makeText(ReturnGoodsActivity.this, "请输入全部回收商品的重量", Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ReturnGoodsActivity.this, ReturnBalanceActivity.class);
                intent.putExtra("goodlist", gson.toJson(goodList));
                intent.putExtra("sumMoney", sumMoney);
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                intent.putExtra("ordernumber", getIntent().getStringExtra("ordernumber"));
                intent.putExtra("checkedGoodList", gson.toJson(goodList));
                startActivity(intent);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    public void resetSumMoney(boolean isNotify) {
        sumMoney = 0;
        for (OrderBean.ItemsBean item : goodList) {
            setReturnOrderItemRealPrice(item);
            sumMoney += Double.parseDouble(item.getStock().getProduct().getRealPrice()) * item.getOprateCount();
        }
        sumMoney = -1 * sumMoney;
        moneyTv.setText((int) sumMoney + "");
        if (isNotify)
            goodsAdapter.notifyDataSetChanged();
    }

    private void setReturnOrderItemRealPrice(OrderBean.ItemsBean item) {
        if (item.getWeight().equals("")) {
            item.getStock().getProduct().setRealPrice("0");
        } else {
            item.getStock().getProduct().setRealPrice((int) (
                    Double.parseDouble(currentGoldPrice) * Double.parseDouble(item.getWeight()) * 1) + "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
