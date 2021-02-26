package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.RepertoryRecordBean;
import com.pos.priory.beans.WarehouseBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryRecordAdapter extends BaseQuickAdapter<RepertoryRecordBean.ResultsBean, BaseViewHolder> {
    Context context;

    public RepertoryRecordAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<RepertoryRecordBean.ResultsBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RepertoryRecordBean.ResultsBean item) {
        helper.setText(R.id.date_tv, item.getCreated());
        helper.setText(R.id.name_tv, item.getPrd_name());
        helper.setText(R.id.code_tv, item.getPrd_no() + "");
        helper.setText(R.id.weight_tv, item.getPrd_weight() + "g");
        helper.setText(R.id.user_tv, item.getUsername());
        helper.setText(R.id.inout_tv, item.getPurpose_display() + "");
        helper.setText(R.id.type_tv, item.getType_display() + "");
        helper.setText(R.id.whform_tv, item.getRelated_whname() + "");
    }
}
