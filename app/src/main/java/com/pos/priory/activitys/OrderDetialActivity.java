package com.pos.priory.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderDetailPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.beans.TransactionBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class OrderDetialActivity extends BaseActivity {
    List<OrderItemBean> goodList = new ArrayList<>();
    List<OrderItemBean> checkedGoodList = new ArrayList<>();
    OrderDetialGoodsAdapter goodsAdapter;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.card_money_tv)
    TextView cardMoneyTv;
    @Bind(R.id.cash_money_tv)
    TextView cashMoneyTv;
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
    @Bind(R.id.right_img)
    ImageView btnPrint;
    @Bind(R.id.count_tv)
    TextView countTv;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.cash_coupon_tv)
    TextView cashCouponTv;
    @Bind(R.id.alipay_tv)
    TextView alipayTv;
    @Bind(R.id.wechatpay_tv)
    TextView wechatpayTv;
    @Bind(R.id.action_layout)
    LinearLayout actionLayout;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_order_detial);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        orderBean = gson.fromJson(getIntent().getStringExtra("order"), OrderBean.class);
        if (orderBean.getStatus().equals("已取消")) {
            btnChange.setEnabled(false);
            btnChange.setAlpha(0.5f);
            btnReturn.setEnabled(false);
            btnReturn.setAlpha(0.5f);
        } else if (orderBean.getStatus().equals("已完成")) {
            btnPrint.setVisibility(View.VISIBLE);
        }
        orderNumberTv.setText(orderBean.getOrdernumber());
        dateTv.setText(DateUtils.covertIso8601ToDate(orderBean.getCreated()));
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(orderBean.getTotalprice()));
        memberNameTv.setText(orderBean.getMember().getLast_name() + orderBean.getMember().getFirst_name());

        goodsAdapter = new OrderDetialGoodsAdapter(R.layout.order_detial_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);

        getOrderDetialData();
    }

    CustomDialog customDialog;
    TransactionBean transactionBean;
    private void getOrderDetialData() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在查询订单详情信息");
        customDialog.show();
        RetrofitManager
                .createString(ApiService.class)
                .getOrderDetailInvoice(orderBean.getOrdernumber())//先獲取訂單的發票
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String results) {
                        String invoiceNumber = "";
                        try {
                            invoiceNumber = new JSONArray(results).getJSONObject(0).getString("invoicenumber");
                        } catch (Exception e) {
                        }
                        return invoiceNumber;
                    }
                })
                .flatMap(new Function<String, Observable<List<TransactionBean>>>() {
                    @Override
                    public Observable<List<TransactionBean>> apply(String s) throws Exception {
                        return RetrofitManager.createGson(ApiService.class).getOrderDetailTransaction(s);//再根據發票號，獲取交易流水信息
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<List<TransactionBean>, Observable<List<OrderItemBean>>>() {
                    @Override
                    public Observable<List<OrderItemBean>> apply(List<TransactionBean> transactionBeans) throws Exception {
                        transactionBean = transactionBeans.get(0);
                        return RetrofitManager.createGson(ApiService.class).getOrderDetailItems(orderBean.getOrdernumber(), "true");//最後根據訂單號，獲取訂單商品列表
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OrderItemBean>>() {
                    @Override
                    public void accept(List<OrderItemBean> orderItemBeanList) throws Exception {
                        customDialog.dismiss();
                        if (transactionBean != null) {
                            if (transactionBean.getPaymentmethod().equals("現金")) {
                                cashMoneyTv.setText(transactionBean.getAmount());
                                cardMoneyTv.setText(0 + "");
                            } else {
                                cardMoneyTv.setText(transactionBean.getAmount());
                                cashMoneyTv.setText(0 + "");
                            }
                        }
                        if (orderItemBeanList != null) {
                            goodList.addAll(orderItemBeanList);
                            goodsAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(OrderDetialActivity.this, "获取订单详情信息失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }



    public static void printViews(final Activity activity, List<OrderItemBean> goodList, String orderNumber,
                                                String memberName, String createDate, double sumMoney, int storeId) {
        List<View> views = new ArrayList<>();
        final View printView = LayoutInflater.from(activity).inflate(R.layout.dialog_preview, null);
        ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
        ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(memberName);
        ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
        ((TextView) printView.findViewById(R.id.good_size_tv)).setText("共" + goodList.size() + "件");
        ((TextView) printView.findViewById(R.id.sum_money_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        if (storeId == 4) {
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.GONE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.VISIBLE);
        } else {
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.VISIBLE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.GONE);
        }
        RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
        List<OrderItemBean> templist = new ArrayList<>();
        for(int i = 0 ; i < 300 ;i++){
            templist.add(goodList.get(0));
        }
        if(templist.size() > 13){
            List<OrderItemBean> reatList = new ArrayList<>();
            int tsize = templist.size();
            for(int i = 0 ; i < tsize ;i++){
                if(i <= 12){
                    reatList.add(templist.get(i));
                }
            }
            OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, reatList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);

            int size = (int)((templist.size() - 13)/30);
            int a = (templist.size() - 13)%30;
            if(a != 0){
                size++;
            }
            Log.e("test","size:" + size + " a:" + a);
            for(int i = 0 ; i < size ; i++){
                List<OrderItemBean> extraList = new ArrayList<>();
                if(i == (size - 1)){
                    for(int t = 0 ; t < a ; t++){
                        extraList.add(templist.get(t + 30 * i + 13));
                    }
                }else {
                    for(int t = 0 ; t < 30 ; t++){
                        extraList.add(templist.get(t + 30 * i + 13));
                    }
                }
                View extraListView = LayoutInflater.from(activity).inflate(R.layout.print_extra_list_view, null);
                RecyclerView recyclerView = (RecyclerView) extraListView.findViewById(R.id.extra_list);
                OrderDetailPrintGoodsAdapter adapter1 = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
                LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(activity);
                mLayoutManager1.setOrientation(OrientationHelper.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager1);
                recyclerView.setAdapter(adapter1);
                views.add(extraListView);
            }
        } else {
            OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, templist);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);
        }
        Log.e("test","viewsize:" + views.size());
        BillActivity.print(activity, views);
    }


    @OnClick({R.id.btn_change, R.id.btn_return, R.id.back_btn, R.id.right_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_img:
                printViews(this, goodList, orderBean.getOrdernumber(), memberNameTv.getText().toString(),
                        dateTv.getText().toString(), orderBean.getTotalprice(), MyApplication.staffInfoBean.getStoreid());
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
