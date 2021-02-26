package com.pos.priory.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.NewGoodTypeDataBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

public class NewGoodTypeDataAdapter extends BaseQuickAdapter<NewGoodTypeDataBean, BaseViewHolder> {

    public NewGoodTypeDataAdapter(int layoutResId, @Nullable List<NewGoodTypeDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewGoodTypeDataBean item) {
        helper.setText(R.id.name_tv,item.getName());
        helper.setText(R.id.weight_tv, LogicUtils.getKeepLastOneNumberAfterLittlePoint(item.getWeight() * item.getCount()) + "g");
        helper.setText(R.id.count_tv,item.getCount() + "件");
    }
}
