package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class GoodDetialAdapter extends BaseQuickAdapter<WarehouseBean.ResultsBean.ItemBean, BaseViewHolder> {
    Context context;


    public GoodDetialAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WarehouseBean.ResultsBean.ItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WarehouseBean.ResultsBean.ItemBean item) {
        RadioButton radioButton = helper.getView(R.id.radio);
        radioButton.setOnClickListener(view -> {
            if (item.isSelected())
                item.setSelected(false);
            else
                item.setSelected(true);
            notifyItemChanged(helper.getAdapterPosition());
        });
        if (item.isSelected()) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
        helper.setText(R.id.code_tv, item.getProductcode() + "");
        helper.setText(R.id.weight_tv, item.getSubtotal().getWeight() + "g");
    }
}
