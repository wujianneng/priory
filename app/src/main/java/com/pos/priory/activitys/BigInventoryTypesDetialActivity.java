package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.InventoryTeypDetialAdapter;
import com.pos.priory.beans.InventoryTypeDetialBean;
import com.pos.priory.networks.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigInventoryTypesDetialActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.finish_btn)
    MaterialButton finishBtn;

    InventoryTeypDetialAdapter inventoryTeypDetialAdapter;
    List<InventoryTypeDetialBean.CategoryBean> datalist = new ArrayList<>();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_inventory_types_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("盘点详情");
        endTimeTv.setVisibility(getIntent().getBooleanExtra("status",
                false) ? View.VISIBLE : View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        inventoryTeypDetialAdapter = new InventoryTeypDetialAdapter(R.layout.big_inventory_detail_list_item, datalist);
        recyclerView.setAdapter(inventoryTeypDetialAdapter);
        inventoryTeypDetialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                goTypeDetailActivity(datalist.get(position).getId());
            }
        });
        getInventoryDetail();
    }

    @OnClick({R.id.back_btn, R.id.finish_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.finish_btn:
                finishInventory();
                break;
        }
    }

    public void goTypeDetailActivity(int categoryId) {
        Intent intent = new Intent(this, BigInventoryDetialActivity.class);
        intent.putExtra("inventoryId", getIntent().getIntExtra("inventoryId", 0));
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("status", getIntent().getBooleanExtra("status", false));
        startActivity(intent);
    }

    private void getInventoryDetail() {
        RetrofitManager.excuteGson(this.bindToLifecycle(), RetrofitManager.createGson(ApiService.class).
                getInventoryTypeDetailById(getIntent().getIntExtra("inventoryId", 0)), new ModelGsonListener<InventoryTypeDetialBean>() {
            @Override
            public void onSuccess(InventoryTypeDetialBean result) throws Exception {
                startTimeTv.setText(result.getCreated());
                datalist.addAll(result.getCategory());
                inventoryTeypDetialAdapter.notifyDataSetChanged();
                finishBtn.setVisibility(result.isDone() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onFailed(String erromsg) {

            }
        });
    }

    private void finishInventory() {
        RetrofitManager.createString(ApiService.class)
                .updateBigInventoryById(getIntent().getIntExtra("inventoryId", 0))
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort("已成功完成该盘点！");
                        onBackPressed();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("完成该盘点失败！");
                    }
                });
    }


}
