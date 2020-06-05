package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DinghuoGoodBean;
import com.pos.priory.beans.MemberBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DinghuoListAdapter extends BaseQuickAdapter<DinghuoGoodBean.ResultsBean, BaseViewHolder> {

    public DinghuoListAdapter(@LayoutRes int layoutResId, @Nullable List<DinghuoGoodBean.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DinghuoGoodBean.ResultsBean item) {
        helper.setText(R.id.name_tv, item.getName());
        helper.setText(R.id.code_tv, item.getProductcode());
        helper.setText(R.id.price_tv,"$" + item.getPrice());
        helper.setText(R.id.fitting_count_tv, item.getQuantity() + "");
        helper.setText(R.id.weight_edt,item.getWeight() + "g");
        helper.setGone(R.id.weight_edt,item.getCategory().equals("黃金"));
        helper.setGone(R.id.weight_unit_tv,item.getCategory().equals("黃金"));
        helper.addOnClickListener(R.id.decrease_btn);
        helper.addOnClickListener(R.id.increase_btn);
        helper.addOnClickListener(R.id.weight_edt);
    }
}
