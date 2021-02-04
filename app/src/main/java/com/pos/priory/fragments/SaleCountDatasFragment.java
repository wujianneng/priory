package com.pos.priory.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.DataCountAdapter;
import com.pos.priory.beans.DataAmountBean;
import com.pos.priory.beans.DataCountBean;
import com.pos.priory.beans.ProductCategoryBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class SaleCountDatasFragment extends BaseFragment {
    View view;
    @Bind(R.id.store_name_tv)
    TextView storeNameTv;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.start_date_tv)
    TextView startDateTv;
    @Bind(R.id.end_date_tv)
    TextView endDateTv;
    @Bind(R.id.btn_search_type)
    MaterialButton btnSearchType;
    @Bind(R.id.btn_select_good_type)
    MaterialButton btnSelectGoodType;
    @Bind(R.id.btn_select_count)
    MaterialButton btnSelectCount;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    DataCountAdapter dataCountAdapter;

    List<DataCountBean.ResultsBean> dataCountBeanList = new ArrayList<>();

    int selectCategoryId = 0;
    List<ProductCategoryBean> productCategoryBeanList = new ArrayList<>();

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    int maxCount = 10;

    boolean isFirstCreate = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale_count_datas, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        storeNameTv.setText("銷售量(" + MyApplication.staffInfoBean.getShop() + ")");
        dataCountAdapter = new DataCountAdapter(R.layout.sale_count_data_item, dataCountBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(dataCountAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isFirstCreate){
            dateTv.setText(DateUtils.getDateOfToday());
            isFirstCreate = false;
            getCategorys();
        }else {
            getDatas();
        }

    }


    private void getCategorys() {
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getProductCategorys(), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                productCategoryBeanList = gson.fromJson(result, new TypeToken<List<ProductCategoryBean>>() {
                }.getType());
                if (productCategoryBeanList.size() != 0)
                    selectCategoryId = productCategoryBeanList.get(0).getId();
                getDatas();
            }

            @Override
            public void onFailed(String erromsg) {

            }
        });
    }

    @OnClick({R.id.date_tv, R.id.start_date_tv, R.id.end_date_tv, R.id.btn_search_type, R.id.btn_select_good_type, R.id.btn_select_count})
    public void onClick(View v) {
        switch (v.getId()) {
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
                showSelectTypePopu();
                break;
            case R.id.btn_select_good_type:
                showSelectGoodTypePopu();
                break;
            case R.id.btn_select_count:
                showSelectCountPopu();
                break;
        }
    }

    private void getDatas() {
        Observable observable = null;
        if (dateTv.getVisibility() == View.VISIBLE) {
            if (dateTv.getText().equals("選擇日期")) {
                return;
            }
            observable = RetrofitManager.createString(ApiService.class).getSaleCountDatas
                    (dateTv.getText().toString(), dateTv.getText().toString(), MyApplication.staffInfoBean.getShopid() + "",
                            selectCategoryId + "", "desc", "asc", "asc");
        } else {
            if (startDateTv.getText().equals("開始日期") || endDateTv.getText().equals("結束日期")) {
                return;
            }
            observable = RetrofitManager.createString(ApiService.class).getSaleCountDatas
                    (startDateTv.getText().toString(), endDateTv.getText().toString(), MyApplication.staffInfoBean.getShopid() + "",
                            selectCategoryId + "", "desc", "asc", "asc");
        }
        RetrofitManager.excute(observable,
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        Log.e("test", "sadatas:" + result);
                        DataCountBean dataCountBean = gson.fromJson(result, DataCountBean.class);
                        if(dataCountBean != null && dataCountBean.getResults().size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            dataCountBeanList.clear();

                            for (int i = 0; i < maxCount; i++) {
                                if (i < dataCountBean.getResults().size())
                                    dataCountBeanList.add(dataCountBean.getResults().get(i));
                            }
                            dataCountAdapter.notifyDataSetChanged();
                        }else {
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        Log.e("test", "sadataserro:" + erromsg);
                        empty_layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
    }


    public void showSelectGoodTypePopu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectGoodType);
        Menu menu = popupMenu.getMenu();
        for (ProductCategoryBean productCategoryBean : productCategoryBeanList) {
            menu.add(productCategoryBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                btnSelectGoodType.setText(menuItem.getTitle());
                for (ProductCategoryBean productCategoryBean : productCategoryBeanList) {
                    if (productCategoryBean.getName().equals(menuItem.getTitle())) {
                        selectCategoryId = productCategoryBean.getId();
                        getDatas();
                    }
                }
                return true;
            }
        });
    }

    public void showSelectCountPopu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectCount);
        Menu menu = popupMenu.getMenu();
        menu.add("10");
        menu.add("20");
        menu.add("30");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                btnSelectCount.setText(menuItem.getTitle());
                maxCount = Integer.parseInt(menuItem.getTitle().toString());
                getDatas();
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
                } else if (num == 1) {
                    startDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                } else if (num == 2) {
                    endDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                }
                getDatas();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showSelectTypePopu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSearchType);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.date_type_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        dateTv.setVisibility(View.VISIBLE);
                        startDateTv.setVisibility(View.GONE);
                        endDateTv.setVisibility(View.GONE);
                        break;
                    case R.id.menu1:
                        dateTv.setVisibility(View.GONE);
                        startDateTv.setVisibility(View.VISIBLE);
                        endDateTv.setVisibility(View.VISIBLE);
                        break;
                }
                getDatas();
                btnSearchType.setText(item.getTitle());
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
