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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.adapters.InventoryRecoverAdapter;
import com.pos.priory.adapters.InventoryStoreAdapter;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.ReturnStockBean;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<ReturnStockBean> recoverDataList = new ArrayList<>();
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
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("訂貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_red))
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
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showActionDialog(0, adapterPosition);
                } else {
                    showActionDialog(1, adapterPosition);
                }
            }
        });
        storeAdapter = new InventoryStoreAdapter(getActivity(), R.layout.inventory_store_list_item, storeDataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerViewStore.setLayoutManager(mLayoutManager);
        recyclerViewStore.setAdapter(storeAdapter);
        storeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.icon_check) {
                    doInventory(position,true);
                }
            }
        });

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
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        recoverAdapter = new InventoryRecoverAdapter(R.layout.inventory_recover_list_item, recoverDataList);
        recyclerViewRecover.setLayoutManager(mLayoutManager2);
        recyclerViewRecover.setAdapter(recoverAdapter);
        recoverAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.icon_check) {
                    doInventory(position,false);
                }
            }
        });
        refreshLayoutStore.autoRefresh();
    }

    private void doInventory(final int position,boolean isInventeStoreGoods) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "盘点中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("check", true);
        if(isInventeStoreGoods) {
            OkHttp3Util.doPatchWithToken(Constants.GET_INVENTORYS_URL + "/" + storeDataList.get(position).getId() + "/update/", gson.toJson(paramMap),
                    sharedPreferences, new Okhttp3StringCallback(getActivity(), "doInventory") {
                        @Override
                        public void onSuccess(String results) throws Exception {
                            customDialog.dismiss();
                            storeDataList.get(position).setCheck(true);
                            storeAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            customDialog.dismiss();
                            Toast.makeText(getActivity(), "盘点失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            OkHttp3Util.doPatchWithToken(Constants.RETURN_STOCKS_URL + "/" + recoverDataList.get(position).getId() + "/update/", gson.toJson(paramMap),
                    sharedPreferences, new Okhttp3StringCallback(getActivity(), "doInventory") {
                        @Override
                        public void onSuccess(String results) throws Exception {
                            customDialog.dismiss();
                            recoverDataList.get(position).setCheck(true);
                            recoverAdapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            customDialog.dismiss();
                            Toast.makeText(getActivity(), "盘点失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    AlertDialog actionDialog;

    private void showActionDialog(final int action, final int position) {
        if (actionDialog == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(getActivity()).setView(view)
                    .create();
            TextView title = (TextView) view.findViewById(R.id.title);
            ImageView icon_good = view.findViewById(R.id.icon_good);
            TextView code_tv = view.findViewById(R.id.code_tv);
            TextView name_tv = view.findViewById(R.id.name_tv);
            final EditText edt_count = view.findViewById(R.id.edt_count);
            if (action == 0) {
                title.setText("訂貨");
                InventoryBean bean = storeDataList.get(position);
                Glide.with(getActivity()).load(Constants.BASE_URL + bean.getStock().getBatch().getProduct().getImage())
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(icon_good);
                code_tv.setText(bean.getStock().getBatch().getProduct().getProductcode() + "");
                name_tv.setText(bean.getStock().getBatch().getProduct().getName());
                edt_count.setText("1");
            }
            if (action == 1) {
                title.setText("退貨");
                InventoryBean bean = storeDataList.get(position);
                Glide.with(getActivity()).load(Constants.BASE_URL + bean.getStock().getBatch().getProduct().getImage())
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(icon_good);
                code_tv.setText(bean.getStock().getBatch().getProduct().getProductcode() + "");
                name_tv.setText(bean.getStock().getBatch().getProduct().getName());
            }

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
                    if (action == 0) {
                        if(count == 0){
                            Toast.makeText(getActivity(), "訂貨數量不能為零", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        InventoryBean bean = storeDataList.get(position);
                        actionDialog.dismiss();
                        createPurchsing(bean, true, count + "");
                    }
                    if (action == 1) {
                        if(count == 0){
                            Toast.makeText(getActivity(), "退貨數量不能為零", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        InventoryBean bean = storeDataList.get(position);
                        if (count > bean.getStock().getQuantity()) {
                            Toast.makeText(getActivity(), "退貨數量不能超過商品庫存數量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        actionDialog.dismiss();
                        createPurchsing(bean, false, count + "");
                    }
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

    private void createPurchsing(final InventoryBean bean, final boolean isdinghuo, final String count) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), isdinghuo ? "訂貨中.." : "退貨中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        String location = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        OkHttp3Util.doGetWithToken(Constants.PURCHASING_URL + "?location=" + location,sharedPreferences,
                new Okhttp3StringCallback(getActivity(), "createPurshing") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        createPurchsingItem(new JSONArray(results).getJSONObject(0).getInt("id") + "", bean.getStock().getId(), count, isdinghuo);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), isdinghuo ? "訂貨失敗" : "退貨失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createPurchsingItem(String id, int stockid, String count, final boolean isdinghuo) {
        Map<String, Object> map = new HashMap<>();
        map.put("purchasing", Integer.parseInt(id));
        map.put("stock", stockid);
        map.put("type", isdinghuo ? "purchase" : "return");
        map.put("quantity", Integer.parseInt(count));
        OkHttp3Util.doPostWithToken(Constants.PURCHASING_ITEM_URL + "/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(getActivity(), "createPurchsingItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), isdinghuo ? "訂貨成功" : "退貨成功", Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putBoolean(Constants.IS_REFRESH_DETIALLISTFRAGMENT,true).commit();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), isdinghuo ? "訂貨失敗" : "退貨失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshStoreRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            storeDataList.clear();
            storeAdapter.notifyDataSetChanged();
        }
        String location = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        OkHttp3Util.doGetWithToken(Constants.GET_INVENTORYS_URL + "/?location=" + location,
                sharedPreferences, new Okhttp3StringCallback("getInventorys") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<InventoryBean> orderBeanList = gson.fromJson(results, new TypeToken<List<InventoryBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
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
                        new RunOnUiThreadSafe(getActivity()) {
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
            recoverAdapter.notifyDataSetChanged();
        }
        OkHttp3Util.doGetWithToken(Constants.RETURN_STOCKS_URL + "?location=" + ((MainActivity) getActivity()).
                        staffInfoBeanList.get(0).getStore(),
                sharedPreferences, new Okhttp3StringCallback("getRecovers") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<ReturnStockBean> orderBeanList = gson.fromJson(results, new TypeToken<List<ReturnStockBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
                                    recoverDataList.addAll(orderBeanList);
                                    recoverAdapter.notifyDataSetChanged();
                                }
                                refreshLayoutRecover.finishLoadMore();
                                refreshLayoutRecover.finishRefresh();
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                refreshLayoutRecover.finishLoadMore();
                                refreshLayoutRecover.finishRefresh();
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
                    refreshLayoutRecover.autoRefresh();
                }
                isNotFirstShowRecover = true;
                break;
        }
    }


}
