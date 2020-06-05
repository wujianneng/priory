package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.InventoryDetialBean;
import com.pos.priory.beans.InventoryTypeDetialBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryTeypDetialAdapter extends BaseQuickAdapter<InventoryTypeDetialBean.CategoryBean, BaseViewHolder> {

    public InventoryTeypDetialAdapter(@LayoutRes int layoutResId, @Nullable List<InventoryTypeDetialBean.CategoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InventoryTypeDetialBean.CategoryBean item) {
      helper.setText(R.id.gold_tv,item.getCategory() + " (" + item.getCount() + "/" + item.getTotal() + ")");
      helper.addOnClickListener(R.id.gold_btn);
    }
}
