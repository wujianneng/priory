package com.pos.priory.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.activitys.GoodDetialActivity;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.adapters.RepertoryReturnAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.ReturnGoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.utils.DeviceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryFragment extends BaseFragment {
    View view;
    List<GoodBean> dataList = new ArrayList<>();
    List<ReturnGoodBean.ReturnstockitemBean> returnGoodBeanList = new ArrayList<>();
    RepertoryAdapter repertoryAdapter;
    RepertoryReturnAdapter repertoryReturnAdapter;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    String currentStr = "";
    @Bind(R.id.return_recycler_view)
    RecyclerView returnRecyclerView;

    boolean isStoreRepertory = true;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.dinghuo_tv)
    TextView dinghuoTv;
    @Bind(R.id.title_layout)
    FrameLayout titleLayout;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.btn_clear)
    ImageView btnClear;
    @Bind(R.id.search_card)
    CardView searchCard;
    @Bind(R.id.btn_select_repertory)
    MaterialButton btnSelectRepertory;
    @Bind(R.id.btn_records)
    MaterialButton btnRecords;
    @Bind(R.id.btn_select_type)
    MaterialButton btnSelectType;
    @Bind(R.id.btn_select_order_params)
    MaterialButton btnSelectOrderParams;
    @Bind(R.id.btn_select_order_type)
    MaterialButton btnSelectOrderType;
    @Bind(R.id.filter_params_layout)
    LinearLayout filterParamsLayout;
    @Bind(R.id.padding_layout)
    View paddingLayout;
    @Bind(R.id.left_tv)
    TextView leftTv;

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
                if (isStoreRepertory) {
                    refreshRecyclerView(currentStr);
                } else {
                    refreshReturnRecyclerView();
                }
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));

        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("退货")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getActivity(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getActivity(), 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
                SwipeMenuItem diaohuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_red))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("调货")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getActivity(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getActivity(), 100));//设置宽
                swipeRightMenu.addMenuItem(diaohuoItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
//                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。0是左，右是1，暂时没有用到
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    showIsReturnDialog(adapterPosition);
                } else {
                    getExchangeableStores(adapterPosition);
                }
            }
        });

        repertoryReturnAdapter = new RepertoryReturnAdapter(getActivity(), R.layout.repertory_list_item, returnGoodBeanList);
        LinearLayoutManager mLayoutManager0 = new LinearLayoutManager(getActivity());
        mLayoutManager0.setOrientation(OrientationHelper.VERTICAL);
        returnRecyclerView.setLayoutManager(mLayoutManager0);
        returnRecyclerView.setAdapter(repertoryReturnAdapter);

        repertoryAdapter = new RepertoryAdapter(getActivity(), R.layout.repertory_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position) -> {
            Intent intent = new Intent(getActivity(), GoodDetialActivity.class);
            intent.putExtra("goodbean", gson.toJson(dataList.get(position)));
            startActivity(intent);
            edtSearch.setText("");
        });
        refreshLayout.autoRefresh();
    }

    private void refreshMainRepertorySumDatas(boolean isMain) {
//        RetrofitManager.createString(ApiService.class).getStockSumDatas()
//                .compose(this.<String>bindToLifecycle())
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        JSONObject jsonObject = new JSONObject(s);
//                        leftTv.setText("黄金：" + jsonObject.getInt("goldcount") + "件 | " + jsonObject.getDouble("goldweight") + "g");
//                        rightTv.setVisibility(View.VISIBLE);
//                        rightTv.setText("晶石：" + jsonObject.getInt("cystalcount") + "件");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
        int count = 0;
        float weight = 0;
        if (isMain) {
            for (GoodBean goodBean : dataList) {
                count += goodBean.getQuantity();
                weight += goodBean.getWeight();
            }
        } else {
            for (ReturnGoodBean.ReturnstockitemBean goodBean : returnGoodBeanList) {
                count++;
                weight += Double.parseDouble(goodBean.getWeight());
            }
        }
        leftTv.setText("數量：" + count + "件，" + "重量：" + LogicUtils.getKeepLastTwoNumberAfterLittlePoint(weight) + "g");
    }

    private void showRepertoryMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectRepertory);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.repertory_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener((item) -> {
            // 控件每一个item的点击事件
            switch (item.getItemId()) {
                case R.id.menu0:
                    btnSelectRepertory.setText(item.getTitle());
                    onChangeRepertoryListener(true);
                    searchCard.setVisibility(View.VISIBLE);
                    filterParamsLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.menu1:
                    btnSelectRepertory.setText(item.getTitle());
                    onChangeRepertoryListener(false);
                    searchCard.setVisibility(View.GONE);
                    filterParamsLayout.setVisibility(View.GONE);
                    break;
            }
            return true;
        });
    }


    public void onChangeRepertoryListener(boolean isStore) {
        isStoreRepertory = isStore;
        if (isStore) {
            recyclerView.setVisibility(View.VISIBLE);
            returnRecyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            returnRecyclerView.setVisibility(View.VISIBLE);
        }
        refreshLayout.autoRefresh();
    }

    private void showIsReturnDialog(final int adapterPosition) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("是否确定要退货该商品？")
                .setNegativeButton("取消", (mdialog, which) -> {
                    mdialog.dismiss();
                })
                .setPositiveButton("确定", (mdialog, which) -> {
                    mdialog.dismiss();
                    doReturnStock(adapterPosition);
                })
                .create();
        dialog.show();
    }

    CustomDialog customDialog;

    private void doReturnStock(int pos) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "退货中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                customDialog = null;
            }
        });
        customDialog.show();
        GoodBean goodBean = dataList.get(pos);
        RetrofitManager.excute(this.bindToLifecycle(), RetrofitManager.createString(ApiService.class)
                .returnStockById(goodBean.getId()), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                customDialog.dismiss();
                ToastUtils.showShort("退货成功");
                refreshLayout.autoRefresh();
            }

            @Override
            public void onFailed(String erromsg) {
                customDialog.dismiss();
                ToastUtils.showShort("退货失败");
            }
        });
    }

    List<String> stores = new ArrayList<>();
    Map<String, Integer> storesMaps = new HashMap<>();

    private void getExchangeableStores(final int adapterPosition) {
        RetrofitManager.excute(this.bindToLifecycle(),
                RetrofitManager.createString(ApiService.class).getTranStores(),
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        JSONArray jsonArray = new JSONArray(result);
                        stores.clear();
                        storesMaps.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            stores.add(jsonObject.getString("name"));
                            storesMaps.put(jsonObject.getString("name"), jsonObject.getInt("id"));
                        }
                        showChoiceDiscountDialog(adapterPosition);
                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }


    int yourChoice = 0;

    private void showChoiceDiscountDialog(final int position) {
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(getActivity());
        singleChoiceDialog.setTitle("请选择调拨店铺");
        // 第二个参数是默认选项，此处设置为0
        ListAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_single_choice, stores);
        singleChoiceDialog.setSingleChoiceItems(adapter, yourChoice,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            doExchange(storesMaps.get(stores.get(yourChoice)), dataList.get(position).getId());
                        }
                        dialog.dismiss();
                    }
                });
        singleChoiceDialog.create().show();
    }


    private void doExchange(int storeid, int stockid) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "调货中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        List<Map<String, Object>> stocktransfer = new ArrayList<>();
        Map<String, Object> stocktransferMap = new HashMap<>();
        stocktransferMap.put("storeid", storeid);
        stocktransferMap.put("stockid", stockid);
        stocktransfer.add(stocktransferMap);
        paramMap.put("stocktransfer", stocktransfer);
        Log.e("test", "params:" + gson.toJson(paramMap));
        RetrofitManager.createString(ApiService.class)
                .tranferGoods(RequestBody.create(MediaType.parse("application/json"), gson.toJson(paramMap)))
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        ToastUtils.showShort("调货成功");
                        refreshLayout.autoRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        ToastUtils.showShort("调货失败");
                    }
                });
    }


    Disposable call;

    public void refreshRecyclerView(String str) {
        currentStr = str;
        if (call != null)
            call.dispose();
        dataList.clear();
        repertoryAdapter.notifyDataSetChanged();
        Observable<String> observable;
        if (str.equals("")) {
            observable = RetrofitManager.createString(ApiService.class).getStockLists();
        } else {
            observable = RetrofitManager.createString(ApiService.class).getStockListByParam(str);
        }
        call = RetrofitManager.excute(this.bindToLifecycle(), observable,
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        JSONObject jsonObject = new JSONObject(result);
                        List<GoodBean> goodBeanList = gson.fromJson(jsonObject.getJSONArray("results").toString(), new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null) {
                            dataList.addAll(goodBeanList);
                            repertoryAdapter.notifyDataSetChanged();
                        }
                        refreshLayout.finishRefresh();
                        refreshMainRepertorySumDatas(true);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    public void refreshReturnRecyclerView() {
        if (call != null)
            call.dispose();
        returnGoodBeanList.clear();
        repertoryReturnAdapter.notifyDataSetChanged();
        call = RetrofitManager.excute(this.<String>bindToLifecycle(),
                RetrofitManager.createString(ApiService.class).getReturnStockLists(),
                new ModelListener() {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        ReturnGoodBean returnGoodBean = gson.fromJson(results, ReturnGoodBean.class);
                        if (returnGoodBean != null) {
                            returnGoodBeanList.addAll(returnGoodBean.getReturnstockitem());
                            repertoryReturnAdapter.notifyDataSetChanged();
                        }
                        refreshLayout.finishRefresh();
                        refreshMainRepertorySumDatas(false);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @OnClick({R.id.btn_select_repertory})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_repertory:
                showRepertoryMenu();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
