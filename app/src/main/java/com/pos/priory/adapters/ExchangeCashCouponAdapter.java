package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.activitys.ExchangeCashCouponActivity;
import com.pos.priory.beans.ExchangeCashCouponBean;

import java.util.ArrayList;
import java.util.List;

public class ExchangeCashCouponAdapter extends BaseQuickAdapter<ExchangeCashCouponBean.ResultBean.RewardListBean, BaseViewHolder> {
    public List<ExchangeCashCouponBean.ResultBean.RewardListBean> selectList = new ArrayList<>();
    ExchangeCashCouponActivity activity;

    public ExchangeCashCouponAdapter(ExchangeCashCouponActivity activity, int layoutResId, @Nullable List<ExchangeCashCouponBean.ResultBean.RewardListBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ExchangeCashCouponBean.ResultBean.RewardListBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.count_tv, item.getCount() + "");
        helper.setGone(R.id.count_layout, item.isSelected());
        helper.setOnCheckedChangeListener(R.id.checkbox, null);
        helper.setChecked(R.id.checkbox, item.isSelected());
        helper.setOnCheckedChangeListener(R.id.checkbox, (buttonView, isChecked) -> {
                if(isChecked){
                    if(activity.lastReward >= item.getReducereward()){
                        selectList.add(item);
                        item.setSelected(isChecked);
                        notifyItemChanged(helper.getAdapterPosition());
                        activity.resetRewardTvs();
                    }else {
                        item.setSelected(false);
                        notifyItemChanged(helper.getAdapterPosition());
                    }
                }else {
                    selectList.remove(item);
                    item.setSelected(isChecked);
                    notifyItemChanged(helper.getAdapterPosition());
                    activity.resetRewardTvs();
                }
        });
        helper.addOnClickListener(R.id.decrease_btn);
        helper.addOnClickListener(R.id.increase_btn);
    }
}
