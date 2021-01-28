package com.pos.priory.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.activitys.AddOrEditReturnItemActivity;
import com.pos.priory.activitys.DinghuoActivity;
import com.pos.priory.activitys.GoodDetialActivity;
import com.pos.priory.activitys.RepertoryRecordActivity;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.RepertoryFiltersBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.beans.WarehouseReturnBean;
import com.pos.priory.networks.ApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryFragment extends BaseFragment {
    View view;
    List<WarehouseBean.ResultsBean> dataList = new ArrayList<>();
    RepertoryAdapter repertoryAdapter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    String currentStr = "";

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
    @Bind(R.id.add_tv)
    TextView addTv;
    @Bind(R.id.btn_select_return_type)
    MaterialButton btnSelectReturnType;
    @Bind(R.id.btn_select_return_order_params)
    MaterialButton btnSelectReturnOrderParams;

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    private List<RepertoryFiltersBean.ResultBean.WarehouseBean> warehouse;
    RepertoryFiltersBean.ResultBean.WarehouseBean currentWarehouse;
    private List<RepertoryFiltersBean.ResultBean.CategoryBean> category;
    private List<RepertoryFiltersBean.ResultBean.ReturntypeBean> returntypeBeanList;

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
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshRecyclerView();
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));

        repertoryAdapter = new RepertoryAdapter(getActivity(), R.layout.repertory_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnItemClickListener((BaseQuickAdapter adapter, View view, int position) -> {
            if (currentWarehouse.isWh_primary()) {
                Intent intent = new Intent(getActivity(), GoodDetialActivity.class);
                intent.putExtra("goodbean", gson.toJson(dataList.get(position)));
                startActivity(intent);
            }
        });
        repertoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent2 = new Intent(getActivity(), AddOrEditReturnItemActivity.class);
                intent2.putExtra("isCreate", false);
                intent2.putExtra("warehouseId", currentWarehouse.getId());
                intent2.putExtra("returnBean", gson.toJson(dataList.get(position)));
                startActivity(intent2);
            }
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
        RetrofitManager.excuteGson(bindToLifecycle(), RetrofitManager.createGson(ApiService.class).getRepertoryFilters()
                , new ModelGsonListener<RepertoryFiltersBean>() {
                    @Override
                    public void onSuccess(RepertoryFiltersBean result) throws Exception {
                        warehouse = result.getResult().getWarehouse();
                        category = result.getResult().getCategory();
                        returntypeBeanList = result.getResult().getReturntype();
                        if (warehouse.size() != 0) {
                            currentWarehouse = warehouse.get(0);
                            btnSelectRepertory.setText(currentWarehouse.getName());
                        }
                        if (category.size() != 0)
                            btnSelectType.setText(category.get(0).getName());
                        if (returntypeBeanList.size() != 0)
                            btnSelectReturnType.setText(returntypeBeanList.get(0).getName());
                        refreshLayout.autoRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        Log.e("test", "erro:" + erromsg);
                    }
                });
    }


    private void showRepertoryMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectRepertory);
        for (RepertoryFiltersBean.ResultBean.WarehouseBean warehouseBean : warehouse) {
            popupMenu.getMenu().add(warehouseBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            for (RepertoryFiltersBean.ResultBean.WarehouseBean warehouseBean : warehouse) {
                if (warehouseBean.getName().equals(item.getTitle()))
                    currentWarehouse = warehouseBean;
            }
            btnSelectRepertory.setText(item.getTitle());
            dinghuoTv.setVisibility(currentWarehouse.isWh_primary() ? View.VISIBLE : View.GONE);
//            addTv.setVisibility(currentWarehouse.isWh_primary() ? View.GONE : View.VISIBLE);
            addTv.setVisibility(View.GONE);
            btnSelectType.setVisibility(currentWarehouse.isWh_primary() ? View.VISIBLE : View.GONE);
            btnSelectReturnType.setVisibility(currentWarehouse.isWh_primary() ? View.GONE : View.VISIBLE);
            btnSelectOrderParams.setVisibility(currentWarehouse.isWh_primary() ? View.VISIBLE : View.GONE);
            btnSelectReturnOrderParams.setVisibility(currentWarehouse.isWh_primary() ? View.GONE : View.VISIBLE);
            refreshLayout.autoRefresh();
            return true;
        });
    }

    private void showCategoryMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectType);
        for (RepertoryFiltersBean.ResultBean.CategoryBean categoryBean : category) {
            popupMenu.getMenu().add(categoryBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            btnSelectType.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }

    private void showReturnCategoryMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectReturnType);
        for (RepertoryFiltersBean.ResultBean.ReturntypeBean returnBean : returntypeBeanList) {
            popupMenu.getMenu().add(returnBean.getName());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener((item) -> {
            btnSelectReturnType.setText(item.getTitle());
            refreshLayout.autoRefresh();
            return true;
        });
    }

    private void showOrderTypeMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectOrderType);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.repertory_order_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener((item) -> {
            // 控件每一个item的点击事件
            switch (item.getItemId()) {
                case R.id.menu0:
                    btnSelectOrderType.setText(item.getTitle());
                    break;
                case R.id.menu1:
                    btnSelectOrderType.setText(item.getTitle());
                    break;
            }
            refreshLayout.autoRefresh();
            return true;
        });
    }

    private void showOrderParamsMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectOrderParams);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.repertory_order_name_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener((item) -> {
            // 控件每一个item的点击事件
            switch (item.getItemId()) {
                case R.id.menu0:
                    btnSelectOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu1:
                    btnSelectOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu2:
                    btnSelectOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu3:
                    btnSelectOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu4:
                    btnSelectOrderParams.setText(item.getTitle());
                    break;
            }
            refreshLayout.autoRefresh();
            return true;
        });
    }

    private void showReturnOrderParamsMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectReturnOrderParams);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.repertory_return_order_name_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener((item) -> {
            // 控件每一个item的点击事件
            switch (item.getItemId()) {
                case R.id.menu0:
                    btnSelectReturnOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu3:
                    btnSelectReturnOrderParams.setText(item.getTitle());
                    break;
                case R.id.menu4:
                    btnSelectReturnOrderParams.setText(item.getTitle());
                    break;
            }
            refreshLayout.autoRefresh();
            return true;
        });
    }

    Disposable call;
    int categoryId = 0, repertoryId = 0;

    public void refreshRecyclerView() {
        if (currentWarehouse == null)
            return;
        if (call != null)
            call.dispose();
        dataList.clear();
        repertoryAdapter.notifyDataSetChanged();
        repertoryId = currentWarehouse.getId();
        String ordering = "", orderName,
                orderType;
        if (currentWarehouse.isWh_primary()) {
            for (RepertoryFiltersBean.ResultBean.CategoryBean categoryBean : category) {
                if (categoryBean.getName().equals(btnSelectType.getText().toString())) {
                    categoryId = categoryBean.getCid();
                }
            }
            orderName = btnSelectOrderParams.getText().toString();
            orderType = btnSelectOrderType.getText().toString();

        } else {
            orderName = btnSelectReturnOrderParams.getText().toString();
            orderType = btnSelectOrderType.getText().toString();
        }

        if (orderName.equals("按入庫時間") && orderType.equals("升序")) ordering = "updated";
        if (orderName.equals("按入庫時間") && orderType.equals("降序")) ordering = "-updated";

        if (orderName.equals("按更新時間") && orderType.equals("升序")) ordering = "wh_updated";
        if (orderName.equals("按更新時間") && orderType.equals("降序")) ordering = "-wh_updated";

        if (orderName.equals("按名稱") && orderType.equals("升序")) ordering = "name";
        if (orderName.equals("按名稱") && orderType.equals("降序")) ordering = "-name";

        if (orderName.equals("按數量") && orderType.equals("升序")) ordering = "prd_stock_quantity";
        if (orderName.equals("按數量") && orderType.equals("降序")) ordering = "-prd_stock_quantity";


        if (currentWarehouse.isWh_primary()) {
            if (orderName.equals("按重量") && orderType.equals("升序")) ordering = "prd_stock_weight";
            if (orderName.equals("按重量") && orderType.equals("降序")) ordering = "-prd_stock_weight";

            if (orderName.equals("按售價") && orderType.equals("升序")) ordering = "unitprice";
            if (orderName.equals("按售價") && orderType.equals("降序"))  ordering = "-unitprice";
            call = RetrofitManager.excuteGson(this.bindToLifecycle(),RetrofitManager.createGson(ApiService.class)
                            .getStockListByParam(currentStr,categoryId, ordering),
                    new ModelGsonListener<WarehouseBean>() {
                        @Override
                        public void onSuccess(WarehouseBean result) throws Exception {
                            Log.e("test", "1111");
                            if (result != null && result.getResults().size() != 0) {
                                empty_layout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                Log.e("test", "22222");
                                dataList.clear();
                                dataList.addAll(result.getResults());
                                repertoryAdapter.notifyDataSetChanged();
                                leftTv.setText("數量：" + result.getQuantity_total() + "件，" + "重量：" + result.getWeight_total() + "g");
                            }else {
                                empty_layout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            refreshLayout.finishRefresh();
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            Log.e("test", erromsg);
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            refreshLayout.finishRefresh();
                        }
                    });
        } else {
            if (orderName.equals("按重量") && orderType.equals("升序")) ordering = "returnweight";
            if (orderName.equals("按重量") && orderType.equals("降序")) ordering = "-returnweight";

            if (orderName.equals("按售價") && orderType.equals("升序")) ordering = "returncost";
            if (orderName.equals("按售價") && orderType.equals("降序"))  ordering = "-returncost";
            Log.e("test", "orderName：" + orderName + " orderType：" + orderType);
            call = RetrofitManager.excuteGson(this.bindToLifecycle(),RetrofitManager.createGson(ApiService.class)
                            .getStockListByParamReturn(currentStr, btnSelectReturnType.getText().toString(), ordering),
                    new ModelGsonListener<WarehouseReturnBean>() {
                        @Override
                        public void onSuccess(WarehouseReturnBean result) throws Exception {
                            if (result != null && result.getResults().size() != 0) {
                                empty_layout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                dataList.clear();
                                for(WarehouseReturnBean.ResultsBean returnResultsBean : result.getResults()){
                                    WarehouseBean.ResultsBean resultsBean = new WarehouseBean.ResultsBean();
                                    resultsBean.setResultsBean(returnResultsBean);
                                    dataList.add(resultsBean);
                                }
                                repertoryAdapter.notifyDataSetChanged();
                                leftTv.setText("數量：" + result.getQuantity_total() + "件，" + "重量：" + result.getWeight_total() + "g");
                            }else {
                                empty_layout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                            refreshLayout.finishRefresh();
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            Log.e("test", erromsg);
                            empty_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            refreshLayout.finishRefresh();
                        }
                    });
        }


    }


    @OnClick({R.id.btn_select_repertory, R.id.btn_select_type, R.id.btn_select_return_type, R.id.dinghuo_tv, R.id.add_tv,
            R.id.btn_records, R.id.btn_select_order_params, R.id.btn_select_return_order_params, R.id.btn_select_order_type,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dinghuo_tv:
                Intent intent = new Intent(getActivity(), DinghuoActivity.class);
                intent.putExtra("mainRepertoryId", repertoryId);
                intent.putExtra("categoryList", gson.toJson(category));
                startActivity(intent);
                break;
            case R.id.add_tv:
                Intent intent2 = new Intent(getActivity(), AddOrEditReturnItemActivity.class);
                intent2.putExtra("isCreate", true);
                intent2.putExtra("warehouseId", currentWarehouse.getId());
                startActivity(intent2);
                break;
            case R.id.btn_select_repertory:
                showRepertoryMenu();
                break;
            case R.id.btn_select_return_type:
                showReturnCategoryMenu();
                break;
            case R.id.btn_select_type:
                showCategoryMenu();
                break;
            case R.id.btn_records:
                startActivity(new Intent(getActivity(), RepertoryRecordActivity.class));
                break;
            case R.id.btn_select_order_params:
                showOrderParamsMenu();
                break;
            case R.id.btn_select_return_order_params:
                showReturnOrderParamsMenu();
                break;
            case R.id.btn_select_order_type:
                showOrderTypeMenu();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(String event) {
        if (event.equals("refreshReturnList")) {
            refreshLayout.autoRefresh();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
