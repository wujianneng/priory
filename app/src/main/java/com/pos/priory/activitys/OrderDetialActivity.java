package com.pos.priory.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.BillPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetailPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.beans.TransactionBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import org.json.JSONArray;

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
    @Bind(R.id.btn_print)
    CardView btnPrint;
    public List<StaffInfoBean> staffInfoBeanList;


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
        staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY,""),
                new TypeToken<List<StaffInfoBean>>(){}.getType());
        orderBean = gson.fromJson(getIntent().getStringExtra("order"), OrderBean.class);
        if (orderBean.getStatus().equals("已取消")) {
            btnChange.setEnabled(false);
            btnChange.setAlpha(0.5f);
            btnReturn.setEnabled(false);
            btnReturn.setAlpha(0.5f);
        }else if(orderBean.getStatus().equals("已完成")){
            btnPrint.setVisibility(View.VISIBLE);
        }
        orderNumberTv.setText(orderBean.getOrdernumber());
        dateTv.setText(DateUtils.covertIso8601ToDate(orderBean.getCreated()));
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(orderBean.getTotalprice()));
        memberNameTv.setText(orderBean.getMember().getLast_name() + orderBean.getMember().getFirst_name());

        goodsAdapter = new OrderDetialGoodsAdapter(R.layout.order_detial_good_list_item, goodList);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderItemBean orderItemBean = goodList.get(position);
                if (view.getId() == R.id.decrease_btn) {
                    if (orderItemBean.getOprateCount() > 1) {
                        orderItemBean.setOprateCount(orderItemBean.getOprateCount() - 1);
                        goodsAdapter.notifyItemChanged(position);
                    }
                } else if (view.getId() == R.id.increase_btn) {
                    if (orderItemBean.getOprateCount() < orderItemBean.getQuantity()) {
                        orderItemBean.setOprateCount(orderItemBean.getOprateCount() + 1);
                        goodsAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(OrderDetialActivity.this, "已达到最大操作数量", Toast.LENGTH_SHORT).show();
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

    private void getOrderDetialGoodList() {
        OkHttp3Util.doGetWithToken(Constants.GET_ORDER_ITEM_URL + "?ordernumber=" + orderBean.getOrdernumber() + "&rmaaction=true", sharedPreferences,
                new Okhttp3StringCallback(OrderDetialActivity.this, "getOrderDetialGoodList") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        if (transactionBean != null) {
                            if (transactionBean.getPaymentmethod().equals("現金")) {
                                cashMoneyTv.setText(transactionBean.getAmount());
                                cardMoneyTv.setText(0 + "");
                            } else {
                                cardMoneyTv.setText(transactionBean.getAmount());
                                cashMoneyTv.setText(0 + "");
                            }
                            smallChangeTv.setText(0 + "");
                        }
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
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    CustomDialog customDialog;

    private void getOrderDetialData() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在查询订单详情信息");
        customDialog.show();
        OkHttp3Util.doGetWithToken(Constants.INVOICES_URL + "?ordernumber=" + orderBean.getOrdernumber(), sharedPreferences,
                new Okhttp3StringCallback(OrderDetialActivity.this, "getOrderDetialData") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        getOrderTransaction(new JSONArray(results).getJSONObject(0).getString("invoicenumber"));
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    TransactionBean transactionBean;

    private void getOrderTransaction(String invoicenumber) {
        OkHttp3Util.doGetWithToken(Constants.TRANSACTION_URL + "?invoicenumber=" + invoicenumber, sharedPreferences,
                new Okhttp3StringCallback(OrderDetialActivity.this, "getOrderTransaction") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        List<TransactionBean> transactionlist = gson.fromJson(results, new TypeToken<List<TransactionBean>>() {
                        }.getType());
                        transactionBean = transactionlist.get(0);
                        getOrderDetialGoodList();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(previewDialog != null)
            previewDialog.dismiss();
    }

    AlertDialog previewDialog;
    public static AlertDialog showPreviewDialog(final Activity activity, List<OrderItemBean> goodList, String orderNumber,
                                         String memberName, String createDate, double sumMoney,int storeId) {
        final View printView = LayoutInflater.from(activity).inflate(R.layout.dialog_preview, null);
        ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
        ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(memberName);
        ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
        ((TextView) printView.findViewById(R.id.good_size_tv)).setText("共" + goodList.size() + "件");
        ((TextView) printView.findViewById(R.id.sum_money_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        if(storeId == 4){
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.GONE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.VISIBLE);
        }else {
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.VISIBLE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.GONE);
        }
        RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
        OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        listview.setLayoutManager(mLayoutManager);
        listview.setAdapter(adapter);
        final AlertDialog previewDialog = new AlertDialog.Builder(activity).setView(printView)
                .setCancelable(false)
                .create();
        previewDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BillActivity.print(activity,printView);
            }
        }, 1000);
        return previewDialog;
    }


    @OnClick({R.id.btn_change, R.id.btn_return, R.id.back_btn,R.id.btn_print})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_print:
                previewDialog = showPreviewDialog(this,goodList,orderBean.getOrdernumber(),memberNameTv.getText().toString(),
                        dateTv.getText().toString(),orderBean.getTotalprice(),staffInfoBeanList.get(0).getStoreid());
                break;
            case R.id.btn_change:
                resetCheckedGoodList(false);
                if (checkedGoodList.size() == 0) {
                    Toast.makeText(OrderDetialActivity.this, "请选择换货商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(OrderDetialActivity.this, ChangeGoodsActivity.class);
                intent.putExtra("checkedGoodList", gson.toJson(checkedGoodList));
                intent.putExtra("memberId", orderBean.getMember().getId());
                intent.putExtra("memberName", orderBean.getMember().getLast_name() +
                        orderBean.getMember().getLast_name());
                intent.putExtra("ordernumber", orderBean.getOrdernumber());
                startActivity(intent);
                break;
            case R.id.btn_return:
                resetCheckedGoodList(true);
                if (checkedGoodList.size() == 0) {
                    Toast.makeText(OrderDetialActivity.this, "请选择退货商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(OrderDetialActivity.this, ReturnGoodsActivity.class);
                intent2.putExtra("checkedGoodList", gson.toJson(checkedGoodList));
                intent2.putExtra("memberId", orderBean.getMember().getId());
                intent2.putExtra("memberName", orderBean.getMember().getLast_name() +
                        orderBean.getMember().getLast_name());
                intent2.putExtra("ordernumber", orderBean.getOrdernumber());
                startActivity(intent2);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void resetCheckedGoodList(boolean isReturn) {
        checkedGoodList.clear();
        for (OrderItemBean bean : goodList) {
            if (bean.isSelected()) {
                if (bean.getStock().getBatch().getProduct().getCatalog().equals("其他") && isReturn) {
                    Toast.makeText(OrderDetialActivity.this, "只有黃金類型商品才能進行回收", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkedGoodList.add(bean);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
