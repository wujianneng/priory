package com.pos.priory.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryDetialAdapter;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.InventryScanBean;
import com.pos.zxinglib.MipcaActivityCapture;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    InventoryDetialAdapter adapter;
    List<InventoryDetialBean> dataList = new ArrayList<>();
    int inventoryId = 0;
    String status = "未完成";
    int page = 1;
    @Bind(R.id.search_img)
    ImageView searchImg;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.search_card)
    CardView searchCard;
    @Bind(R.id.search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_inventory_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        inventoryId = getIntent().getIntExtra("inventoryId", 0);
        status = getIntent().getStringExtra("status");
        scanBtn.setVisibility(status.equals("未完成") ? View.VISIBLE : View.GONE);
        titleTv.setText("盘点分类详情");
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

        tabLayout.addTab(tabLayout.newTab().setText("未盤點"));
        tabLayout.addTab(tabLayout.newTab().setText("已盤點"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("未盤點"))
                    refreshRecyclerView(true);
                else
                    refreshRecyclerView(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        refreshLayout.autoRefresh();
    }

    private void refreshRecyclerView(final boolean isRefresh) {
        RetrofitManager.createString(ApiService.class)
                .getBigInventoryById(inventoryId, page)
                .compose(this.<String>bindToLifecycle())
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
                .compose(this.<String>bindToLifecycle())
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



}
