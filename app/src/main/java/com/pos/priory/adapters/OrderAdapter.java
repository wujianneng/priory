package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.DateUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    List<OrderBean> data;


    public OrderAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if(DateUtils.covertIso8601ToDate2(data.get(position).getCreated()).equals(DateUtils.getToday()))
            return 0;
        else
            return 1;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        helper.setGone(R.id.right_line,DateUtils.covertIso8601ToDate2(item.getCreated()).equals(DateUtils.getToday())
                && MyApplication.staffInfoBean.getPermission().equals("店長") ? true : false);

        helper.setText(R.id.detial_tv, item.getOrdernumber());
        helper.setText(R.id.date_tv,DateUtils.covertIso8601ToDate(item.getCreated()));
        helper.setText(R.id.money_tv, "" + item.getTotalprice());
        helper.setText(R.id.name_tv,item.getMember().getLast_name() + item.getMember().getFirst_name());
        helper.setImageResource(R.id.sex_img,item.getMember().getSex().equals("男") ? R.drawable.icon_boy : R.drawable.icon_girl);
    }
}
