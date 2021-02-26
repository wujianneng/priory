package com.pos.priory.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.coustomViews.LastInputEditText;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class OrderDetialGoodsAdapter extends BaseMultiItemQuickAdapter<OrderDetailReslutBean.OrderItemsBean, BaseViewHolder> {
    public int operatingStatus = 0;
    Context context;


    public OrderDetialGoodsAdapter(List<OrderDetailReslutBean.OrderItemsBean> data, Context context) {
        super(data);
        this.context = context;
        addItemType(0, R.layout.order_detial_good_list_item);
        addItemType(1, R.layout.order_detial_fitting_list_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderDetailReslutBean.OrderItemsBean item) {
        Log.e("test", "operatingStatus:" + operatingStatus);
        switch (helper.getItemViewType()) {
            case 0:
                if (item.isSelected()) {
                    helper.setChecked(R.id.radio,true);
                } else {
                    helper.setChecked(R.id.radio,false);
                }
                helper.addOnClickListener(R.id.radio);
                helper.setVisible(R.id.radio, operatingStatus != 0 ? item.getItem_status().equals("正常") : false);
                helper.setGone(R.id.input_layout, operatingStatus != 0 && item.isSelected() ? true : false);
                final LastInputEditText edtWeight = helper.getView(R.id.edt_weight);
                edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
                edtWeight.setText(item.getWeight() + "");
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
                                    String str = s.toString();
                                    item.setWeight(TextUtils.isEmpty(str) ? 0 : Double.parseDouble(str));
                                    Log.e("itemweight", "itemweight:" + item.getWeight());
                                    ((OrderDetialActivity) context).resetCheckedGoodList();
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            };
                            edtWeight.setTag(textWatcher);
                            edtWeight.addTextChangedListener(textWatcher);
                        } else {
                            edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
                            edtWeight.setText(item.getWeight() + "");
                        }
                    }

                });
                Glide.with(mContext).load(item.getPrd_image())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into((ImageView) helper.getView(R.id.icon_good));
                helper.setText(R.id.code_tv,item.getNumber());
                helper.setText(R.id.name_tv,item.getPrd_name());
                helper.setText(R.id.status_tv,"狀態：" + item.getItem_status());
                helper.setText(R.id.weight_tv,item.getWeight() + "克");
                Log.e("glide","item.getWeight():" + item.getWeight());
                helper.setText(R.id.origin_price_tv,"原价： " + item.getOriginprice() + "元");
                helper.setText(R.id.discount_price_tv,"折后价： "+ item.getPrice() + "元");
                break;
            case 1:
                helper.setVisible(R.id.radio, false);
                Glide.with(mContext).load(item.getPrd_image())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into((ImageView) helper.getView(R.id.icon_good));
                helper.setText(R.id.name_tv,item.getPrd_name());
                helper.setText(R.id.price_tv,"單價： " + item.getPrice() + "元");
                helper.setText(R.id.sum_tv,"小計： " + item.getOriginprice() * item.getQuantity() + "元");
                Log.e("glide","item.getWeight():" + item.getWeight());
                helper.setText(R.id.sale_tv,"售價： " + item.getOriginprice() + "元");
                helper.setText(R.id.count_tv,"x" + item.getQuantity());
                break;
        }



//
    }
}
