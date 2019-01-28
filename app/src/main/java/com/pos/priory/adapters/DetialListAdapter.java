package com.pos.priory.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.beans.PurchasingItemBean;
import com.pos.priory.utils.Constants;

import java.util.List;

import javax.sql.DataSource;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class DetialListAdapter extends BaseQuickAdapter<PurchasingItemBean, BaseViewHolder> {
    Context mContext;

    public DetialListAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<PurchasingItemBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, PurchasingItemBean item) {
        helper.setText(R.id.code_tv, item.getStock().getBatch().getProduct().getProductcode() + item.getStock().getBatch().getBatchno());
        helper.setText(R.id.name_tv, item.getStock().getBatch().getProduct().getName());
        helper.setText(R.id.count_tv, item.getQuantity() + "");
        helper.setText(R.id.status_tv, item.getType().equals("return") ? "退貨" : "訂貨");
        helper.setTextColor(R.id.count_tv, item.getType().equals("return") ? ContextCompat.getColor(mContext, R.color.drag_btn_red) :
                ContextCompat.getColor(mContext, R.color.drag_btn_green));
        String imageurl = Constants.BASE_URL_HTTP + item.getStock().getBatch().getProduct().getImage();
        Log.e("imageurl", "imageurl:" + imageurl);
        Glide.with(MyApplication.getContext()).load(imageurl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into((ImageView) helper.getView(R.id.icon_good));
    }

    RequestListener mRequestListener = new RequestListener() {


        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.d("glide", "onException: " + e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e("glide", "model:" + model + " isFirstResource: " + isFirstResource);
            return false;
        }

    };
}
