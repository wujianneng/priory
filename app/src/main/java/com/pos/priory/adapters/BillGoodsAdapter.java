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
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class BillGoodsAdapter extends BaseQuickAdapter<FittingBean.ResultsBean, BaseViewHolder> {
    Context context;

    public BillGoodsAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<FittingBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FittingBean.ResultsBean item) {
         helper.setText(R.id.code_tv,item.getProductcode() + "");
         helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.price_tv,item.getPrice().get(0).getSymbol() + item.getPrice().get(0).getPrice() + "x" + item.getBuyCount());
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
