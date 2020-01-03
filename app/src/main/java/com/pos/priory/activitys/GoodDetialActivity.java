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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.GoodDetialAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
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

import org.json.JSONArray;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    RecyclerView recyclerView;
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
    @Bind(R.id.code_tv)
    TextView codeTv;
    @Bind(R.id.exchange_btn)
    Button exchangeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detial);
        ButterKnife.bind(this);
        initViews();
    }

    GoodBean goodBean;
    String productcode = "";
    int slefCount = 0;

    protected void initViews() {
        goodBean = gson.fromJson(getIntent().getStringExtra("goodbean"), GoodBean.class);
        String goodname = goodBean.getProduct().getName();
        slefCount = 1;
        productcode = goodBean.getProduct().getProductcode() + "";
        titleTv.setText(goodname);
        priceTv.setText("售价：" + goodBean.getProduct().getPrice());
        Glide.with(this).load(Constants.BASE_URL_HTTP + goodBean.getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(goodImg);
        repertoryTv.setText("库存：" + slefCount);

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> refreshRecyclerView(false));
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> refreshRecyclerView(true));
        goodDetialAdapter = new GoodDetialAdapter(this, R.layout.good_detial_list_item, orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        recyclerView.setAdapter(goodDetialAdapter);
        smartRefreshLayout.autoRefresh();
    }

    CustomDialog customDialog;
    int yourChoice = -1;
    List<String> stores = new ArrayList<>();
    List<Integer> storeIds = new ArrayList<>();

    private void showIsExchangeDialog() {
        try {
            JSONArray jsonArray = new JSONArray(MyApplication.getContext().storeListJsonString);
            stores.clear();
            storeIds.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stores.add(jsonObject.getString("name"));
                storeIds.add(jsonObject.getInt("id"));
            }
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("请选择调拨店铺");
            // 第二个参数是默认选项，此处设置为0
            ListAdapter adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, stores);
            singleChoiceDialog.setSingleChoiceItems(adapter, yourChoice, (dialog, which) -> yourChoice = which);
            singleChoiceDialog.setPositiveButton("确定", (dialog, which) -> {
                if (yourChoice != -1) {
//                                doExchange(storeIds.get(yourChoice), orderList.get(position).getId());
                }
                dialog.dismiss();
            });
            singleChoiceDialog.create().show();
        } catch (Exception e) {

        }
    }

    private void doExchange(int storeid, int stockid) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "调货中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        List<Map<String, Object>> stocktransfer = new ArrayList<>();
        Map<String, Object> stocktransferMap = new HashMap<>();
        stocktransferMap.put("storeid", storeid);
        stocktransferMap.put("stockid", stockid);
        stocktransfer.add(stocktransferMap);
        paramMap.put("stocktransfer", stocktransfer);
        Log.e("test", "params:" + gson.toJson(paramMap));
        RetrofitManager.createString(ApiService.class)
                .tranferGoods(RequestBody.create(MediaType.parse("application/json"), gson.toJson(paramMap)))
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    customDialog.dismiss();
                    ToastUtils.showShort("调货成功");
                    smartRefreshLayout.autoRefresh();
                }, t -> {
                    customDialog.dismiss();
                    ToastUtils.showShort("调货失败");
                });
    }


    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            orderList.clear();
            goodDetialAdapter.notifyDataSetChanged();
        }
        RetrofitManager.createString(ApiService.class)
                .getStockListByParam(productcode)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    JSONObject jsonObject = new JSONObject(s);
                    List<GoodBean> goodBeanList = gson.fromJson(jsonObject.getJSONArray("results").toString(), new TypeToken<List<GoodBean>>() {
                    }.getType());
                    if (goodBeanList != null) {
                        orderList.addAll(goodBeanList);
                        goodDetialAdapter.notifyDataSetChanged();
                        repertoryTv.setText("库存：" + orderList.size());
                        priceTv.setText("售价：" + goodBeanList.get(0).getProduct().getPrice());
                        double sumweight = 0;
                        for (GoodBean goodBean : goodBeanList) {
                            sumweight += goodBean.getWeight();
                        }
                        weightTv.setText("库存重量：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumweight) + "g");
                        if (sumweight == 0) {
                            weightTv.setVisibility(View.GONE);
                        }
                    }
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }, throwable -> {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                });
    }


    private void doDingHuo(final GoodBean bean, final int count) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "订货中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("product", bean.getProduct().getId());
        map.put("quantity", count);
        RetrofitManager.createString(ApiService.class).createPurchasingitem(map)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    customDialog.dismiss();
                    Toast.makeText(GoodDetialActivity.this, "订货成功", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    customDialog.dismiss();
                    Toast.makeText(GoodDetialActivity.this, "订货失败", Toast.LENGTH_SHORT).show();
                });
    }


    @OnClick({R.id.back_btn, R.id.btn_dinghuo, R.id.exchange_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_dinghuo:
                showIsDingHuoDialog();
                break;
            case R.id.exchange_btn:
                showIsExchangeDialog();
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
            Glide.with(this).load(Constants.BASE_URL_HTTP + goodBean.getProduct().getImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(icon_good);
            code_tv.setText(goodBean.getProduct().getProductcode() + "");
            name_tv.setText(goodBean.getProduct().getName());
            view.findViewById(R.id.btn_close).setOnClickListener(v -> actionDialog.dismiss());
            view.findViewById(R.id.btn_commit).setOnClickListener(v -> {
                if (edt_count.getText().toString().equals("")) {
                    Toast.makeText(GoodDetialActivity.this, "请输入数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                int count = Integer.parseInt(edt_count.getText().toString());
                doDingHuo(goodBean, count);
                actionDialog.dismiss();
            });
            actionDialog.setOnDismissListener(dialog -> actionDialog = null);
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
