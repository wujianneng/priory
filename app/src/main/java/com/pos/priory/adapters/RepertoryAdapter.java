package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.WarehouseBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryAdapter extends BaseQuickAdapter<WarehouseBean.ResultsBean, BaseViewHolder> {
    Context context;

    public RepertoryAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WarehouseBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WarehouseBean.ResultsBean item) {
        if (item.getResultsBean() == null) {
            helper.setText(R.id.weight_tv, item.getPrd_stock_weight() + "g");
            helper.setText(R.id.code_tv, item.getProductcode() + "");
            helper.setText(R.id.count_tv, item.getPrd_stock_quantity() + "件");
            helper.setGone(R.id.img_go, true);
            helper.setText(R.id.name_tv, item.getName());
            helper.setText(R.id.price_tv, item.getSymbol() + item.getUnitprice());
            Glide.with(context).load(item.getImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into((ImageView) helper.getView(R.id.icon_good));
        } else {
            helper.setText(R.id.weight_tv, item.getResultsBean().getReturnweight() + "g");
            helper.setText(R.id.code_tv, item.getResultsBean().getPrd_no() + "");
            helper.setText(R.id.count_tv, item.getResultsBean().getQuantity() + "件");
            helper.setGone(R.id.img_go, false);
            helper.setText(R.id.price_tv, "$" + item.getResultsBean().getReturncost());
            helper.setText(R.id.name_tv, item.getResultsBean().getPrd_name() + "(" + item.getResultsBean().getReturntype() + ")");
            Glide.with(context).load(item.getImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into((ImageView) helper.getView(R.id.icon_good));
        }
    }
}
