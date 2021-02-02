package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.CashCouponResultBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashCouponAdapter extends BaseQuickAdapter<CashCouponResultBean, BaseViewHolder> {
    public List<CashCouponResultBean> selectList = new ArrayList<>();

    public CashCouponAdapter(int layoutResId, @Nullable List<CashCouponResultBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashCouponResultBean item) {
        helper.setGone(R.id.detail_layout, item.isShowDetail());
        helper.setText(R.id.detial_tv, item.getCode());
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.start_time_tv, "開始時間：" + item.getStarttime());
        helper.setText(R.id.end_time_tv, "結束時間：" + item.getEndtime());
        helper.setText(R.id.code_tv, item.getCode());
        helper.setText(R.id.date_tv, item.getUsed_datetime());
        helper.setText(R.id.amount_tv, "$" + item.getValue());
        helper.setImageResource(R.id.down_img, item.isShowDetail() ? R.drawable.go_top : R.drawable.go_bottom);
        helper.setChecked(R.id.checkbox, item.isSelected());

        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) {
                selectList.add(item);
            } else {
                selectList.remove(item);
            }
            item.setSelected(isChecked);
        });
        helper.setOnClickListener(R.id.down_img, v -> {
            item.setShowDetail(!item.isShowDetail());
            notifyItemChanged(helper.getAdapterPosition());
        });
    }
}
