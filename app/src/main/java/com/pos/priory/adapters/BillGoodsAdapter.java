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
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class BillGoodsAdapter extends BaseQuickAdapter<OrderDetailReslutBean.OrderItemsBean, BaseViewHolder> {
    Context context;

    public BillGoodsAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<OrderDetailReslutBean.OrderItemsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderDetailReslutBean.OrderItemsBean item) {
         helper.setText(R.id.code_tv,item.getNumber() + "");
         helper.setText(R.id.name_tv,item.getPrd_name());
        helper.setText(R.id.price_tv,item.getPrice() + "元 x" + item.getQuantity());
        Glide.with(context).load(Constants.BASE_URL_HTTP + item.getPrd_image())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
