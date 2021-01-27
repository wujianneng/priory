package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.FittingBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class AddNewOrderGoodsAdapter extends BaseQuickAdapter<FittingBean.ResultsBean, BaseViewHolder> {
    Context context;

    public AddNewOrderGoodsAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<FittingBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FittingBean.ResultsBean item) {
        helper.setText(R.id.code_tv, item.getProductcode() + item.getWhitem().get(0).getWhnumber());
        helper.setText(R.id.name_tv, item.getName());
        if (item.getWhitem().size() != 0)
            helper.setText(R.id.weight_tv, item.getWhitem().get(0).getWeight() * item.getBuyCount() + "g");
        helper.setText(R.id.origin_price_tv, "售價：" + item.getPrice().get(0).getSymbol() + item.getPrice().get(0).getPrice());
        helper.setGone(R.id.price_tv,false);
        Glide.with(context).load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.addOnClickListener(R.id.decrease_btn);
        helper.addOnClickListener(R.id.increase_btn);

        helper.setText(R.id.fitting_count_tv, item.getBuyCount() + "");
        helper.setGone(R.id.fitting_count_layout, item.isAccessory());
    }
}
