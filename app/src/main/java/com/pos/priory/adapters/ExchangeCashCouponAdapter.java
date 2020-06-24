package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.activitys.ExchangeCashCouponActivity;
import com.pos.priory.beans.ExchangeCashCouponBean;

import java.util.ArrayList;
import java.util.List;

public class ExchangeCashCouponAdapter extends BaseQuickAdapter<ExchangeCashCouponBean.ResultsBean, BaseViewHolder> {
    public List<ExchangeCashCouponBean.ResultsBean> selectList = new ArrayList<>();
    ExchangeCashCouponActivity activity;

    public ExchangeCashCouponAdapter(ExchangeCashCouponActivity activity,int layoutResId, @Nullable List<ExchangeCashCouponBean.ResultsBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExchangeCashCouponBean.ResultsBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.count_tv,item.getCount() + "");
        helper.setChecked(R.id.checkbox, item.isSelected());
        helper.setGone(R.id.count_layout,item.isSelected());
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
            if(item.getReducereward() > activity.lastReward){
                helper.setChecked(R.id.checkbox,false);
                notifyItemChanged(helper.getAdapterPosition());
                return;
            }
            if (isChecked) selectList.add(item);
            else selectList.remove(item);
            item.setSelected(isChecked);
            notifyItemChanged(helper.getAdapterPosition());
            activity.resetRewardTvs();
        });
        helper.addOnClickListener(R.id.decrease_btn);
        helper.addOnClickListener(R.id.increase_btn);
    }
}
