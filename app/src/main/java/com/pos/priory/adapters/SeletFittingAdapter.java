package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.FittingBean;

import java.util.ArrayList;
import java.util.List;

public class SeletFittingAdapter extends BaseQuickAdapter<FittingBean.ResultsBean, BaseViewHolder> {
    public List<FittingBean.ResultsBean> selectFittingList = new ArrayList<>();

    public SeletFittingAdapter(int layoutResId, @Nullable List<FittingBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FittingBean.ResultsBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.price_tv, item.getPrice().size() == 0 ? "" : item.getPrice().get(0).getSymbol() + item.getPrice().get(0).getPrice());
        helper.setChecked(R.id.checkbox, item.isSelected());
        helper.getView(R.id.checkbox).setEnabled(item.getWhitem().size() != 0);
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) selectFittingList.add(item);
            else selectFittingList.remove(item);
            item.setSelected(isChecked);
        });
    }
}
