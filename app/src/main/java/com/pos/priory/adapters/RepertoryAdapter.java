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

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryAdapter extends BaseQuickAdapter<GoodBean,BaseViewHolder> {
    Context context;

    public RepertoryAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodBean item) {
        helper.setText(R.id.code_tv,item.getBatch().getProduct().getProductcode() + item.getBatch().getBatchno());
        helper.setText(R.id.name_tv,item.getBatch().getProduct().getName());
        helper.setText(R.id.count_tv,item.getQuantity() + "");
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getBatch().getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
