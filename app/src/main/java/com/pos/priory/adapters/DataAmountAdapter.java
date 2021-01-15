package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.DataAmountBean;

import java.util.List;

public class DataAmountAdapter extends BaseQuickAdapter<DataAmountBean.DataBean, BaseViewHolder> {

    public String currency = "MOP";
    Context context;

    public DataAmountAdapter(int layoutResId, @Nullable List<DataAmountBean.DataBean> data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataAmountBean.DataBean item) {
        helper.setText(R.id.date_tv, item.getDate());
        double sum = 0;
        for (DataAmountBean.DataBean.PaymentBean paymentBean : item.getPayment()) {
            sum += paymentBean.getAmount();
        }
        helper.setText(R.id.amount_tv, sum + currency);
        helper.setImageResource(R.id.right_img, item.isExpand() ? android.R.drawable.arrow_up_float : R.drawable.icon_right);

        helper.setGone(R.id.product_title_tv,item.isExpand());
        helper.setGone(R.id.product_listview,item.isExpand());
        helper.setGone(R.id.paytype_title_tv,item.isExpand());
        helper.setGone(R.id.paytype_listview,item.isExpand());
        helper.setGone(R.id.day_sum_btn,item.isExpand());

        helper.addOnClickListener(R.id.day_sum_btn);

        ListView listView = (ListView) helper.getView(R.id.product_listview);
        ProductAdapter productAdapter = new ProductAdapter(item.getItem());
        listView.setAdapter(productAdapter);
        setListViewHeightBasedOnChildren(listView);

        ListView paytypelistView = (ListView) helper.getView(R.id.paytype_listview);
        PayTypeAdapter payTypeAdapter = new PayTypeAdapter(item.getPayment());
        paytypelistView.setAdapter(payTypeAdapter);
        setListViewHeightBasedOnChildren(paytypelistView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class ProductAdapter extends BaseAdapter {

        List<DataAmountBean.DataBean.ItemBean> subtitles;

        public ProductAdapter(List<DataAmountBean.DataBean.ItemBean> subTitleList) {
            subtitles = subTitleList;
        }

        @Override
        public int getCount() {
            return subtitles.size();
        }

        @Override
        public Object getItem(int position) {
            return subtitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sale_amount_data_detail_item, null);
            TextView nametv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView weighttv = (TextView) convertView.findViewById(R.id.weight_tv);
            TextView counttv = (TextView) convertView.findViewById(R.id.count_tv);
            TextView amounttv = (TextView) convertView.findViewById(R.id.amount_tv);
            nametv.setText(subtitles.get(position).getCategory());
            weighttv.setText(subtitles.get(position).getWeight() + "g");
            counttv.setText(subtitles.get(position).getQuanity() + "件");
            amounttv.setText(subtitles.get(position).getTurnover() + "元");
            return convertView;
        }
    }

    private class PayTypeAdapter extends BaseAdapter {

        List<DataAmountBean.DataBean.PaymentBean> subtitles;

        public PayTypeAdapter(List<DataAmountBean.DataBean.PaymentBean> subTitleList) {
            subtitles = subTitleList;
        }

        @Override
        public int getCount() {
            return subtitles.size();
        }

        @Override
        public Object getItem(int position) {
            return subtitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sale_amount_data_detail_item, null);
            TextView nametv = (TextView) convertView.findViewById(R.id.name_tv);

            TextView amounttv = (TextView) convertView.findViewById(R.id.amount_tv);
            nametv.setText(subtitles.get(position).getMethod());
            amounttv.setText(subtitles.get(position).getAmount() + "元");
            return convertView;
        }
    }
}
