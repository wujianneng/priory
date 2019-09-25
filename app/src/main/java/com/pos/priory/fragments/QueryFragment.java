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
import android.util.Log;
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
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
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
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        orderRecyclerView.setLayoutManager(mLayoutManager2);
        orderRecyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("orderId", orderList.get(position).getId());
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
        refreshMemberRecyclerView("");
//        showKeyBord();
    }


    Disposable memberCall;

    private void refreshMemberRecyclerView(String str) {
        if (memberCall != null)
            memberCall.dispose();
        memberList.clear();
        memberAdapter.notifyDataSetChanged();
        memberCall = RetrofitManager.createString(ApiService.class).getMembers(str).compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    Disposable dateCall;

    private void refreshDateRecyclerView(String date) {
        if (dateCall != null)
            dateCall.dispose();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        if (date.equals(""))
            return;
        dateCall = RetrofitManager.createString(ApiService.class)
                .getOrdersByDate(date)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        Log.e("test", "....refreshDateRecyclerView");
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        if (orderBeanList != null) {
                            Log.e("test", "....orderBeanList != null");
                            orderList.addAll(orderBeanList);
                            orderAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    Disposable orderCall;

    private void refreshOrderRecyclerView(String orderNum) {
        if (orderCall != null)
            orderCall.dispose();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        if (orderNum.equals(""))
            return;
        orderCall = RetrofitManager.createString(ApiService.class)
                .getOrdersByOrdernumber(orderNum)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        final List<OrderBean> orderBeanList = gson.fromJson(results, new TypeToken<List<OrderBean>>() {
                        }.getType());
                        if (orderBeanList != null) {
                            orderList.addAll(orderBeanList);
                            orderAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
                btnOrderNumber.setTextColor(Color.parseColor("#000000"));
                btnDate.setTextColor(Color.parseColor("#000000"));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("电话");
                edtMemberInput.setVisibility(View.VISIBLE);
                edtOrderInput.setVisibility(View.GONE);
                memberRecyclerView.setVisibility(View.VISIBLE);
                orderRecyclerView.setVisibility(View.GONE);
                inputLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_date:
                btnMember.setTextColor(Color.parseColor("#000000"));
                btnOrderNumber.setTextColor(Color.parseColor("#000000"));
                btnDate.setTextColor(getResources().getColor(R.color.colorAccent));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("订单号");
                edtMemberInput.setVisibility(View.GONE);
                edtOrderInput.setVisibility(View.VISIBLE);
                memberRecyclerView.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.GONE);
                dateLayout.setVisibility(View.VISIBLE);
                if (!dateTv.getText().toString().equals("请选择日期"))
                    refreshDateRecyclerView(dateTv.getText().toString());
                break;
            case R.id.btn_order_number:
                btnMember.setTextColor(Color.parseColor("#000000"));
                btnDate.setTextColor(Color.parseColor("#000000"));
                btnOrderNumber.setTextColor(getResources().getColor(R.color.colorAccent));
                icon.setImageResource(R.drawable.icon_detail);
                titleTv.setText("订单号");
                edtMemberInput.setVisibility(View.GONE);
                edtOrderInput.setVisibility(View.VISIBLE);
                memberRecyclerView.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.VISIBLE);
                dateLayout.setVisibility(View.GONE);
                refreshOrderRecyclerView(edtOrderInput.getText().toString());
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
                refreshDateRecyclerView(dateTv.getText().toString());
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
