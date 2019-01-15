package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class BillPrintGoodsAdapter extends BaseQuickAdapter<GoodBean,BaseViewHolder> {

    public BillPrintGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {
        helper.setText(R.id.good_name_tv,item.getBatch().getProduct().getName());
        helper.setText(R.id.good_count_tv,item.getSaleCount() + "");
        helper.setText(R.id.discount_tv,item.getDiscountRate() + "");
        helper.setText(R.id.reatial_price_tv,item.getBatch().getProduct().getPrice());
        helper.setText(R.id.price_tv,item.getBatch().getProduct().getPrice());
    }
}
