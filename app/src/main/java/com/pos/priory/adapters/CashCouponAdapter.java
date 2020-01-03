package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.CashCouponBean;
import com.pos.priory.beans.DiscountBean;

import java.util.ArrayList;
import java.util.List;

public class CashCouponAdapter extends BaseQuickAdapter<CashCouponBean, BaseViewHolder> {
    public List<CashCouponBean> selectList = new ArrayList<>();

    public CashCouponAdapter(int layoutResId, @Nullable List<CashCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashCouponBean item) {
        helper.setGone(R.id.detail_layout, item.isShowDetail());
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.code_tv, item.getCode());
        helper.setText(R.id.date_tv, item.getStartDate() + "~" + item.getEndDate());
        helper.setText(R.id.code_tv, item.getCode());
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
