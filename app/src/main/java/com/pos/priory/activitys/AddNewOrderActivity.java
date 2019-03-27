package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderGoodsAdapter;
import com.pos.priory.beans.CreateOrderItemResultBean;
import com.pos.priory.beans.CreateOrderResultBean;
import com.pos.priory.beans.DiscountBean;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.zxinglib.MipcaActivityCapture;
import com.pos.zxinglib.utils.DeviceUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.math.BigDecimal;
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


    int memberid;

    double sumMoney = 0, changeGoodsMoeny = 0;
    CreateOrderResultBean createOrderResultBean;
    @Bind(R.id.right_img)
    ImageView rightImg;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_add_new_order);
        ButterKnife.bind(this);
    }


    @Override
    protected void initViews() {
        titleTv.setText("添加商品");
        rightImg.setVisibility(View.GONE);
        changeGoodsMoeny = getIntent().getDoubleExtra("sumMoney", 0);
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
                    deleteOrderItem(adapterPosition, goodList.get(adapterPosition).getId());
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        memberid = getIntent().getIntExtra("memberId", 0);
        createNewOrder();
        getStoreDiscountList();
    }

    List<DiscountBean> discountBeanList;
    List<String> discountNames = new ArrayList<>();

    private void getStoreDiscountList() {
        OkHttp3Util.doGetWithToken(Constants.GET_DISCOUNT_LIST_URL + "?location=" + MyApplication.staffInfoBean.getStore(), sharedPreferences,
                new Okhttp3StringCallback(this, "getStoreDiscountList") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        discountBeanList = gson.fromJson(results, new TypeToken<List<DiscountBean>>() {
                        }.getType());
                        if (discountBeanList != null) {
                            for (DiscountBean bean : discountBeanList) {
                                discountNames.add(bean.getName());
                            }
                        }
                        if (discountNames.size() == 0) {
                            discountNames.add("無折扣");
                        }

                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }

    CustomDialog customDialog;
    boolean hadCreateOrder = false;

    private void createNewOrder() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在新增订单..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ordernumber", DateUtils.getCurrentOrderNumber());
        paramMap.put("member", memberid);
        paramMap.put("location", MyApplication.staffInfoBean.getStore());
        OkHttp3Util.doPostWithToken(Constants.GET_ORDERS_URL + "/", gson.toJson(paramMap),
                sharedPreferences, new Okhttp3StringCallback(this, "createOrder") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        createOrderResultBean = gson.fromJson(results, CreateOrderResultBean.class);
                        hadCreateOrder = true;
                        customDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                    }
                });

    }

    private void deleteOrder() {
        if (!hadCreateOrder) {
            finish();
            return;
        }
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在删除订单..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        OkHttp3Util.doDeleteWithToken(Constants.GET_ORDERS_URL + "/" + createOrderResultBean.getId() + "/update/",
                sharedPreferences, new Okhttp3StringCallback(this, "deleteOrder") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(AddNewOrderActivity.this, "删除订单失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        deleteOrder();
    }

    private void refreshSumMoney() {
        sumMoney = 0;
        for (GoodBean bean : goodList) {
            sumMoney += new BigDecimal(bean.getBatch().getProduct().getPrice()).doubleValue() * bean.getSaleCount();
        }
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
    }

    int yourChoice;
    AlertDialog choiceSexDialog;


    private void showChoiceDiscountDialog(final int position) {
        if (choiceSexDialog == null) {
            if (discountNames.size() == 1 && discountNames.get(0).equals("無折扣")) {
                yourChoice = 0;
            } else {
                for (int i = 0; i < discountBeanList.size(); i++) {
                    if (goodList.get(position).getDiscountRate() == new BigDecimal(discountBeanList.get(i).getValue()).doubleValue()) {
                        yourChoice = i;
                    }
                }
            }
            Log.e("yourChoice", "yourChoice:" + yourChoice);
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("請選擇折扣");
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
            singleChoiceDialog.setPositiveButton("確定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yourChoice != -1) {
                                if (discountNames.size() == 1 && discountNames.get(0).equals("無折扣")) {
                                    editOrderItemOnOperate(position, goodList.get(position).getId(),
                                            goodList.get(position).getSaleCount(), 1, "调折扣");
                                } else {
                                    editOrderItemOnOperate(position, goodList.get(position).getId(),
                                            goodList.get(position).getSaleCount(), new BigDecimal(discountBeanList.get(yourChoice).getValue()).doubleValue(), "调折扣");
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
                if ((sumMoney + changeGoodsMoeny) < 0) {
                    Toast.makeText(AddNewOrderActivity.this, "新商品总额要大于换货商品总额", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(AddNewOrderActivity.this, BalanceActivity.class);
                intent.putExtra("goodlist", gson.toJson(goodList));
                intent.putExtra("sumMoney", sumMoney + changeGoodsMoeny);
                intent.putExtra("newOrderSumMoney", sumMoney);
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                intent.putExtra("ordernumber", createOrderResultBean.getOrdernumber());
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
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在添加商品..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        OkHttp3Util.doGetWithToken(Constants.GET_STOCK_URL + "?productcode=" + productcode + "&location="
                        + MyApplication.staffInfoBean.getStore(),
                sharedPreferences, new Okhttp3StringCallback(this, "getStockList") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        List<GoodBean> goodBeanList = gson.fromJson(results, new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null && goodBeanList.size() != 0) {
                            if (goodBeanList.get(0).getQuantity() >= 1) {
                                createOrderItem(goodBeanList.get(0));
                            } else {
                                customDialog.dismiss();
                                Toast.makeText(AddNewOrderActivity.this, "此商品庫存不足", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            customDialog.dismiss();
                            Toast.makeText(AddNewOrderActivity.this, "添加商品失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(AddNewOrderActivity.this, "添加商品失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createOrderItem(final GoodBean goodBean) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ordernumber", createOrderResultBean.getOrdernumber());
        paramMap.put("stockid", goodBean.getId());
        OkHttp3Util.doPostWithToken(Constants.GET_ORDER_ITEM_URL + "/", gson.toJson(paramMap), sharedPreferences,
                new Okhttp3StringCallback(this, "createOrderItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        CreateOrderItemResultBean createOrderItemResultBean = gson.fromJson(results,
                                CreateOrderItemResultBean.class);
                        goodBean.setId(createOrderItemResultBean.getId());
                        editOrderItem(goodBean);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(AddNewOrderActivity.this, "添加商品失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editOrderItem(final GoodBean goodBean) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("quantity", goodBean.getSaleCount());
        paramMap.put("discount", goodBean.getDiscountRate());
        OkHttp3Util.doPatchWithToken(Constants.GET_ORDER_ITEM_URL + "/" + goodBean.getId()
                + "/update/", gson.toJson(paramMap), sharedPreferences, new Okhttp3StringCallback(this, "editOrderItem") {
            @Override
            public void onSuccess(String results) throws Exception {
                customDialog.dismiss();
                goodBean.getBatch().getProduct().setPrePrice(goodBean.getBatch().getProduct().getPrice());
                goodList.add(goodBean);
                goodsAdapter.notifyItemInserted(goodList.size() - 1);
                refreshSumMoney();
            }

            @Override
            public void onFailed(String erromsg) {
                customDialog.dismiss();
                Toast.makeText(AddNewOrderActivity.this, "添加商品失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editOrderItemOnOperate(final int position, int orderitemId, int quantity, final double discount,
                                        final String oprateName) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在修改商品信息..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("quantity", quantity);
        paramMap.put("discount", discount);
        OkHttp3Util.doPatchWithToken(Constants.GET_ORDER_ITEM_URL + "/" + orderitemId
                + "/update/", gson.toJson(paramMap), sharedPreferences, new Okhttp3StringCallback(this, "editOrderItemOnOperate") {
            @Override
            public void onSuccess(String results) throws Exception {
                customDialog.dismiss();
                GoodBean goodBean = goodList.get(position);
                if (oprateName.equals("加数量")) {
                    goodBean.setSaleCount(goodBean.getSaleCount() + 1);
                    goodsAdapter.notifyItemChanged(position);
                    refreshSumMoney();
                } else if (oprateName.equals("减数量")) {
                    goodBean.setSaleCount(goodBean.getSaleCount() - 1);
                    goodsAdapter.notifyItemChanged(position);
                    refreshSumMoney();
                } else if (oprateName.equals("调折扣")) {
                    goodBean.setDiscountRate(discount);
                    goodBean.getBatch().getProduct().setPrice(LogicUtils.getKeepLastOneNumberAfterLittlePoint(
                            Double.parseDouble(goodBean.getBatch().getProduct().getPrePrice()) * discount));
                    goodsAdapter.notifyItemChanged(position);
                    refreshSumMoney();
                }

            }

            @Override
            public void onFailed(String erromsg) {
                customDialog.dismiss();
                Toast.makeText(AddNewOrderActivity.this, "修改商品信息失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteOrderItem(final int position, int orderitemid) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在删除该商品..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        OkHttp3Util.doDeleteWithToken(Constants.GET_ORDER_ITEM_URL + "/" + orderitemid + "/update/",
                sharedPreferences, new Okhttp3StringCallback(this, "deleteOrderItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        goodList.remove(position);
                        goodsAdapter.notifyItemRangeChanged(0, goodList.size());
                        goodsAdapter.notifyItemRemoved(position);
                        refreshSumMoney();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(AddNewOrderActivity.this, "删除商品失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
