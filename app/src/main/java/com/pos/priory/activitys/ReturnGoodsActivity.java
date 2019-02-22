package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.pos.priory.beans.CreateRefundOrderResultBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class ReturnGoodsActivity extends BaseActivity {

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
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_next)
    CardView btnNext;
    double sumMoney = 0;

    List<OrderItemBean> goodList = new ArrayList<>();
    List<OrderItemBean> tempgoodList = new ArrayList<>();
    ChangeGoodsAdapter goodsAdapter;
    String currentGoldPrice = "";
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_return_good);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY, ""),
                new TypeToken<List<StaffInfoBean>>() {
                }.getType());

        tempgoodList = gson.fromJson(getIntent().getStringExtra("checkedGoodList"),
                new TypeToken<List<OrderItemBean>>() {
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
        OkHttp3Util.doGetWithToken(Constants.GOLD_PRICE_URL, sharedPreferences, new Okhttp3StringCallback(this, "getCurrentGoldPrice") {
            @Override
            public void onSuccess(String results) throws Exception {
                currentGoldPrice = new JSONObject(results).getString("price");
                goldPriceTv.setText("當前金價(MOP)：" + currentGoldPrice + "/g" + "  " + LogicUtils.
                        getKeepLastTwoNumberAfterLittlePoint(Double.parseDouble(currentGoldPrice) * 37.5) + "/兩");
                goodsAdapter = new ChangeGoodsAdapter(ReturnGoodsActivity.this, R.layout.change_good_list_item, goodList);
                goodRecyclerView.setAdapter(goodsAdapter);
                createReturnGoodsOrder();
            }

            @Override
            public void onFailed(String erromsg) {
                if (customDialog != null) {
                    customDialog.dismiss();
                    Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }


    CustomDialog customDialog;
    CreateRefundOrderResultBean createRefundOrderResultBean;

    private void createReturnGoodsOrder() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ordernumber", getIntent().getStringExtra("ordernumber"));
        paramMap.put("type", "return");
        OkHttp3Util.doPostWithToken(Constants.CHANGE_OR_RETURN_GOOD_URL + "/", gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(this, "createChangeGoodsOrder") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        createRefundOrderResultBean = gson.fromJson(results, CreateRefundOrderResultBean.class);
                        editReturnGoodOrder();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    public List<StaffInfoBean> staffInfoBeanList;
    int accessCount = 0, tempAccessCount = 0;

    private void editReturnGoodOrder() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("staff", staffInfoBeanList.get(0).getUser());
        OkHttp3Util.doPatchWithToken(Constants.CHANGE_OR_RETURN_GOOD_URL + "/" + createRefundOrderResultBean.getId() + "/update/",
                gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(this, "editReturnGoodOrder") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        accessCount = tempgoodList.size();
                        for (OrderItemBean bean : tempgoodList) {
                            createReurnOrderItem(bean);
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        if (customDialog != null) {
                            customDialog.dismiss();
                            Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }

    private void createReurnOrderItem(final OrderItemBean orderitem) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("rmanumber", createRefundOrderResultBean.getRmanumber());
        paramMap.put("orderitemid", orderitem.getId());
        OkHttp3Util.doPostWithToken(Constants.CHANGE_OR_RETURN_GOOD_ITEM_URL + "/", gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(this, "createReurnOrderItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        createReurnStockItem(orderitem);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        if (customDialog != null) {
                            customDialog.dismiss();
                            Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }

    private void createReurnStockItem(final OrderItemBean orderitem) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("rmanumber", createRefundOrderResultBean.getRmanumber());
        paramMap.put("name", orderitem.getStock().getBatch().getProduct().getName());
        paramMap.put("quantity", orderitem.getOprateCount());
        paramMap.put("weight", 0);
        paramMap.put("location", staffInfoBeanList.get(0).getStore());
        OkHttp3Util.doPostWithToken(Constants.RETURN_STOCKS_URL + "/", gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(this, "createReurnStockItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        tempAccessCount += 1;
                        orderitem.setReturnStockId(new JSONObject(results).getInt("id"));
                        goodList.add(orderitem);
                        if (tempAccessCount == accessCount) {
                            customDialog.dismiss();
                            resetSumMoney(true);
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        if (customDialog != null) {
                            customDialog.dismiss();
                            Toast.makeText(ReturnGoodsActivity.this, "创建回收单失败", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        deleteReturnOrder();
    }

    CustomDialog deleteDialog;

    private void deleteReturnOrder() {
        if (createRefundOrderResultBean == null) {
            finish();
            return;
        }
        if (deleteDialog == null)
            deleteDialog = new CustomDialog(this, "正在删除退货订单..");
        deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                deleteDialog = null;
            }
        });
        deleteDialog.show();
        OkHttp3Util.doDeleteWithToken(Constants.CHANGE_OR_RETURN_GOOD_URL + "/" + createRefundOrderResultBean.getId() + "/update/", sharedPreferences,
                new Okhttp3StringCallback(this, "deleteReturnOrder") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        deleteDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        deleteDialog.dismiss();
                        Toast.makeText(ReturnGoodsActivity.this, "删除退货单失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private boolean isAllWeightEdtHasInput() {
        for (OrderItemBean bean : goodList) {
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
                createVoices();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    private void createVoices() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在生成回收單發票..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("rmaorder", createRefundOrderResultBean.getId());
        paramMap.put("type", "refund");
        paramMap.put("amount", sumMoney * -1);
        OkHttp3Util.doPostWithToken(Constants.CHANGE_OR_RETURN_GOOD_VOICES_URL + "/", gson.toJson(paramMap), sharedPreferences,
                new Okhttp3StringCallback(this, "createVoices") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        Intent intent = new Intent(ReturnGoodsActivity.this, ReturnBalanceActivity.class);
                        intent.putExtra("goodlist", gson.toJson(goodList));
                        intent.putExtra("sumMoney", sumMoney);
                        intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                        intent.putExtra("ordernumber", getIntent().getStringExtra("ordernumber"));
                        startActivity(intent);
                        customDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(ReturnGoodsActivity.this, "生成回收單發票失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void resetSumMoney(boolean isNotify) {
        sumMoney = 0;
        for (OrderItemBean item : goodList) {
            item.getStock().getBatch().getProduct().setPrice(item.getPrice() + "");
            setReturnOrderItemRealPrice(item);
            sumMoney += Double.parseDouble(item.getStock().getBatch().getProduct().getRealPrice()) * item.getOprateCount();
        }
        sumMoney = -1 * sumMoney;
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        if (isNotify)
            goodsAdapter.notifyDataSetChanged();
    }

    private void setReturnOrderItemRealPrice(OrderItemBean item) {
        if (item.getStock().getBatch().getProduct().getCatalog().equals("黃金")) {
            if (item.getWeight().equals("")) {
                item.getStock().getBatch().getProduct().setRealPrice("0");
            } else {
                item.getStock().getBatch().getProduct().setRealPrice(LogicUtils.getKeepLastOneNumberAfterLittlePoint(
                        Double.parseDouble(currentGoldPrice) * Double.parseDouble(item.getWeight()) * 0.95));
            }
        } else {
            item.getStock().getBatch().getProduct().setRealPrice(item.getStock().getBatch().getProduct().getPrice());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
