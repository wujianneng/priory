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
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class AddNewOrderGoodsAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    Context context;

    public AddNewOrderGoodsAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {
        helper.addOnClickListener(R.id.btn_change_price).addOnClickListener(R.id.decrease_btn)
                .addOnClickListener(R.id.increase_btn);
        helper.setText(R.id.code_tv, item.getBatch().getProduct().getProductcode() + item.getBatch().getBatchno());
        helper.setText(R.id.name_tv, item.getBatch().getProduct().getName());
        helper.setText(R.id.price_tv, "$" + item.getBatch().getProduct().getPrice());
        helper.setText(R.id.account_btn, item.getSaleCount() + "");
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getBatch().getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
