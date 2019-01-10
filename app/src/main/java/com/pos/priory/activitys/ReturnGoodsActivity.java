package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderDiscountAdapter;
import com.pos.priory.adapters.ChangeGoodsAdapter;
import com.pos.priory.beans.CreateRefundOrderResultBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
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
        goodsAdapter = new ChangeGoodsAdapter(this,R.layout.change_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        createReturnGoodsOrder();
    }

    CustomDialog customDialog;
    CreateRefundOrderResultBean createRefundOrderResultBean;
    private void createReturnGoodsOrder() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在新增换货订单..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
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
                        Toast.makeText(ReturnGoodsActivity.this,"创建回收单失败",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    public List<StaffInfoBean> staffInfoBeanList;
    int accessCount = 0,tempAccessCount = 0;
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
                        if(customDialog != null) {
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
                        if(customDialog != null) {
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
        paramMap.put("name", orderitem.getStock().getProduct().getName());
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
                        if(tempAccessCount == accessCount) {
                            customDialog.dismiss();
                            goodsAdapter.notifyDataSetChanged();
                            resetSumMoney();
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        if(customDialog != null) {
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


    @OnClick({R.id.btn_next, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                Intent intent = new Intent(ReturnGoodsActivity.this,ReturnBalanceActivity.class);
                intent.putExtra("goodlist",gson.toJson(goodList));
                intent.putExtra("sumMoney",sumMoney);
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                intent.putExtra("ordernumber",getIntent().getStringExtra("ordernumber"));
                startActivity(intent);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    private void resetSumMoney() {
        for (OrderItemBean bean : goodList) {
            sumMoney += Double.parseDouble(bean.getStock().getProduct().getPrice()) * Constants.RETURN_GOOD_RAGE
                    * bean.getOprateCount();
        }
        sumMoney = -1 * sumMoney;
        moneyTv.setText(sumMoney + "");
    }

}
