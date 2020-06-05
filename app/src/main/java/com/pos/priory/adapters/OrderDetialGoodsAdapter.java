package com.pos.priory.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.LastInputEditText;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderDetialGoodsAdapter extends BaseMultiItemQuickAdapter<OrderBean.ResultsBean, BaseViewHolder> {
    public int operatingStatus = 0;
    Context context;


    public OrderDetialGoodsAdapter(List<OrderBean.ResultsBean> data, Context context) {
        super(data);
        this.context = context;
        addItemType(0, R.layout.order_detial_good_list_item);
        addItemType(1, R.layout.order_detial_fitting_list_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderBean.ResultsBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                RadioButton radioButton = helper.getView(R.id.radio);
//                radioButton.setOnClickListener(view -> {
//                    if (item.isSelected())
//                        item.setSelected(false);
//                    else
//                        item.setSelected(true);
//                    item.setOprateCount(1);
//                    notifyItemChanged(helper.getAdapterPosition());
//                });
//                if (item.isSelected()) {
//                    radioButton.setChecked(true);
//                } else {
//                    radioButton.setChecked(false);
//                }
//                helper.setVisible(R.id.radio, operatingStatus != 0 ? true : false);
//                helper.setGone(R.id.input_layout, operatingStatus != 0 && item.isSelected() ? true : false);
//                final LastInputEditText edtWeight = helper.getView(R.id.edt_weight);
//                edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
//                edtWeight.setText(item.getWeight());
//                edtWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (hasFocus) {
//                            TextWatcher textWatcher = new TextWatcher() {
//                                @Override
//                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                }
//
//                                @Override
//                                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                    String str = s.toString();
//                                    item.setWeight(str);
//                                    Log.e("itemweight", "itemweight:" + item.getWeight());
////                                    ((OrderDetialActivity) context).resetSumMoney(false);
////                                    notifyItemChanged(helper.getAdapterPosition());
//                                }
//
//                                @Override
//                                public void afterTextChanged(Editable s) {
//
//                                }
//                            };
//                            edtWeight.setTag(textWatcher);
//                            edtWeight.addTextChangedListener(textWatcher);
//                        } else {
//                            edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
//                            edtWeight.setText(item.getWeight());
//                        }
//                    }
//                });
                break;
            case 1:

                break;
        }

//        Log.e("glide","url:" + Constants.BASE_URL_HTTP + item.getStock().getProduct().getImage());
//        Glide.with(mContext).load(Constants.BASE_URL_HTTP + item.getStock().getProduct().getImage())
//                .placeholder(android.R.drawable.ic_menu_gallery)
//                .error(android.R.drawable.ic_menu_gallery)
//                .into((ImageView) helper.getView(R.id.icon_good));
//        helper.setText(R.id.code_tv,item.getStock().getProduct().getProductcode() + item.getStock().getStockno());
//        helper.setText(R.id.name_tv,item.getStock().getProduct().getName());
//        helper.setText(R.id.weight_tv,item.getStock().getWeight() + "g");
//        Log.e("glide","item.getWeight():" + item.getStock().getWeight());
//        helper.setText(R.id.origin_price_tv,"原价： " + item.getOriginprice() + "元");
//        String discountName = item.getDiscount();
//        helper.setText(R.id.discount_tv,"折扣： " + discountName);
//        helper.setText(R.id.discount_price_tv,"折后价： "+ item.getPrice() + "元");

//
    }
}
