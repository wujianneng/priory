package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
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

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.GoodDetialAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.zxinglib.utils.DeviceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONObject;

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

/**
 * Created by Lenovo on 2018/12/31.
 */

public class GoodDetialActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.repertory_tv)
    TextView repertoryTv;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    GoodDetialAdapter goodDetialAdapter;
    List<GoodBean> orderList = new ArrayList<>();
    @Bind(R.id.price_tv)
    TextView priceTv;
    @Bind(R.id.weight_tv)
    TextView weightTv;
    @Bind(R.id.btn_dinghuo)
    CardView btnDinghuo;
    @Bind(R.id.good_img)
    ImageView goodImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_good_detial);
        ButterKnife.bind(this);
    }

    GoodBean goodBean;
    String productcode = "";
    int slefCount = 0;

    @Override
    protected void initViews() {
        goodBean = gson.fromJson(getIntent().getStringExtra("goodbean"), GoodBean.class);
        String goodname = goodBean.getProduct().getName();
        slefCount = 1;
        productcode = goodBean.getProduct().getProductcode() + "";
        titleTv.setText(goodname);
        priceTv.setText("售價：" + goodBean.getProduct().getPrice());
        Glide.with(this).load(Constants.BASE_URL_HTTP + goodBean.getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(goodImg);
        repertoryTv.setText("庫存：" + slefCount);

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(true);
            }
        });
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(GoodDetialActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(GoodDetialActivity.this, R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("退貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(GoodDetialActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(GoodDetialActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
                SwipeMenuItem diaohuoItem = new SwipeMenuItem(GoodDetialActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(GoodDetialActivity.this, R.color.drag_btn_red))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("調貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(GoodDetialActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(GoodDetialActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(diaohuoItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showIsReturnDialog(adapterPosition);
                } else {
                    showChoiceDiscountDialog(adapterPosition);
                }
            }
        });
        goodDetialAdapter = new GoodDetialAdapter(this, R.layout.good_detial_list_item, orderList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(goodDetialAdapter);
        smartRefreshLayout.autoRefresh();
    }

    private void showIsReturnDialog(final int adapterPosition) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否確定要退貨該商品？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doReturnStock(adapterPosition);
                    }
                })
                .create();
        dialog.show();
    }

    CustomDialog customDialog;
    private void doReturnStock(int pos) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "提货中..");
        customDialog.show();
        GoodBean goodBean = orderList.get(pos);
        RetrofitManager.createString(ApiService.class).returnStockById(goodBean.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        ToastUtils.showShort("退货成功");
                        smartRefreshLayout.autoRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        ToastUtils.showShort("退货失败");
                    }
                });
    }

    int yourChoice = 0;
    String[] stores = new String[]{"高士德店", "揚名廣場店", "拱北萬家店"};

    private void showChoiceDiscountDialog(final int position) {
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("請選擇調撥店鋪");
        // 第二个参数是默认选项，此处设置为0
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, stores);
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

                        }
                        dialog.dismiss();
                    }
                });
        singleChoiceDialog.create().show();
    }



    private void allocateElseStock(final int stockid, int elseStoreCount, final int changeCount) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "訂貨中..");
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", elseStoreCount - changeCount);
        OkHttp3Util.doPatchWithToken(Constants.GET_STOCK_URL + "/" + stockid + "/update/", gson.toJson(map),
                new Okhttp3StringCallback(this, "allocateElseStock") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        allocateSelfStock(goodBean.getId(), stockid, changeCount);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "調撥失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void allocateSelfStock(final int stockid, final int elseStockid, final int changeCount) {
        Map<String, Object> map = new HashMap<>();
        slefCount += changeCount;
        map.put("quantity", slefCount);
        OkHttp3Util.doPatchWithToken(Constants.GET_STOCK_URL + "/" + stockid + "/update/", gson.toJson(map),
                new Okhttp3StringCallback(this, "allocateSelfStock") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        repertoryTv.setText(slefCount + "");
                        Toast.makeText(GoodDetialActivity.this, "調撥成功", Toast.LENGTH_SHORT).show();
                        smartRefreshLayout.autoRefresh();
                        createStockTransaction(changeCount, stockid, elseStockid);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        slefCount -= changeCount;
                        Toast.makeText(GoodDetialActivity.this, "調撥失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createStockTransaction(final int changeCount, int selfId, int elseid) {
        Map<String, Object> map = new HashMap<>();
        map.put("stockout", selfId);
        map.put("stockin", elseid);
        map.put("quantity", changeCount);
        OkHttp3Util.doPostWithToken(Constants.GET_STOCK_TRANSACTIONS_URL + "/", gson.toJson(map),
                new Okhttp3StringCallback(this, "createStockTransaction") {
                    @Override
                    public void onSuccess(String results) throws Exception {

                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }

    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            orderList.clear();
            goodDetialAdapter.notifyDataSetChanged();
        }
        RetrofitManager.createString(ApiService.class).getStockListByProductCode(productcode)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        List<GoodBean> goodBeanList = gson.fromJson(jsonObject.getJSONArray("stockitem").toString(), new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null) {
                            orderList.addAll(goodBeanList);
                            goodDetialAdapter.notifyDataSetChanged();
                            repertoryTv.setText("庫存：" + orderList.size());
                            priceTv.setText("售價：" + goodBeanList.get(0).getProduct().getPrice());
                            weightTv.setText("庫存重量：" + jsonObject.getDouble("productweight") + "g");
                            if(jsonObject.getDouble("productweight") == 0){
                                weightTv.setVisibility(View.GONE);
                            }
                        }
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }


    private void doDingHuo(final GoodBean bean, final int count) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "訂貨中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String,Object> map = new HashMap<>();
        map.put("product", bean.getProduct().getId());
        map.put("quantity",count);
        RetrofitManager.createString(ApiService.class).createPurchasingitem(map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "訂貨成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "訂貨失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @OnClick({R.id.back_btn, R.id.btn_dinghuo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_dinghuo:
                showIsDingHuoDialog();
                break;
        }
    }

    AlertDialog actionDialog;

    private void showIsDingHuoDialog() {
        if (actionDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(this).setView(view)
                    .create();
            ImageView icon_good = view.findViewById(R.id.icon_good);
            TextView code_tv = view.findViewById(R.id.code_tv);
            TextView name_tv = view.findViewById(R.id.name_tv);
            final EditText edt_count = view.findViewById(R.id.edt_count);
            Glide.with(this).load(goodBean.getProduct().getImage())
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(icon_good);
            code_tv.setText(goodBean.getProduct().getProductcode() + "");
            name_tv.setText(goodBean.getProduct().getName());
            view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edt_count.getText().toString().equals("")) {
                        Toast.makeText(GoodDetialActivity.this, "請輸入數量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(edt_count.getText().toString());
                    doDingHuo(goodBean,count);
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
            layoutParams.height = DeviceUtil.dip2px(this, 270);
            window.setGravity(Gravity.CENTER);
            window.setAttributes(layoutParams);
        }
    }
}
