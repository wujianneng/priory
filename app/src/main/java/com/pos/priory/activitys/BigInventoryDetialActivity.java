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

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryDetialAdapter;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.zxinglib.MipcaActivityCapture;
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
    List<InventoryBean.InventoryitemBean> dataList = new ArrayList<>();
    int inventoryId = 0;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_big_inventory_detial);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        inventoryId = getIntent().getIntExtra("inventoryId",0);
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
        refreshLayout.autoRefresh();
    }

    private void refreshRecyclerView() {
        RetrofitManager.createString(ApiService.class).getBigInventoryById(inventoryId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        List<InventoryBean>  inventoryBean = gson.fromJson(s,new TypeToken<List<InventoryBean>>(){}.getType());
                        dataList.clear();
                        dataList.addAll(inventoryBean.get(0).getInventoryitem());
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        leftTv.setText("黃金：" + inventoryBean.get(0).getGolditemcount()+ "件 | " + inventoryBean.get(0).getGolditemweight() + "克");
                        rightTv.setText("晶石：" + inventoryBean.get(0).getCystalitemcount() + "件");
                        Log.e("test","size:" + dataList.size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @OnClick({R.id.back_btn})
    public void onClickBack(){
        finish();
    }

    @OnClick({R.id.right_img})
    public void onClickScan(){
        startActivityForResult(new Intent(BigInventoryDetialActivity.this, MipcaActivityCapture.class), 1000);
    }

    @OnClick({R.id.btn_finish})
    public void onClickFinish(){
        finishInventory();
    }

    private void finishInventory() {
        RetrofitManager.createString(ApiService.class)
                .updateBigInventoryById(inventoryId,"已完成")
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
        Log.e("result", "result:" + data.getStringExtra("resultString"));
        switch (resultCode) {
            case 1:
                String str = data.getStringExtra("resultString");
                Log.e("result", "result:" + str);
                for(InventoryBean.InventoryitemBean bean : dataList){
                    if(bean.getStockno().equals(str)){
                        doInventry(bean.getId());
                    }
                }
                break;
        }
    }

    private void doInventry(int id) {
       RetrofitManager.createString(ApiService.class)
               .updateOnBigInventoryItemById(id,true)
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
