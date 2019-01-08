package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class BillGoodsAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    Context context;

    public BillGoodsAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodBean item) {
         helper.setText(R.id.code_tv,item.getProduct().getProductcode() + "");
         helper.setText(R.id.name_tv,item.getProduct().getName());
        helper.setText(R.id.price_tv,"$" + item.getProduct().getPrice() + "x" + item.getSaleCount());
        Glide.with(context).load(item.getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
