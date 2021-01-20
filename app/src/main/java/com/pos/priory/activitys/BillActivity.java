package com.pos.priory.activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.BillGoodsAdapter;
import com.pos.priory.adapters.BillPrintGoodsAdapter;
import com.pos.priory.adapters.OrderDetailPrintDiscountAdapter;
import com.pos.priory.adapters.OrderDetailPrintPayTypeAdapter;
import com.pos.priory.beans.CouponResultBean;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.beans.OrderDetailReslutBean;
import com.pos.priory.beans.PayTypesResultBean;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.utils.BitmapUtils;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.MyPrintHelper;
import com.pos.zxinglib.utils.DeviceUtil;
import com.pos.zxinglib.utils.PermissionsManager;
import com.pos.zxinglib.utils.PermissionsResultAction;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BillActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_unit_tv)
    TextView moneyUnitTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.good_recycler_view)
    RecyclerView goodRecyclerView;
    @Bind(R.id.edt_cash_money)
    TextView edtCashMoney;
    @Bind(R.id.receive_layout)
    CardView receiveLayout;
    @Bind(R.id.small_change_tv)
    TextView smallChangeTv;
    @Bind(R.id.small_change_layout)
    CardView smallChangeLayout;
    @Bind(R.id.btn_print)
    MaterialButton btnPrint;

    List<FittingBean.ResultsBean> goodList;
    BillGoodsAdapter goodsAdapter;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.create_date_tv)
    TextView createDateTv;
    @Bind(R.id.right_img)
    ImageView rightImg;

    List<PayTypesResultBean.ResultsBean> orderPayTypeList = new ArrayList<>();
    List<CouponResultBean> discountBeans = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        titleTv.setText("账单");
        rightImg.setVisibility(View.GONE);
        discountBeans = gson.fromJson(getIntent().getStringExtra("couponList"),
                new TypeToken<List<CouponResultBean>>() {
                }.getType());
        orderNumberTv.setText(getIntent().getStringExtra("ordernumber"));
        createDateTv.setText(DateUtils.getCurrentTime());
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("sumMoney", 0)));
        edtCashMoney.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("receiveMoney", 0)));
        smallChangeTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("returnMoney", 0)));
        goodList = gson.fromJson(getIntent().getStringExtra("goodlist"), new TypeToken<List<FittingBean.ResultsBean>>() {
        }.getType());
        orderPayTypeList = gson.fromJson(getIntent().getStringExtra("orderPayTypeList"), new TypeToken<List<PayTypesResultBean.ResultsBean>>() {
        }.getType());

        goodsAdapter = new BillGoodsAdapter(this, R.layout.bill_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
    }

    @OnClick({R.id.btn_print, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_print:
                printViews(this, goodList, orderPayTypeList,discountBeans,orderNumberTv.getText().toString(), getIntent().getStringExtra("memberName"),
                        createDateTv.getText().toString(), getIntent().getDoubleExtra("sumMoney", 0), MyApplication.staffInfoBean.getShopid());
                break;
            case R.id.back_btn:
                onBackPressed();
                finish();
                break;
        }
    }


    public static void printViews(final Activity activity, List<FittingBean.ResultsBean> goodList,
                                  List<PayTypesResultBean.ResultsBean> orderPayTypeList,List<CouponResultBean> discountBeans, String orderNumber,
                                  String memberName, String createDate, double sumMoney, int storeid) {
        List<View> views = new ArrayList<>();
        List<FittingBean.ResultsBean> templist = new ArrayList<>();
        templist.addAll(goodList);
        int perPageSize = 8;
        int size = templist.size() / perPageSize;
        int a = templist.size() % perPageSize;
        if (a != 0) {
            size++;
        }else {
            if(size == 1){
                a = perPageSize;
            }
        }

        Log.e("test", "size:" + size + " a:" + a);
        for (int i = 0; i < size; i++) {
            List<FittingBean.ResultsBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                for (int t = 0; t < a; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            }
            int layoutid = 0;
//            if (MyApplication.getContext().staffInfoBean..equals("中国大陆")) {
//                layoutid = R.layout.dialog_preview;
//            } else {
                layoutid = R.layout.dialog_preview2;
//            }
            final View printView = LayoutInflater.from(activity).inflate(layoutid, null);
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.getContext().staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.address_tv)).setText(MyApplication.getContext().staffInfoBean.getShop());
            ((TextView) printView.findViewById(R.id.tel_tv)).setText(MyApplication.getContext().staffInfoBean.getMobile());
            ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
            ((TextView) printView.findViewById(R.id.count_tv)).setText("合計(" + goodList.size() + "件)");
            ((TextView) printView.findViewById(R.id.amount_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney) + "元");
            ((TextView) printView.findViewById(R.id.sum_pay_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney) + "元");
            ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);
            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            BillPrintGoodsAdapter adapter = new BillPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);

            if (i == size - 1) {
                List<OrderDetailReslutBean.PayDetailBean.CouponsBean> couponsBeans = new ArrayList<>();
                for(CouponResultBean bean : discountBeans){
                    OrderDetailReslutBean.PayDetailBean.CouponsBean couponsBean = new OrderDetailReslutBean.PayDetailBean.CouponsBean();
                    couponsBean.setCoupon_amount(Double.parseDouble(bean.getValue()));
                    couponsBean.setName(bean.getName());
                    couponsBeans.add(couponsBean);
                }
                RecyclerView dcListview = (RecyclerView) printView.findViewById(R.id.discount_list);
                OrderDetailPrintDiscountAdapter dcmLayoutManageradapter = new OrderDetailPrintDiscountAdapter
                        (R.layout.order_detail_print_discount_list_item, couponsBeans);
                LinearLayoutManager dcmLayoutManager = new LinearLayoutManager(activity);
                dcmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                dcListview.setLayoutManager(dcmLayoutManager);
                dcListview.setAdapter(dcmLayoutManageradapter);

                List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> payTypeBeans = new ArrayList<>();
                for(PayTypesResultBean.ResultsBean bean : orderPayTypeList){
                    OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean payTypeBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                    payTypeBean.setPaymethod(bean.isCashCoupon() ? "現金券" : bean.getName());
                    payTypeBean.setAmount(bean.getPayAmount());
                    payTypeBean.setCash_coupon_name(bean.getName());
                    payTypeBeans.add(payTypeBean);
                }
                RecyclerView ptListview = (RecyclerView) printView.findViewById(R.id.pay_type_list);
                OrderDetailPrintPayTypeAdapter ptmLayoutManageradapter = new OrderDetailPrintPayTypeAdapter
                        (R.layout.order_detail_print_discount_list_item, payTypeBeans);
                LinearLayoutManager ptmLayoutManager = new LinearLayoutManager(activity);
                ptmLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                ptListview.setLayoutManager(ptmLayoutManager);
                ptListview.setAdapter(ptmLayoutManageradapter);
            }
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        print(activity, views);
    }


    public static void print(final Activity activity, List<View> views) {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(activity, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {
                String message = String.format(Locale.getDefault(), activity.getString(com.pos.zxinglib.R.string.message_denied), permission);
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
//        A5 - 148x210
        List<Bitmap> bitmaps = new ArrayList<>();
        MyPrintHelper myPrintHelper = new MyPrintHelper(activity);
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            Log.e("test", "bitmapsfor:" + i);
            Bitmap bitmap = BitmapUtils.loadBitmapFromViewBySystem(view, DeviceUtil.dip2px(activity, 444), DeviceUtil.dip2px(activity, 630));
            Log.e("test", "bitmapsfora:" + i);
            bitmaps.add(bitmap);
        }
        Log.e("test", "bitmaps:" + bitmaps.size());
        myPrintHelper.printBitmap("jpgTestPrint", bitmaps);

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(OrderFragment.UPDATE_ORDER_LIST);
        EventBus.getDefault().post(MemberInfoActivity.UPDATE_ORDER_LIST);
        super.onBackPressed();
    }
}
