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
        helper.setText(R.id.code_tv, item.getProduct().getProductcode() + item.getStockno());
        helper.setText(R.id.name_tv, item.getProduct().getName());
//        helper.setText(R.id.weight_tv,item.getBatch().getWeight());
        helper.setText(R.id.origin_price_tv,"原價：" + item.getProduct().getPrePrice());
        helper.setText(R.id.discount_tv,"折扣：" + item.getDiscountRate());
        helper.setText(R.id.price_tv, "折後價：" + item.getProduct().getPrice());
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
