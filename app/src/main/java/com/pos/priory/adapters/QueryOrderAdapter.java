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

public class QueryOrderAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {

    public QueryOrderAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        if(item.getStatus().equals("draft")){
            helper.setImageResource(R.id.check_img,R.drawable.icon_cilck);
        }else if(item.getStatus().equals("completed")){
            helper.setImageResource(R.id.check_img,R.drawable.icon_cilck_h);
        }else if(item.getStatus().equals("cancelled")) {
            helper.setImageResource(R.id.check_img,R.drawable.icon_click_red);
        }else {
            helper.setImageResource(R.id.check_img,R.drawable.icon_click_orige);
        }
        helper.setText(R.id.detial_tv, item.getOrdernumber());
        helper.setText(R.id.date_tv,DateUtils.covertIso8601ToDate(item.getCreated()));
        helper.setText(R.id.money_tv, "$" + item.getTotalprice());
    }
}
