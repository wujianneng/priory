package com.pos.priory.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.BalanceActivity;
import com.pos.priory.activitys.BillActivity;
import com.pos.priory.activitys.EditPasswordActivity;
import com.pos.priory.activitys.LoginActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.NfcActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.adapters.TablePrintGoodsAdapter;
import com.pos.priory.beans.CreateOrderResultBean;
import com.pos.priory.beans.DayReportBean;
import com.pos.priory.beans.DayReportDataBean;
import com.pos.priory.beans.DayReportDetailBean;
import com.pos.priory.beans.GoldpriceBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.RollbackOrderParamBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    List<OrderBean.ResultsBean> orderList = new ArrayList<>();
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    int currentPage = 1;
    @Bind(R.id.gold_price_layout)
    FrameLayout goldPriceLayout;
    public static final String UPDATE_ORDER_LIST = "orderFragment_update_list";
    @Bind(R.id.search_img)
    ImageView searchImg;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.search_card)
    CardView searchCard;
    @Bind(R.id.search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.setting_img)
    ImageView settingImg;
    @Bind(R.id.title_layout)
    FrameLayout titleLayout;
    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

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
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> refreshRecyclerView(false));
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> refreshRecyclerView(true));
        recyclerView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
            Log.e("viewtype", "viewtype:" + viewType);
            if (viewType == 0 && MyApplication.staffInfoBean.getApppermit().equals("店长")) {
                SwipeMenuItem cancelItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_red))
                        .setImage(R.drawable.edit)
                        .setText("撤回")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(cancelItem);//设置右边的侧滑
            }
        });
        orderAdapter = new OrderAdapter(R.layout.order_list_item, orderList);



        //设置侧滑菜单的点击事件
        recyclerView.setSwipeMenuItemClickListener(menuBridge -> {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (menuPosition == 0) {
                showIsCancelOrderDialog(adapterPosition);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
            intent.putExtra("orderId", orderList.get(position).getId());
            startActivity(intent);
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshRecyclerView(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        smartRefreshLayout.autoRefresh();
    }

    private void showIsCancelOrderDialog(final int adapterPosition) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("是否确定要撤回该订单？")
                .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("确定", (dialog1, which) -> {
                    dialog1.dismiss();
                    cancelOrder(adapterPosition);
                })
                .create();
        dialog.show();
    }

    CustomDialog customDialog;

    private void cancelOrder(final int pos) {
        if (customDialog == null) {
            customDialog = new CustomDialog(getActivity(), "正在撤回订单..");
            customDialog.setOnDismissListener(dialog -> customDialog = null);
            customDialog.show();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("member", orderList.get(pos).getMember());
            paramMap.put("order", orderList.get(pos).getId());
            Log.e("test", "param:" + gson.toJson(paramMap));
            RetrofitManager.excute(RetrofitManager.createString(ApiService.class)
                    .rollbackOrder(paramMap), new ModelListener() {
                @Override
                public void onSuccess(String result) throws Exception {
                    Log.e("test", "撤回成功");
                    customDialog.dismiss();
                    orderList.remove(pos);
                    orderAdapter.notifyItemRangeChanged(0, orderList.size());
                    orderAdapter.notifyItemRemoved(pos);
                    Toast.makeText(getActivity(), "撤回订单成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(String erromsg) {
                    customDialog.dismiss();
                    Log.e("test", "撤回失败:" + erromsg);
                    Toast.makeText(getActivity(), "撤回订单失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void refreshRecyclerView(final boolean isLoadMore) {
        if (!isLoadMore) {
            currentPage = 1;
            orderList.clear();
            orderAdapter.notifyDataSetChanged();
        }
        Observable<String> observable = edtSearch.getText().toString().isEmpty() ?
                RetrofitManager.createString(ApiService.class).getTodayOrders(currentPage,"true")
                : RetrofitManager.createString(ApiService.class).getOrdersByOrdernumber(edtSearch.getText().toString(),currentPage);
        observable
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        currentPage++;
                        OrderBean orderBean = gson.fromJson(results, OrderBean.class);
                        if (orderBean != null && orderBean.getResults().size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            orderList.addAll(orderBean.getResults());
                            orderAdapter.notifyDataSetChanged();
                        }else {
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        goldPriceLayout.setVisibility(View.VISIBLE);
                        dateTv.setText(orderBean.getDate());
                        goldPriceTv.setText("金價：" + orderBean.getGram_goldprice() + "/g，" + orderBean.getTael_goldprice() + "/两");
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(orderList.size() == 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
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

    @OnClick({R.id.fab, R.id.setting_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), MemberActivity.class));
                break;
            case R.id.setting_img:
                showMainMenu();
                break;
        }
    }

    private void showMainMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), settingImg);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        getDayReport(DateUtils.getDateOfToday(), getActivity(), gson);
                        break;
                    case R.id.menu1:
                        startActivity(new Intent(getActivity(), EditPasswordActivity.class));
                        break;
                    case R.id.menu2:
                        ColseActivityUtils.closeAllAcitivty();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    public static void getDayReport(String date, Activity context, Gson gson) {
        Log.e("test", "date:" + date + " shopid:" + MyApplication.staffInfoBean.getShopid());
        RetrofitManager.createString(ApiService.class).getDayReport(MyApplication.staffInfoBean.getShopid(), date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test", "result:" + s);
                        DayReportBean dayReportBean = gson.fromJson(s, DayReportBean.class);
                        RetrofitManager.createString(ApiService.class).getDayReportDetail(dayReportBean.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        DayReportDetailBean dayReportDetailBean = gson.fromJson(s, DayReportDetailBean.class);
                                        Log.e("test", "result:" + s);
                                        printGoldTable(context, dayReportDetailBean);

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e("test", "throwable:" + gson.toJson(throwable));
                                        Toast.makeText(context, "查不到日报表数据", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test", "throwable:" + gson.toJson(throwable));
                        Toast.makeText(context, "查不到日报表数据", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void printGoldTable(Activity context, DayReportDetailBean dayReportBean) {
        Log.e("test", "printGoldTable");
        List<View> views = new ArrayList<>();
        List<DayReportDetailBean.ProductItemsBean> dataarry = new ArrayList<>();

        for(DayReportDetailBean.ProductItemsBean productItemsBean : dayReportBean.getProduct_items()){
            DayReportDetailBean.ProductItemsBean goldTitle = new DayReportDetailBean.ProductItemsBean();
            goldTitle.setAmount_total(productItemsBean.getAmount_total());
            goldTitle.setCategory_name(productItemsBean.getCategory_name());
            goldTitle.setQuantity_total(productItemsBean.getQuantity_total());
            goldTitle.setReturn_goldpricd(productItemsBean.getReturn_goldpricd());
            goldTitle.setWeight_total(productItemsBean.getWeight_total());
            goldTitle.itemType = 0;
            dataarry.add(goldTitle);
            for (DayReportDetailBean.ProductItemsBean.ItemBean itemBean : productItemsBean.getItem()) {
                DayReportDetailBean.ProductItemsBean golddataBean = new DayReportDetailBean.ProductItemsBean();
                List<DayReportDetailBean.ProductItemsBean.ItemBean> golditemBeanList = new ArrayList<>();
                DayReportDetailBean.ProductItemsBean.ItemBean goldItemBean = new DayReportDetailBean.ProductItemsBean.ItemBean();
                goldItemBean.setCode(itemBean.getCode());
                goldItemBean.setProduct_name(itemBean.getProduct_name());
                goldItemBean.setQuantity(itemBean.getQuantity());
                goldItemBean.setWeight(itemBean.getWeight());
                goldItemBean.setPrice_total(itemBean.getPrice_total());
                goldItemBean.setPrice(itemBean.getPrice());

                golditemBeanList.add(goldItemBean);

                golddataBean.setItem(golditemBeanList);
                golddataBean.setAmount_total(productItemsBean.getAmount_total());
                golddataBean.setCategory_name(productItemsBean.getCategory_name());
                golddataBean.setQuantity_total(productItemsBean.getQuantity_total());
                golddataBean.setReturn_goldpricd(productItemsBean.getReturn_goldpricd());
                golddataBean.setWeight_total(productItemsBean.getWeight_total());

                golddataBean.itemType = 1;
                dataarry.add(golddataBean);
            }
        }

        int perPageSize = 24;
        int size = dataarry.size() / perPageSize;
        int a = dataarry.size() % perPageSize;
        if (a != 0) {
            size++;
        }
        Log.e("test", "size:" + size + " a:" + a + "templist.size():" + dataarry.size());
        for (int i = 0; i < size; i++) {
            List<DayReportDetailBean.ProductItemsBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                if (a == 0) {
                    a = perPageSize;
                }
                for (int t = 0; t < a; t++) {
                    extraList.add(dataarry.get(t + perPageSize * i));
                }

            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(dataarry.get(t + perPageSize * i));
                }
            }
            int layoutid = 0;
//            if (MyApplication.getContext().region.equals("中国大陆")) {
//                layoutid = R.layout.gold_daliy_table;
//            } else {
            layoutid = R.layout.gold_daliy_table2;
//            }
            final View printView = LayoutInflater.from(context).inflate(layoutid, null);
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.no_tv)).setText("日結編號：" + dayReportBean.getDayend_no());
            ((TextView) printView.findViewById(R.id.date_tv)).setText("日結日期：" + dayReportBean.getDayend_date());
            ((TextView) printView.findViewById(R.id.time_tv)).setText("打印時間：" + DateUtils.covertIso8601ToDate(dayReportBean.getCreated()));
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);

            ((TextView) printView.findViewById(R.id.sum_pay_tv)).setText(dayReportBean.getPay_amount() + "");
            ((TextView) printView.findViewById(R.id.cash_tv)).setText(dayReportBean.getCash() + "");
            ((TextView) printView.findViewById(R.id.e_pay_tv)).setText(dayReportBean.getE_pay() + "");
            ((TextView) printView.findViewById(R.id.coupon_tv)).setText(dayReportBean.getCashcoupon() + "");
            ((TextView) printView.findViewById(R.id.yingshou_tv)).setText(dayReportBean.getInvoice_amount() + "");
            ((TextView) printView.findViewById(R.id.exchange_tv)).setText(dayReportBean.getExchange() + "");
            ((TextView) printView.findViewById(R.id.people_exchange_tv)).setText(dayReportBean.getManual_exchange() + "");
            ((TextView) printView.findViewById(R.id.return_tv)).setText(dayReportBean.getReturned() + "");


            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            TablePrintGoodsAdapter adapter = new TablePrintGoodsAdapter(extraList, dayReportBean.getReturn_goldprice());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        BillActivity.print(context, views);
    }

}
