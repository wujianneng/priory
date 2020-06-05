package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class OrderDetailPrintPayTypeAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public OrderDetailPrintPayTypeAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.title_text,item);
    }
}
