package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.ExchangeCashCouponBean;

import java.util.ArrayList;
import java.util.List;

public class ExchangeCashCouponAdapter extends BaseQuickAdapter<ExchangeCashCouponBean, BaseViewHolder> {
    public List<ExchangeCashCouponBean> selectList = new ArrayList<>();

    public ExchangeCashCouponAdapter(int layoutResId, @Nullable List<ExchangeCashCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExchangeCashCouponBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.integal_tv,item.getNeedIntegal() + "");
        helper.setText(R.id.count_tv,item.getCount() + "");
        helper.setChecked(R.id.checkbox, item.isSelected());
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if (isChecked) selectList.add(item);
            else selectList.remove(item);
            item.setSelected(isChecked);
        });
        helper.setOnClickListener(R.id.decrease_btn, v -> {
            item.setCount(item.getCount() == 1 ? 1 : item.getCount() - 1);
            notifyItemChanged(helper.getAdapterPosition());
        });
        helper.setOnClickListener(R.id.increase_btn, v -> {
            item.setCount(item.getCount() + 1);
            notifyItemChanged(helper.getAdapterPosition());
        });
    }
}
