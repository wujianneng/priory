package com.pos.priory.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.adapters.InventoryRecoverAdapter;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.beans.InventoryBean;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryFragment extends BaseFragment {
    View view;
    InventoryStoreAdapter storeAdapter;
    InventoryRecoverAdapter recoverAdapter;
    List<InventoryBean> storeDataList = new ArrayList<>();
    List<String> recoverDataList = new ArrayList<>();
    int currentStorePage = 1, currentRecoverPage = 1;
    @Bind(R.id.btn_store)
    TextView btnStore;
    @Bind(R.id.btn_recover)
    TextView btnRecover;
    @Bind(R.id.recycler_view_store)
    SwipeMenuRecyclerView recyclerViewStore;
    @Bind(R.id.refresh_layout_store)
    SmartRefreshLayout refreshLayoutStore;
    @Bind(R.id.recycler_view_recover)
    SwipeMenuRecyclerView recyclerViewRecover;
    @Bind(R.id.refresh_layout_recover)
    SmartRefreshLayout refreshLayoutRecover;

    boolean isNotFirstShowRecover = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        refreshLayoutStore.setEnableLoadMore(false);
        refreshLayoutStore.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshStoreRecyclerView(false);
            }
        });
        refreshLayoutStore.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayoutStore.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayoutStore.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshStoreRecyclerView(true);
            }
        });
        //设置侧滑菜单
        recyclerViewStore.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem dinghuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("訂貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.drag_btn_red))
                        .setImage(R.drawable.icon_tuihuo)
                        .setText("退貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        recyclerViewStore.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
//                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showActionDialog(true);
                } else {
                    showActionDialog(false);
                }
            }
        });
        storeAdapter = new InventoryStoreAdapter(getActivity(),R.layout.inventory_store_list_item, storeDataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerViewStore.setLayoutManager(mLayoutManager);
        recyclerViewStore.setAdapter(storeAdapter);

        refreshLayoutRecover.setEnableLoadMore(false);
        refreshLayoutRecover.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecoverRecyclerView(false);
            }
        });
        refreshLayoutRecover.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayoutRecover.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayoutRecover.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecoverRecyclerView(true);
            }
        });
        //设置侧滑菜单
        recyclerViewRecover.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem dinghuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("訂貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.drag_btn_red))
                        .setImage(R.drawable.icon_tuihuo)
                        .setText("退貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        recyclerViewRecover.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
//                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showActionDialog(true);
                } else {
                    showActionDialog(false);
                }
            }
        });
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        recoverAdapter = new InventoryRecoverAdapter(R.layout.inventory_recover_list_item, recoverDataList);
        recyclerViewRecover.setLayoutManager(mLayoutManager2);
        recyclerViewRecover.setAdapter(recoverAdapter);

        refreshStoreRecyclerView(false);
    }

    AlertDialog actionDialog;

    private void showActionDialog(boolean isDingHuo) {
        if (actionDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(getActivity()).setView(view)
                    .create();
            TextView title = (TextView) view.findViewById(R.id.title) ;
            title.setText(isDingHuo ? "訂貨" : "退貨");
            view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                }
            });
            actionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    actionDialog = null;
                }
            });
            actionDialog.show();
            Window window = actionDialog.getWindow();
            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.inventory_dialog_bg));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = DeviceUtil.dip2px(getActivity(), 200);
            layoutParams.height = DeviceUtil.dip2px(getActivity(), 270);
            window.setGravity(Gravity.CENTER);
            window.setAttributes(layoutParams);
        }
    }

    private void refreshStoreRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            storeDataList.clear();
            storeAdapter.notifyDataSetChanged();
        }
        String location = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        OkHttp3Util.doGetWithToken(Constants.GET_INVENTORYS_URL + "?location=" + location,
                sharedPreferences, new Okhttp3StringCallback("getInventorys") {
            @Override
            public void onSuccess(String results) throws Exception {
                final List<InventoryBean> orderBeanList = gson.fromJson(results,new TypeToken<List<InventoryBean>>(){}.getType());
                new RunOnUiThreadSafe(getActivity()){
                    @Override
                    public void runOnUiThread() {
                        if(orderBeanList != null){
                            storeDataList.addAll(orderBeanList);
                            storeAdapter.notifyDataSetChanged();
                        }
                        refreshLayoutStore.finishLoadMore();
                        refreshLayoutStore.finishRefresh();
                    }
                };
            }

            @Override
            public void onFailed(String erromsg) {
                new RunOnUiThreadSafe(getActivity()){
                    @Override
                    public void runOnUiThread() {
                        refreshLayoutStore.finishLoadMore();
                        refreshLayoutStore.finishRefresh();
                    }
                };
            }
        });
    }

    private void refreshRecoverRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            recoverDataList.clear();
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverAdapter.notifyDataSetChanged();
        } else {
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverDataList.add("0");
            recoverAdapter.notifyDataSetChanged();
        }
        refreshLayoutRecover.finishLoadMore();
        refreshLayoutRecover.finishRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_store, R.id.btn_recover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_store:
                btnStore.setTextColor(getResources().getColor(R.color.colorAccent));
                btnRecover.setTextColor(Color.parseColor("#cccccc"));
                refreshLayoutStore.setVisibility(View.VISIBLE);
                refreshLayoutRecover.setVisibility(View.GONE);
                break;
            case R.id.btn_recover:
                btnStore.setTextColor(Color.parseColor("#cccccc"));
                btnRecover.setTextColor(getResources().getColor(R.color.colorAccent));
                refreshLayoutStore.setVisibility(View.GONE);
                refreshLayoutRecover.setVisibility(View.VISIBLE);
                if (!isNotFirstShowRecover) {
                    refreshRecoverRecyclerView(false);
                }
                isNotFirstShowRecover = true;
                break;
        }
    }


}
