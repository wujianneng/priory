package com.pos.priory.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    List<InventoryDetialBean.ItemBean> dataList = new ArrayList<>();
    int inventoryId = 0;
    int categoryId = 0;
    boolean status = false;
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

    boolean counted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_inventory_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        inventoryId = getIntent().getIntExtra("inventoryId", 0);
        status = getIntent().getBooleanExtra("status", false);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        scanBtn.setVisibility(!status ? View.VISIBLE : View.GONE);
        titleTv.setText("盘点分类详情");
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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("未盤點"));
        tabLayout.addTab(tabLayout.newTab().setText("已盤點"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                counted = tab.getPosition() == 1;
                refreshRecyclerView();
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

    Disposable disposable;
    private void refreshRecyclerView() {
        if(disposable != null && !disposable.isDisposed()) disposable.dispose();
        Observable<InventoryDetialBean> observable;
        if (edtSearch.getText().toString().isEmpty())
            observable = RetrofitManager.createGson(ApiService.class)
                    .getBigInventoryDetail(inventoryId, categoryId, counted);
        else
            observable = RetrofitManager.createGson(ApiService.class)
                    .getBigInventoryDetailWithSearch(inventoryId, categoryId, counted,edtSearch.getText().toString());
        disposable = RetrofitManager.excuteGson(this.<String>bindToLifecycle(), observable, new ModelGsonListener<InventoryDetialBean>() {
            @Override
            public void onSuccess(InventoryDetialBean result) throws Exception {
                dataList.clear();
                dataList.addAll(result.getItem());
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailed(String erromsg) {
                refreshLayout.finishRefresh();
            }
        });

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
                    String str = data.getStringExtra("resultString");
                    for (InventoryDetialBean.ItemBean bean : dataList) {
                        Log.e("result", "str:" + str + " code:" + bean.getCode());
                        if (bean.getCode().equals(str)) {
                            doInventry(bean);
                        }
                    }
                }
                break;
        }
    }

    private void doInventry(InventoryDetialBean.ItemBean bean) {
        RetrofitManager.createString(ApiService.class)
                .updateOnBigInventoryItemById(inventoryId, bean.getCode())
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test","s:" + s);
                        refreshLayout.autoRefresh();
                        showInventorySuccessDialog(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test","throwable:" + throwable.getMessage());
                        if(throwable.getMessage().contains("204")){
                            showInventoryedDialog(bean);
                        }
                    }
                });
    }

    private void showInventoryedDialog(InventoryDetialBean.ItemBean bean) {
        new AlertDialog.Builder(this).setTitle("已盤點過")
                .setMessage(bean.getName() + "   " + bean.getCode())
                .setPositiveButton("繼續掃描",((dialog, which) -> {
                    onClickScan();
                    dialog.dismiss();
                })).create().show();
    }

    private void showInventorySuccessDialog(InventoryDetialBean.ItemBean bean) {
        new AlertDialog.Builder(this).setTitle("盤點成功")
                .setMessage(bean.getName() + "   " + bean.getCode())
                .setPositiveButton("繼續掃描",((dialog, which) -> {
                    onClickScan();
                    dialog.dismiss();
                })).create().show();
    }


}
