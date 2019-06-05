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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.BalanceActivity;
import com.pos.priory.activitys.BillActivity;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.ColseActivityUtils;
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
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    public static final String UPDATE_ORDER_LIST = "orderFragment_update_list";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void handleEventBus(String event) {
        super.handleEventBus(event);
        if (event.equals(UPDATE_ORDER_LIST)) {
            smartRefreshLayout.autoRefresh();
        }
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
                Log.e("viewtype", "viewtype:" + viewType);
                if (viewType == 0 && MyApplication.staffInfoBean.getPermission().equals("店長")) {
                    SwipeMenuItem cancelItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                            .setImage(R.drawable.edit)
                            .setText("撤回")
                            .setTextColor(Color.WHITE)
                            .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                            .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                    swipeRightMenu.addMenuItem(cancelItem);//设置右边的侧滑
                }
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
    }

    private void showIsCancelOrderDialog(final int adapterPosition) {
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
                        cancelOrder(adapterPosition);
                    }
                })
                .create();
        dialog.show();
    }

    CustomDialog customDialog;
    private void cancelOrder(final int pos) {
        if (customDialog == null) {
            customDialog = new CustomDialog(getActivity(), "正在撤回訂單..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            RetrofitManager.createString(ApiService.class)
                    .deleteOrder(orderList.get(pos).getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.e("test", "結算成功");
                            customDialog.dismiss();
                            orderList.remove(pos);
                            orderAdapter.notifyItemRangeChanged(0,orderList.size());
                            orderAdapter.notifyItemRemoved(pos);
                            Toast.makeText(getActivity(), "撤回訂單成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            customDialog.dismiss();
                            Log.e("test", "結算失败:" + throwable.getMessage());
                            Toast.makeText(getActivity(), "撤回訂單失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void getCurrentGoldPrice() {
        RetrofitManager.createString(ApiService.class)
                .getCurrentGoldPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("okhttp", "getCurrentGoldPriceresult:" + s);
                        String currentGoldPrice = new JSONObject(s).getString("price");
                        goldPriceLayout.setVisibility(View.VISIBLE);
                        row1Tv2.setText("金價：" + (int) Double.parseDouble(currentGoldPrice) + "/g");
                        row2Tv2.setText("金價：" + (int) (Double.parseDouble(currentGoldPrice) * 37.5) + "/两");
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        return  RetrofitManager.createString(ApiService.class).getDashboard();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        Log.e("okhttp", "getDashboardresult:" + s);
                        row1Tv1.setText("營業額：" + jsonObject.getDouble("turnover"));
                        row2Tv1.setText("現金：" + jsonObject.getDouble("cash") + " | " + " 現金券：" + jsonObject.getDouble("voucher"));
                        row3Tv1.setText("信用卡：" + jsonObject.getDouble("credit"));
                        row3Tv2.setText("總數：" + (int)jsonObject.getDouble("orderitem") + "件");
                        row4Tv1.setText("支付寶：" + jsonObject.getDouble("alipay") + " | " + " 微信支付：" + jsonObject.getDouble("wechatpay"));
                        row4Tv2.setText("總重：" + jsonObject.getDouble("weight") + "g");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
        RetrofitManager.createString(ApiService.class).getTodayOrders(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        currentPage++;
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        if (orderBeanList != null) {
                            orderList.addAll(orderBeanList);
                            orderAdapter.notifyDataSetChanged();
                        }
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
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
