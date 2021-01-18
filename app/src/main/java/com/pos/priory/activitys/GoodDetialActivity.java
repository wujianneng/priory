package com.pos.priory.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.GoodDetialAdapter;
import com.pos.priory.beans.TranferStoresBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.beans.WhitemDetailResultBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

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
    RecyclerView recyclerView;
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    GoodDetialAdapter goodDetialAdapter;
    List<WhitemDetailResultBean.WhitemBean> orderList = new ArrayList<>();
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
    TextView exchangeBtn;
    @Bind(R.id.name_tv)
    TextView nameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detial);
        ButterKnife.bind(this);
        initViews();
    }

    WarehouseBean.ResultsBean.ItemBean goodBean;
    String productcode = "";

    protected void initViews() {
        goodBean = gson.fromJson(getIntent().getStringExtra("goodbean"), WarehouseBean.ResultsBean.ItemBean.class);
        String goodname = goodBean.getName();
        productcode = goodBean.getProductcode() + "";
        titleTv.setText(goodname);
        Glide.with(this).load(goodBean.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(goodImg);
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
    int yourChoice = 0;
    List<String> stores = new ArrayList<>();
    List<Integer> storeIds = new ArrayList<>();

    private void showIsExchangeDialog() {
        if (hasSelected()) {
            RetrofitManager.excuteGson(bindToLifecycle(), RetrofitManager.createGson(ApiService.class).getTranStores(),
                    new ModelGsonListener<TranferStoresBean>() {
                        @Override
                        public void onSuccess(TranferStoresBean result) throws Exception {
                            try {
                                stores.clear();
                                storeIds.clear();
                                for (TranferStoresBean.ResultsBean resultBean : result.getResults()) {
                                    stores.add(resultBean.getName());
                                    storeIds.add(resultBean.getId());
                                }
                                AlertDialog.Builder singleChoiceDialog =
                                        new AlertDialog.Builder(GoodDetialActivity.this);
                                singleChoiceDialog.setTitle("请选择调拨店铺");
                                // 第二个参数是默认选项，此处设置为0
                                ListAdapter adapter = new ArrayAdapter<String>(GoodDetialActivity.this,
                                        android.R.layout.simple_list_item_single_choice, stores);
                                singleChoiceDialog.setSingleChoiceItems(adapter, yourChoice, (dialog, which) -> yourChoice = which);
                                singleChoiceDialog.setPositiveButton("确定", (dialog, which) -> {
                                    doExchange(storeIds.get(yourChoice));
                                    dialog.dismiss();
                                });
                                singleChoiceDialog.create().show();
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailed(String erromsg) {

                        }
                    });
        }
    }

    private boolean hasSelected() {
        for (WhitemDetailResultBean.WhitemBean itemBean : orderList) {
            if (itemBean.isSelected())
                return true;
        }
        return false;
    }

    private void doExchange(int storeid) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "调货中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        List<Integer> items = new ArrayList<>();
        for (WhitemDetailResultBean.WhitemBean resultsBean : orderList) {
            if (resultsBean.isSelected())
                items.add(resultsBean.getId());
        }
        Log.e("test","shopid:" + storeid + " items:" + gson.toJson(items));
        RetrofitManager.createString(ApiService.class)
                .tranferGoods(storeid, items)
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
        RetrofitManager.createGson(ApiService.class)
                .getStockDetail(goodBean.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(warehouseBean -> {
                    if (warehouseBean != null) {
                        orderList.addAll(warehouseBean.getWhitem());
                        goodDetialAdapter.notifyDataSetChanged();
                        codeTv.setText(goodBean.getProductcode() + "");
                        nameTv.setText(warehouseBean.getPrd_name());
                        repertoryTv.setText(warehouseBean.getQuantity_total() + "件");
                        priceTv.setText(goodBean.getPrice().getSymbol() +  goodBean.getPrice().getPrice());
                        weightTv.setText(warehouseBean.getPrd_weight() + "g");
                        if (warehouseBean.getPrd_weight() == 0) {
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


    private void doDingHuo(final WarehouseBean.ResultsBean.ItemBean bean) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "订货中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        RetrofitManager.createString(ApiService.class).createPurchasingitem(bean.getProduct_id())
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
            actionDialog = new AlertDialog.Builder(this).setTitle("確認要訂貨嗎？")
                    .setNegativeButton("取消", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("確定", ((dialog, which) -> {
                        dialog.dismiss();
                        doDingHuo(goodBean);
                    }))
                    .create();
            actionDialog.setOnDismissListener(dialog -> actionDialog = null);
            actionDialog.show();
        }
    }
}
