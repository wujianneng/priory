package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DinghuoGoodBean;
import com.pos.priory.beans.WarehouseBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DinghuoAdapter extends BaseQuickAdapter<WarehouseBean.ResultsBean.ItemBean, BaseViewHolder> {

    public DinghuoAdapter(@LayoutRes int layoutResId, @Nullable List<WarehouseBean.ResultsBean.ItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WarehouseBean.ResultsBean.ItemBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.weight_tv, item.getSubtotal().getWeight() + "g");
        helper.setText(R.id.code_tv, item.getProductcode() + "");
        helper.setText(R.id.price_tv, item.getPrice().get(0).getSymbol() +  item.getPrice().get(0).getPrice());
        Glide.with(helper.getConvertView().getContext()).load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.addOnClickListener(R.id.dinghuo_btn);
    }
}
