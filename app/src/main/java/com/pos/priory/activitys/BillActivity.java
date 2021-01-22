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
import com.infitack.rxretorfit2library.RetrofitManager;
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
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.networks.ApiService;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    BillGoodsAdapter goodsAdapter;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.create_date_tv)
    TextView createDateTv;
    @Bind(R.id.right_img)
    ImageView rightImg;

    OrderDetailReslutBean orderDetailReslutBean;
    List<OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean> paytypeList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        getOrderBean(getIntent().getIntExtra("orderId",0));
    }

    protected void initViews() {
        titleTv.setText("账单");
        rightImg.setVisibility(View.GONE);
        orderNumberTv.setText(orderDetailReslutBean.getOrderno());
        createDateTv.setText(orderDetailReslutBean.getCreated());
        moneyTv.setText(orderDetailReslutBean.getAmount_payable() + "元");
        edtCashMoney.setText(orderDetailReslutBean.getAmount_paid() + "元");
        double smallChange = orderDetailReslutBean.getAmount_paid() - orderDetailReslutBean.getAmount_payable();
        smallChangeTv.setText((smallChange < 0 ? 0 : smallChange) + "元");

        goodsAdapter = new BillGoodsAdapter(this, R.layout.bill_good_list_item, orderDetailReslutBean.getOrder_items());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goodRecyclerView.setLayoutManager(mLayoutManager);
        goodRecyclerView.setAdapter(goodsAdapter);
    }

    private void getOrderBean(int orderid) {
        if (customDialog == null) {
            customDialog = new CustomDialog(this, "正在查询订单信息..");
            customDialog.setOnDismissListener((dialogInterface) -> customDialog = null);
            customDialog.show();
            RetrofitManager.createGson(ApiService.class).getOrderByOrderId(orderid)
                    .compose(this.<OrderDetailReslutBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resluts -> {
                        customDialog.dismiss();
                        orderDetailReslutBean = resluts;
                        paytypeList.addAll(orderDetailReslutBean.getPay_detail().getPay_methods().getCash_coupon());
                        for (OrderDetailReslutBean.PayDetailBean.PayMethodsBean.OtherBean otherBean : orderDetailReslutBean.getPay_detail().getPay_methods().getOther()) {
                            OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean cashCouponBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                            cashCouponBean.setAmount(otherBean.getAmount());
                            cashCouponBean.setPaymethod(otherBean.getPaymethod());
                            paytypeList.add(cashCouponBean);
                        }
                        for (OrderDetailReslutBean.PayDetailBean.PayMethodsBean.ExchangeOrRefundBean exchangeOrRefundBean : orderDetailReslutBean.getPay_detail().getPay_methods().getExchange_or_refund()) {
                            OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean cashCouponBean = new OrderDetailReslutBean.PayDetailBean.PayMethodsBean.CashCouponBean();
                            cashCouponBean.setAmount(exchangeOrRefundBean.getAmount());
                            paytypeList.add(cashCouponBean);
                        }
                        initViews();

                    }, throwable -> {
                        customDialog.dismiss();
                    });
        }

    }

    @OnClick({R.id.btn_print, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_print:
                OrderDetialActivity.printViews(BillActivity.this,orderDetailReslutBean,paytypeList);
                break;
            case R.id.back_btn:
                onBackPressed();
                finish();
                break;
        }
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
