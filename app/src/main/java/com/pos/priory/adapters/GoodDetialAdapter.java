package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.beans.WhitemDetailResultBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class GoodDetialAdapter extends BaseQuickAdapter<WhitemDetailResultBean.WhitemBean, BaseViewHolder> {
    Context context;


    public GoodDetialAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WhitemDetailResultBean.WhitemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper,WhitemDetailResultBean.WhitemBean item) {
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
        helper.setText(R.id.code_tv, item.getPrd_no()  + "");
        helper.setText(R.id.weight_tv, item.getWeight() + "g");
    }
}
