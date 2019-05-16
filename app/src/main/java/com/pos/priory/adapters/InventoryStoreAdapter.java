package com.pos.priory.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.DateUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryStoreAdapter extends BaseQuickAdapter<InventoryBean, BaseViewHolder> {
    Context context;

    public InventoryStoreAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<InventoryBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryBean item) {
        helper.setText(R.id.date_tv, DateUtils.covertIso8601ToDate(item.getCreated()));
        helper.setText(R.id.status_tv, item.getStatus());
        if(item.getStatus().equals("未完成")){
            ((CardView)helper.getView(R.id.status_btn)).setCardBackgroundColor(Color.parseColor("#FF8D8D"));
        }else {
            ((CardView)helper.getView(R.id.status_btn)).setCardBackgroundColor(Color.parseColor("#71C671"));
        }
    }
}
