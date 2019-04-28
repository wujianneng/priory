package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class OrderDetailPrintGoodsAdapter extends BaseQuickAdapter<OrderItemBean,BaseViewHolder> {

    public OrderDetailPrintGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItemBean item) {
        helper.setText(R.id.good_code_tv,item.getStock().getBatch().getBatchno() + item.getStock().getBatch().getProduct().getProductcode());
        helper.setText(R.id.good_name_tv,item.getStock().getBatch().getProduct().getName());
        helper.setText(R.id.good_count_tv,item.getQuantity() + "");
        if(Double.parseDouble(item.getDiscount()) == 1){
            helper.setText(R.id.discount_tv, "-");
        }else if(Double.parseDouble(item.getDiscount()) == 0){
            helper.setText(R.id.discount_tv, "赠送");
        } else {
            helper.setText(R.id.discount_tv, LogicUtils.getKeepLastOneNumberAfterLittlePoint(Double.parseDouble(item.getDiscount()) * 10) + "折");
        }
        helper.setText(R.id.reatial_price_tv,item.getStock().getBatch().getProduct().getPrice());
        helper.setText(R.id.price_tv,item.getPrice() + "");
    }
}
