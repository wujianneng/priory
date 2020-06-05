package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.MemberBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class QueryMemberAdapter extends BaseQuickAdapter<MemberBean.ResultsBean, BaseViewHolder> {

    public QueryMemberAdapter(@LayoutRes int layoutResId, @Nullable List<MemberBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberBean.ResultsBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setImageResource(R.id.sex_img, item.getGender().equals("男") ? R.drawable.icon_boy : R.drawable.icon_girl);
        helper.setText(R.id.phone_tv,item.getMobile());
    }
}
