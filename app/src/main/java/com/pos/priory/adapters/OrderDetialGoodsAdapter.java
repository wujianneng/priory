package com.pos.priory.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.OrderItemBean;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderDetialGoodsAdapter extends BaseQuickAdapter<OrderItemBean, BaseViewHolder> {

    public OrderDetialGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<OrderItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderItemBean item) {
        helper.setText(R.id.code_tv,item.getStock().getProduct().getProductcode() + "");
        helper.setText(R.id.name_tv,item.getStock().getProduct().getName());
        helper.setText(R.id.price_tv,"$" + item.getStock().getProduct().getPrice() + "x" +
                item.getQuantity());
        helper.setText(R.id.account_btn,item.getOprateCount() + "");
        RadioButton radioButton = helper.getView(R.id.radio);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isSelected())
                    item.setSelected(false);
                else
                    item.setSelected(true);
                item.setOprateCount(item.getQuantity());
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
        if(item.isSelected()) {
            radioButton.setChecked(true);
            helper.setVisible(R.id.change_count_layout,true);
        }else {
            radioButton.setChecked(false);
            helper.setVisible(R.id.change_count_layout,false);
        }
        helper.addOnClickListener(R.id.decrease_btn)
                .addOnClickListener(R.id.increase_btn);
    }
}
