package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryDetialAdapter;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.beans.InventoryDetialBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_big_inventory_detial);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("大盤點");
        scanBtn.setImageResource(R.drawable.scan);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView();
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        adapter = new InventoryDetialAdapter(R.layout.inventory_recover_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        dataList.clear();
        dataList.add(new InventoryDetialBean());
        dataList.add(new InventoryDetialBean());
        dataList.add(new InventoryDetialBean());
        dataList.add(new InventoryDetialBean());
        dataList.add(new InventoryDetialBean());
    }

}
