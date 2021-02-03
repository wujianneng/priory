package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.DateUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class QueryOrderAdapter extends BaseQuickAdapter<OrderBean.ResultsBean,BaseViewHolder> {

    public QueryOrderAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.ResultsBean item) {

        helper.setText(R.id.detial_tv, item.getMember_mobile());
        helper.setText(R.id.date_tv,item.getCreated());
        helper.setText(R.id.money_tv, "$" + item.getAmount());
        helper.setText(R.id.name_tv,item.getMember_lastname() + " " + item.getMember_firstname());
    }
}
