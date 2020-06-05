package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class OrderDetailPrintGoodsAdapter extends BaseQuickAdapter<OrderBean.ResultsBean,BaseViewHolder> {

    public OrderDetailPrintGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.ResultsBean item) {
//        helper.setText(R.id.good_code_tv, item.getStock().getProduct().getProductcode() + item.getStock().getStockno());
//        helper.setText(R.id.good_name_tv,item.getStock().getProduct().getName());
//        helper.setText(R.id.good_count_tv, "1");
//        helper.setText(R.id.discount_tv, item.getDiscount());
//        helper.setText(R.id.reatial_price_tv,item.getOriginprice() + "");
//        helper.setText(R.id.price_tv,item.getPrice() + "");
    }
}
