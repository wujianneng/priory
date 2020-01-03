package com.pos.priory.adapters;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DiscountBean;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseQuickAdapter<DiscountBean, BaseViewHolder> {
    public List<DiscountBean> selectList = new ArrayList<>();

    public DiscountAdapter(int layoutResId, @Nullable List<DiscountBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscountBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.detial_tv, item.getDetail());
        helper.setGone(R.id.detail_layout, item.isShowDetail());
        helper.setImageResource(R.id.down_img, item.isShowDetail() ? R.drawable.go_top : R.drawable.go_bottom);
        helper.setChecked(R.id.checkbox, item.isSelected());
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) selectList.add(item);
            else selectList.remove(item);
            item.setSelected(isChecked);
        });
        helper.setOnClickListener(R.id.down_img,v -> {
            item.setShowDetail(!item.isShowDetail());
            notifyItemChanged(helper.getAdapterPosition());
        });
    }
}
