package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryStoreAdapter extends BaseQuickAdapter<InventoryBean, BaseViewHolder> {
    Context context;

    public InventoryStoreAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<InventoryBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryBean item) {
        helper.setImageResource(R.id.icon_check, item.isCheck() ? R.drawable.icon_cilck_h : R.drawable.icon_cilck);
        helper.setText(R.id.code_tv, item.getStock().getProduct().getProductcode() + "");
        helper.setText(R.id.name_tv,item.getStock().getProduct().getName());
        Glide.with(context).load(Constants.BASE_URL + item.getStock().getProduct().getImage())
                .error(android.R.drawable.ic_menu_gallery)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.setText(R.id.weight_tv,"" + item.getStock().getQuantity());
        helper.addOnClickListener(R.id.icon_check);
    }
}
