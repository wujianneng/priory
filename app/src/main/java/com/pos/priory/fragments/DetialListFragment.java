package com.pos.priory.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.adapters.DetialListAdapter;
import com.pos.priory.beans.PurchasingItemBean;
import com.pos.priory.coustomViews.CustomDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DetialListFragment extends BaseFragment {
    View view;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    DetialListAdapter detialListAdapter;
    List<PurchasingItemBean> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detiallist, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(true);
            }
        });
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem commitItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                        .setImage(R.drawable.edit)
                        .setText("确认")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(commitItem);//设置右边的侧滑
                SwipeMenuItem dinghuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(Color.parseColor("#87CEFA"))
                        .setImage(R.drawable.edit)
                        .setText("編輯")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_red))
                        .setImage(R.drawable.icon_delete)
                        .setText("刪除")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
            }
        });
        detialListAdapter = new DetialListAdapter(getActivity(), R.layout.detial_list_item, dataList);
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                   comfirmPurshing(dataList.get(adapterPosition));
                } else if(menuPosition == 1){
                    showEditDialog(adapterPosition);
                }else {
                    deletePurshing(dataList.get(adapterPosition),adapterPosition);
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(detialListAdapter);

        refreshLayout.autoRefresh();
    }

    AlertDialog actionDialog;

    private void showEditDialog(int position) {
        if (actionDialog == null) {
            final PurchasingItemBean purchasingBean = dataList.get(position);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(getActivity()).setView(view)
                    .create();
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText("編輯");
            ImageView icon_good = view.findViewById(R.id.icon_good);
            TextView code_tv = view.findViewById(R.id.code_tv);
            TextView name_tv = view.findViewById(R.id.name_tv);
            final EditText edt_count = view.findViewById(R.id.edt_count);
            Glide.with(getActivity()).load(Constants.BASE_URL + purchasingBean.getStock().getBatch().getProduct().getImage())
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(icon_good);
            code_tv.setText(purchasingBean.getStock().getBatch().getProduct().getProductcode() + "");
            name_tv.setText(purchasingBean.getStock().getBatch().getProduct().getName());
            view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edt_count.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "請輸入數量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(edt_count.getText().toString());
                    editPurshing(purchasingBean, count);
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

    CustomDialog customDialog;

    private void editPurshing(PurchasingItemBean bean, int count) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "編輯中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", count);
        OkHttp3Util.doPatchWithToken(Constants.PURCHASING_ITEM_URL + "/" + bean.getId() + "/update/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(getActivity(), "editPurshing") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "編輯成功", Toast.LENGTH_SHORT).show();
                        refreshLayout.autoRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "編輯失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void comfirmPurshing(PurchasingItemBean bean) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "確認中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("confirmed", true);
        OkHttp3Util.doPatchWithToken(Constants.PURCHASING_URL + "/" + bean.getPurchasing().getId() + "/update/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(getActivity(), "comfirmPurshing") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "確認成功", Toast.LENGTH_SHORT).show();
                        refreshLayout.autoRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "確認失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePurshing(PurchasingItemBean bean,final int position) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "删除中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        OkHttp3Util.doDeleteWithToken(Constants.PURCHASING_URL + "/" + bean.getPurchasing().getId() + "/update/",sharedPreferences,
                new Okhttp3StringCallback(getActivity(), "comfirmPurshing") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dataList.remove(position);
                        detialListAdapter.notifyItemRangeChanged(0, dataList.size());
                        detialListAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "删除失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            dataList.clear();
            detialListAdapter.notifyDataSetChanged();
        }
        final String location = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        OkHttp3Util.doGetWithToken(Constants.PURCHASING_ITEM_URL + "?location=" + location,
                sharedPreferences, new Okhttp3StringCallback("getPurchasings") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<PurchasingItemBean> orderBeanList = gson.fromJson(results, new TypeToken<List<PurchasingItemBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
                                    for(PurchasingItemBean bean : orderBeanList){
                                        if(!bean.getPurchasing().isConfirmed()){
                                            dataList.add(bean);
                                        }
                                    }
                                    detialListAdapter.notifyDataSetChanged();
                                }
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
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
}
