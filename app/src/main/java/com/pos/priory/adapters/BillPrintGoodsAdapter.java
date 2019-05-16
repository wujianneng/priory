package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.utils.LogicUtils;

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
        helper.setText(R.id.good_code_tv,item.getProduct().getProductcode() + item.getStockno());
        helper.setText(R.id.good_name_tv,item.getProduct().getName());
        helper.setText(R.id.good_count_tv,1 + "");
        if(item.getDiscountRate() == 1){
            helper.setText(R.id.discount_tv, "-");
        }else if(item.getDiscountRate() == 0){
            helper.setText(R.id.discount_tv, "赠送");
        } else {
            helper.setText(R.id.discount_tv, LogicUtils.getKeepLastOneNumberAfterLittlePoint(item.getDiscountRate() * 10) + "折");
        }
        helper.setText(R.id.reatial_price_tv,item.getProduct().getPrice());
        helper.setText(R.id.price_tv,item.getProduct().getPrice());
    }
}
