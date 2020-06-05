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
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.AddNewOrderGoodsAdapter;
import com.pos.priory.adapters.NewGoodTypeDataAdapter;
import com.pos.priory.beans.DiscountBean;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.NewGoodTypeDataBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
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


    List<WarehouseBean.ResultsBean> goodList = new ArrayList<>();
    AddNewOrderGoodsAdapter goodsAdapter;
    int memberid, memberReward;
    String memberName, memberMobile;
    double sumMoney = 0, changeGoodsMoeny = 0;
    double sumWeight = 0;
    List<DiscountBean> discountBeans = new ArrayList<>();
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
        changeGoodsMoeny = getIntent().getIntExtra("sumMoney", 0);

        moneyTv.setText("合計：" +LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
        paymentTv.setText("客戶應付：" +LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));

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
        memberid = getIntent().getIntExtra("memberId", 0);
        memberMobile = getIntent().getStringExtra("memberMobile");
        memberReward = getIntent().getIntExtra("memberReward", 0);
        memberName = getIntent().getStringExtra("memberName");
        memberNameTv.setText("会员：" + memberName);
        memberPhoneTv.setText("联繫电话：" + memberMobile);
        memberRewardTv.setText("积分：" + memberReward);

        typesRecyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,
                false));
        newGoodTypeDataAdapter = new NewGoodTypeDataAdapter(R.layout.type_data_list_item, newGoodTypeList);
        typesRecyclerView.setAdapter(newGoodTypeDataAdapter);
    }

    private void refreshSumMoney() {
        sumMoney = 0;
        sumWeight = 0;
        newGoodTypeList.clear();
        for (WarehouseBean.ResultsBean bean : goodList) {
            sumMoney += new BigDecimal(bean.getItem().get(0).getPrice().get(0).getPrice()).doubleValue();
            sumWeight += bean.getTotal().getWeight();
            if (!isTypeDatasContains(bean)) {
                NewGoodTypeDataBean newGoodTypeDataBean = new NewGoodTypeDataBean();
                newGoodTypeDataBean.setName(bean.getItem().get(0).getName());
                newGoodTypeDataBean.setWeight(bean.getTotal().getWeight());
                newGoodTypeDataBean.setCount(1);
                newGoodTypeList.add(newGoodTypeDataBean);
            }
        }
        newGoodTypeDataAdapter.notifyDataSetChanged();
        moneyTv.setText("合計：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
        paymentTv.setText("客戶應付：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney + changeGoodsMoeny));
//        countTv.setText(goodList.size() + "件|" + LogicUtils.getKeepLastTwoNumberAfterLittlePoint(sumWeight) + "g");
    }

    private boolean isTypeDatasContains(WarehouseBean.ResultsBean goodBean) {
        for (NewGoodTypeDataBean bean : newGoodTypeList) {
            if (bean.getName().equals(goodBean.getName())) {
                bean.setWeight(bean.getWeight() + goodBean.getTotal().getWeight());
                bean.setCount(bean.getCount() + 1);
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.next_tv, R.id.add_fitting_btn, R.id.add_product_btn, R.id.back_btn, R.id.discount_layout,R.id.member_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_layout:
                startActivityForResult(new Intent(AddNewOrderActivity.this, MemberInfoActivity.class), 100);
                break;
            case R.id.discount_layout:
                startActivityForResult(new Intent(AddNewOrderActivity.this, SelectDiscountActivity.class), 100);
                break;
            case R.id.add_fitting_btn:
                startActivityForResult(new Intent(AddNewOrderActivity.this, AddFittingActivity.class), 100);
                break;
            case R.id.add_product_btn:
                showSelectAddProductMethodDialog();
                break;
            case R.id.next_tv:
//                if (changeGoodsMoeny == 0) {
//                    if (sumMoney < 0 || goodList.size() == 0) {
//                        showToast("请先增加购买商品");
//                        return;
//                    }
//                } else {
//                    if ((sumMoney + changeGoodsMoeny) < 0) {
//                        showToast("新商品总额要大于等于换货商品总额");
//                        return;
//                    }
//                    if (goodList.size() == 0) {
//                        showToast("请先增加购买商品");
//                        return;
//                    }
//                }
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
                intent.putExtra("checkedGoodList", getIntent().getStringExtra("checkedGoodList"));
                startActivity(intent);
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
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
                discountBeans.clear();
                discountBeans = gson.fromJson(data.getStringExtra("selectDiscountList"),
                        new TypeToken<List<DiscountBean>>() {
                        }.getType());
                if (discountBeans.size() != 0)
                    discountTv.setText(discountBeans.get(0).getName());
                break;
            case 3:
                Log.e("test", "fittinglist:" + data.getStringExtra("selectFittingList"));
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
        RetrofitManager.excuteGson(bindToLifecycle(), RetrofitManager.createGson(ApiService.class).getStockListByParam(productcode),
                new ModelGsonListener<WarehouseBean>() {
                    @Override
                    public void onSuccess(WarehouseBean result) throws Exception {
                        if (result != null && result.getResults().size() != 0 && result.getResults().get(0).getTotal().getQuantity() > 0) {
                            hideLoadingDialog();
                            goodList.add(result.getResults().get(0));
                            goodsAdapter.notifyItemInserted(goodList.size() - 1);
                            refreshSumMoney();
                        } else {
                            hideLoadingDialog();
                            showToast("增加商品失败");
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        hideLoadingDialog();
                        showToast("增加商品失败");
                    }
                });
    }

    private boolean productExitInList(String productcode) {
        boolean result = false;
        Log.e("test", "productcode:" + productcode);
        for (WarehouseBean.ResultsBean goodBean : goodList) {
            if (String.valueOf(goodBean.getItem().get(0).getProductcode()).equals(productcode)) {
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
