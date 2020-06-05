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
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class AddNewOrderGoodsAdapter extends BaseQuickAdapter<WarehouseBean.ResultsBean, BaseViewHolder> {
    Context context;

    public AddNewOrderGoodsAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WarehouseBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WarehouseBean.ResultsBean item) {
        helper.setText(R.id.code_tv, item.getItem().get(0).getProductcode() + "");
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.weight_tv, item.getTotal().getWeight() + "g");
        helper.setText(R.id.origin_price_tv, "原价：" + item.getItem().get(0).getPrice().get(0).getSymbol() +  item.getItem().get(0).getPrice().get(0).getPrice());
        Glide.with(context).load(item.getItem().get(0).getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        helper.setOnClickListener(R.id.decrease_btn, v -> {
            item.getTotal().setQuantity(item.getTotal().getQuantity() == 1 ? 1 : item.getTotal().getQuantity() - 1);
            notifyItemChanged(helper.getAdapterPosition());
        });
        helper.setOnClickListener(R.id.increase_btn, v -> {
            item.getTotal().setQuantity(item.getTotal().getQuantity() + 1);
            notifyItemChanged(helper.getAdapterPosition());
        });
        helper.setText(R.id.fitting_count_tv, item.getTotal().getQuantity() + "");
    }
}
