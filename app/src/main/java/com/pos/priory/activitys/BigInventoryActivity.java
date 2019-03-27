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
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.InventoryBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        adapter = new InventoryStoreAdapter(this,R.layout.inventory_store_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                  Intent intent = new Intent(BigInventoryActivity.this,BigInventoryDetialActivity.class);
                  startActivity(intent);
            }
        });
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        dataList.clear();
        dataList.add(new InventoryBean());
        dataList.add(new InventoryBean());
        dataList.add(new InventoryBean());
        dataList.add(new InventoryBean());
        dataList.add(new InventoryBean());
    }


    @OnClick({R.id.back_btn,R.id.right_img})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_img:
                break;
        }
    }
}
