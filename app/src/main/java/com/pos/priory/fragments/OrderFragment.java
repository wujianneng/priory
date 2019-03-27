package com.pos.priory.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.priory.utils.RunOnUiThreadSafe;
import com.pos.zxinglib.utils.DeviceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

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
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    OrderAdapter orderAdapter;
    List<OrderBean> orderList = new ArrayList<>();
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    int currentPage = 1;
    @Bind(R.id.gold_price_layout)
    LinearLayout goldPriceLayout;
    @Bind(R.id.row1_tv1)
    TextView row1Tv1;
    @Bind(R.id.row1_tv2)
    TextView row1Tv2;
    @Bind(R.id.row1)
    FrameLayout row1;
    @Bind(R.id.row2_tv1)
    TextView row2Tv1;
    @Bind(R.id.row2_tv2)
    TextView row2Tv2;
    @Bind(R.id.row2)
    FrameLayout row2;
    @Bind(R.id.row3_tv1)
    TextView row3Tv1;
    @Bind(R.id.row3_tv2)
    TextView row3Tv2;
    @Bind(R.id.row3)
    FrameLayout row3;
    @Bind(R.id.row4_tv1)
    TextView row4Tv1;
    @Bind(R.id.row4_tv2)
    TextView row4Tv2;
    @Bind(R.id.row4)
    FrameLayout row4;


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
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                Log.e("viewtype","viewtype:" + viewType);
//                if(viewType == 0 && MyApplication.staffInfoBean.getPermission().equals("店員")) {
                    SwipeMenuItem cancelItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                            .setImage(R.drawable.edit)
                            .setText("撤回")
                            .setTextColor(Color.WHITE)
                            .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                            .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                    swipeRightMenu.addMenuItem(cancelItem);//设置右边的侧滑
//                }
            }
        });
        orderAdapter = new OrderAdapter(R.layout.order_list_item, orderList);
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showIsCancelOrderDialog(adapterPosition);
                }
            }
        });
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

    private void showIsCancelOrderDialog(int adapterPosition) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("是否確定要撤回該訂單？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
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
        row1Tv1.setText("營業額：22222元");
        row2Tv1.setText("現金：222元 | 現金卷：100元");
        row3Tv1.setText("信用卡：100元");
        row3Tv2.setText("总数：12件");
        row4Tv1.setText("支付寶：222元 | 微信支付：100元");
        row4Tv2.setText("总重：15g");
        OkHttp3Util.doGetWithToken(Constants.GOLD_PRICE_URL, sharedPreferences, new Okhttp3StringCallback(this, "getCurrentGoldPrice") {
            @Override
            public void onSuccess(String results) throws Exception {
                String currentGoldPrice = new JSONObject(results).getString("price");
                goldPriceLayout.setVisibility(View.VISIBLE);
                row1Tv2.setText("金价：" + (int) Double.parseDouble(currentGoldPrice) + "/g");
                row2Tv2.setText("金价：" + (int) (Double.parseDouble(currentGoldPrice) * 37.5) + "/兩");
            }

            @Override
            public void onFailed(String erromsg) {
                goldPriceLayout.setVisibility(View.GONE);
            }
        });
    }



    private void refreshRecyclerView(final boolean isLoadMore) {
        if (!isLoadMore) {
            currentPage = 1;
            orderList.clear();
            orderAdapter.notifyDataSetChanged();
            getCurrentGoldPrice();
        }
        String storeName = MyApplication.staffInfoBean.getStore();
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
