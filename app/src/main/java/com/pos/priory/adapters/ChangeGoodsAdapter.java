package com.pos.priory.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.activitys.ReturnGoodsActivity;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class ChangeGoodsAdapter extends BaseQuickAdapter<OrderItemBean,BaseViewHolder> {
    Context context;

    public ChangeGoodsAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<OrderItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderItemBean item) {
        helper.setText(R.id.code_tv,item.getStock().getProduct().getProductcode() + "");
        helper.setText(R.id.name_tv,item.getStock().getProduct().getName());
        if(context instanceof ReturnGoodsActivity){
            helper.setText(R.id.price_tv, "$" + item.getStock().getProduct().getPrice());
            helper.setGone(R.id.price_tv, true);
            helper.setText(R.id.retial_price_tv, "$" + LogicUtils.getKeepLastThreeNumberAfterLittlePoint
                    (Double.parseDouble(item.getStock().getProduct().getPrice()) * Constants.RETURN_GOOD_RAGE));
        }else {
            helper.setGone(R.id.price_tv, false);
            helper.setText(R.id.retial_price_tv, "$" + item.getStock().getProduct().getPrice() + " x " + item.getOprateCount());
        }
        Glide.with(context).load(item.getStock().getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        ((TextView)helper.getView(R.id.price_tv)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        final EditText edtWeight = helper.getView(R.id.edt_weight);
        edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
        edtWeight.setText(item.getWeight());
        edtWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TextWatcher textWatcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!s.toString().equals("")) {
                                item.setWeight(s.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };
                    edtWeight.setTag(textWatcher);
                    edtWeight.addTextChangedListener(textWatcher);
                } else {
                    edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
                    edtWeight.setText(item.getWeight());
                }
            }
        });
    }
}
