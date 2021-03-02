package com.pos.priory.activitys;

import android.app.Activity;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderDetailDiscountAdapter;
import com.pos.priory.adapters.OrderDetailPayTypeAdapter;
import com.pos.priory.adapters.OrderDetailPrintDiscountAdapter;
import com.pos.priory.adapters.OrderDetailPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetailPrintPayTypeAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.coustomViews.DisableReclyerView;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class OrderDetialActivity extends BaseActivity {
    List<OrderDetailReslutBean.OrderItemsBean> goodList = new ArrayList<>();
    List<OrderDetailReslutBean.OrderItemsBean> checkedGoodList = new ArrayList<>();
    OrderDetialGoodsAdapter goodsAdapter;
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.sex_img)
    ImageView sexImg;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.need_money_title)
    TextView needMoneyTitle;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.payed_money_title)
    TextView payedMoneyTitle;
    @Bind(R.id.payed_money_tv)
    TextView payedMoneyTv;
    @Bind(R.id.trasition_time_tv)
    TextView trasitionTimeTv;
    @Bind(R.id.update_time_tv)
    TextView updateTimeTv;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.gold_count_tv)
    TextView goldCountTv;
    @Bind(R.id.fitting_count_tv)
    TextView fittingCountTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
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
    @Bind(R.id.btn_return)
    MaterialButton btnReturn;
    @Bind(R.id.btn_change)
    MaterialButton btnChange;
    @Bind(R.id.btn_cancel)
    MaterialButton btnCancel;
    @Bind(R.id.action_layout)
    FrameLayout actionLayout;
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    @Bind(R.id.discount_recycler_view)
    DisableReclyerView discountRecyclerView;
    @Bind(R.id.paytype_recycler_view)
    DisableReclyerView paytypeRecyclerView;
    @Bind(R.id.detail_layout)
    LinearLayout detailLayout;
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;
    @Bind(R.id.btn_operator)
    TextView btnOperator;
    @Bind(R.id.operating_layout)
    LinearLayout operatingLayout;
    @Bind(R.id.operating_weight_tv)
    TextView operatingWeightTv;
    @Bind(R.id.operating_money_tv)
    TextView operatingMoneyTv;

    List<OrderDetailReslutBean.PayDetailBean.CouponsBean> discountList = new ArrayList<>();
    List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> paytypeList = new ArrayList<>();
    OrderDetailDiscountAdapter discountAdapter;
    OrderDetailPayTypeAdapter payTypeAdapter;
    @Bind(R.id.right_layout)
    FrameLayout rightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        rightLayout.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon_print);

        goodsAdapter = new OrderDetialGoodsAdapter(goodList, this);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(OrderDetialActivity.this, LinearLayoutManager.VERTICAL, false));
        goodRecyclerView.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (goodList.get(position).isSelected())
                    goodList.get(position).setSelected(false);
                else
                    goodList.get(position).setSelected(true);
                goodList.get(position).setOprateCount(1);
                adapter.notifyItemChanged(position);
                resetCheckedGoodList();
            }
        });

        discountAdapter = new OrderDetailDiscountAdapter(R.layout.order_detial_discount_list_item, discountList);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        discountRecyclerView.setAdapter(discountAdapter);

        payTypeAdapter = new OrderDetailPayTypeAdapter(R.layout.order_detial_paytype_list_item, paytypeList);
        paytypeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        paytypeRecyclerView.setAdapter(payTypeAdapter);

        int orderid = getIntent().getIntExtra("orderId", 0);
        getOrderBean(orderid);
    }

    CustomDialog customDialog;
    OrderDetailReslutBean orderDetailReslutBean;

    private void getOrderBean(int orderid) {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在查询订单信息..");
            customDialog.setOnDismissListener((dialogInterface) -> customDialog = null);
            customDialog.show();
            RetrofitManager.createGson(ApiService.class).getOrderByOrderId(orderid)
                    .compose(this.<OrderDetailReslutBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resluts -> {
                        customDialog.dismiss();
                        orderDetailReslutBean = resluts;
                        goodList.clear();
                        goodList.addAll(orderDetailReslutBean.getOrder_items());
                        for (OrderDetailReslutBean.OrderItemsBean resultsBean : goodList) {
                            resultsBean.itemType = resultsBean.isIs_gold() ? 0 : 1;
                        }
                        goodsAdapter.notifyDataSetChanged();

                        discountList.addAll(orderDetailReslutBean.getPay_detail().getCoupons());
                        discountAdapter.notifyDataSetChanged();

                        paytypeList.addAll(orderDetailReslutBean.getPay_detail().getPay_methods().getCash_coupon());
                        for (OrderDetailReslutBean.PayDetailBean.PayMethodsBean.OtherBean otherBean : orderDetailReslutBean.getPay_detail().getPay_methods().getOther()) {
                            OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean cashCouponBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                            cashCouponBean.setAmount(otherBean.getAmount());
                            cashCouponBean.setPaymethod(otherBean.getPaymethod());
                            paytypeList.add(cashCouponBean);
                        }
                        for (OrderDetailReslutBean.PayDetailBean.PayMethodsBean.ExchangeBean exchangeBean : orderDetailReslutBean.getPay_detail().getPay_methods().getExchange()) {
                            OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean cashCouponBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                            cashCouponBean.setAmount(exchangeBean.getAmount());
                            cashCouponBean.setPaymethod("換貨抵扣");
                            paytypeList.add(cashCouponBean);
                        }
                        for (OrderDetailReslutBean.PayDetailBean.PayMethodsBean.RefundBean refundBean : orderDetailReslutBean.getPay_detail().getPay_methods().getRefund()) {
                            OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean cashCouponBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                            cashCouponBean.setAmount(refundBean.getAmount());
                            cashCouponBean.setPaymethod("回收");
                            paytypeList.add(cashCouponBean);
                        }
                        payTypeAdapter.notifyDataSetChanged();

                        memberNameTv.setText("會員：" + orderDetailReslutBean.getMember_detail().getLastname() +
                              " "  + orderDetailReslutBean.getMember_detail().getFirstname());
                        sexImg.setImageResource(orderDetailReslutBean.getMember_detail().getGender() == 1 ? R.drawable.icon_boy : R.drawable.icon_girl);
                        memberPhoneTv.setText("手機號碼：" + orderDetailReslutBean.getMember_detail().getMobile());
                        needMoneyTv.setText(orderDetailReslutBean.getAmount_payable() + "元");
                        payedMoneyTv.setText(orderDetailReslutBean.getAmount_paid() + "元");

                        updateTimeTv.setText("更新時間：" + orderDetailReslutBean.getUpdated());
                        trasitionTimeTv.setText("交易時間：" + orderDetailReslutBean.getCreated());

                        orderNumberTv.setText("訂單編號：" + orderDetailReslutBean.getOrderno());

                        goldCountTv.setText("黃金：" + orderDetailReslutBean.getGold_info().getQuantity() + "件|"
                                + orderDetailReslutBean.getGold_info().getWeight() + "克");
                        fittingCountTv.setText("配件：" + orderDetailReslutBean.getQuantity_accessory() + "件");

                    }, throwable -> {
                        customDialog.dismiss();
                    });
        }
        goodsAdapter.notifyDataSetChanged();

    }

    public static void printViews(Activity activity, OrderDetailReslutBean orderDetailReslutBean, List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> paytypeList) {
        List<View> views = new ArrayList<>();
        List<OrderDetailReslutBean.OrderItemsBean> templist = new ArrayList<>();
        templist.addAll(orderDetailReslutBean.getOrder_items());
        int perPageSize = 13;
        int lastPageSize = 7;
        int sumSize = templist.size();
        int pageCount = sumSize / perPageSize;
        int remainder = sumSize % perPageSize;//餘數
        if (remainder != 0) {
            if (remainder > lastPageSize) {
                pageCount = pageCount + 2;
            } else {
                pageCount++;
            }
        } else {
            if (pageCount == 1) {
                remainder = perPageSize;
            }
        }

        Log.e("test", "pageCount:" + pageCount + " remainder:" + remainder);
        for (int i = 0; i < pageCount; i++) {
            List<OrderDetailReslutBean.OrderItemsBean> extraList = new ArrayList<>();
            if (remainder > lastPageSize) {
                if (i == (pageCount - 1)) {
                    //最後一頁放匯總
                } else if (i == (pageCount - 2)) {
                    for (int t = 0; t < remainder; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                } else {
                    Log.e("test", "i:" + i);
                    for (int t = 0; t < perPageSize; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                }
            } else {
                if (i == (pageCount - 1)) {
                    for (int t = 0; t < remainder; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                } else {
                    for (int t = 0; t < perPageSize; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                }
            }
            int layoutid = 0;
//            if (MyApplication.getContext().region.equals("中国大陆")) {
//                layoutid = R.layout.dialog_preview;
//            } else {
            layoutid = R.layout.dialog_preview2;
//            }
            final View printView = LayoutInflater.from(activity).inflate(layoutid, null);
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.getContext().staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.address_tv)).setText(MyApplication.getContext().staffInfoBean.getShop_address());
            ((TextView) printView.findViewById(R.id.tel_tv)).setText(MyApplication.getContext().staffInfoBean.getShop_tel());
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + pageCount);
            ((TextView) printView.findViewById(R.id.order_number_tv)).setText("收據編號: " + orderDetailReslutBean.getOrderno());
            ((TextView) printView.findViewById(R.id.count_tv)).setText("合計(" + (orderDetailReslutBean.getQuantity_accessory()
                    + orderDetailReslutBean.getGold_info().getQuantity()) + "件)");
            ((TextView) printView.findViewById(R.id.date_tv)).setText(("購買日期: " + orderDetailReslutBean.getCreated()));
            ((TextView) printView.findViewById(R.id.amount_tv)).setText(orderDetailReslutBean.getAmount_payable() + "元");
            ((TextView) printView.findViewById(R.id.sum_pay_tv)).setText(orderDetailReslutBean.getAmount_paid() + "元");
            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            if (i == pageCount - 1) {
                RecyclerView dcListview = (RecyclerView) printView.findViewById(R.id.discount_list);
                OrderDetailPrintDiscountAdapter dcmLayoutManageradapter = new OrderDetailPrintDiscountAdapter
                        (R.layout.order_detail_print_discount_list_item, orderDetailReslutBean.getPay_detail().getCoupons());
                LinearLayoutManager dcmLayoutManager = new LinearLayoutManager(activity);
                dcmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                dcListview.setLayoutManager(dcmLayoutManager);
                dcListview.setAdapter(dcmLayoutManageradapter);

                RecyclerView ptListview = (RecyclerView) printView.findViewById(R.id.pay_type_list);
                OrderDetailPrintPayTypeAdapter ptmLayoutManageradapter = new OrderDetailPrintPayTypeAdapter
                        (R.layout.order_detail_print_discount_list_item, paytypeList);
                LinearLayoutManager ptmLayoutManager = new LinearLayoutManager(activity);
                ptmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                ptListview.setLayoutManager(ptmLayoutManager);
                ptListview.setAdapter(ptmLayoutManageradapter);
            }
            printView.findViewById(R.id.sum_data_layout).setVisibility(i == pageCount - 1 ? View.VISIBLE : View.GONE);
            if (remainder > lastPageSize) {
                printView.findViewById(R.id.line0).setVisibility(i == pageCount - 1 ? View.GONE : View.VISIBLE);
                printView.findViewById(R.id.title_layout).setVisibility(i == pageCount - 1 ? View.GONE : View.VISIBLE);
                listview.setVisibility(i == pageCount - 1 ? View.GONE : View.VISIBLE);
            }
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        BillActivity.print(activity, views);
    }


    private void cancelOrder() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在撤回订单..");
            customDialog.setOnDismissListener(dialog -> customDialog = null);
            customDialog.show();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("member", orderDetailReslutBean.getMember());
            paramMap.put("order", orderDetailReslutBean.getId());
            Log.e("test", "param:" + gson.toJson(paramMap));
            RetrofitManager.excute(RetrofitManager.createString(ApiService.class)
                    .rollbackOrder(paramMap), new ModelListener() {
                @Override
                public void onSuccess(String result) throws Exception {
                    Log.e("test", "撤回成功");
                    customDialog.dismiss();
                    EventBus.getDefault().post(OrderFragment.UPDATE_ORDER_LIST);
                    EventBus.getDefault().post(MemberInfoActivity.UPDATE_ORDER_LIST);
                    finish();
                    Toast.makeText(OrderDetialActivity.this, "撤回订单成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String erromsg) {
                    customDialog.dismiss();
                    Log.e("test", "撤回失败:" + erromsg);
                    Toast.makeText(OrderDetialActivity.this, "撤回订单失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick({R.id.btn_change, R.id.btn_return, R.id.btn_cancel, R.id.back_btn, R.id.right_layout, R.id.btn_operator})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_layout:
                printViews(OrderDetialActivity.this, orderDetailReslutBean, paytypeList);
                break;
            case R.id.btn_change:
                goodsAdapter.operatingStatus = 1;
                goodsAdapter.notifyDataSetChanged();
                changeBottomLayout();
                break;
            case R.id.btn_return:
                goodsAdapter.operatingStatus = 2;
                goodsAdapter.notifyDataSetChanged();
                changeBottomLayout();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.btn_cancel:
                cancelOrder();
                break;
            case R.id.btn_operator:
                if (checkedGoodList.size() == 0) {
                    Toast.makeText(this, "請先選擇商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (goodsAdapter.operatingStatus == 1) {
                    Intent intent = new Intent(OrderDetialActivity.this, AddNewOrderActivity.class);
                    intent.putExtra("sumMoney", -1 * sumMoney * orderDetailReslutBean.getExchange_rate());
                    intent.putExtra("orderId", orderDetailReslutBean.getId());
                    intent.putExtra("changeGoodList", gson.toJson(checkedGoodList));
                    MemberBean.ResultsBean member = new MemberBean.ResultsBean();
                    member.setName(orderDetailReslutBean.getMember_detail().getLastname() + " " + orderDetailReslutBean.getMember_detail().getFirstname());
                    member.setReward(orderDetailReslutBean.getMember_detail().getReward());
                    member.setMobile(orderDetailReslutBean.getMember_detail().getMobile());
                    member.setId(orderDetailReslutBean.getMember_detail().getId());
                    intent.putExtra("memberInfo", gson.toJson(member));
                    startActivity(intent);
                } else if (goodsAdapter.operatingStatus == 2) {
                    Intent intent = new Intent(OrderDetialActivity.this, ReturnBalanceActivity.class);
                    intent.putExtra("sumMoney", -1 * orderDetailReslutBean.getGoldprice() * sumWeight);
                    intent.putExtra("orderId", orderDetailReslutBean.getId());
                    intent.putExtra("changeGoodList", gson.toJson(checkedGoodList));
                    MemberBean.ResultsBean member = new MemberBean.ResultsBean();
                    member.setName(orderDetailReslutBean.getMember_detail().getLastname() + " " + orderDetailReslutBean.getMember_detail().getFirstname());
                    member.setReward(orderDetailReslutBean.getMember_detail().getReward());
                    member.setMobile(orderDetailReslutBean.getMember_detail().getMobile());
                    member.setId(orderDetailReslutBean.getMember_detail().getId());
                    intent.putExtra("memberInfo", gson.toJson(member));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (goodsAdapter.operatingStatus == 0)
            finish();
        else {
            goodsAdapter.operatingStatus = 0;
            goodsAdapter.notifyDataSetChanged();
            changeBottomLayout();
        }
    }

    public void changeBottomLayout() {
        switch (goodsAdapter.operatingStatus) {
            case 0:
                detailLayout.setVisibility(View.VISIBLE);
                operatingLayout.setVisibility(View.GONE);
                backBtn.setImageResource(R.drawable.icon_back);
                break;
            case 1://change
                resetCheckedGoodList();
                detailLayout.setVisibility(View.GONE);
                operatingLayout.setVisibility(View.VISIBLE);
                goldPriceTv.setVisibility(View.GONE);
                operatingWeightTv.setVisibility(View.GONE);
                btnOperator.setText("确认换货");
                backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                break;
            case 2://return
                resetCheckedGoodList();
                detailLayout.setVisibility(View.GONE);
                operatingLayout.setVisibility(View.VISIBLE);
                goldPriceTv.setVisibility(View.VISIBLE);
                operatingWeightTv.setVisibility(View.VISIBLE);
                goldPriceTv.setText("回收金價：" + orderDetailReslutBean.getGoldprice() + "/克");
                btnOperator.setText("确认回收");
                backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                break;
        }
    }

    double sumMoney = 0, sumWeight = 0;

    public void resetCheckedGoodList() {
        checkedGoodList.clear();
        sumMoney = 0;
        sumWeight = 0;
        for (OrderDetailReslutBean.OrderItemsBean bean : goodList) {
            if (bean.isSelected()) {
                checkedGoodList.add(bean);
                sumMoney += bean.getPrice();
                sumWeight += bean.getWeight();
            }
            Log.e("itemweight", "itemweight1:" + bean.getWeight());
        }
        if (goodsAdapter.operatingStatus == 1) {
            operatingMoneyTv.setText("换货金额：" +  LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney * orderDetailReslutBean.getExchange_rate()));
        } else {
            operatingMoneyTv.setText("回收金额：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(orderDetailReslutBean.getGoldprice() * sumWeight));
            operatingWeightTv.setText("回收金重：" + sumWeight + "克");
        }
    }


}
