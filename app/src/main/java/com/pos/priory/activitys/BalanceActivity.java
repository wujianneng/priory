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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.PayTypeAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.PayTypeBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.utils.DeviceUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BalanceActivity extends BaseActivity {

    double sumMoney = 0, needPayMoney = 0;
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
    List<PayTypeBean> payTypeBeanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        titleTv.setText("合計付款");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("結算");
        sumMoney = getIntent().getDoubleExtra("sumMoney", 0);
        checkedGoodListString = getIntent().getStringExtra("checkedGoodList");
        sumMoney = new BigDecimal(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney)).doubleValue();
        paymentTv.setText(sumMoney + "");
        memberNameTv.setText("会员:  " + getIntent().getStringExtra("memberName"));
        memberPhoneTv.setText("联係电话:  " + getIntent().getStringExtra("memberMobile"));
        int reward = getIntent().getIntExtra("memberReward", 0);
        memberRewardTv.setText("积分:  " + reward + "分");
        needPayMoney = sumMoney;
        needTv.setText("馀额: " + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));

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
        adapter = new PayTypeAdapter(R.layout.pay_type_list_item, payTypeBeanList);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        goodRecyclerView.setAdapter(adapter);
    }

    private void deleteOrderItem(int adapterPosition) {
        adapter.remove(adapterPosition);
    }


    @OnClick({R.id.next_tv, R.id.back_btn, R.id.add_pay_btn, R.id.exchange_coupon_btn, R.id.use_coupon_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                if (needPayMoney > 0) {
                    Toast.makeText(BalanceActivity.this, "还需付" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney), Toast.LENGTH_SHORT).show();
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
                startActivityForResult(new Intent(BalanceActivity.this,ExchangeCashCouponActivity.class),100);
                break;
            case R.id.use_coupon_btn:
                startActivityForResult(new Intent(BalanceActivity.this,SelectCashCouponActivity.class),100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            Log.e("test","list:" + data.getStringExtra("selectCashCouponList"));
        }else if(resultCode == 2){
            Log.e("test","list2:" + data.getStringExtra("selectCashCouponList"));
        }
    }

    private void showSelectPayType() {
        String[] payType = new String[]{"現金", "信用卡", "微信", "支付寶"};
        boolean[] booleans = new boolean[payType.length];
        for (boolean b : booleans) {
            b = false;
        }
        new AlertDialog.Builder(this).setTitle("請選擇付款方式")
                .setMultiChoiceItems(payType, booleans, (dialog, which, isChecked) -> {
                    booleans[which] = isChecked;
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("確定", (dialog, which) -> {
                    dialog.dismiss();
                    payTypeBeanList.clear();
                    for (int i = 0; i < booleans.length; i++) {
                        if (booleans[i]) {
                            PayTypeBean payTypeBean = new PayTypeBean();
                            payTypeBean.setPayTypeName(payType[i]);
                            payTypeBean.setPayAmount(0);
                            payTypeBeanList.add(payTypeBean);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .create().show();
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
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("member", getIntent().getIntExtra("memberId", 0));
            List<Map<String, Object>> items = new ArrayList<>();
            List<GoodBean> goodlist = gson.fromJson(getIntent().getStringExtra("goodlist"), new TypeToken<List<GoodBean>>() {
            }.getType());
            for (GoodBean goodBean : goodlist) {
                Map<String, Object> item = new HashMap<>();
                item.put("stock", goodBean.getId());
                item.put("discount", goodBean.getDiscountId());
                items.add(item);
            }
            paramMap.put("items", items);
//            if (hasPayedCashMoney != 0 && radioBtnCash.isChecked())
//                paramMap.put("cash", hasPayedCashMoney);
//            if (hasPayedAlipayMoney != 0 && radioBtnAlipay.isChecked())
//                paramMap.put("alipay", hasPayedAlipayMoney);
//            if (hasPayedWechatMoney != 0 && radioBtnWechat.isChecked())
//                paramMap.put("wechatpay", hasPayedWechatMoney);
//            if (hasPayedCardMoney != 0 && radioBtnCard.isChecked())
//                paramMap.put("creditcard", hasPayedCardMoney);
//            if (hasPayedCouponMoney != 0 && radioBtnCoupon.isChecked())
//                paramMap.put("voucher", hasPayedCouponMoney);
//            if (radioBtnIntegral.isChecked())
//                paramMap.put("reward", hasPayedIntegralMoney);
            if (checkedGoodListString != null) {
//                List<OrderBean.ItemsBean> itemsBeanList = gson.fromJson(getIntent().getStringExtra("checkedGoodList"),
//                        new TypeToken<List<OrderBean.ItemsBean>>() {
//                        }.getType());
//                List<Map<String, Object>> returnorderitems = new ArrayList<>();
//                for (OrderBean.ItemsBean itemsBean : itemsBeanList) {
//                    Map<String, Object> returnorderitemMap = new HashMap<>();
//                    returnorderitemMap.put("orderitemid", itemsBean.getId());
//                    returnorderitemMap.put("weight", itemsBean.getWeight());
//                    returnorderitems.add(returnorderitemMap);
//                }
//                paramMap.put("returnorderitems", returnorderitems);
            }
            Log.e("test", "paramMap:" + gson.toJson(paramMap));
            RetrofitManager.createString(ApiService.class)
                    .createOrder(RequestBody.create(MediaType.parse("application/json"), gson.toJson(paramMap)))
                    .compose(this.<String>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Log.e("test", "结算成功");
                        JSONObject jsonObject = new JSONObject(s);
                        customDialog.dismiss();
                        Intent intent = new Intent(BalanceActivity.this, BillActivity.class);
                        intent.putExtra("goodlist", getIntent().getStringExtra("goodlist"));
                        intent.putExtra("sumMoney", getIntent().getDoubleExtra("newOrderSumMoney", 0));
                        intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                        intent.putExtra("receiveMoney", sumMoney);
                        intent.putExtra("returnMoney", needPayMoney * -1);
                        intent.putExtra("ordernumber", jsonObject.getString("ordernumber"));
                        startActivity(intent);
                        ColseActivityUtils.finishWholeFuntionActivitys();
                        finish();
                    }, t -> {
                        customDialog.dismiss();
                        Log.e("test", "throwable:" + t.getMessage());
                        Toast.makeText(BalanceActivity.this, "结算失败", Toast.LENGTH_SHORT).show();
                    });
        }
    }


}
