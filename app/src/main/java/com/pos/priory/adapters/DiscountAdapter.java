package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.CouponResultBean;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseQuickAdapter<CouponResultBean.ResultBean, BaseViewHolder> {
    public List<CouponResultBean.ResultBean> selectList = new ArrayList<>();

    public DiscountAdapter(int layoutResId, @Nullable List<CouponResultBean.ResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponResultBean.ResultBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.detial_tv, item.getDescription());
        helper.setGone(R.id.detail_layout, item.isShowDetail());
        helper.setImageResource(R.id.down_img, item.isShowDetail() ? R.drawable.go_top : R.drawable.go_bottom);
        helper.setChecked(R.id.checkbox, item.isSelected());

        if (selectList.size() != 0) {
            if (selectList.get(0).isMultiple()) {
                helper.getView(R.id.checkbox).setEnabled(item.isMultiple() ? true : false);
            } else {
                if (!item.isSelected())
                    helper.getView(R.id.checkbox).setEnabled(false);
            }
        } else {
            helper.getView(R.id.checkbox).setEnabled(true);
        }

        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) selectList.add(item);
            else selectList.remove(item);
            item.setSelected(isChecked);
            try {
                notifyDataSetChanged();
            } catch (Exception e) {

            }

        });
        helper.setOnClickListener(R.id.down_img, v -> {
            item.setShowDetail(!item.isShowDetail());
            notifyItemChanged(helper.getAdapterPosition());
        });
    }
}
