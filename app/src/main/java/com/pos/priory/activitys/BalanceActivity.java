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
    List<PayTypesResultBean.ResultsBean> orderPayTypeList = new ArrayList<>();
    List<CouponResultBean> discountBeans = new ArrayList<>();
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
        pay_spread = getIntent().getDoubleExtra("pay_spread", 0);
        cache_token = getIntent().getStringExtra("cache_token");
        order_type = getIntent().getIntExtra("order_type", 0);
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);
        goodList = gson.fromJson(getIntent().getStringExtra("goodlist"),
                new TypeToken<List<FittingBean.ResultsBean>>() {
                }.getType());
        discountBeans = gson.fromJson(getIntent().getStringExtra("couponList"),
                new TypeToken<List<CouponResultBean>>() {
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
                    .setHeight(DeviceUtil.dip2px(BalanceActivity.this, 61))//设置高，这里使用match_parent，就是与item的高相同
                    .setWidth(DeviceUtil.dip2px(BalanceActivity.this, 100));//设置宽
            swipeRightMenu.addMenuItem(deleteItem);//设置右边的侧滑
        });
        //设置侧滑菜单的点击事件
        goodRecyclerView.setSwipeMenuItemClickListener(menuBridge -> {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (menuPosition == 0) {
                removePayListItem(adapterPosition);
            }
        });
        adapter = new PayTypeAdapter(R.layout.pay_type_list_item, orderPayTypeList, this);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        goodRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.next_tv, R.id.back_btn, R.id.add_pay_btn, R.id.exchange_coupon_btn, R.id.use_coupon_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                if ((needPayMoney.doubleValue() - payedMoney.doubleValue()) > pay_spread) {
                    Toast.makeText(BalanceActivity.this, "还需付" +
                            LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney.doubleValue() - payedMoney.doubleValue()), Toast.LENGTH_SHORT).show();
                    return;
                } else if ((needPayMoney.doubleValue() - payedMoney.doubleValue()) < -1 * pay_spread) {
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
                intent1.putExtra("selectCashCouponList", gson.toJson(selectCashCouponList));
                startActivityForResult(intent1, 100);
                break;
        }
    }

    List<CashCouponResultBean> selectCashCouponList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Log.e("test", "list:" + data.getStringExtra("selectCashCouponList"));
            List<CashCouponResultBean> couponBeanList = gson.fromJson(data.getStringExtra("selectCashCouponList"),
                    new TypeToken<List<CashCouponResultBean>>() {
                    }.getType());
            if (couponBeanList != null) {
                selectCashCouponList.clear();
                selectCashCouponList.addAll(couponBeanList);
                for (CashCouponResultBean resultsBean : selectCashCouponList) {
                    if (isPayListContainsCoupon(resultsBean.getId()) == -1) {
                        PayTypesResultBean.ResultsBean paybean = new PayTypesResultBean.ResultsBean();
                        paybean.setId(resultsBean.getPaymethod_id());
                        paybean.setName(resultsBean.getName());
                        paybean.setPayAmount(resultsBean.getValue());
                        paybean.setCouponId(resultsBean.getId());
                        paybean.setCashCoupon(true);
                        addPayListItem(paybean);
                    }
                }
                List<Integer> needDeleteList = new ArrayList<>();
                for (int i = 0; i < orderPayTypeList.size(); i++) {
                    if(orderPayTypeList.get(i).isCashCoupon()) {
                        if(!isCouponListContains(selectCashCouponList,orderPayTypeList.get(i).getCouponId())){
                            needDeleteList.add(i);
                        }
                    }
                }
                for(Integer index : needDeleteList){
                    removePayListItem(index);
                }

            }

        } else if (resultCode == 2) {
//            exchangCashList = gson.fromJson(data.getStringExtra("selectExchangeCashCouponList"),
//                    new TypeToken<List<ExchangeCashCouponBean.ResultBean.RewardListBean>>() {
//                    }.getType());
//            updatePayList(true);
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
                    for (PayTypesResultBean.ResultsBean resultsBean : orderPayTypeList) {
                        if (resultsBean.getName().equals(payType[i])) {
                            booleans[i] = true;
                        }
                    }
                }
                new AlertDialog.Builder(BalanceActivity.this).setTitle("請選擇付款方式")
                        .setMultiChoiceItems(payType, booleans, (dialog, which, isChecked) -> {
                            booleans[which] = isChecked;
                        })
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("確定", (dialog, which) -> {
                            dialog.dismiss();
                            for (int i = 0; i < booleans.length; i++) {
                                int isContain = isPayListContains(payType[i]);
                                if (booleans[i]) {
                                    if (isContain == -1)
                                        addPayListItem(payTypesResultBean.getResults().get(i));
                                } else {
                                    if (isContain != -1)
                                        removePayListItem(isContain);
                                }
                            }

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

    private boolean isCouponListContains(List<CashCouponResultBean> list,int id) {
        boolean result = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                result = true;
            }
        }
        return result;
    }

    private int isPayListContainsCoupon(int id) {
        int result = -1;
        for (int i = 0; i < orderPayTypeList.size(); i++) {
            if (orderPayTypeList.get(i).getCouponId() == id) {
                result = i;
            }
        }
        return result;
    }

    private int isPayListContains(String name) {
        int result = -1;
        for (int i = 0; i < orderPayTypeList.size(); i++) {
            if (orderPayTypeList.get(i).getName().equals(name)) {
                result = i;
            }
        }
        return result;
    }

    public void addPayListItem(PayTypesResultBean.ResultsBean resultsBean) {
        orderPayTypeList.add(resultsBean);
        adapter.notifyDataSetChanged();
        retCaculationMoneys();
    }

    public void removePayListItem(int position) {
        if(orderPayTypeList.get(position).isCashCoupon()){
            int removeIndex = 0;
            for(int i = 0 ; i < selectCashCouponList.size() ; i++){
                if(orderPayTypeList.get(position).getId() == selectCashCouponList.get(i).getId()){
                    removeIndex = i;
                }
            }
            selectCashCouponList.remove(removeIndex);
        }
        orderPayTypeList.remove(position);
        adapter.notifyDataSetChanged();
        retCaculationMoneys();
    }

    public void retCaculationMoneys() {
        payedMoney = new BigDecimal(0);
        for (PayTypesResultBean.ResultsBean resultsBean : orderPayTypeList) {
            payedMoney = new BigDecimal(payedMoney.doubleValue() + resultsBean.getPayAmount());
        }
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
            if (orderPayTypeList.size() != 0) {
                for (PayTypesResultBean.ResultsBean resultsBean : orderPayTypeList) {
                    CreateOrderParamsBean.PaymethodsBean paymentsBean = new CreateOrderParamsBean.PaymethodsBean();
                    paymentsBean.setId(resultsBean.getId());
                    paymentsBean.setAmount(resultsBean.getPayAmount() == -1 ? 0 : resultsBean.getPayAmount());
                    paymentsBean.setCash_coupon_id(resultsBean.getCouponId());
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
                    intent.putExtra("orderPayTypeList", gson.toJson(orderPayTypeList));
                    intent.putExtra("couponList", gson.toJson(discountBeans));
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
