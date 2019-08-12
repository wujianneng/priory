package com.pos.priory.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryDetialAdapter;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.InventryScanBean;
import com.pos.zxinglib.MipcaActivityCapture;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigInventoryDetialActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView scanBtn;
    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    InventoryDetialAdapter adapter;
    List<InventoryDetialBean> dataList = new ArrayList<>();
    int inventoryId = 0;
    String status = "未完成";
    @Bind(R.id.btn_finish)
    MaterialButton btnFinish;

    int page = 1;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_big_inventory_detial);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        inventoryId = getIntent().getIntExtra("inventoryId", 0);
        status = getIntent().getStringExtra("status");
        scanBtn.setVisibility(status.equals("未完成") ? View.VISIBLE : View.GONE);
        btnFinish.setVisibility(status.equals("未完成") ? View.VISIBLE : View.GONE);
        titleTv.setText("大盘点");
        scanBtn.setImageResource(R.drawable.scan);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                refreshRecyclerView(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        adapter = new InventoryDetialAdapter(R.layout.inventory_recover_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        refreshLayout.autoRefresh();
    }

    private void refreshRecyclerView(final boolean isRefresh) {
        RetrofitManager.createString(ApiService.class).getBigInventoryById(inventoryId, page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        List<InventoryDetialBean> inventoryBean = gson.fromJson(jsonObject.getJSONArray("results").toString(), new TypeToken<List<InventoryDetialBean>>() {
                        }.getType());
                        if (isRefresh) {
                            dataList.clear();
                        }
                        dataList.addAll(inventoryBean);
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        double sumweight = 0;
                        for (InventoryDetialBean inventoryDetialBean : dataList) {
                            sumweight += inventoryDetialBean.getStockweight();
                        }
                        leftTv.setText("黄金：" + dataList.size() + "件 | " + LogicUtils.getKeepLastTwoNumberAfterLittlePoint(sumweight) + "g");
                        Log.e("test", "size:" + dataList.size());
                        page++;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScanedCode(InventryScanBean inventryScanBean) {
        Log.e("result", "result:" + inventryScanBean.getScanResult());
        doInventry(inventryScanBean.getScanResult());
        final ProgressDialog customDialog = ProgressDialog.show(this, "", "正在准备下一次扫码", true);
        customDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                customDialog.dismiss();
                onClickScan();
            }
        }, 1500);
    }

    @OnClick({R.id.back_btn})
    public void onClickBack() {
        finish();
    }

    @OnClick({R.id.right_img})
    public void onClickScan() {
        Intent intent = new Intent(BigInventoryDetialActivity.this, MipcaActivityCapture.class);
        intent.putExtra("preferences_bulk_mode", true);
        startActivityForResult(intent, 1000);
    }

    @OnClick({R.id.btn_finish})
    public void onClickFinish() {
        finishInventory();
    }

    private void finishInventory() {
        RetrofitManager.createString(ApiService.class)
                .updateBigInventoryById(inventoryId, "已完成")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort("已成功完成该大盘点！");
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("完成该大盘点失败！");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                if (data != null) {
//                    String str = data.getStringExtra("resultString");
//                    Log.e("result", "result:" + str);
//                    for (InventoryBean.InventoryitemBean bean : dataList) {
//                        if (bean.getStockno().equals(str)) {
//                            doInventry(bean.getId());
//                        }
//                    }
                }
                break;
        }
    }

    private void doInventry(String code) {
        RetrofitManager.createString(ApiService.class)
                .updateOnBigInventoryItemById(inventoryId, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        refreshLayout.autoRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
