package com.pos.priory.fragments;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.DetialListAdapter;
import com.pos.priory.beans.PurchasingItemBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DetialListFragment extends BaseFragment {
    View view;
    @Bind(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    public SmartRefreshLayout refreshLayout;

    DetialListAdapter detialListAdapter;
    List<PurchasingItemBean> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detiallist, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(false);
            }
        });
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshRecyclerView(true);
            }
        });
        //设置侧滑菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem dinghuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_green))
                        .setImage(R.drawable.edit)
                        .setText("编辑")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(dinghuoItem);//设置右边的侧滑
                SwipeMenuItem tuihuoItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.drag_btn_red))
                        .setImage(R.drawable.icon_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setHeight(DeviceUtil.dip2px(getContext(), 91))//设置高，这里使用match_parent，就是与item的高相同
                        .setWidth(DeviceUtil.dip2px(getContext(), 100));//设置宽
                swipeRightMenu.addMenuItem(tuihuoItem);//设置右边的侧滑
            }
        });
        detialListAdapter = new DetialListAdapter(getActivity(), R.layout.detial_list_item, dataList);
        detialListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                sureOneDetialItem(position);
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
                    showEditDialog(adapterPosition);
                } else {
                    deletePurshing(adapterPosition);
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(detialListAdapter);

        refreshLayout.autoRefresh();
    }

    private void sureOneDetialItem(final int position) {
        if (isComfirmDialog == null) {
            isComfirmDialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
                    .setMessage("确认送出订货单到系统？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            comfirmPurshing(dataList.get(position).getId());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isComfirmDialog.dismiss();
                        }
                    })
                    .create();
        }
        isComfirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isComfirmDialog = null;
            }
        });
        isComfirmDialog.show();
    }

    AlertDialog isComfirmDialog;

    AlertDialog actionDialog;

    private void showEditDialog(int position) {
        if (actionDialog == null) {
            final PurchasingItemBean purchasingBean = dataList.get(position);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_invertory_action, null);
            actionDialog = new AlertDialog.Builder(getActivity()).setView(view)
                    .create();
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText("编辑");
            ImageView icon_good = view.findViewById(R.id.icon_good);
            TextView code_tv = view.findViewById(R.id.code_tv);
            TextView name_tv = view.findViewById(R.id.name_tv);
            final EditText edt_count = view.findViewById(R.id.edt_count);
            Glide.with(getActivity()).load(RetrofitManager.hostname + purchasingBean.getProduct().getImage())
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(icon_good);
            code_tv.setText(purchasingBean.getProduct().getProductcode() + "");
            name_tv.setText(purchasingBean.getProduct().getName());
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
                        Toast.makeText(getActivity(), "请输入数量", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(edt_count.getText().toString());
                    editPurshing(purchasingBean, count);
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
            layoutParams.width = DeviceUtil.dip2px(getActivity(), 200);
            layoutParams.height = DeviceUtil.dip2px(getActivity(), 270);
            window.setGravity(Gravity.CENTER);
            window.setAttributes(layoutParams);
        }
    }

    CustomDialog customDialog;

    private void editPurshing(PurchasingItemBean bean, int count) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "编辑中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        RetrofitManager.createString(ApiService.class).editPurchasingitem(bean.getId(),count).compose(this.<String>bindToLifecycle()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "编辑成功", Toast.LENGTH_SHORT).show();
                        refreshLayout.autoRefresh();;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "编辑失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void comfirmPurshing(int id) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "确认中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        RetrofitManager.createString(ApiService.class).comfirmPurchasingitem(id,true).compose(this.<String>bindToLifecycle()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "确认成功", Toast.LENGTH_SHORT).show();
                        refreshLayout.autoRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "确认失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePurshing(final int position) {
        if (customDialog == null)
            customDialog = new CustomDialog(getActivity(), "删除中..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        RetrofitManager.createString(ApiService.class).deletePurchasingitem(dataList.get(position).getId()).compose(this.<String>bindToLifecycle()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dataList.remove(position);
                        detialListAdapter.notifyItemRangeChanged(0, dataList.size());
                        detialListAdapter.notifyItemRemoved(position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void refreshRecyclerView(boolean isLoadMore) {
        if (!isLoadMore) {
            dataList.clear();
            detialListAdapter.notifyDataSetChanged();
        }
        RetrofitManager.createString(ApiService.class)
                .getPurchasingitems()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        final List<PurchasingItemBean> orderBeanList = gson.fromJson(s, new TypeToken<List<PurchasingItemBean>>() {
                        }.getType());
                        if (orderBeanList != null) {
                            for (PurchasingItemBean bean : orderBeanList) {
                                if (!bean.isStatus()) {
                                    dataList.add(bean);
                                }
                            }
                            detialListAdapter.notifyDataSetChanged();
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
