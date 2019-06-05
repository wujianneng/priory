package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.ReturnGoodBean;
import com.pos.priory.utils.DateUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryReturnAdapter extends BaseQuickAdapter<ReturnGoodBean.ReturnstockitemBean,BaseViewHolder> {
    Context context;

    public RepertoryReturnAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<ReturnGoodBean.ReturnstockitemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReturnGoodBean.ReturnstockitemBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.count_tv, item.getWeight() + "g");
        helper.setText(R.id.code_tv, DateUtils.covertIso8601ToDate2(item.getCreated()));
        helper.setText(R.id.status_tv,"待辦中");
        helper.setText(R.id.price_tv,item.getReturnprice() + "");
    }
}
