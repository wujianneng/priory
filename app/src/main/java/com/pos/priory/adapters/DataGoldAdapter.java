package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DatasSaleBean;

import java.util.List;

public class DataGoldAdapter extends BaseQuickAdapter<DatasSaleBean, BaseViewHolder> {


    public DataGoldAdapter(@LayoutRes int layoutResId, @Nullable List<DatasSaleBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, DatasSaleBean item) {
        helper.setText(R.id.index_tv,(helper.getAdapterPosition() + 1) + ".");
        helper.setText(R.id.count_tv,item.getCount() + "件");
        helper.setText(R.id.name_tv, item.getProductName());
    }
}
