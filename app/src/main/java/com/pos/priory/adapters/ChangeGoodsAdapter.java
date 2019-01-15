package com.pos.priory.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.pos.priory.R;
import com.pos.priory.activitys.ChangeGoodsActivity;
import com.pos.priory.activitys.ReturnGoodsActivity;
import com.pos.priory.beans.OrderItemBean;
import com.pos.priory.coustomViews.LastInputEditText;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class ChangeGoodsAdapter extends BaseQuickAdapter<OrderItemBean, BaseViewHolder> {
    Context context;

    public ChangeGoodsAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<OrderItemBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderItemBean item) {
        helper.setText(R.id.code_tv, item.getStock().getBatch().getProduct().getProductcode() + "");
        helper.setText(R.id.name_tv, item.getStock().getBatch().getProduct().getName());
        if (context instanceof ReturnGoodsActivity) {
            if (item.getStock().getBatch().getProduct().getCatalog().equals("黃金")) {
                helper.setText(R.id.price_tv, "$" + item.getStock().getBatch().getProduct().getPrice());
                helper.setGone(R.id.price_tv, true);
                helper.setText(R.id.retial_price_tv, "$" + item.getStock().getBatch().getProduct().getRealPrice() + " x " + item.getOprateCount());
            } else {
                helper.setGone(R.id.price_tv, false);
                helper.setText(R.id.retial_price_tv, "$" + item.getStock().getBatch().getProduct().getRealPrice() + " x " + item.getOprateCount());
            }
        } else if (context instanceof ChangeGoodsActivity) {
            if (item.getStock().getBatch().getProduct().getCatalog().equals("黃金")) {
                helper.setText(R.id.price_tv, "$" + item.getStock().getBatch().getProduct().getPrice());
                helper.setGone(R.id.price_tv, true);
                helper.setText(R.id.retial_price_tv, "$" + item.getStock().getBatch().getProduct().getRealPrice() + " x " + item.getOprateCount());
            } else {
                helper.setText(R.id.retial_price_tv, "$" + item.getStock().getBatch().getProduct().getRealPrice() + " x " + item.getOprateCount());
            }
        }
        Glide.with(context).load(Constants.BASE_URL + item.getStock().getBatch().getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
        ((TextView) helper.getView(R.id.price_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        final LastInputEditText edtWeight = helper.getView(R.id.edt_weight);
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
                            String str = s.toString();
                            item.setWeight(str);
                            if (context instanceof ReturnGoodsActivity) {
                                ((ReturnGoodsActivity) context).resetSumMoney(false);
                                notifyItemChanged(helper.getAdapterPosition());
                            }
                            if (!str.equals("")) {
                                if (item.getReturnStockId() != -1)
                                    editReturnStocks(str, item.getReturnStockId());
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


    Call editCall;

    private void editReturnStocks(String weight, int returnStockId) {
        if (editCall != null)
            editCall.cancel();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("weight", Double.parseDouble(weight));
        editCall = OkHttp3Util.doPatchWithToken(Constants.RETURN_STOCKS_URL + "/" + returnStockId
                        + "/update/", new Gson().toJson(paramMap),
                PreferenceManager.getDefaultSharedPreferences(context), new Okhttp3StringCallback((Activity) context,
                        "editReturnStocks") {
                    @Override
                    public void onSuccess(String results) throws Exception {

                    }

                    @Override
                    public void onFailed(String erromsg) {

                    }
                });
    }

}
