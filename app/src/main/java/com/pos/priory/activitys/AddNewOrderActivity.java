package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderGoodsAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.MipcaActivityCapture;
import com.pos.zxinglib.utils.DeviceUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

import java.math.BigDecimal;
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

public class AddNewOrderActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.good_recycler_view)
    SwipeMenuRecyclerView goodRecyclerView;

    List<GoodBean> goodList = new ArrayList<>();
    AddNewOrderGoodsAdapter goodsAdapter;


    int memberid, memberReward;
    String memberName, memberMobile;

    double sumMoney = 0, changeGoodsMoeny = 0;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.count_tv)
    TextView countTv;
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.sex_img)
    ImageView sexImg;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.member_reward_tv)
    TextView memberRewardTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.icon_scan)
    ImageView iconScan;
    @Bind(R.id.text_scan)
    TextView textScan;
    @Bind(R.id.btn_scan)
    CardView btnScan;
    @Bind(R.id.icon_add)
    ImageView iconAdd;
    @Bind(R.id.text_add)
    TextView textAdd;
    @Bind(R.id.btn_add)
    CardView btnAdd;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_next)
    CardView btnNext;

    double sumWeight = 0;


    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_add_new_order);
        ButterKnife.bind(this);
    }


    @Override
    protected void initViews() {
        titleTv.setText("增加商品");
        rightImg.setVisibility(View.GONE);
        changeGoodsMoeny = getIntent().getIntExtra("sumMoney", 0);
        Log.e("test","changegm:" + changeGoodsMoeny);

        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));

        goodsAdapter = new AddNewOrderGoodsAdapter(this, R.layout.add_new_order_good_list_item, goodList);
        //设置侧滑菜单
        goodRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem discountItem = new SwipeMenuItem(AddNewOrderActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(AddNewOrderActivity.this, R.color.drag_btn_green))
                        .setImage(R.drawable.icon_control)
                        .setText("折扣")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(AddNewOrderActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(AddNewOrderActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(discountItem);//设置右边的侧滑
                SwipeMenuItem deleteItem = new SwipeMenuItem(AddNewOrderActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(AddNewOrderActivity.this, R.color.drag_btn_red))
                        .setImage(R.drawable.icon_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(AddNewOrderActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(AddNewOrderActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(deleteItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        goodRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showChoiceDiscountDialog(adapterPosition);
                } else {
                    deleteOrderItem(adapterPosition);
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        memberid = getIntent().getIntExtra("memberId", 0);
        memberMobile = getIntent().getStringExtra("memberMobile");
        memberReward = getIntent().getIntExtra("memberReward", 0);
        memberName = getIntent().getStringExtra("memberName");
        memberNameTv.setText("会员：" + memberName);
        memberPhoneTv.setText("联繫电话：" + memberMobile);
        memberRewardTv.setText("积分：" + memberReward);
    }

    List<String> discountNames = new ArrayList<>();


    CustomDialog customDialog;
    private void refreshSumMoney() {
        sumMoney = 0;
        sumWeight = 0;
        for (GoodBean bean : goodList) {
            sumMoney += new BigDecimal(bean.getProduct().getPrice()).doubleValue();
            sumWeight += bean.getWeight();
        }
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
        countTv.setText(goodList.size() + "件|" + LogicUtils.getKeepLastTwoNumberAfterLittlePoint(sumWeight) + "g");
    }

    int yourChoice;
    AlertDialog choiceSexDialog;


    private void showChoiceDiscountDialog(final int position) {
        if (choiceSexDialog == null) {
            discountNames.clear();
            for (GoodBean.ProductBean.CatalogBean.DiscountsBean bean : goodList.get(position).getProduct().getCatalog().getDiscounts()) {
                discountNames.add(bean.getName());
            }
            if (discountNames.size() == 0) {
                discountNames.add("无折扣");
            }
            if (discountNames.size() == 1 && discountNames.get(0).equals("无折扣")) {
                yourChoice = 0;
            } else {
                for (int i = 0; i < goodList.get(position).getProduct().getCatalog().getDiscounts().size(); i++) {
                    if (goodList.get(position).getDiscountRate() == new BigDecimal(goodList.get(position).getProduct().getCatalog().getDiscounts().get(i).getValue()).doubleValue()) {
                        yourChoice = i;
                    }
                }
            }
            Log.e("yourChoice", "yourChoice:" + yourChoice);
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("请选择折扣");
            // 第二个参数是默认选项，此处设置为0
            ListAdapter adapter = new ArrayAdapter<String>(AddNewOrderActivity.this,
                    android.R.layout.simple_list_item_single_choice, discountNames);
            singleChoiceDialog.setSingleChoiceItems(adapter, yourChoice,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yourChoice = which;
                        }
                    });
            singleChoiceDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yourChoice != -1) {
                                if (discountNames.size() == 1 && discountNames.get(0).equals("无折扣")) {
                                    editOrderItemOnOperate(position, 1,1, "调折扣");
                                } else {
                                    editOrderItemOnOperate(position, new BigDecimal(goodList.get(position).getProduct().getCatalog().getDiscounts().get(yourChoice).getValue()).doubleValue(),
                                            goodList.get(position).getProduct().getCatalog().getDiscounts().get(yourChoice).getId(), "调折扣");
                                }
                            }
                            choiceSexDialog.dismiss();
                        }
                    });
            choiceSexDialog = singleChoiceDialog.create();
            choiceSexDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    choiceSexDialog = null;
                }
            });
            choiceSexDialog.show();
        }
    }

    @OnClick({R.id.btn_scan, R.id.btn_next, R.id.btn_add, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                startActivityForResult(new Intent(AddNewOrderActivity.this, MipcaActivityCapture.class), 1000);
                break;
            case R.id.btn_add:
                showInputCodeDialog();
                break;
            case R.id.btn_next:
                if(changeGoodsMoeny == 0){
                    if (sumMoney <= 0) {
                        Toast.makeText(AddNewOrderActivity.this, "请先增加购买商品", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    if ((sumMoney + changeGoodsMoeny) < 0) {
                        Toast.makeText(AddNewOrderActivity.this, "新商品总额要大于等于换货商品总额", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Intent intent = new Intent(AddNewOrderActivity.this, BalanceActivity.class);
                intent.putExtra("goodlist", gson.toJson(goodList));
                intent.putExtra("sumMoney", sumMoney + changeGoodsMoeny);
                intent.putExtra("newOrderSumMoney", sumMoney);
                intent.putExtra("memberId", memberid);
                intent.putExtra("memberName", memberName);
                intent.putExtra("memberMobile", memberMobile);
                intent.putExtra("memberReward", memberReward);
                intent.putExtra("sumCount", goodList.size());
                intent.putExtra("sumWeight", sumWeight);
                intent.putExtra("checkedGoodList",getIntent().getStringExtra("checkedGoodList"));
                startActivity(intent);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    AlertDialog actionDialog;

    private void showInputCodeDialog() {
        if (actionDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_code, null);
            actionDialog = new AlertDialog.Builder(this).setView(view)
                    .create();
            final EditText edt_count = (EditText) view.findViewById(R.id.edt_count);
            view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!edt_count.getText().toString().equals(""))
                        getGoodBeanByCode(edt_count.getText().toString());
                    actionDialog.dismiss();
                }
            });
            actionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    actionDialog = null;
                }
            });
            actionDialog.show();
            Window window = actionDialog.getWindow();
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.inventory_dialog_bg));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = DeviceUtil.dip2px(this, 200);
            layoutParams.height = DeviceUtil.dip2px(this, 170);
            window.setGravity(Gravity.CENTER);
            window.setAttributes(layoutParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                Log.e("result", "result:" + data.getStringExtra("resultString"));
                getGoodBeanByCode(data.getStringExtra("resultString"));
                break;
        }
    }


    private void getGoodBeanByCode(String productcode) {
        if(productExitInList(productcode)){
            Toast.makeText(AddNewOrderActivity.this, "已增加该商品", Toast.LENGTH_SHORT).show();
            return;
        }
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在增加商品..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        RetrofitManager.createString(ApiService.class)
                .getStockListByParam(productcode)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        List<GoodBean> goodBeanList = gson.fromJson(jsonObject.getJSONArray("results").toString(), new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null && goodBeanList.size() != 0 && goodBeanList.get(0).getQuantity() > 0) {
                            customDialog.dismiss();
                            goodBeanList.get(0).getProduct().setPrePrice(goodBeanList.get(0).getProduct().getPrice());
                            goodList.add(goodBeanList.get(0));
                            goodsAdapter.notifyItemInserted(goodList.size() - 1);
                            refreshSumMoney();
                        } else {
                            customDialog.dismiss();
                            Toast.makeText(AddNewOrderActivity.this, "增加商品失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(AddNewOrderActivity.this, "增加商品失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean productExitInList(String productcode) {
        boolean result = false;
        Log.e("test","productcode:" + productcode);
        for(GoodBean goodBean : goodList){
            Log.e("test","code:" + goodBean.getProduct().getProductcode() + goodBean.getStockno());
            if((goodBean.getProduct().getProductcode() + goodBean.getStockno()).equals(productcode)){
                return true;
            }
        }
        return result;
    }


    private void editOrderItemOnOperate(int position, double discount,int discountId, String oprateName) {
        GoodBean goodBean = goodList.get(position);
        goodBean.setDiscountRate(discount);
        goodBean.setDiscountId(discountId);
        goodBean.getProduct().setPrice(LogicUtils.getKeepLastOneNumberAfterLittlePoint(
                Double.parseDouble(goodBean.getProduct().getPrePrice()) * discount));
        goodsAdapter.notifyItemChanged(position);
        refreshSumMoney();

    }

    private void deleteOrderItem(final int position) {
        goodList.remove(position);
        goodsAdapter.notifyItemRangeChanged(0, goodList.size());
        goodsAdapter.notifyItemRemoved(position);
        refreshSumMoney();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
