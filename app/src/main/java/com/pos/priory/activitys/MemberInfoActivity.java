package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class MemberInfoActivity extends BaseActivity {


    OrderAdapter orderAdapter;
    List<OrderBean> orderList = new ArrayList<>();
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.edt_first_name)
    TextView edtFirstName;
    @Bind(R.id.edt_name)
    TextView edtName;
    @Bind(R.id.sex_tv)
    TextView sexTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.scout_tv)
    TextView scoutTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.order_title)
    TextView orderTitle;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.srf_lay)
    SmartRefreshLayout smartRefreshLayout;
    MemberBean memberBean;
    @Bind(R.id.right_img)
    ImageView rightImg;
    public static final String UPDATE_ORDER_LIST = "memberInfoActivity_update_list";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_member_info);
        ButterKnife.bind(this);
    }

    @Override
    public void handleEventBus(String event) {
        super.handleEventBus(event);
        if(event.equals(UPDATE_ORDER_LIST)){
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected void initViews() {
        titleTv.setText("會員信息");
        rightImg.setVisibility(View.GONE);
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.class);
        edtFirstName.setText(memberBean.getLast_name());
        edtName.setText(memberBean.getFirst_name());
        sexTv.setText(memberBean.getSex());
        phoneTv.setText(memberBean.getMobile());
        scoutTv.setText(memberBean.getReward() + "");

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
        orderAdapter = new OrderAdapter(R.layout.order_list_item, orderList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MemberInfoActivity.this, OrderDetialActivity.class);
                intent.putExtra("order", gson.toJson(orderList.get(position)));
                startActivity(intent);
            }
        });
        refreshRecyclerView(false);
    }


    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            orderList.clear();
            orderAdapter.notifyDataSetChanged();
        }
        OkHttp3Util.doGetWithToken(Constants.GET_ORDERS_URL + "?member=" + memberBean.getMobile(),
             new Okhttp3StringCallback("getOrders") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(MemberInfoActivity.this) {
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
                        new RunOnUiThreadSafe(MemberInfoActivity.this) {
                            @Override
                            public void runOnUiThread() {
                                smartRefreshLayout.finishLoadMore();
                                smartRefreshLayout.finishRefresh();
                            }
                        };
                    }
                });
    }

    @OnClick({R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

}
