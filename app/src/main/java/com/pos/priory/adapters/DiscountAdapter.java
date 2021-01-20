package com.pos.priory.adapters;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.CouponResultBean;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseQuickAdapter<CouponResultBean, BaseViewHolder> {
    public List<CouponResultBean> selectList = new ArrayList<>();

    public DiscountAdapter(int layoutResId, @Nullable List<CouponResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponResultBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.detial_tv, item.getTerms());
        helper.setGone(R.id.detail_layout, item.isShowDetail());
        helper.setImageResource(R.id.down_img, item.isShowDetail() ? R.drawable.go_top : R.drawable.go_bottom);
        helper.setChecked(R.id.checkbox, item.isSelected());
        if (selectList.size() != 0) {
            if (!item.isSelected())
                helper.getView(R.id.checkbox).setEnabled(item.isWith_another_coupon() ? item.isIs_usable() : false);
            else
                helper.getView(R.id.checkbox).setEnabled(true);
        } else {
            helper.getView(R.id.checkbox).setEnabled(item.isIs_usable());
        }

        helper.addOnClickListener(R.id.checkbox);

        helper.setOnClickListener(R.id.down_img, v -> {
            item.setShowDetail(!item.isShowDetail());
            notifyItemChanged(helper.getAdapterPosition());
        });
    }
}
