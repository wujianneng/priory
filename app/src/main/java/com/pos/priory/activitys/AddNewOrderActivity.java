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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderGoodsAdapter;
import com.pos.priory.adapters.NewGoodTypeDataAdapter;
import com.pos.priory.beans.CashCouponResultBean;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.NewGoodTypeDataBean;
import com.pos.priory.beans.OrderCalculationParamBean;
import com.pos.priory.beans.OrderCalculationResultBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.MipcaActivityCapture;
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

public class AddNewOrderActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.good_recycler_view)
    SwipeMenuRecyclerView goodRecyclerView;
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
    @Bind(R.id.add_fitting_btn)
    MaterialButton addFittingBtn;
    @Bind(R.id.add_product_btn)
    MaterialButton addProductBtn;
    @Bind(R.id.types_recycler_view)
    RecyclerView typesRecyclerView;
    @Bind(R.id.discount_tv)
    TextView discountTv;
    @Bind(R.id.discount_layout)
    RelativeLayout discountLayout;
    @Bind(R.id.payment_tv)
    TextView paymentTv;


    List<FittingBean.ResultsBean> goodList = new ArrayList<>();
    AddNewOrderGoodsAdapter goodsAdapter;
    MemberBean.ResultsBean memberBean;
    double sumMoney = 0, changeGoodsMoeny = 0;
    double sumWeight = 0;
    int oldOrderId = 0;
    List<OrderDetailReslutBean.OrderItemsBean> changeGoodList = new ArrayList<>();
    List<CouponResultBean> discountBeans = new ArrayList<>();
    AlertDialog actionDialog;

    List<NewGoodTypeDataBean> newGoodTypeList = new ArrayList<>();
    NewGoodTypeDataAdapter newGoodTypeDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);
        ButterKnife.bind(this);
        initViews();
    }


    protected void initViews() {
        titleTv.setText("销售");
        rightImg.setVisibility(View.GONE);
        nextTv.setVisibility(View.VISIBLE);
        changeGoodsMoeny = getIntent().getDoubleExtra("sumMoney", 0);
        oldOrderId = getIntent().getIntExtra("orderId", 0);
        changeGoodList = gson.fromJson(getIntent().getStringExtra("changeGoodList"), new TypeToken<List<OrderDetailReslutBean.OrderItemsBean>>() {
        }.getType());

        moneyTv.setText("合計：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
        paymentTv.setText("客戶應付：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));

        goodsAdapter = new AddNewOrderGoodsAdapter(this, R.layout.add_new_order_good_list_item, goodList);
        //设置侧滑菜单
        goodRecyclerView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
            SwipeMenuItem deleteItem = new SwipeMenuItem(AddNewOrderActivity.this)
                    .setBackgroundColor(ContextCompat.getColor(AddNewOrderActivity.this, R.color.drag_btn_red))
                    .setImage(R.drawable.icon_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setHeight(DeviceUtil.dip2px(AddNewOrderActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                    .setWidth(DeviceUtil.dip2px(AddNewOrderActivity.this, 100));//设置宽
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
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        goodRecyclerView.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.decrease_btn) {
                    goodList.get(position).setBuyCount(goodList.get(position).getBuyCount() == 1 ? 1 : goodList.get(position).getBuyCount() - 1);
                    adapter.notifyItemChanged(position);
                } else if (view.getId() == R.id.increase_btn) {
                    if (goodList.get(position).getBuyCount() + 1 <= goodList.get(position).getPrd_stock_quantity())
                        goodList.get(position).setBuyCount(goodList.get(position).getBuyCount() + 1);
                    adapter.notifyItemChanged(position);
                }
                refreshSumMoney();
            }
        });
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);
        memberNameTv.setText("会员：" + memberBean.getName());
        memberPhoneTv.setText("联繫电话：" + memberBean.getMobile());
        memberRewardTv.setText("积分：" + memberBean.getReward());

        typesRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        newGoodTypeDataAdapter = new NewGoodTypeDataAdapter(R.layout.type_data_list_item, newGoodTypeList);
        typesRecyclerView.setAdapter(newGoodTypeDataAdapter);
    }

    private void refreshSumMoney() {
        sumMoney = 0;
        sumWeight = 0;
        newGoodTypeList.clear();
        for (FittingBean.ResultsBean bean : goodList) {
            sumMoney += bean.getPrice().size() == 0 ? 0 : new BigDecimal(bean.getPrice().get(0).getPrice()).doubleValue() * bean.getBuyCount();
            sumWeight += bean.getWhitem().size() == 0 ? 0 : bean.getWhitem().get(0).getWeight() * bean.getBuyCount();
            if (!isTypeDatasContains(bean)) {
                NewGoodTypeDataBean newGoodTypeDataBean = new NewGoodTypeDataBean();
                newGoodTypeDataBean.setName(bean.getName());
                newGoodTypeDataBean.setWeight(bean.getWhitem().size() == 0 ? 0 : bean.getWhitem().get(0).getWeight());
                newGoodTypeDataBean.setCount(bean.getBuyCount());
                newGoodTypeList.add(newGoodTypeDataBean);
            }
        }
        newGoodTypeDataAdapter.notifyDataSetChanged();
        discountBeans.clear();
        discountTv.setText("不使用");
        paymentTv.setText("客戶應付：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
        moneyTv.setText("合計：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
    }

    private boolean isTypeDatasContains(FittingBean.ResultsBean goodBean) {
        for (NewGoodTypeDataBean bean : newGoodTypeList) {
            if (bean.getName().equals(goodBean.getName())) {
                bean.setWeight(bean.getWeight() + goodBean.getWhitem().size() == 0 ? 0 : goodBean.getWhitem().get(0).getWeight());
                bean.setCount(bean.getCount() + 1);
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.next_tv, R.id.add_fitting_btn, R.id.add_product_btn, R.id.back_btn, R.id.discount_layout, R.id.member_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_layout:
                Intent intent0 = new Intent(AddNewOrderActivity.this, MemberInfoActivity.class);
                intent0.putExtra("memberInfo", gson.toJson(memberBean));
                startActivityForResult(intent0, 100);
                break;
            case R.id.discount_layout:
                Intent intent1 = new Intent(AddNewOrderActivity.this, SelectDiscountActivity.class);
                intent1.putExtra("memberInfo", gson.toJson(memberBean));
                intent1.putExtra("goodlist", gson.toJson(goodList));
                intent1.putExtra("couponList", gson.toJson(discountBeans));
                startActivityForResult(intent1, 100);
                break;
            case R.id.add_fitting_btn:
                startActivityForResult(new Intent(AddNewOrderActivity.this, AddFittingActivity.class), 100);
                break;
            case R.id.add_product_btn:
                showSelectAddProductMethodDialog();
                break;
            case R.id.next_tv:
                if (changeGoodsMoeny == 0) {
                    if (sumMoney < 0 || goodList.size() == 0) {
                        showToast("请先增加购买商品");
                        return;
                    }
                } else {
                    if ((sumMoney + changeGoodsMoeny) < 0) {
                        showToast("新商品总额要大于等于换货商品总额");
                        return;
                    }
                    if (goodList.size() == 0) {
                        showToast("请先增加购买商品");
                        return;
                    }
                }
                orderCalculation(true);

                break;
            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }

    CustomDialog customDialog;

    public void orderCalculation(boolean isNext) {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在計算客戶應付..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            OrderCalculationParamBean calculationParamBean = new OrderCalculationParamBean();
            calculationParamBean.setOrder_type(changeGoodsMoeny == 0 ? 0 : 1);
            calculationParamBean.setShop(MyApplication.staffInfoBean.getShopid());
            calculationParamBean.setMember(memberBean.getId());


            if (calculationParamBean.getOrder_type() == 1) {
                calculationParamBean.setOld_order(oldOrderId);
                List<OrderCalculationParamBean.OldOrderItemBean> old_order_items = new ArrayList<>();
                for (OrderDetailReslutBean.OrderItemsBean orderItemsBean : changeGoodList) {
                    OrderCalculationParamBean.OldOrderItemBean oldOrderItemBean = new OrderCalculationParamBean.OldOrderItemBean();
                    oldOrderItemBean.setId(orderItemsBean.getId());
                    oldOrderItemBean.setWeight(orderItemsBean.getWeight());
                    old_order_items.add(oldOrderItemBean);
                }
                calculationParamBean.setOld_order_items(old_order_items);
            }

            List<Integer> coupons = new ArrayList<>();
            if (discountBeans.size() != 0) {
                for (CouponResultBean resultsBean : discountBeans) {
                    coupons.add(resultsBean.getId());
                }
            }
            calculationParamBean.setCoupons(coupons);

            List<OrderCalculationParamBean.ProductsBean> products = new ArrayList<>();
            for (FittingBean.ResultsBean fittingBean : goodList) {
                OrderCalculationParamBean.ProductsBean productsBean = new OrderCalculationParamBean.ProductsBean();
                productsBean.setId(fittingBean.getId());
                productsBean.setCount(fittingBean.getBuyCount());
                productsBean.setWhnumber(fittingBean.getWhitem().get(0).getWhnumber());
                products.add(productsBean);
            }
            calculationParamBean.setProducts(products);
            calculationParamBean.setShop(MyApplication.staffInfoBean.getShopid());
            Log.e("test", "params:" + gson.toJson(calculationParamBean));
            RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class)
                    .orderCalculation(calculationParamBean), new ModelGsonListener<OrderCalculationResultBean>() {
                @Override
                public void onSuccess(OrderCalculationResultBean result) throws Exception {
                    customDialog.dismiss();
                    double needMoney = result.getAmount_payable();
                    paymentTv.setText("客戶應付：" + needMoney);
                    if(isNext) {
                        Intent intent = new Intent(AddNewOrderActivity.this, BalanceActivity.class);
                        intent.putExtra("pay_spread", result.getPay_spread());
                        intent.putExtra("goodlist", gson.toJson(goodList));
                        intent.putExtra("sumMoney", needMoney);
                        intent.putExtra("newOrderSumMoney", needMoney + result.getExchange_amount());
                        intent.putExtra("memberInfo", gson.toJson(memberBean));
                        intent.putExtra("sumCount", goodList.size());
                        intent.putExtra("sumWeight", sumWeight);
                        intent.putExtra("cache_token", result.getCache_token());
                        intent.putExtra("order_type", changeGoodsMoeny == 0 ? 0 : 1);
                        intent.putExtra("checkedGoodList", getIntent().getStringExtra("checkedGoodList"));
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailed(String erromsg) {
                    customDialog.dismiss();
                    Log.e("test", "erromsg:" + erromsg);
                    Toast.makeText(AddNewOrderActivity.this, "計算失败", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public void showSelectAddProductMethodDialog() {
        String[] methods = new String[]{"手动添加", "扫码添加"};
        new AlertDialog.Builder(this).setTitle("请选择新增商品的方式")
                .setItems(methods, (dialog, which) -> {
                    dialog.dismiss();
                    if (which == 0) showInputCodeDialog();
                    else startActivityForResult(new Intent(AddNewOrderActivity.this,
                            MipcaActivityCapture.class), 1000);
                }).create().show();
    }

    private void showInputCodeDialog() {
        if (actionDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_code, null);
            actionDialog = new AlertDialog.Builder(this).setView(view)
                    .create();
            final EditText edt_count = view.findViewById(R.id.edt_count);
            view.findViewById(R.id.btn_close).setOnClickListener(v -> {
                actionDialog.dismiss();
            });
            view.findViewById(R.id.btn_commit).setOnClickListener(v -> {
                if (!edt_count.getText().toString().equals(""))
                    getGoodBeanByCode(edt_count.getText().toString());
                actionDialog.dismiss();
            });
            actionDialog.setOnDismissListener(dialog -> actionDialog = null);
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
            case 2:
                List<CouponResultBean> discountList = gson.fromJson(data.getStringExtra("selectDiscountList"),
                        new TypeToken<List<CouponResultBean>>() {
                        }.getType());
                String result = "";
                if (discountList != null) {
                    discountBeans.clear();
                    discountBeans.addAll(discountList);
                    for (int i = 0; i < discountBeans.size(); i++) {
                        result += ((i + 1) + ". " + discountBeans.get(i).getName() + "\n");
                    }
                }
                discountTv.setText(discountBeans.size() == 0 ? "不使用" : result);
                orderCalculation(false);
                break;
            case 3:
                Log.e("test", "fittinglist:" + data.getStringExtra("selectFittingList"));
                goodList.addAll(gson.fromJson(data.getStringExtra("selectFittingList"),
                        new TypeToken<List<FittingBean.ResultsBean>>() {
                        }.getType()));
                goodsAdapter.notifyDataSetChanged();
                refreshSumMoney();
                break;
            case 4:
                Log.e("test", "member:" + data.getStringExtra("member"));
                break;
        }
    }


    private void getGoodBeanByCode(String productcode) {
        if (productExitInList(productcode)) {
            Toast.makeText(AddNewOrderActivity.this, "已增加该商品", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadingDialog("正在增加商品..");
        RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).getProductBySearch(productcode), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                FittingBean fittingBean = gson.fromJson(result, FittingBean.class);
                if (fittingBean.getResults().size() == 0) {
                    Toast.makeText(AddNewOrderActivity.this, "搜索不到該商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                FittingBean.ResultsBean resultsBean = fittingBean.getResults().get(0);
                if (productExitInList(resultsBean.getWhitem().get(0).getWhnumber() + resultsBean.getProductcode())) {
                    Toast.makeText(AddNewOrderActivity.this, "已增加该商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                goodList.add(resultsBean);
                goodsAdapter.notifyDataSetChanged();
                refreshSumMoney();
            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
                Toast.makeText(AddNewOrderActivity.this, "搜索不到該商品", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean productExitInList(String productcode) {
        boolean result = false;
        Log.e("test", "productcode:" + productcode);
        for (FittingBean.ResultsBean goodBean : goodList) {
            if ((goodBean.getWhitem().get(0).getWhnumber() + goodBean.getProductcode()).equals(productcode)) {
                return true;
            }
        }
        return result;
    }


    private void deleteOrderItem(final int position) {
        goodsAdapter.remove(position);
        refreshSumMoney();
    }

}
