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

    public TablePrintGoodsAdapter(List<DayReportDataBean> data) {
        super(data);
        addItemType(0,R.layout.gold_table_print_good_list_title_item);
        addItemType(1,R.layout.gold_table_print_good_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, DayReportDataBean item) {

    }
}
