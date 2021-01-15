package com.pos.priory.adapters;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DayReportBean;
import com.pos.priory.beans.DayReportDataBean;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.utils.LogicUtils;

import java.util.List;

/**
 * Created by Lenovo on 2019/1/8.
 */

public class TablePrintGoodsAdapter extends BaseMultiItemQuickAdapter<DayReportDataBean,BaseViewHolder> {
    double return_goldprice = 0;

    public TablePrintGoodsAdapter(List<DayReportDataBean> data,double return_goldprice) {
        super(data);
        this.return_goldprice = return_goldprice;
        addItemType(0,R.layout.gold_table_print_good_list_title_item);
        addItemType(1,R.layout.gold_table_print_good_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, DayReportDataBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.title_tv,item.getTitle());
                helper.setText(R.id.money_tv,"金額 " + item.getAmount_total());
                helper.setText(R.id.count_tv,"數量 " + item.getQuantity_total());
                helper.setText(R.id.gold_price_tv,"回收金價（元/克） " + return_goldprice);
                helper.setGone(R.id.gold_price_tv,item.getTitle().equals("回收"));
                break;
            case 1:
                if(item.getItem().size() != 0) {
                    helper.setText(R.id.good_name_tv, item.getItem().get(0).getProduct_name());
                    helper.setText(R.id.good_code_tv, item.getItem().get(0).getCode());
                    helper.setText(R.id.weight_tv, item.getItem().get(0).getWeight() + "g");
                    helper.setText(R.id.good_count_tv, item.getItem().get(0).getQuantity() + "件");
                }
                break;
        }
    }
}
