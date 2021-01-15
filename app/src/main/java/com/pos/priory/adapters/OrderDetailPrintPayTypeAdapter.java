package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderDetailReslutBean;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class OrderDetailPrintPayTypeAdapter extends BaseQuickAdapter<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean,BaseViewHolder> {

    public OrderDetailPrintPayTypeAdapter(@LayoutRes int layoutResId, @Nullable List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean item) {
        helper.setText(R.id.title_text,item.getPaymethod());
        helper.setText(R.id.money_text,item.getAmount() + "元");
    }
}
