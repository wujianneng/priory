package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkImageLoader;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryAdapter extends BaseQuickAdapter<WarehouseBean.ResultsBean.ItemBean, BaseViewHolder> {
    Context context;

    public RepertoryAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WarehouseBean.ResultsBean.ItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WarehouseBean.ResultsBean.ItemBean item) {
        helper.setText(R.id.weight_tv, item.getReturninfo() != null && item.getReturninfo().getDate() != null ? item.getReturninfo().getWeight() + "g"
                : item.getSubtotal().getWeight() + "g");
        helper.setText(R.id.code_tv, item.getProductcode() + "");
        helper.setGone(R.id.img_go, item.getReturninfo() != null && item.getReturninfo().getDate() != null ? false : true);
        helper.setGone(R.id.edit_btn, item.getReturninfo() != null && item.getReturninfo().getDate() != null ? true : false);
        if (item.getReturninfo() != null && item.getReturninfo().getDate() != null) {
            helper.setText(R.id.price_tv, "$" + item.getReturninfo().getCost());
            helper.setText(R.id.name_tv, item.getName() + "(" + item.getReturninfo().getType() + ")");
        } else {
            helper.setText(R.id.name_tv, item.getName());
            helper.setText(R.id.price_tv, item.getPrice().get(0).getSymbol() +  item.getPrice().get(0).getPrice());
        }
        Glide.with(context).load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.addOnClickListener(R.id.edit_btn);
    }
}
