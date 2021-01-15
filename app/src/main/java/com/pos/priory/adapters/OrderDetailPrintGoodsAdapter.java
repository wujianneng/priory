package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class OrderDetailPrintGoodsAdapter extends BaseQuickAdapter<OrderDetailReslutBean.OrderItemsBean,BaseViewHolder> {

    public OrderDetailPrintGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderDetailReslutBean.OrderItemsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailReslutBean.OrderItemsBean item) {
        helper.setText(R.id.good_code_tv, item.getNumber());
        helper.setText(R.id.good_name_tv,item.getPrd_name());
        helper.setText(R.id.good_count_tv, item.getQuantity() + "件");
//        helper.setText(R.id.discount_tv, item.getDiscount());
        helper.setText(R.id.reatial_price_tv,item.getOriginprice() + "元");
        helper.setText(R.id.price_tv,item.getPrice() + "元");
    }
}
