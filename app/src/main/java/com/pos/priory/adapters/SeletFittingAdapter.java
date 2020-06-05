package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.FittingBean;

import java.util.ArrayList;
import java.util.List;

public class SeletFittingAdapter extends BaseQuickAdapter<FittingBean, BaseViewHolder> {
    public List<FittingBean> selectFittingList = new ArrayList<>();

    public SeletFittingAdapter(int layoutResId, @Nullable List<FittingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FittingBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.price_tv, item.getPrice());
        helper.setChecked(R.id.checkbox, item.isSeleted());
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) selectFittingList.add(item);
            else selectFittingList.remove(item);
            item.setSeleted(isChecked);
        });
    }
}
