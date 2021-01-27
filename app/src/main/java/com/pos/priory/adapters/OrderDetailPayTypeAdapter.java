package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderDetailReslutBean;

import java.util.List;

public class OrderDetailPayTypeAdapter extends BaseQuickAdapter<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean, BaseViewHolder> {

    public OrderDetailPayTypeAdapter(int layoutResId, @Nullable List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean item) {
       helper.setText(R.id.name_tv,item.getPaymethod());
        helper.setText(R.id.detail_tv,item.getCash_coupon_name() == null ? "" : (item.getCash_coupon_name() + "(" + item.getCash_coupon_code() + ")"));
        helper.setText(R.id.money_tv,item.getAmount() + "元");
    }
}
