package com.pos.priory.adapters;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderDetialGoodsAdapter extends BaseQuickAdapter<OrderItemBean, BaseViewHolder> {

    public OrderDetialGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderItemBean item) {
        Log.e("glide","url:" + Constants.BASE_URL + item.getStock().getBatch().getProduct().getImage());
        Glide.with(mContext).load(Constants.BASE_URL + item.getStock().getBatch().getProduct().getImage())
                .asBitmap()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.setText(R.id.code_tv,item.getStock().getBatch().getProduct().getProductcode() + item.getStock().getBatch().getBatchno());
        helper.setText(R.id.name_tv,item.getStock().getBatch().getProduct().getName());
        helper.setText(R.id.price_tv,"$" + item.getPrice() + "x" +
                item.getQuantity());
        helper.setText(R.id.type_tv,"類型:" + item.getStock().getBatch().getProduct().getCatalog());
        helper.setText(R.id.account_btn,item.getOprateCount() + "");
        RadioButton radioButton = helper.getView(R.id.radio);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isSelected())
                    item.setSelected(false);
                else
                    item.setSelected(true);
                item.setOprateCount(item.getQuantity());
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
        if(item.isSelected()) {
            radioButton.setChecked(true);
            helper.setVisible(R.id.change_count_layout,true);
        }else {
            radioButton.setChecked(false);
            helper.setVisible(R.id.change_count_layout,false);
        }
        helper.setVisible(R.id.radio,item.getStock().getBatch().getProduct().isReturnable() && item.getQuantity() > 0 ? true : false);
        helper.addOnClickListener(R.id.decrease_btn)
                .addOnClickListener(R.id.increase_btn);
    }
}
