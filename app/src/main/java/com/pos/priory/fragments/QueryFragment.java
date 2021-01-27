package com.pos.priory.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.activitys.MemberInfoActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.adapters.QueryMemberAdapter;
import com.pos.priory.adapters.QueryOrderAdapter;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.RunOnUiThreadSafe;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/29.
 * 查询页面
 */

public class QueryFragment extends BaseFragment {
    View view;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.member_recycler_view)
    RecyclerView memberRecyclerView;
    @Bind(R.id.order_recycler_view)
    SwipeMenuRecyclerView orderRecyclerView;

    QueryMemberAdapter memberAdapter;
    List<MemberBean.ResultsBean> memberList = new ArrayList<>();
    QueryOrderAdapter orderAdapter;
    List<OrderBean.ResultsBean> orderList = new ArrayList<>();
    @Bind(R.id.padding_layout)
    View paddingLayout;
    @Bind(R.id.title_layout)
    FrameLayout titleLayout;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.input_layout)
    CardView inputLayout;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.start_date_tv)
    TextView startDateTv;
    @Bind(R.id.end_date_tv)
    TextView endDateTv;
    @Bind(R.id.btn_search_type)
    MaterialButton btnSearchType;

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_query, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void showKeyBord() {
        if (edtSearch != null)
            LogicUtils.openKeybord(edtSearch.getVisibility() == View.VISIBLE ? edtSearch : edtSearch, getActivity());
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
                edtSearch.setText("");
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
                edtSearch.setText("");
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (btnSearchType.getText().toString().equals("搜索会员"))
                    refreshMemberRecyclerView(charSequence.toString());
                else
                    refreshOrderRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        showKeyBord();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btnSearchType.getText().toString().equals("搜索会员"))
            refreshMemberRecyclerView(edtSearch.getText().toString());
        else
            refreshOrderRecyclerView(edtSearch.getText().toString());
    }

    Disposable memberCall;

    private void refreshMemberRecyclerView(String str) {
        if (memberCall != null)
            memberCall.dispose();
        memberList.clear();
        memberAdapter.notifyDataSetChanged();
        memberCall = RetrofitManager.createGson(ApiService.class).getMembers(str)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MemberBean>() {
                    @Override
                    public void accept(MemberBean results) throws Exception {
                        if (results.getResults() != null && results.getResults().size() != 0) {
                            new RunOnUiThreadSafe(getActivity()) {
                                @Override
                                public void runOnUiThread() {
                                    empty_layout.setVisibility(View.GONE);
                                    memberRecyclerView.setVisibility(View.VISIBLE);
                                    orderRecyclerView.setVisibility(View.GONE);
                                    memberList.addAll(results.getResults());
                                    memberAdapter.notifyDataSetChanged();
                                }
                            };
                        } else {
                            new RunOnUiThreadSafe(getActivity()) {
                                @Override
                                public void runOnUiThread() {
                                    empty_layout.setVisibility(View.VISIBLE);
                                    memberRecyclerView.setVisibility(View.GONE);
                                    orderRecyclerView.setVisibility(View.GONE);
                                }
                            };
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        empty_layout.setVisibility(View.VISIBLE);
                        memberRecyclerView.setVisibility(View.GONE);
                        orderRecyclerView.setVisibility(View.GONE);
                    }
                });
    }

    Disposable dateCall;

    private void refreshDateRecyclerView(String sd, String ed) {
        if (dateCall != null)
            dateCall.dispose();
        orderList.clear();
        orderAdapter.notifyDataSetChanged();
        if (sd.equals("") || ed.equals(""))
            return;
        dateCall = RetrofitManager.createString(ApiService.class)
                .getOrdersByDate(sd, ed)
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String results) throws Exception {
                        Log.e("test", "....refreshDateRecyclerView");
                        OrderBean orderBean = gson.fromJson(results, OrderBean.class);
                        if (orderBean != null && orderBean.getResults().size() != 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                            orderList.addAll(orderBean.getResults());
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        empty_layout.setVisibility(View.VISIBLE);
                        memberRecyclerView.setVisibility(View.GONE);
                        orderRecyclerView.setVisibility(View.GONE);
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
                        OrderBean orderBean = gson.fromJson(results, OrderBean.class);
                        if (orderBean != null && orderBean.getResults().size() != 0) {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                            orderList.addAll(orderBean.getResults());
                            orderAdapter.notifyDataSetChanged();
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        empty_layout.setVisibility(View.VISIBLE);
                        memberRecyclerView.setVisibility(View.GONE);
                        orderRecyclerView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.date_tv, R.id.start_date_tv, R.id.end_date_tv, R.id.btn_search_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_tv:
                getDate(0);
                break;
            case R.id.start_date_tv:
                getDate(1);
                break;
            case R.id.end_date_tv:
                getDate(2);
                break;
            case R.id.btn_search_type:
                showSelectSearchTypePopu();
                break;
        }
    }

    private void showSelectSearchTypePopu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSearchType);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.search_type_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        inputLayout.setVisibility(View.VISIBLE);
                        dateTv.setVisibility(View.GONE);
                        startDateTv.setVisibility(View.GONE);
                        endDateTv.setVisibility(View.GONE);
                        if (memberList.size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            memberRecyclerView.setVisibility(View.VISIBLE);
                            orderRecyclerView.setVisibility(View.GONE);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.menu1:
                        inputLayout.setVisibility(View.VISIBLE);
                        dateTv.setVisibility(View.GONE);
                        startDateTv.setVisibility(View.GONE);
                        endDateTv.setVisibility(View.GONE);
                        if (orderList.size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.menu2:
                        inputLayout.setVisibility(View.GONE);
                        dateTv.setVisibility(View.VISIBLE);
                        startDateTv.setVisibility(View.GONE);
                        endDateTv.setVisibility(View.GONE);
                        if (orderList.size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.menu3:
                        inputLayout.setVisibility(View.GONE);
                        dateTv.setVisibility(View.GONE);
                        startDateTv.setVisibility(View.VISIBLE);
                        endDateTv.setVisibility(View.VISIBLE);
                        if (orderList.size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            empty_layout.setVisibility(View.VISIBLE);
                            memberRecyclerView.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                }
                btnSearchType.setText(item.getTitle());
                return true;
            }
        });
    }


    private void getDate(final int num) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                if (num == 0) {
                    dateTv.setText(DateFormat.format("yyy-MM-dd", c));
                    refreshDateRecyclerView(dateTv.getText().toString(), dateTv.getText().toString());
                } else if (num == 1) {
                    startDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                    refreshDateRecyclerView(startDateTv.getText().toString(), endDateTv.getText().toString());
                } else if (num == 2) {
                    endDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                    refreshDateRecyclerView(startDateTv.getText().toString(), endDateTv.getText().toString());
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
