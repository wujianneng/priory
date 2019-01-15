package com.pos.priory.activitys;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.GoodDetialAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class GoodDetialActivity extends BaseActivity {


    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.repertory_tv)
    TextView repertoryTv;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;

    GoodDetialAdapter goodDetialAdapter;
    List<GoodBean> orderList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_good_detial);
        ButterKnife.bind(this);
    }

    String productcode = "";
    int slefCount = 0;

    @Override
    protected void initViews() {
        staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY, ""),
                new TypeToken<List<StaffInfoBean>>() {
                }.getType());

        String goodname = getIntent().getStringExtra("name");
        slefCount = getIntent().getIntExtra("count", 0);
        productcode = getIntent().getStringExtra("productcode");
        titleTv.setText(goodname);
        repertoryTv.setText(slefCount + "");

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
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
                SwipeMenuItem dinghuoItem = new SwipeMenuItem(GoodDetialActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(GoodDetialActivity.this, R.color.drag_btn_green))
                        .setImage(R.drawable.icon_dinghuo)
                        .setText("訂貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(GoodDetialActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(GoodDetialActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(GoodDetialActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(GoodDetialActivity.this, R.color.drag_btn_red))
                        .setImage(R.drawable.icon_tuihuo)
                        .setText("調貨")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(GoodDetialActivity.this, 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(GoodDetialActivity.this, 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
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
                    showActionDialog(true, adapterPosition);
                } else {
                    showActionDialog(false, adapterPosition);
                }
            }
        });
        goodDetialAdapter = new GoodDetialAdapter(this, R.layout.good_detial_list_item, orderList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(goodDetialAdapter);
        smartRefreshLayout.autoRefresh();
    }

    AlertDialog actionDialog;

    private void showActionDialog(final boolean isDingHuo, final int position) {
        final GoodBean goodbean = orderList.get(position);
        if(goodbean.getLocation().getName().equals(staffInfoBeanList.get(0).getStore())){
            Toast.makeText(GoodDetialActivity.this, "只能從其他店鋪調撥", Toast.LENGTH_SHORT).show();
            return;
        }
        if (actionDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(this).setView(view)
                    .create();
            TextView title = view.findViewById(R.id.title);
            final EditText edt_count = view.findViewById(R.id.edt_count);
            title.setText(isDingHuo ? "訂貨" : "調貨");
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
                        Toast.makeText(GoodDetialActivity.this, "请输入数量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isDingHuo)
                        createPurchsing(goodbean, edt_count.getText().toString());
                    else {
                        int edtCount = Integer.parseInt(edt_count.getText().toString());
                        if (edtCount > goodbean.getQuantity()) {
                            Toast.makeText(GoodDetialActivity.this, "調撥數量不能大於此店鋪該商品庫存", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int elseStoreCount = goodbean.getQuantity();
                        allocateElseStock(goodbean.getId(), elseStoreCount, edtCount);
                    }
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
            layoutParams.width = DeviceUtil.dip2px(this, 200);
            layoutParams.height = DeviceUtil.dip2px(this, 270);
            window.setGravity(Gravity.CENTER);
            window.setAttributes(layoutParams);
        }
    }

    private void allocateElseStock(int stockid, int elseStoreCount, final int changeCount) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "訂貨中..");
        customDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("quantity", elseStoreCount - changeCount);
        OkHttp3Util.doPatchWithToken(Constants.GET_STOCK_URL + "/" + stockid + "/update/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(this, "allocateElseStock") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        allocateSelfStock(getIntent().getIntExtra("stockId", 0),changeCount);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "調撥失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void allocateSelfStock(int stockid,final int changeCount) {
        Map<String, Object> map = new HashMap<>();
        slefCount += changeCount;
        map.put("quantity", slefCount);
        OkHttp3Util.doPatchWithToken(Constants.GET_STOCK_URL + "/" + stockid + "/update/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(this, "allocateSelfStock") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        repertoryTv.setText(slefCount + "");
                        Toast.makeText(GoodDetialActivity.this, "調撥成功", Toast.LENGTH_SHORT).show();
                        smartRefreshLayout.autoRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        slefCount -= changeCount;
                        Toast.makeText(GoodDetialActivity.this, "調撥失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            orderList.clear();
            goodDetialAdapter.notifyDataSetChanged();
        }
        OkHttp3Util.doGetWithToken(Constants.GET_STOCK_URL + "?productcode=" + productcode,
                sharedPreferences, new Okhttp3StringCallback(this, "getStockList") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        List<GoodBean> goodBeanList = gson.fromJson(results, new TypeToken<List<GoodBean>>() {
                        }.getType());
                        if (goodBeanList != null) {
                            orderList.addAll(goodBeanList);
                            goodDetialAdapter.notifyDataSetChanged();
                        }
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    CustomDialog customDialog;

    private void createPurchsing(final GoodBean bean, final String count) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "訂貨中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        String location = staffInfoBeanList.get(0).getStore();
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("confirmed", false);
        OkHttp3Util.doPostWithToken(Constants.PURCHASING_URL + "/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(this, "createPurshing") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        createPurchsingItem(new JSONObject(results).getInt("id") + "", bean.getId(), count);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "訂貨失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createPurchsingItem(String id, int stockid, String count) {
        Map<String, Object> map = new HashMap<>();
        map.put("purchasing", Integer.parseInt(id));
        map.put("stock", stockid);
        map.put("type", "purchase");
        map.put("quantity", Integer.parseInt(count));
        OkHttp3Util.doPostWithToken(Constants.PURCHASING_ITEM_URL + "/", gson.toJson(map), sharedPreferences,
                new Okhttp3StringCallback(this, "createPurchsingItem") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "訂貨成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(GoodDetialActivity.this, "訂貨失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public List<StaffInfoBean> staffInfoBeanList;

    @OnClick({R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
