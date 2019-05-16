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
import com.pos.priory.beans.ReturnGoodBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryReturnAdapter extends BaseQuickAdapter<ReturnGoodBean,BaseViewHolder> {
    Context context;

    public RepertoryReturnAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<ReturnGoodBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReturnGoodBean item) {
        helper.setText(R.id.name_tv,item.geName());
        helper.setText(R.id.count_tv, item.getWeight() + "克");
        helper.setText(R.id.code_tv,item.getStatus());
    }
}
