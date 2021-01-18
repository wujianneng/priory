package com.pos.priory.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.DinghuoListAdapter;
import com.pos.priory.beans.DinghuoGoodBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
import com.pos.zxinglib.utils.DeviceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DinghuoListActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.submit_tv)
    TextView submitTv;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    DinghuoListAdapter adapter;

    List<DinghuoGoodBean.ResultBean> goodBeanList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinghuo_list);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshRecyclerView();
        });
        recyclerView.setSwipeMenuCreator((swipeLeftMenu, swipeRightMenu, viewType) -> {
            SwipeMenuItem cancelItem = new SwipeMenuItem(this)
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.drag_btn_red))
                    .setImage(R.drawable.icon_delete)
                    .setText("刪除")
                    .setTextColor(Color.WHITE)
                    .setHeight(DeviceUtil.dip2px(this, 91))//设置高，这里使用match_parent，就是与item的高相同
                    .setWidth(DeviceUtil.dip2px(this, 100));//设置宽
            swipeRightMenu.addMenuItem(cancelItem);//设置右边的侧滑
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DinghuoListAdapter(R.layout.dinghuolist_list_item, goodBeanList);
        recyclerView.setSwipeMenuItemClickListener(menuBridge -> {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (menuPosition == 0) {
                deleteOneItem(adapterPosition);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.decrease_btn) {
                    int quantity = goodBeanList.get(position).getQuantity();
                    if (quantity > 1) {
                        quantity -= 1;
                        editOneItem(position, quantity, goodBeanList.get(position).getWeight() + "");
                    }
                } else if (view.getId() == R.id.increase_btn) {
                    int quantity = goodBeanList.get(position).getQuantity();
                    quantity += 1;
                    editOneItem(position, quantity, goodBeanList.get(position).getWeight() + "");
                } else if (view.getId() == R.id.weight_edt) {
                    showEditItemWeightDialog(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        refreshLayout.autoRefresh();
    }

    private void showEditItemWeightDialog(int position) {
        EditText editText = new EditText(this);
        editText.setText(goodBeanList.get(position).getWeight() + "");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("修改重量")
                .setView(editText)
                .setNegativeButton("取消", ((dialog, which) -> dialog.dismiss()))
                .setPositiveButton("確定", ((dialog, which) -> {
                    String str = editText.getText().toString();
                    if(!str.isEmpty() && Double.parseDouble(str) > 0) {
                        dialog.dismiss();
                        editOneItem(position, goodBeanList.get(position).getQuantity(), str);
                    }
                }))
                .create().show();
    }

    private void editOneItem(int position, int quantity, String weight) {
        Log.e("test","quantity：" + quantity);
        if (customDialog == null)
            customDialog = new CustomDialog(this, "更改中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).editOneDinghuoItem(goodBeanList.get(position).getId(),
                quantity, weight),
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        customDialog.dismiss();
                        showToast("更改成功！");
                        goodBeanList.get(position).setQuantity(quantity);
                        goodBeanList.get(position).setWeight(Double.parseDouble(weight));
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        showToast("更改失敗！");
                    }
                });
    }

    private void deleteOneItem(int position) {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "刪除中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).deleteOneDinghuoItem(goodBeanList.get(position).getId()),
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        customDialog.dismiss();
                        showToast("刪除成功！");
                        adapter.notifyItemRangeChanged(0, goodBeanList.size());
                        adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        showToast("刪除失敗！");
                    }
                });
    }

    private void refreshRecyclerView() {
        RetrofitManager.excuteGson(bindToLifecycle(), RetrofitManager.createGson(ApiService.class).getDinghuoList(),
                new ModelGsonListener<DinghuoGoodBean>() {
                    @Override
                    public void onSuccess(DinghuoGoodBean result) throws Exception {
                        goodBeanList.clear();
                        goodBeanList.addAll(result.getResult());
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.submit_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.submit_tv:
                doSubmit();
                break;
        }
    }

    private void doSubmit() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "提交訂貨列表中..");
        customDialog.setOnDismissListener(dialog -> customDialog = null);
        customDialog.show();
        RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).submitDinghuoList(goodBeanList.get(0).getPurchase_id() + ""),
                new ModelListener() {
                    @Override
                    public void onSuccess(String result) throws Exception {
                        customDialog.dismiss();
                        showToast("提交成功！");
                        finish();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        showToast("提交失敗！");
                    }
                });

    }

}
