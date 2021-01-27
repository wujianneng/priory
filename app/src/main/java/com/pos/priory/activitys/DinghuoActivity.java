package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.DinghuoAdapter;
import com.pos.priory.beans.ProductSelectResultBean;
import com.pos.priory.beans.RepertoryFiltersBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DinghuoActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.btn_select_type)
    MaterialButton btnSelectType;
    @Bind(R.id.edt_search)
    EditText edtSearch;
    @Bind(R.id.btn_clear)
    ImageView btnClear;
    @Bind(R.id.search_card)
    CardView searchCard;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    DinghuoAdapter adapter;
    List<ProductSelectResultBean> goodBeanList = new ArrayList<>();
    String currentStr = "";
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.right_layout)
    FrameLayout rightLayout;

    @Bind(R.id.empty_layout)
    FrameLayout empty_layout;

    private List<RepertoryFiltersBean.ResultBean.CategoryBean> category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinghuo);
        ButterKnife.bind(this);
        initViews();
        refreshLayout.autoRefresh();
    }

    int categoryId = 0;

    private void initViews() {
        category = gson.fromJson(getIntent().getStringExtra("categoryList"),
                new TypeToken<List<RepertoryFiltersBean.ResultBean.CategoryBean>>() {
                }.getType());
        btnSelectType.setText(category.get(0).getName());
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshRecyclerView();
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DinghuoAdapter(R.layout.dinghuo_list_item, goodBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showIsDingHuoDialog(goodBeanList.get(position));
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
    }

    AlertDialog actionDialog;

    private void showIsDingHuoDialog(ProductSelectResultBean goodBean) {
        if (actionDialog == null) {
            actionDialog = new AlertDialog.Builder(this).setTitle("確認要訂貨嗎？")
                    .setNegativeButton("取消", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("確定", ((dialog, which) -> {
                        dialog.dismiss();
                        doDingHuo(goodBean);
                    }))
                    .create();
            actionDialog.setOnDismissListener(dialog -> actionDialog = null);
            actionDialog.show();
        }
    }

    private void doDingHuo(ProductSelectResultBean goodBean) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "订货中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        RetrofitManager.createString(ApiService.class).createPurchasingitem(goodBean.getId())
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    customDialog.dismiss();
                    Toast.makeText(DinghuoActivity.this, "订货成功", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    customDialog.dismiss();
                    Toast.makeText(DinghuoActivity.this, "订货失败", Toast.LENGTH_SHORT).show();
                });
    }

    private void showCategoryMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnSelectType);
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

    Disposable call;

    private void refreshRecyclerView() {
        if (call != null)
            call.dispose();
        Observable<String> observable;
        for (RepertoryFiltersBean.ResultBean.CategoryBean categoryBean : category) {
            if (categoryBean.getName().equals(btnSelectType.getText().toString())) {
                categoryId = categoryBean.getCid();
            }
        }
        observable = RetrofitManager.createString(ApiService.class).getProductSelectList(categoryId, currentStr);
        call = RetrofitManager.excute(bindToLifecycle(), observable,
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        List<ProductSelectResultBean> datalist = gson.fromJson(result,new TypeToken<List<ProductSelectResultBean>>(){}.getType());
                        if (datalist != null && datalist.size() != 0) {
                            empty_layout.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                            goodBeanList.clear();
                            goodBeanList.addAll(datalist);
                            adapter.notifyDataSetChanged();
                        }else{
                            empty_layout.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        Log.e("test","erro:" + erromsg);
                        empty_layout.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.btn_select_type, R.id.right_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_select_type:
                showCategoryMenu();
                break;
            case R.id.right_layout:
                startActivity(new Intent(DinghuoActivity.this, DinghuoListActivity.class));
                break;
        }
    }
}
