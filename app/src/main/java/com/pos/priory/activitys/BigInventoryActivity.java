package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigInventoryActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView addBtn;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    InventoryStoreAdapter adapter;
    List<InventoryBean> dataList = new ArrayList<>();

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_big_inventory);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initViews() {
        titleTv.setText("大盤點");
        addBtn.setImageResource(R.drawable.add_new);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView();
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        adapter = new InventoryStoreAdapter(this, R.layout.inventory_store_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(BigInventoryActivity.this, BigInventoryDetialActivity.class);
                intent.putExtra("inventoryId", dataList.get(position).getId());
                startActivity(intent);
            }
        });
        refreshLayout.autoRefresh();
    }

    private void refreshRecyclerView() {
        RetrofitManager.createString(ApiService.class).getBigInventorys()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        refreshLayout.finishRefresh();
                        List<InventoryBean> list = gson.fromJson(s, new TypeToken<List<InventoryBean>>() {
                        }.getType());
                        if (list != null) {
                            dataList.clear();
                            dataList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        Log.e("test", "size:" + dataList.size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                    }
                });
    }


    @OnClick({R.id.back_btn, R.id.right_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_img:
                createBigInventory();
                break;
        }
    }

    CustomDialog customDialog;

    private void createBigInventory() {
        Log.e("test", "createBigInventory（）");
        if (customDialog == null)
            customDialog = new CustomDialog(this, "創建中..");
        customDialog.show();
        RetrofitManager.createString(ApiService.class).createBigInventory()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test", "createBigInventoryaccept（）");
                        customDialog.dismiss();
                        refreshLayout.autoRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(BigInventoryActivity.this, "創建大盤點失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
