package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.beans.DataAmountBean;

import java.util.List;

public class DataAmountAdapter extends BaseQuickAdapter<DataAmountBean, BaseViewHolder> {

    public DataAmountAdapter(int layoutResId, @Nullable List<DataAmountBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataAmountBean item) {

    }
}
