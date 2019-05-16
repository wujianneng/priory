package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.InventoryBean;
import com.pos.priory.beans.InventoryDetialBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryDetialAdapter extends BaseQuickAdapter<InventoryBean.InventoryitemBean, BaseViewHolder> {

    public InventoryDetialAdapter(@LayoutRes int layoutResId, @Nullable List<InventoryBean.InventoryitemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryBean.InventoryitemBean item) {
         helper.setText(R.id.name_tv,item.getProductname());
         helper.setText(R.id.code_tv,item.getStockno());
         helper.setText(R.id.weight_tv,item.getStockweight() + "g");
    }
}
