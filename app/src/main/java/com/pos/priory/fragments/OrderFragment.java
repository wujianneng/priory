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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.activitys.BillActivity;
import com.pos.priory.activitys.EditPasswordActivity;
import com.pos.priory.activitys.LoginActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.adapters.TablePrintGoodsAdapter;
import com.pos.priory.beans.DayReportBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.DateUtils;
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
    List<OrderBean> orderList = new ArrayList<>();
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
                if (viewType == 0 && MyApplication.staffInfoBean.getPermission().equals("店长")) {
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
                intent.putExtra("orderId", orderList.get(position).getId());
                startActivity(intent);
            }
        });
        smartRefreshLayout.autoRefresh();
    }

    private void showIsCancelOrderDialog(final int adapterPosition) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("是否确定要撤回该订单？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
            customDialog = new CustomDialog(getActivity(), "正在撤回订单..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            RetrofitManager.createString(ApiService.class)
                    .deleteOrder(orderList.get(pos).getId())
                    .compose(this.<String>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.e("test", "结算成功");
                            customDialog.dismiss();
                            orderList.remove(pos);
                            orderAdapter.notifyItemRangeChanged(0, orderList.size());
                            orderAdapter.notifyItemRemoved(pos);
                            Toast.makeText(getActivity(), "撤回订单成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            customDialog.dismiss();
                            Log.e("test", "结算失败:" + throwable.getMessage());
                            Toast.makeText(getActivity(), "撤回订单失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void getCurrentGoldPrice() {
        RetrofitManager.createString(ApiService.class)
                .getCurrentGoldPrice()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("okhttp", "getCurrentGoldPriceresult:" + s);
                        String currentGoldPrice = new JSONObject(s).getString("price");
                        goldPriceLayout.setVisibility(View.VISIBLE);
                        dateTv.setText(DateUtils.getDateOfToday());
                        goldPriceTv.setText("金價：" + (int) Double.parseDouble(currentGoldPrice) + "/g，" + (int) (Double.parseDouble(currentGoldPrice) * 37.5) + "/两");
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
                .compose(this.<String>bindToLifecycle())
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
                        getDayReport();
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

    public void getDayReport() {
        RetrofitManager.createString(ApiService.class).getDayReport()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test", "result:" + s);
                        DayReportBean dayReportBean = gson.fromJson(s, DayReportBean.class);
                        printGoldTable(dayReportBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test", "throwable:" + gson.toJson(throwable));
                        Toast.makeText(getActivity(), "查不到日报表数据", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void printGoldTable(DayReportBean dayReportBean) {
        Log.e("test", "printGoldTable");
        List<View> views = new ArrayList<>();
        List<DayReportBean.ItemsBean> templist = new ArrayList<>();
        templist.addAll(dayReportBean.getItems());
        if (dayReportBean.getRefunditem().size() != 0) {
            DayReportBean.ItemsBean returnTitleBean = new DayReportBean.ItemsBean();
            returnTitleBean.setProductname("退货标题");
            templist.add(returnTitleBean);
            for (DayReportBean.RefunditemBean refunditemBean : dayReportBean.getRefunditem()) {
                DayReportBean.ItemsBean itemsBean = new DayReportBean.ItemsBean();
                itemsBean.setProductname(refunditemBean.getProductname());
                itemsBean.setCatalog(refunditemBean.getCatalog());
                itemsBean.setDiscount(refunditemBean.getDiscount());
                itemsBean.setDiscountprice(refunditemBean.getDiscountprice());
                itemsBean.setQuantity(refunditemBean.getQuantity());
                itemsBean.setPrice(refunditemBean.getPrice());
                itemsBean.setStock(refunditemBean.getStock());
                itemsBean.setWeight(refunditemBean.getWeight());
                itemsBean.setReturnItem(true);
                templist.add(itemsBean);
            }
        }

        int perPageSize = 18;
        int size = templist.size() / perPageSize;
        int a = templist.size() % perPageSize;
        if (a != 0) {
            size++;
        }
        Log.e("test", "size:" + size + " a:" + a + "templist.size():" + templist.size());
        for (int i = 0; i < size; i++) {
            List<DayReportBean.ItemsBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                if (a == 0) {
                    a = perPageSize;
                }
                for (int t = 0; t < a; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }

            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            }
            Log.e("test", "MyApplication.getContext().region:" + MyApplication.getContext().region);
            int layoutid = 0;
            if (MyApplication.getContext().region.equals("中国大陆")) {
                layoutid = R.layout.gold_daliy_table;
            } else {
                layoutid = R.layout.gold_daliy_table2;
            }
            final View printView = LayoutInflater.from(getActivity()).inflate(layoutid, null);
            if (extraList.size() != 0 && !extraList.get(0).isReturnItem()) {
                printView.findViewById(R.id.list_title_layout0).setVisibility(View.VISIBLE);
                printView.findViewById(R.id.list_title_layout1).setVisibility(View.GONE);
            } else {
                printView.findViewById(R.id.list_title_layout0).setVisibility(View.GONE);
                printView.findViewById(R.id.list_title_layout1).setVisibility(View.VISIBLE);
            }
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.staffInfoBean.getStore());
            ((TextView) printView.findViewById(R.id.date_tv)).setText(DateUtils.getDateOfToday());
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);
            ((TextView) printView.findViewById(R.id.sum_pay_tv)).setText(dayReportBean.getTurnovertotal() + "");
            ((TextView) printView.findViewById(R.id.cash_tv)).setText(dayReportBean.getCash() + "");
            ((TextView) printView.findViewById(R.id.coupon_tv)).setText(dayReportBean.getVoucher() + "");
            ((TextView) printView.findViewById(R.id.card_pay_tv)).setText(dayReportBean.getCredit() + "");
            ((TextView) printView.findViewById(R.id.ali_pay_tv)).setText(dayReportBean.getAlipay() + "");
            ((TextView) printView.findViewById(R.id.wechat_pay_tv)).setText(dayReportBean.getWechatpay() + "");
            ((TextView) printView.findViewById(R.id.return_mount_tv)).setText(dayReportBean.getRefund() + "");
            ((TextView) printView.findViewById(R.id.gold_amount_tv)).setText(dayReportBean.getGoldturnover() + "");
            ((TextView) printView.findViewById(R.id.gold_count_tv)).setText(dayReportBean.getGolditemcount() + "");
            ((TextView) printView.findViewById(R.id.gold_weight_tv)).setText(dayReportBean.getGolditemweight() + "g");
            ((TextView) printView.findViewById(R.id.spar_amount_tv)).setText(dayReportBean.getCystalturnover() + "");
            ((TextView) printView.findViewById(R.id.spar_count_tv)).setText(dayReportBean.getCystalitemcount() + "");
            ((TextView) printView.findViewById(R.id.hand_amount_tv)).setText(dayReportBean.getBraceletturnover() + "");
            ((TextView) printView.findViewById(R.id.hand_count_tv)).setText(dayReportBean.getBraceletitemcount() + "");

            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            TablePrintGoodsAdapter adapter = new TablePrintGoodsAdapter(R.layout.gold_table_print_good_list_item, extraList,
                    i, dayReportBean.getItems().size(), dayReportBean.getRefunditem().size(), perPageSize);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        BillActivity.print(getActivity(), views);
    }

}
