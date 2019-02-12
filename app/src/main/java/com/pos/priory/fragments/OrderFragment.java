package com.pos.priory.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.priory.utils.RunOnUiThreadSafe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderFragment extends BaseFragment {
    View view;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    OrderAdapter orderAdapter;
    List<OrderBean> orderList = new ArrayList<>();
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    int currentPage = 1;
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;
    @Bind(R.id.gold_price_layout)
    FrameLayout goldPriceLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(true);
            }
        });
        orderAdapter = new OrderAdapter(R.layout.order_list_item, orderList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("order", gson.toJson(orderList.get(position)));
                startActivity(intent);
            }
        });
        smartRefreshLayout.autoRefresh();
        sharedPreferences.edit().putBoolean("isRefreshOrderFragment", false).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean("isRefreshOrderFragment", false)) {
            smartRefreshLayout.autoRefresh();
            sharedPreferences.edit().putBoolean("isRefreshOrderFragment", false).commit();
        }
    }

    private void getCurrentGoldPrice() {
        goldPriceTv.setVisibility(View.GONE);
        OkHttp3Util.doGetWithToken(Constants.GOLD_PRICE_URL, sharedPreferences, new Okhttp3StringCallback(this, "getCurrentGoldPrice") {
            @Override
            public void onSuccess(String results) throws Exception {
                String currentGoldPrice = new JSONObject(results).getString("price");
                goldPriceLayout.setVisibility(View.VISIBLE);
                goldPriceTv.setVisibility(View.VISIBLE);
                setGoldPriceTvAplaAnimator();
                goldPriceTv.setText("當前金價(MOP)：" + (int)Double.parseDouble(currentGoldPrice) + "/g" + "  " +
                        (int)(Double.parseDouble(currentGoldPrice) * 37.5) + "/兩");
            }

            @Override
            public void onFailed(String erromsg) {
                goldPriceLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setGoldPriceTvAplaAnimator(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f,1f);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float)valueAnimator.getAnimatedValue();
                goldPriceTv.setAlpha(value);
            }
        });
        valueAnimator.start();
    }


    private void refreshRecyclerView(final boolean isLoadMore) {
        if (!isLoadMore) {
            currentPage = 1;
            orderList.clear();
            orderAdapter.notifyDataSetChanged();
            getCurrentGoldPrice();
        }
        String storeName = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        OkHttp3Util.doGetWithToken(Constants.GET_ORDERS_URL + "?location=" + storeName + "&daycontrol=true",
                sharedPreferences, new Okhttp3StringCallback("getOrders") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        currentPage++;
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
                                    orderList.addAll(orderBeanList);
                                    orderAdapter.notifyDataSetChanged();
                                }
                                smartRefreshLayout.finishLoadMore();
                                smartRefreshLayout.finishRefresh();
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                smartRefreshLayout.finishLoadMore();
                                smartRefreshLayout.finishRefresh();
                            }
                        };
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), MemberActivity.class));
                break;
        }
    }
}
