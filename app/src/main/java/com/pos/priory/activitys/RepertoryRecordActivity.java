package com.pos.priory.activitys;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.RepertoryRecordAdapter;
import com.pos.priory.beans.RepertoryRecordBean;
import com.pos.priory.beans.RepertoryRecordFiltersBean;
import com.pos.priory.networks.ApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RepertoryRecordActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.next_tv)
    TextView nextTv;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.size_tv)
    TextView sizeTv;
    @Bind(R.id.start_date_tv)
    TextView startDateTv;
    @Bind(R.id.end_date_tv)
    TextView endDateTv;
    @Bind(R.id.btn_select_repertory)
    MaterialButton btnSelectRepertory;
    @Bind(R.id.btn_select_where)
    MaterialButton btnSelectWhere;
    @Bind(R.id.btn_select_type)
    MaterialButton btnSelectType;
    @Bind(R.id.btn_select_address)
    MaterialButton btnSelectAddress;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.btn_clear)
    ImageView btnClear;
    @Bind(R.id.search_card)
    CardView searchCard;
    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    List<RepertoryRecordBean.ResultsBean> dataList = new ArrayList<>();

    RepertoryRecordAdapter repertoryAdapter;

    String currentStr = "";
    @Bind(R.id.left_layout)
    FrameLayout leftLayout;

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    private List<RepertoryRecordFiltersBean.ResultBean.WarehouseBean> warehouse;
    private List<RepertoryRecordFiltersBean.ResultBean.TypeBean> type;
    private List<RepertoryRecordFiltersBean.ResultBean.PurposeBean> purpose;
    private List<RepertoryRecordFiltersBean.ResultBean.WhfromBean> whfrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_repertory_record);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleTv.setText("倉庫記錄");
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshRecyclerView();
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));

        repertoryAdapter = new RepertoryRecordAdapter(this, R.layout.repertory_record_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position) -> {

        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentStr = s.toString();
                refreshRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getFilters();
    }

    private void getFilters() {
        RetrofitManager.excuteGson(bindToLifecycle(), RetrofitManager.createGson(ApiService.class).getRepertoryRecordFilters()
                , new ModelGsonListener<RepertoryRecordFiltersBean>() {
                    @Override
                    public void onSuccess(RepertoryRecordFiltersBean result) throws Exception {
                        warehouse = result.getResult().getWarehouse();
                        purpose = result.getResult().getPurpose();
                        type = result.getResult().getType();
                        whfrom = result.getResult().getWhfrom();
                        currentWarehouse = warehouse.get(0);
                        currentPurpose = purpose.get(0);
                        currentType = type.get(0);
                        currentWhfrom = whfrom.get(0);
                        btnSelectRepertory.setText(currentWarehouse.getName());
                        btnSelectWhere.setText(currentPurpose.getName());
                        btnSelectType.setText(currentType.getName());
                        btnSelectAddress.setText(currentWhfrom.getName());
                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }

    RepertoryRecordFiltersBean.ResultBean.WarehouseBean currentWarehouse;

    private void showRepertoryMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnSelectRepertory);
        for (RepertoryRecordFiltersBean.ResultBean.WarehouseBean warehouseBean : warehouse) {
            popupMenu.getMenu().add(warehouseBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            for (RepertoryRecordFiltersBean.ResultBean.WarehouseBean warehouseBean : warehouse) {
                if (warehouseBean.getName().equals(item.getTitle()))
                    currentWarehouse = warehouseBean;
            }
            btnSelectRepertory.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }

    RepertoryRecordFiltersBean.ResultBean.PurposeBean currentPurpose;

    private void showInoutMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnSelectWhere);
        for (RepertoryRecordFiltersBean.ResultBean.PurposeBean purposeBean : purpose) {
            popupMenu.getMenu().add(purposeBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            for (RepertoryRecordFiltersBean.ResultBean.PurposeBean purposeBean : purpose) {
                if (purposeBean.getName().equals(item.getTitle()))
                    currentPurpose = purposeBean;
            }
            btnSelectWhere.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }

    RepertoryRecordFiltersBean.ResultBean.TypeBean currentType;

    private void showTypeMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnSelectType);
        for (RepertoryRecordFiltersBean.ResultBean.TypeBean typeBean : type) {
            popupMenu.getMenu().add(typeBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            for (RepertoryRecordFiltersBean.ResultBean.TypeBean typeBean : type) {
                if (typeBean.getName().equals(item.getTitle()))
                    currentType = typeBean;
            }
            btnSelectType.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }

    RepertoryRecordFiltersBean.ResultBean.WhfromBean currentWhfrom;

    private void showShopMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnSelectAddress);
        for (RepertoryRecordFiltersBean.ResultBean.WhfromBean whfromBean : whfrom) {
            popupMenu.getMenu().add(whfromBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            for (RepertoryRecordFiltersBean.ResultBean.WhfromBean whfromBean : whfrom) {
                if (whfromBean.getName().equals(item.getTitle()))
                    currentWhfrom = whfromBean;
            }
            btnSelectAddress.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }


    Disposable call;

    private void refreshRecyclerView() {
        if(startDateTv.getText().toString().equals("请选择开始日期") || endDateTv.getText().toString().equals("请选择结束日期")){
            refreshLayout.finishRefresh();
            return;
        }
        if (call != null)
            call.dispose();
        dataList.clear();
        Observable<RepertoryRecordBean> observable = null;
        if (currentStr.isEmpty())
            observable = RetrofitManager.createGson(ApiService.class).getRepertoryRecords(currentWarehouse.getId(), currentWhfrom.getId(),
                    currentType.getValue(), currentPurpose.getValue(), startDateTv.getText().toString(),
                    endDateTv.getText().toString());
        else
            observable = RetrofitManager.createGson(ApiService.class).getRepertoryRecordsWithSearch(currentWarehouse.getId(), currentWhfrom.getId(),
                    currentType.getValue(), currentPurpose.getValue(), startDateTv.getText().toString(),
                    endDateTv.getText().toString(), currentStr);
        call = RetrofitManager.excuteGson(this.bindToLifecycle(), observable,
                new ModelGsonListener<RepertoryRecordBean>() {
                    @Override
                    public void onSuccess(RepertoryRecordBean result) throws Exception {
                        if (result != null && result.getResults().size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                            dataList.clear();
                            dataList.addAll(result.getResults());
                            repertoryAdapter.notifyDataSetChanged();
                            leftTv.setText("數量：" + result.getQuantity_total() + "件，" + "重量：" + result.getWeight_total() + "g");
                            leftLayout.setVisibility(View.VISIBLE);
                            sizeTv.setText("記錄:" + dataList.size());
                        }else {
                            empty_layout.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        empty_layout.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.btn_select_repertory, R.id.btn_select_where, R.id.btn_select_type,
            R.id.btn_select_address, R.id.start_date_tv, R.id.end_date_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_select_repertory:
                showRepertoryMenu();
                break;
            case R.id.btn_select_where:
                showInoutMenu();
                break;
            case R.id.btn_select_type:
                showTypeMenu();
                break;
            case R.id.btn_select_address:
                showShopMenu();
                break;
            case R.id.start_date_tv:
                getDate(0);
                break;
            case R.id.end_date_tv:
                getDate(1);
                break;
        }
    }

    private void getDate(final int num) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                if (num == 0) {
                    startDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                } else if (num == 1) {
                    endDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                }
                refreshLayout.autoRefresh();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
