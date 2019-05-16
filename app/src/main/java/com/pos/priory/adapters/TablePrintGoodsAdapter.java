package com.pos.priory.adapters;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DayReportBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class TablePrintGoodsAdapter extends BaseQuickAdapter<DayReportBean.ItemsBean,BaseViewHolder> {
    public int page = 0,saleCount = 0,returnCount = 0;

    public TablePrintGoodsAdapter(@LayoutRes int layoutResId, @Nullable List<DayReportBean.ItemsBean> data,int page,int saleCount,int returnCount) {
        super(layoutResId, data);
        this.page = page;
        this.saleCount = saleCount;
        this.returnCount = returnCount;

    }

    @Override
    protected void convert(BaseViewHolder helper, DayReportBean.ItemsBean item) {
        if(item.getProductname().equals("退货标题")){
            helper.setBackgroundColor(R.id.layout, Color.parseColor("#cccccc"));
            helper.setText(R.id.index_tv,"No.");
            helper.setText(R.id.good_code_tv,"編號");
            helper.setText(R.id.good_name_tv,"名稱");
            helper.setText(R.id.good_count_tv,"數量");
            helper.setText(R.id.type_tv,"類別");
            helper.setText(R.id.weight_tv,"金重");
            helper.setText(R.id.reatil_price_tv,"售價");
            helper.setText(R.id.discount_tv,"折扣");
            helper.setText(R.id.price_tv,"退換價");
        }else {
            helper.setBackgroundColor(R.id.layout, Color.parseColor("#ffffff"));
            if(!item.isReturnItem()){
                helper.setText(R.id.index_tv, helper.getAdapterPosition() + 1 + page * 15 + "");
            }else {
                helper.setText(R.id.index_tv, helper.getAdapterPosition()  + page * 15 - saleCount + "");
            }
            helper.setText(R.id.good_code_tv,item.getStock());
            helper.setText(R.id.good_name_tv,item.getProductname());
            helper.setText(R.id.good_count_tv,item.getQuantity() + "");
            helper.setText(R.id.type_tv,item.getCatalog());
            helper.setText(R.id.weight_tv,item.getWeight() == 0 ? "" : item.getWeight() + "g");
            helper.setText(R.id.reatil_price_tv,item.getPrice() + "");
            helper.setText(R.id.discount_tv,item.getDiscount());
            helper.setText(R.id.price_tv,item.getDiscountprice() + "");
        }
    }
}
