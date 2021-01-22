package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderDetailReslutBean;

import java.util.List;

public class OrderDetailDiscountAdapter extends BaseQuickAdapter<OrderDetailReslutBean.PayDetailBean.CouponsBean, BaseViewHolder> {

    public OrderDetailDiscountAdapter(int layoutResId, @Nullable List<OrderDetailReslutBean.PayDetailBean.CouponsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailReslutBean.PayDetailBean.CouponsBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.amount_tv,"-" + item.getCoupon_amount() + "元");
    }
}
