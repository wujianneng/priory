package com.pos.priory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.GoodDetialActivity;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryFragment extends BaseFragment {
    View view;
    List<GoodBean> dataList = new ArrayList<>();
    RepertoryAdapter repertoryAdapter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    String currentStr = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repertory, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(currentStr);
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        repertoryAdapter = new RepertoryAdapter(getActivity(), R.layout.repertory_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), GoodDetialActivity.class);
                intent.putExtra("goodbean", gson.toJson(dataList.get(position)));
                startActivity(intent);
                ((MainActivity) getActivity()).edtSearch.setText("");
            }
        });
        refreshLayout.autoRefresh();
    }


    Disposable call;

    public void refreshRecyclerView(String str) {
        currentStr = str;
        if (call != null)
            call.dispose();
        dataList.clear();
        repertoryAdapter.notifyDataSetChanged();
        String location = MyApplication.staffInfoBean.getStore();
        Observable<String> observable;
        if (str.equals("")) {
            observable = RetrofitManager.createString(ApiService.class).getStockListByLocation(location);
        } else {
            if (LogicUtils.isNumeric(str))
                observable = RetrofitManager.createString(ApiService.class).getStockListByLocationAndProductCode(location, str);
            else
                observable = RetrofitManager.createString(ApiService.class).getStockListByNameAndProductCode(str, location);
        }
        call = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        List<GoodBean> goodBeanList = gson.fromJson(results, new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null) {
                            dataList.addAll(goodBeanList);
                            repertoryAdapter.notifyDataSetChanged();
                        }
                        refreshLayout.finishRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
