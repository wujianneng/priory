package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.ReturnStockBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryRecoverAdapter extends BaseQuickAdapter<ReturnStockBean, BaseViewHolder> {

    public InventoryRecoverAdapter(@LayoutRes int layoutResId, @Nullable List<ReturnStockBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReturnStockBean item) {
        helper.setText(R.id.code_tv,item.getRmaorder() + "");
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.count_tv,item.getQuantity() + "");
        helper.setText(R.id.weight_tv,item.getWeight());
    }
}
