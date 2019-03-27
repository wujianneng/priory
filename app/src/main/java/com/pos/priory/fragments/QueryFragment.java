package com.pos.priory.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.MemberInfoActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.QueryMemberAdapter;
import com.pos.priory.adapters.QueryOrderAdapter;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.priory.utils.RunOnUiThreadSafe;
import com.pos.zxinglib.utils.DeviceUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Lenovo on 2018/12/29.
 * 查询页面
 */

public class QueryFragment extends BaseFragment {
    View view;
    @Bind(R.id.btn_member)
    TextView btnMember;
    @Bind(R.id.btn_order_number)
    TextView btnOrderNumber;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.member_recycler_view)
    RecyclerView memberRecyclerView;
    @Bind(R.id.order_recycler_view)
    SwipeMenuRecyclerView orderRecyclerView;

    QueryMemberAdapter memberAdapter;
    List<MemberBean> memberList = new ArrayList<>();
    QueryOrderAdapter orderAdapter;
    List<OrderBean> orderList = new ArrayList<>();
    @Bind(R.id.edt_member_input)
    EditText edtMemberInput;
    @Bind(R.id.edt_order_input)
    EditText edtOrderInput;
    @Bind(R.id.btn_date)
    TextView btnDate;
    @Bind(R.id.input_layout)
    CardView inputLayout;
    @Bind(R.id.date_img)
    ImageView dateImg;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.date_layout)
    CardView dateLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_query, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void showKeyBord() {
        if (edtMemberInput != null)
            LogicUtils.openKeybord(edtMemberInput.getVisibility() == View.VISIBLE ? edtMemberInput : edtOrderInput, getActivity());
    }

    private void initViews() {
        memberAdapter = new QueryMemberAdapter(R.layout.query_member_list_item, memberList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        memberRecyclerView.setLayoutManager(mLayoutManager);
        memberRecyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), MemberInfoActivity.class);
                intent.putExtra("memberInfo", gson.toJson(memberList.get(position)));
                startActivity(intent);
                edtMemberInput.setText("");
            }
        });

        orderAdapter = new QueryOrderAdapter(R.layout.order_list_item, orderList);
        //设置侧滑菜单
        orderRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem cancelItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                        .setImage(R.drawable.edit)
                        .setText("撤回")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(cancelItem);//设置右边的侧滑
            }
        });
        //设置侧滑菜单的点击事件
        orderRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {

                }
            }
        });
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        orderRecyclerView.setLayoutManager(mLayoutManager2);
        orderRecyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("order", gson.toJson(orderList.get(position)));
                startActivity(intent);
                edtOrderInput.setText("");
            }
        });

        edtMemberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshMemberRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtOrderInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshOrderRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        showKeyBord();
    }


    Call memberCall;

    private void refreshMemberRecyclerView(String str) {
        if (memberCall != null)
            memberCall.cancel();
        memberList.clear();
        memberAdapter.notifyDataSetChanged();
        if (str.equals(""))
            return;
        memberCall = OkHttp3Util.doGetWithToken(Constants.GET_MEMBERS_URL + "?mobile="
                        + str, sharedPreferences,
                new Okhttp3StringCallback("getMembers") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<MemberBean> memberBeanList = gson.fromJson(results, new TypeToken<List<MemberBean>>() {
                        }.getType());
                        if (memberBeanList != null) {
                            new RunOnUiThreadSafe(getActivity()) {
                                @Override
                                public void runOnUiThread() {
                                    memberList.addAll(memberBeanList);
                                    memberAdapter.notifyDataSetChanged();
                                }
                            };
                        }

                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }

    Call dateCall;

    private void refreshDateRecyclerView(String date) {
        if (dateCall != null)
            dateCall.cancel();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        if (date.equals("") )
            return;
        dateCall = OkHttp3Util.doGetWithToken(Constants.GET_ORDERS_URL + "?date=" + date,
                sharedPreferences, new Okhttp3StringCallback("getOrders") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
                                    orderList.addAll(orderBeanList);
                                    orderAdapter.notifyDataSetChanged();
                                }
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {
                    }
                });
    }

    Call orderCall;

    private void refreshOrderRecyclerView(String orderNum) {
        if (orderCall != null)
            orderCall.cancel();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        if (orderNum.equals(""))
            return;
        orderCall = OkHttp3Util.doGetWithToken(Constants.GET_ORDERS_URL + "?ordernumber=" + orderNum,
                sharedPreferences, new Okhttp3StringCallback("getOrders") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        new RunOnUiThreadSafe(getActivity()) {
                            @Override
                            public void runOnUiThread() {
                                if (orderBeanList != null) {
                                    orderList.addAll(orderBeanList);
                                    orderAdapter.notifyDataSetChanged();
                                }
                            }
                        };
                    }

                    @Override
                    public void onFailed(String erromsg) {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_member, R.id.btn_order_number, R.id.btn_date, R.id.date_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_member:
                btnMember.setTextColor(getResources().getColor(R.color.colorAccent));
                btnOrderNumber.setTextColor(Color.parseColor("#cccccc"));
                btnDate.setTextColor(Color.parseColor("#cccccc"));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("電話");
                edtMemberInput.setVisibility(View.VISIBLE);
                edtOrderInput.setVisibility(View.GONE);
                memberRecyclerView.setVisibility(View.VISIBLE);
                orderRecyclerView.setVisibility(View.GONE);
                inputLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_date:
                btnDate.setTextColor(getResources().getColor(R.color.colorAccent));
                btnMember.setTextColor(Color.parseColor("#cccccc"));
                btnOrderNumber.setTextColor(Color.parseColor("#cccccc"));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("電話");
                edtMemberInput.setVisibility(View.VISIBLE);
                edtOrderInput.setVisibility(View.GONE);
                memberRecyclerView.setVisibility(View.VISIBLE);
                orderRecyclerView.setVisibility(View.GONE);
                inputLayout.setVisibility(View.GONE);
                dateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_order_number:
                btnMember.setTextColor(Color.parseColor("#cccccc"));
                btnDate.setTextColor(Color.parseColor("#cccccc"));
                btnOrderNumber.setTextColor(getResources().getColor(R.color.colorAccent));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("訂單號");
                edtMemberInput.setVisibility(View.GONE);
                edtOrderInput.setVisibility(View.VISIBLE);
                memberRecyclerView.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.GONE);
                break;
            case R.id.date_layout:
                getDate();
                break;
        }
    }

    private void getDate() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                dateTv.setText(DateFormat.format("yyy-MM-dd", c));
//                refreshDateRecyclerView(dateTv.getText().toString());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
