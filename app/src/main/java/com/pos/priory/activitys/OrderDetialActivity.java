package com.pos.priory.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.OrderDetailDiscountAdapter;
import com.pos.priory.adapters.OrderDetailPayTypeAdapter;
import com.pos.priory.adapters.OrderDetailPrintDiscountAdapter;
import com.pos.priory.adapters.OrderDetailPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetailPrintPayTypeAdapter;
import com.pos.priory.adapters.OrderDetialGoodsAdapter;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.coustomViews.DisableReclyerView;
import com.pos.priory.utils.LogicUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class OrderDetialActivity extends BaseActivity {
    List<OrderBean.ResultsBean> goodList = new ArrayList<>();
    List<OrderBean.ResultsBean> checkedGoodList = new ArrayList<>();
    OrderDetialGoodsAdapter goodsAdapter;
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.sex_img)
    ImageView sexImg;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.need_money_title)
    TextView needMoneyTitle;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.payed_money_title)
    TextView payedMoneyTitle;
    @Bind(R.id.payed_money_tv)
    TextView payedMoneyTv;
    @Bind(R.id.trasition_time_tv)
    TextView trasitionTimeTv;
    @Bind(R.id.update_time_tv)
    TextView updateTimeTv;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.gold_count_tv)
    TextView goldCountTv;
    @Bind(R.id.fitting_count_tv)
    TextView fittingCountTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.next_tv)
    TextView nextTv;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.btn_return)
    MaterialButton btnReturn;
    @Bind(R.id.btn_change)
    MaterialButton btnChange;
    @Bind(R.id.btn_cancel)
    MaterialButton btnCancel;
    @Bind(R.id.action_layout)
    FrameLayout actionLayout;
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    @Bind(R.id.discount_recycler_view)
    DisableReclyerView discountRecyclerView;
    @Bind(R.id.paytype_recycler_view)
    DisableReclyerView paytypeRecyclerView;
    @Bind(R.id.detail_layout)
    LinearLayout detailLayout;
    @Bind(R.id.gold_price_tv)
    TextView goldPriceTv;
    @Bind(R.id.btn_operator)
    TextView btnOperator;
    @Bind(R.id.operating_layout)
    LinearLayout operatingLayout;
    @Bind(R.id.operating_weight_tv)
    TextView operatingWeightTv;
    @Bind(R.id.operating_money_tv)
    TextView operatingMoneyTv;

    List<String> discountList = new ArrayList<>();
    List<String> paytypeList = new ArrayList<>();
    OrderDetailDiscountAdapter discountAdapter;
    OrderDetailPayTypeAdapter payTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon_print);

        goodsAdapter = new OrderDetialGoodsAdapter(goodList,this);
        goodRecyclerView.setLayoutManager(new LinearLayoutManager(OrderDetialActivity.this,LinearLayoutManager.VERTICAL,false));
        goodRecyclerView.setAdapter(goodsAdapter);

        discountList.add("");
        discountList.add("");
        discountAdapter = new OrderDetailDiscountAdapter(R.layout.order_detial_discount_list_item,discountList);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        discountRecyclerView.setAdapter(discountAdapter);

        paytypeList.add("");
        paytypeList.add("");
        payTypeAdapter = new OrderDetailPayTypeAdapter(R.layout.order_detial_paytype_list_item,paytypeList);
        paytypeRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        paytypeRecyclerView.setAdapter(payTypeAdapter);

        int orderid = getIntent().getIntExtra("orderId", 0);
        getOrderBean(orderid);
    }

    CustomDialog customDialog;

    private void getOrderBean(int orderid) {
//        if (customDialog == null) {
//            customDialog = new CustomDialog(this, "正在查询订单信息..");
//            customDialog.setOnDismissListener((dialogInterface) -> customDialog = null);
//            customDialog.show();
//            RetrofitManager.createString(ApiService.class).getOrderByOrderId(orderid)
//                    .compose(this.<String>bindToLifecycle())
//                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(resluts -> {
//                        customDialog.dismiss();
//                    }, throwable -> {
//                        customDialog.dismiss();
//                    });
//        }
        for(int i = 0 ; i < 33 ; i++) {
            OrderBean.ResultsBean itemsBean = new OrderBean.ResultsBean();
            goodList.add(itemsBean);
        }
        goodsAdapter.notifyDataSetChanged();

    }

    public void printViews() {
        List<View> views = new ArrayList<>();
        List<OrderBean.ResultsBean> templist = new ArrayList<>();
        templist.addAll(goodList);
        int perPageSize = 13;
        int lastPageSize = 7;
        int sumSize = templist.size();
        int pageCount = sumSize / perPageSize;
        int remainder = sumSize % perPageSize;//餘數
        if (remainder != 0) {
            if(remainder > lastPageSize){
                pageCount = pageCount + 2;
            }else {
                pageCount++;
            }
        } else {
            if (pageCount == 1) {
                remainder = perPageSize;
            }
        }

        Log.e("test", "pageCount:" + pageCount + " remainder:" + remainder);
        for (int i = 0; i < pageCount; i++) {
            List<OrderBean.ResultsBean> extraList = new ArrayList<>();
            if(remainder > lastPageSize){
                if (i == (pageCount - 1)) {
                   //最後一頁放匯總
                }else if (i == (pageCount - 2)) {
                    for (int t = 0; t < remainder; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                } else {
                    Log.e("test", "i:" + i);
                    for (int t = 0; t < perPageSize; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                }
            }else {
                if (i == (pageCount - 1)) {
                    for (int t = 0; t < remainder; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                } else {
                    for (int t = 0; t < perPageSize; t++) {
                        extraList.add(templist.get(t + perPageSize * i));
                    }
                }
            }
            int layoutid = 0;
//            if (MyApplication.getContext().region.equals("中国大陆")) {
//                layoutid = R.layout.dialog_preview;
//            } else {
                layoutid = R.layout.dialog_preview2;
//            }
            final View printView = LayoutInflater.from(this).inflate(layoutid, null);
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.getContext().staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.address_tv)).setText(MyApplication.getContext().staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.tel_tv)).setText(MyApplication.getContext().staffInfoBean.getMobile());
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + pageCount);
            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            OrderDetailPrintGoodsAdapter adapter = new OrderDetailPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            if(i == pageCount - 1){
                List<String> dclist = new ArrayList<>();
                dclist.add("千足金滿1000元95折");
                dclist.add("關閘店新店開張千足金額外95折");
                dclist.add("95折");
                dclist.add("千足金滿1000元98折");
                dclist.add("關閘店新店開張千足金額外98折");
                dclist.add("98折");
                dclist.add("千足金滿1000元66折");
                dclist.add("關閘店新店開張千足金額外66折");
                dclist.add("66折");
                RecyclerView dcListview = (RecyclerView) printView.findViewById(R.id.discount_list);
                OrderDetailPrintDiscountAdapter dcmLayoutManageradapter = new OrderDetailPrintDiscountAdapter
                        (R.layout.order_detail_print_discount_list_item, dclist);
                LinearLayoutManager dcmLayoutManager = new LinearLayoutManager(this);
                dcmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                dcListview.setLayoutManager(dcmLayoutManager);
                dcListview.setAdapter(dcmLayoutManageradapter);

                List<String> ptlist = new ArrayList<>();
                ptlist.add("現金券 #201287410084");
                ptlist.add("信用卡");
                ptlist.add("現金");
                ptlist.add("支付寶");
                ptlist.add("微信支付");
                RecyclerView ptListview = (RecyclerView) printView.findViewById(R.id.pay_type_list);
                OrderDetailPrintPayTypeAdapter ptmLayoutManageradapter = new OrderDetailPrintPayTypeAdapter
                        (R.layout.order_detail_print_discount_list_item, ptlist);
                LinearLayoutManager ptmLayoutManager = new LinearLayoutManager(this);
                ptmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                ptListview.setLayoutManager(ptmLayoutManager);
                ptListview.setAdapter(ptmLayoutManageradapter);
            }
            printView.findViewById(R.id.sum_data_layout).setVisibility(i == pageCount - 1 ? View.VISIBLE :View.GONE);
            if(remainder > lastPageSize){
                printView.findViewById(R.id.line0).setVisibility(i == pageCount - 1 ? View.GONE :View.VISIBLE);
                printView.findViewById(R.id.title_layout).setVisibility(i == pageCount - 1 ? View.GONE :View.VISIBLE);
                listview.setVisibility(i == pageCount - 1 ? View.GONE :View.VISIBLE);
            }
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        BillActivity.print(this, views);
    }


    @OnClick({R.id.btn_change, R.id.btn_return, R.id.btn_cancel, R.id.back_btn, R.id.right_img, R.id.btn_operator})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_img:
                printViews();
                break;
            case R.id.btn_change:
                goodsAdapter.operatingStatus = 1;
                goodsAdapter.notifyDataSetChanged();
                changeBottomLayout();
//                Intent intent = new Intent(OrderDetialActivity.this, ChangeGoodsActivity.class);
//                intent.putExtra("checkedGoodList", gson.toJson(checkedGoodList));
//                intent.putExtra("memberId", orderBean.getMember().getId());
//                intent.putExtra("memberMobile", orderBean.getMember().getMobile());
//                intent.putExtra("memberReward", orderBean.getMember().getReward());
//                intent.putExtra("memberName", orderBean.getMember().getLast_name() +
//                        orderBean.getMember().getFirst_name());
//                intent.putExtra("ordernumber", orderBean.getOrdernumber());
//                startActivity(intent);
                break;
            case R.id.btn_return:
                goodsAdapter.operatingStatus = 2;
                goodsAdapter.notifyDataSetChanged();
                changeBottomLayout();
//                resetCheckedGoodList(true);
//                if (checkedGoodList.size() == 0) {
//                    Toast.makeText(OrderDetialActivity.this, "请选择退货商品", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.btn_cancel:

                break;
            case R.id.btn_operator:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (goodsAdapter.operatingStatus == 0)
            finish();
        else {
            goodsAdapter.operatingStatus = 0;
            changeBottomLayout();
        }
    }

    private void changeBottomLayout() {
        switch (goodsAdapter.operatingStatus) {
            case 0:
                detailLayout.setVisibility(View.VISIBLE);
                operatingLayout.setVisibility(View.GONE);
                backBtn.setImageResource(R.drawable.icon_back);
                break;
            case 1://change
                detailLayout.setVisibility(View.GONE);
                operatingLayout.setVisibility(View.VISIBLE);
                goldPriceTv.setVisibility(View.GONE);
                operatingMoneyTv.setText("换货金额：" + "$666");
                btnOperator.setText("确认换货");
                backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                break;
            case 2://return
                detailLayout.setVisibility(View.GONE);
                operatingLayout.setVisibility(View.VISIBLE);
                goldPriceTv.setVisibility(View.VISIBLE);
                operatingMoneyTv.setText("回收金额：" + "$666");
                btnOperator.setText("确认回收");
                backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                break;
        }
    }

    private void resetCheckedGoodList(boolean isReturn) {
        checkedGoodList.clear();
//        for (OrderBean.ItemsBean bean : goodList) {
//            if (bean.isSelected()) {
//                if (bean.getStock().getProduct().getCatalog().equals("其他") && isReturn) {
//                    Toast.makeText(OrderDetialActivity.this, "只有黄金类型商品才能进行回收", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                checkedGoodList.add(bean);
//            }
//        }
    }


}
