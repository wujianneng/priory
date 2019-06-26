package com.pos.priory.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
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

import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderDetailPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;

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

public class OrderDetialActivity extends BaseActivity {
    List<OrderBean.ItemsBean> goodList = new ArrayList<>();
    List<OrderBean.ItemsBean> checkedGoodList = new ArrayList<>();
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
    @Bind(R.id.btn_change)
    MaterialButton btnChange;
    @Bind(R.id.btn_return)
    MaterialButton btnReturn;
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
        goodsAdapter = new OrderDetialGoodsAdapter(R.layout.order_detial_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(OrderDetialActivity.this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        int orderid = getIntent().getIntExtra("orderId",0);
        getOrderBean(orderid);
    }

    CustomDialog customDialog;
    private void getOrderBean(int orderid) {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在查询订单信息..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            RetrofitManager.createString(ApiService.class).getOrderByOrderId(orderid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String results) throws Exception {
                            customDialog.dismiss();
                            orderBean = gson.fromJson(results, OrderBean.class);
                            Log.e("test","results:" + gson.toJson(orderBean));
                            if (orderBean.getStatus().equals("取消")) {
                                btnChange.setEnabled(false);
                                btnChange.setAlpha(0.5f);
                                btnReturn.setEnabled(false);
                                btnReturn.setAlpha(0.5f);
                            } else {
                                btnPrint.setVisibility(View.VISIBLE);
                                btnPrint.setImageResource(R.drawable.dayin);
                            }
                            orderNumberTv.setText("订单号：" + orderBean.getOrdernumber());
                            dateTv.setText(DateUtils.covertIso8601ToDate(orderBean.getCreated()));
                            moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(orderBean.getTotalprice()));
                            memberPhoneTv.setText("联繫电话：" + orderBean.getMember().getMobile());
                            memberNameTv.setText("会员：" + orderBean.getMember().getLast_name() + orderBean.getMember().getFirst_name());
                            if (orderBean.getInvoiceitem() != null)
                                for (OrderBean.InvoiceitemBean.TransactionitemBean bean : orderBean.getInvoiceitem().getTransactionitem()) {
                                    Log.e("test","getPaymentmethod:" + bean.getPaymentmethod());
                                    if (bean.getPaymentmethod().equals("信用卡")) {
                                        cardMoneyTv.setText(bean.getAmount());
                                    } else if (bean.getPaymentmethod().equals("现金")) {
                                        cashMoneyTv.setText(bean.getAmount());
                                    } else if (bean.getPaymentmethod().equals("现金券")) {
                                        cashCouponTv.setText(bean.getAmount());
                                    } else if (bean.getPaymentmethod().equals("支付宝")) {
                                        alipayTv.setText(bean.getAmount());
                                    } else if (bean.getPaymentmethod().equals("微信支付")) {
                                        wechatpayTv.setText(bean.getAmount());
                                    }
                                }
                            goodList.addAll(orderBean.getItems());
                            countTv.setText(goodList.size() + "件|" + orderBean.getItemsweight() + "g");
                            goodsAdapter.notifyDataSetChanged();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            customDialog.dismiss();
                        }
                    });
        }
    }

    public static void printViews(final Activity activity, List<OrderBean.ItemsBean> goodList, String orderNumber,
                                  String memberName, String createDate, double sumMoney, int storeId) {
        List<View> views = new ArrayList<>();
        List<OrderBean.ItemsBean> templist = new ArrayList<>();
        templist.addAll(goodList);
        int perPageSize = 8;
        int size = templist.size() / perPageSize;
        int a = templist.size() % perPageSize;
        if (a != 0) {
            size++;
        }
        Log.e("test", "size:" + size + " a:" + a);
        for (int i = 0; i < size; i++) {
            List<OrderBean.ItemsBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                for (int t = 0; t < a; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            }
            int layoutid = 0;
            if (MyApplication.getContext().region.equals("中国大陆")) {
                layoutid = R.layout.dialog_preview;
            } else {
                layoutid = R.layout.dialog_preview2;
            }
            final View printView = LayoutInflater.from(activity).inflate(layoutid, null);
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.getContext().storeName);
            ((TextView) printView.findViewById(R.id.address_tv)).setText(MyApplication.getContext().storeAddress);
            ((TextView) printView.findViewById(R.id.tel_tv)).setText(MyApplication.getContext().storeTel);
            ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
            ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(memberName);
            ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);
            ((TextView) printView.findViewById(R.id.good_size_tv)).setText(templist.size() + "");
            ((TextView) printView.findViewById(R.id.sum_money_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
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
                intent.putExtra("memberMobile",orderBean.getMember().getMobile());
                intent.putExtra("memberReward",orderBean.getMember().getReward());
                intent.putExtra("memberName",orderBean.getMember().getLast_name() +
                        orderBean.getMember().getFirst_name());
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
        for (OrderBean.ItemsBean bean : goodList) {
            if (bean.isSelected()) {
                if (bean.getStock().getProduct().getCatalog().equals("其他") && isReturn) {
                    Toast.makeText(OrderDetialActivity.this, "只有黄金类型商品才能进行回收", Toast.LENGTH_SHORT).show();
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
