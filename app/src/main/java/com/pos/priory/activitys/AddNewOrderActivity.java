package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderDiscountAdapter;
import com.pos.priory.adapters.AddNewOrderGoodsAdapter;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.CreateOrderItemResultBean;
import com.pos.priory.beans.CreateOrderResultBean;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.DateUtils;
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
    SwipeMenuRecyclerView goodRecyclerView;

    List<GoodBean> goodList = new ArrayList<>();
    AddNewOrderGoodsAdapter goodsAdapter;

    int memberid;
    public List<StaffInfoBean> staffInfoBeanList;

    double sumMoney = 0, changeGoodsMoeny = 0;
    CreateOrderResultBean createOrderResultBean;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_add_new_order);
        ButterKnife.bind(this);
    }


    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        changeGoodsMoeny = getIntent().getDoubleExtra("sumMoney", 0);
        moneyTv.setText("" + (sumMoney + changeGoodsMoeny));

        goodsAdapter = new AddNewOrderGoodsAdapter(this, R.layout.add_new_order_good_list_item, goodList);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.btn_change_price:
                        String[] items0 = {"6折", "65折", "7折", "75折", "8折", "85折", "9折", "95折", "无折扣"};
                        String[] items1 = {"免费","6折", "65折", "7折", "75折", "8折", "85折", "9折", "95折", "无折扣"};
                        if(goodList.get(position).getProduct().isDiscountcontrol()){
                            showChoiceDiscountDialog(items0,position);
                        }else {
                            showChoiceDiscountDialog(items1,position);
                        }
                        break;
                    case R.id.decrease_btn:
                        if (goodList.get(position).getSaleCount() > 1) {
                            editOrderItemOnOperate(position, goodList.get(position).getId(), goodList.get(position).getSaleCount() - 1,
                                    goodList.get(position).getDiscountRate(), "减数量");
                        }
                        break;
                    case R.id.increase_btn:
                        if (goodList.get(position).getSaleCount() < goodList.get(position).getQuantity()) {
                            editOrderItemOnOperate(position, goodList.get(position).getId(), goodList.get(position).getSaleCount() + 1,
                                    goodList.get(position).getDiscountRate(), "加数量");
                        } else {
                            Toast.makeText(AddNewOrderActivity.this, "出售数量已经等于库存量", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        //设置侧滑菜单
        goodRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
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
                    deleteOrderItem(adapterPosition, goodList.get(adapterPosition).getId());
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
        memberid = getIntent().getIntExtra("memberId", 0);
        staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY, ""),
                new TypeToken<List<StaffInfoBean>>() {
                }.getType());
        createNewOrder();
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
        paramMap.put("location", staffInfoBeanList.get(0).getStore());
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
            sumMoney += Double.parseDouble(bean.getProduct().getPrice()) * bean.getSaleCount();
        }
        moneyTv.setText((sumMoney + changeGoodsMoeny) + "");
    }

    int yourChoice;
    AlertDialog choiceSexDialog;

    private double getDiscountByName(String discountName) {
        if (discountName.equals("免费")) {
            return 0;
        }
        if (discountName.equals("6折")) {
            return 0.6;
        } else if (discountName.equals("65折")) {
            return 0.65;
        } else if (discountName.equals("7折")) {
            return 0.7;
        } else if (discountName.equals("75折")) {
            return 0.75;
        } else if (discountName.equals("8折")) {
            return 0.8;
        } else if (discountName.equals("85折")) {
            return 0.85;
        } else if (discountName.equals("9折")) {
            return 0.9;
        } else if (discountName.equals("95折")) {
            return 0.95;
        }
        return 1;
    }

    private void showChoiceDiscountDialog(final String[] items,final int position) {
        if (choiceSexDialog == null) {
            yourChoice = 0;
            for (int i = 0; i < items.length; i++) {
                if (goodList.get(position).getDiscountRate() == getDiscountByName(items[i])) {
                    yourChoice = i;
                }
            }
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("請選擇折扣");
            // 第二个参数是默认选项，此处设置为0
            singleChoiceDialog.setSingleChoiceItems(items, yourChoice,
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
                                editOrderItemOnOperate(position, goodList.get(position).getId(),
                                        goodList.get(position).getSaleCount(),
                                        getDiscountByName(items[yourChoice]), "调折扣");
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
                intent.putExtra("sumMoney", sumMoney);
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
        OkHttp3Util.doGetWithToken(Constants.GET_STOCK_URL + "?productcode=" + productcode,
                sharedPreferences, new Okhttp3StringCallback(this, "getStockList") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        List<GoodBean> goodBeanList = gson.fromJson(results, new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null) {
                            createOrderItem(goodBeanList.get(0));
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

    private void editOrderItemOnOperate(final int position, int orderitemId, int quantity, final double discount, final String oprateName) {
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
                + "/update/", gson.toJson(paramMap), sharedPreferences, new Okhttp3StringCallback(this, "editOrderItem") {
            @Override
            public void onSuccess(String results) throws Exception {
                customDialog.dismiss();
                if (oprateName.equals("加数量")) {
                    goodList.get(position).setSaleCount(goodList.get(position).getSaleCount() + 1);
                    goodsAdapter.notifyItemChanged(position);
                    refreshSumMoney();
                } else if (oprateName.equals("减数量")) {
                    goodList.get(position).setSaleCount(goodList.get(position).getSaleCount() - 1);
                    goodsAdapter.notifyItemChanged(position);
                    refreshSumMoney();
                } else if (oprateName.equals("调折扣")) {
                    goodList.get(position).setDiscountRate(discount);
                    goodsAdapter.notifyItemChanged(position);
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
}
