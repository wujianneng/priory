package com.pos.priory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.BigInventoryTypesDetialActivity;
import com.pos.priory.activitys.NfcActivity;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.beans.CreateInventoryResultBean;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryFragment extends BaseFragment {
    View view;
    @Bind(R.id.padding_layout)
    View paddingLayout;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.title_layout)
    FrameLayout titleLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    InventoryStoreAdapter adapter;
    List<InventoryBean.ResultsBean> dataList = new ArrayList<>();
    @Bind(R.id.otg_btn)
    Button otgBtn;

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    boolean needRefresh = false;

    CustomDialog customDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView();
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        adapter = new InventoryStoreAdapter(getActivity(), R.layout.inventory_store_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), BigInventoryTypesDetialActivity.class);
                intent.putExtra("inventoryId", dataList.get(position).getId());
                intent.putExtra("status", dataList.get(position).isDone());
                startActivity(intent);
            }
        });
        refreshLayout.autoRefresh();

        otgBtn.setVisibility(View.GONE);
        otgBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), NfcActivity.class)));

        fab.setOnClickListener(view1 -> {
            if (customDialog == null) {
                customDialog = new CustomDialog(getActivity(), "創建盤點中..");
                customDialog.setOnDismissListener(dialogInterface -> customDialog = null);
                customDialog.show();
                RetrofitManager.excute(RetrofitManager.createString(ApiService.class).createNewInventry(MyApplication.staffInfoBean.getShopid()), new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        if (customDialog != null)
                            customDialog.dismiss();
                        CreateInventoryResultBean createInventoryResultBean = gson.fromJson(result, CreateInventoryResultBean.class);
                        Intent intent = new Intent(getActivity(), BigInventoryTypesDetialActivity.class);
                        intent.putExtra("inventoryId", createInventoryResultBean.getId());
                        intent.putExtra("status", createInventoryResultBean.isDone());
                        startActivity(intent);
                        needRefresh = true;
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        if (customDialog != null)
                            customDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh) {
            refreshRecyclerView();
            needRefresh = false;
        }
    }

    private void refreshRecyclerView() {
        dataList.clear();
        adapter.notifyDataSetChanged();
        RetrofitManager.excuteGson(this.bindToLifecycle(),
                RetrofitManager.createGson(ApiService.class).getInventorys(),
                new ModelGsonListener<InventoryBean>() {
                    @Override
                    public void onSuccess(InventoryBean result) throws Exception {
                        if (result != null && result.getResults().size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            dataList.addAll(result.getResults());
                            adapter.notifyDataSetChanged();
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishRefresh();
                        Log.e("test", "size:" + dataList.size());
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        Log.e("test", "throwable:" + erromsg);
                        empty_layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
