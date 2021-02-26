package com.pos.priory.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DataAmountBean;
import com.pos.priory.beans.DataCountBean;

import java.util.List;

public class DataCountAdapter extends BaseQuickAdapter<DataCountBean.ResultsBean, BaseViewHolder> {

    public String currency = "MOP";

    public DataCountAdapter(int layoutResId, @Nullable List<DataCountBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataCountBean.ResultsBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.code_tv,item.getProductcode() + "");
        helper.setText(R.id.sale_count_tv,"銷量：" + item.getSales_quantity());
        helper.setText(R.id.sale_amount_tv,"銷售額：" + item.getSales_amount());
        helper.setText(R.id.weight_tv,"平均重量：" + item.getWeight());

        Glide.with(mContext).load(item.getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
