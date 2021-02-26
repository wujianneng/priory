package com.pos.priory.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.utils.DateUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryStoreAdapter extends BaseQuickAdapter<InventoryBean.ResultsBean, BaseViewHolder> {
    Context context;

    public InventoryStoreAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<InventoryBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryBean.ResultsBean item) {
        helper.setText(R.id.date_tv, item.getCreated());
        helper.setText(R.id.count_tv,"(" + item.getCount() + ")");
        helper.setText(R.id.status_tv,item.isDone() ? "已完成" : "未完成");
    }
}
