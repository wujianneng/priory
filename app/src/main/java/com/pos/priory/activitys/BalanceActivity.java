package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.PayTypeAdapter;
import com.pos.priory.beans.CashCouponResultBean;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.CreateOrderParamsBean;
import com.pos.priory.beans.CreateOrderResultBean;
import com.pos.priory.beans.ExchangeCashCouponBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.PayTypesResultBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.utils.DeviceUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BalanceActivity extends BaseActivity {

    BigDecimal sumMoney = new BigDecimal(0), needPayMoney = new BigDecimal(0), payedMoney = new BigDecimal(0);
    String checkedGoodListString;
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
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.sex_img)
    ImageView sexImg;
    @Bind(R.id.member_reward_tv)
    TextView memberRewardTv;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.member_layout)
    RelativeLayout memberLayout;
    @Bind(R.id.exchange_coupon_btn)
    MaterialButton exchangeCouponBtn;
    @Bind(R.id.use_coupon_btn)
    MaterialButton useCouponBtn;
    @Bind(R.id.add_pay_btn)
    MaterialButton addPayBtn;
    @Bind(R.id.good_recycler_view)
    SwipeMenuRecyclerView goodRecyclerView;
    @Bind(R.id.payment_tv)
    TextView paymentTv;
    @Bind(R.id.payed_tv)
    TextView payedTv;
    @Bind(R.id.need_tv)
    TextView needTv;

    PayTypeAdapter adapter;

    List<FittingBean.ResultsBean> goodList = new ArrayList<>();
    List<ExchangeCashCouponBean.ResultBean.RewardListBean> exchangCashList = new ArrayList<>();
    List<CashCouponResultBean> couponBeanList = new ArrayList<>();
    List<PayTypesResultBean.ResultsBean> selectedPayTypeList = new ArrayList<>();
    List<PayTypesResultBean.ResultsBean> orderPayTypeList = new ArrayList<>();
    MemberBean.ResultsBean memberBean;

    String cache_token;
    int order_type = 0;

    double pay_spread = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        pay_spread = getIntent().getDoubleExtra("pay_spread",0);
        cache_token = getIntent().getStringExtra("cache_token");
        order_type = getIntent().getIntExtra("order_type",0);
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);
        goodList = gson.fromJson(getIntent().getStringExtra("goodlist"),
                new TypeToken<List<FittingBean.ResultsBean>>() {
                }.getType());

        titleTv.setText("合計付款");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("結算");
        double sMoney = getIntent().getDoubleExtra("sumMoney", 0);
        checkedGoodListString = getIntent().getStringExtra("checkedGoodList");
        sumMoney = new BigDecimal(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sMoney));


        memberNameTv.setText("会员:  " + memberBean.getName());
        memberPhoneTv.setText("联係电话:  " + memberBean.getMobile());
        memberRewardTv.setText("积分:  " + memberBean.getReward() + "分");
        needPayMoney = sumMoney;
        paymentTv.setText("客戶應付：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue()));
        payedTv.setText("已付款：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(payedMoney.doubleValue()));
        needTv.setText("馀额: " + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue() - payedMoney.doubleValue()));

        goodRecyclerView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
            SwipeMenuItem deleteItem = new SwipeMenuItem(BalanceActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(BalanceActivity.this, R.color.drag_btn_red))
                    .setImage(R.drawable.icon_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setHeight(DeviceUtil.dip2px(BalanceActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                    .setWidth(DeviceUtil.dip2px(BalanceActivity.this, 100));//设置宽
            swipeRightMenu.addMenuItem(deleteItem);//设置右边的侧滑
        });
        //设置侧滑菜单的点击事件
        goodRecyclerView.setSwipeMenuItemClickListener(menuBridge -> {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (menuPosition == 0) {
                deleteOrderItem(adapterPosition);
            }
        });
        adapter = new PayTypeAdapter(R.layout.pay_type_list_item, orderPayTypeList, this);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        goodRecyclerView.setAdapter(adapter);
    }

    private void deleteOrderItem(int adapterPosition) {
        if (orderPayTypeList.get(adapterPosition).getId() == -1) {//现金券
            CashCouponResultBean deleteBean = null;
            for (CashCouponResultBean resultsBean : couponBeanList) {
                if (resultsBean.getName().equals(orderPayTypeList.get(adapterPosition).getName())) {
                    deleteBean = resultsBean;
                }
            }
            couponBeanList.remove(deleteBean);
        } else if (orderPayTypeList.get(adapterPosition).getId() == -2) {//积分兑换
            exchangCashList.clear();
        } else {//付款方式
            PayTypesResultBean.ResultsBean deletePaytype = null;
            for (PayTypesResultBean.ResultsBean resultsBean : selectedPayTypeList) {
                if (resultsBean.getId() == orderPayTypeList.get(adapterPosition).getId()) {
                    deletePaytype = resultsBean;
                }
            }
            selectedPayTypeList.remove(deletePaytype);
        }
        adapter.remove(adapterPosition);
    }


    @OnClick({R.id.next_tv, R.id.back_btn, R.id.add_pay_btn, R.id.exchange_coupon_btn, R.id.use_coupon_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                if ((needPayMoney.doubleValue() - payedMoney.doubleValue()) > pay_spread) {
                    Toast.makeText(BalanceActivity.this, "还需付" +
                            LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue() - payedMoney.doubleValue()), Toast.LENGTH_SHORT).show();
                    return;
                }else if((needPayMoney.doubleValue() - payedMoney.doubleValue()) < -1 * pay_spread){
                    Toast.makeText(BalanceActivity.this, "支付金額大於應付金額！", Toast.LENGTH_SHORT).show();
                    return;
                }
                createOrder();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_pay_btn:
                showSelectPayType();
                break;
            case R.id.exchange_coupon_btn:
                Intent intent = new Intent(BalanceActivity.this, ExchangeCashCouponActivity.class);
                intent.putExtra("memberInfo", gson.toJson(memberBean));
                startActivityForResult(intent, 100);
                break;
            case R.id.use_coupon_btn:
                Intent intent1 = new Intent(BalanceActivity.this, SelectCashCouponActivity.class);
                intent1.putExtra("memberInfo", gson.toJson(memberBean));
                intent1.putExtra("goodlist", gson.toJson(goodList));
                startActivityForResult(intent1, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Log.e("test", "list:" + data.getStringExtra("selectCashCouponList"));
            couponBeanList = gson.fromJson(data.getStringExtra("selectCashCouponList"),
                    new TypeToken<List<CashCouponResultBean>>() {
                    }.getType());
            updatePayList(true);
        } else if (resultCode == 2) {
            exchangCashList = gson.fromJson(data.getStringExtra("selectExchangeCashCouponList"),
                    new TypeToken<List<ExchangeCashCouponBean.ResultBean.RewardListBean>>() {
                    }.getType());
            updatePayList(true);
        }
    }


    private void showSelectPayType() {
        showLoadingDialog("正在獲取付款方式列表...");
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getPayTypes(), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                PayTypesResultBean payTypesResultBean = gson.fromJson(result, PayTypesResultBean.class);
                hideLoadingDialog();
                String[] payType = new String[payTypesResultBean.getResults().size()];
                boolean[] booleans = new boolean[payTypesResultBean.getResults().size()];
                for (int i = 0; i < payTypesResultBean.getResults().size(); i++) {
                    payType[i] = payTypesResultBean.getResults().get(i).getName();
                    booleans[i] = false;
                }
                new AlertDialog.Builder(BalanceActivity.this).setTitle("請選擇付款方式")
                        .setMultiChoiceItems(payType, booleans, (dialog, which, isChecked) -> {
                            booleans[which] = isChecked;
                        })
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("確定", (dialog, which) -> {
                            dialog.dismiss();
                            selectedPayTypeList.clear();
                            for (int i = 0; i < booleans.length; i++) {
                                if (booleans[i]) {
                                    selectedPayTypeList.add(payTypesResultBean.getResults().get(i));
                                }
                            }
                            updatePayList(true);
                        })
                        .create().show();
            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
                showToast("獲取付款方式列表失敗");
            }
        });
    }

    public void updatePayList(boolean isUpdateList) {
        if (isUpdateList)
            orderPayTypeList.clear();
        payedMoney = new BigDecimal(0);
        for (PayTypesResultBean.ResultsBean resultsBean : selectedPayTypeList) {
            if (isUpdateList) {
                orderPayTypeList.add(resultsBean);
            }
            payedMoney = new BigDecimal(payedMoney.doubleValue() + resultsBean.getPayAmount());
        }
        for (CashCouponResultBean resultsBean : couponBeanList) {
            if(isUpdateList) {
                PayTypesResultBean.ResultsBean paybean = new PayTypesResultBean.ResultsBean();
                paybean.setId(-1);
                paybean.setName(resultsBean.getName());
                paybean.setPayAmount(resultsBean.getValue());
                orderPayTypeList.add(paybean);
            }
            payedMoney = new BigDecimal(payedMoney.doubleValue() + resultsBean.getValue());
        }
        if (isUpdateList)
            adapter.notifyDataSetChanged();


        paymentTv.setText("客戶應付：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue()));
        payedTv.setText("已付款：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(payedMoney.doubleValue()));
        needTv.setText("馀额: " + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue() - payedMoney.doubleValue()));
    }


    CustomDialog customDialog;

    private void createOrder() {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在结算..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            CreateOrderParamsBean paramsBean = new CreateOrderParamsBean();
            paramsBean.setCache_token(cache_token);
            paramsBean.setMember(memberBean.getId());
            paramsBean.setOrder_type(order_type);
            paramsBean.setShop(MyApplication.staffInfoBean.getShopid());


            List<CreateOrderParamsBean.PaymethodsBean> paymentsBeanList = new ArrayList<>();
            if (couponBeanList.size() != 0) {
                List<Integer> cashcouponIds = new ArrayList<>();
                for (CashCouponResultBean resultsBean : couponBeanList) {
                    cashcouponIds.add(resultsBean.getId());

                    CreateOrderParamsBean.PaymethodsBean paymentsBean = new CreateOrderParamsBean.PaymethodsBean();
                    paymentsBean.setId(resultsBean.getId());
                    paymentsBean.setAmount(resultsBean.getValue());
                    paymentsBean.setCash_coupon_id(resultsBean.getId());
                    paymentsBeanList.add(paymentsBean);
                }
                paramsBean.setCash_coupons(cashcouponIds);
            }

            if (selectedPayTypeList.size() != 0) {
                for (PayTypesResultBean.ResultsBean resultsBean : selectedPayTypeList) {
                    CreateOrderParamsBean.PaymethodsBean paymentsBean = new CreateOrderParamsBean.PaymethodsBean();
                    paymentsBean.setId(resultsBean.getId());
                    paymentsBean.setAmount(resultsBean.getPayAmount());
                    paymentsBeanList.add(paymentsBean);
                }
            }
            paramsBean.setPaymethods(paymentsBeanList);
            paramsBean.setAmount_payable(needPayMoney);
            Log.e("test", "paramMap:" + gson.toJson(paramsBean));
            RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class)
                    .createOrder(paramsBean), new ModelGsonListener<CreateOrderResultBean>() {
                @Override
                public void onSuccess(CreateOrderResultBean result) throws Exception {
                    customDialog.dismiss();
                    Intent intent = new Intent(BalanceActivity.this, BillActivity.class);
                    intent.putExtra("goodlist", getIntent().getStringExtra("goodlist"));
                    intent.putExtra("sumMoney", getIntent().getDoubleExtra("newOrderSumMoney", 0));
                    intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                    intent.putExtra("receiveMoney", sumMoney.doubleValue());
                    intent.putExtra("returnMoney", 0);
                    intent.putExtra("ordernumber", result.getOrderno());
                    startActivity(intent);
                    ColseActivityUtils.finishWholeFuntionActivitys();
                    finish();
                }

                @Override
                public void onFailed(String erromsg) {
                    customDialog.dismiss();
                    Log.e("test", "erromsg:" + erromsg);
                    Toast.makeText(BalanceActivity.this, "结算失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
