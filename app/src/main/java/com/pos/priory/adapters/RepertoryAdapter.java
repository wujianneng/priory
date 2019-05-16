package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
        helper.setText(R.id.name_tv,item.getProduct().getName());
        helper.setText(R.id.count_tv, item.getWeight() + "克");
        helper.setText(R.id.code_tv,item.getProduct().getProductcode() + item.getStockno());
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
