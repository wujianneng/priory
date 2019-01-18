package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.PurchasingItemBean;
import com.pos.priory.utils.Constants;

import java.util.List;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DetialListAdapter extends BaseQuickAdapter<PurchasingItemBean,BaseViewHolder> {
    Context mContext;

    public DetialListAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<PurchasingItemBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PurchasingItemBean item) {
        helper.setText(R.id.code_tv,item.getStock().getBatch().getProduct().getProductcode() + item.getStock().getBatch().getBatchno());
        helper.setText(R.id.name_tv,item.getStock().getBatch().getProduct().getName());
        helper.setText(R.id.count_tv,item.getQuantity() + "");
        helper.setText(R.id.status_tv,item.getType().equals("return") ? "退貨" : "訂貨");
        Glide.with(mContext).load(Constants.BASE_URL + item.getStock().getBatch().getProduct().getImage())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .priority(Priority.LOW)
                .into((ImageView) helper.getView(R.id.icon_good));
    }
}
