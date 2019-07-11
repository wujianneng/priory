package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderDetialGoodsAdapter extends BaseQuickAdapter<OrderBean.ItemsBean, BaseViewHolder> {

    public OrderDetialGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean.ItemsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean.ItemsBean item) {
        Log.e("glide","url:" + Constants.BASE_URL_HTTP + item.getStock().getProduct().getImage());
        Glide.with(mContext).load(Constants.BASE_URL_HTTP + item.getStock().getProduct().getImage())
                .asBitmap()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.setText(R.id.code_tv,item.getStock().getProduct().getProductcode() + item.getStock().getStockno());
        helper.setText(R.id.name_tv,item.getStock().getProduct().getName());
        helper.setText(R.id.weight_tv,item.getStock().getWeight() + "g");
        Log.e("glide","item.getWeight():" + item.getStock().getWeight());
        helper.setText(R.id.origin_price_tv,"原价： " + item.getStock().getProduct().getPrice() + "元");
        String discountName = item.getDiscount();
        helper.setText(R.id.discount_tv,"折扣： " + discountName);
        helper.setText(R.id.discount_price_tv,"折后价： "+ item.getPrice() + "元");
        RadioButton radioButton = helper.getView(R.id.radio);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isSelected())
                    item.setSelected(false);
                else
                    item.setSelected(true);
                item.setOprateCount(1);
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
        if(item.isSelected()) {
            radioButton.setChecked(true);
        }else {
            radioButton.setChecked(false);
        }

        helper.setVisible(R.id.radio,!item.isReturned() && item.getStock().getProduct().getCatalog().getName().equals("黄金") ? true : false);
    }
}
