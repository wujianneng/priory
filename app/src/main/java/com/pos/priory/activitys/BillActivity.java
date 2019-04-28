package com.pos.priory.activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
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
import com.pos.priory.beans.GoodBean;
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
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_print)
    CardView btnPrint;

    List<GoodBean> goodList;
    BillGoodsAdapter goodsAdapter;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.create_date_tv)
    TextView createDateTv;
    @Bind(R.id.right_img)
    ImageView rightImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("賬單");
        rightImg.setVisibility(View.GONE);
        orderNumberTv.setText(getIntent().getStringExtra("ordernumber"));
        createDateTv.setText(DateUtils.getCurrentTime());
        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("sumMoney", 0)));
        edtCashMoney.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("receiveMoney", 0)));
        smallChangeTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(getIntent().getDoubleExtra("returnMoney", 0)));
        goodList = gson.fromJson(getIntent().getStringExtra("goodlist"), new TypeToken<List<GoodBean>>() {
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
                printViews(this, goodList, orderNumberTv.getText().toString(), getIntent().getStringExtra("memberName"),
                        createDateTv.getText().toString(), getIntent().getDoubleExtra("sumMoney", 0), MyApplication.staffInfoBean.getStoreid());
                break;
            case R.id.back_btn:
                onBackPressed();
                finish();
                break;
        }
    }


    public static void printViews(final Activity activity, List<GoodBean> goodList, String orderNumber,
                                                String memberName, String createDate, double sumMoney, int storeid) {
        List<View> views = new ArrayList<>();
        List<GoodBean> templist = new ArrayList<>();
        templist.addAll(goodList);
        int perPageSize = 8;
        int size = templist.size() / perPageSize;
        int a = templist.size() % perPageSize;
        if (a != 0) {
            size++;
        }
        Log.e("test", "size:" + size + " a:" + a);
        for (int i = 0; i < size; i++) {
            List<GoodBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                for (int t = 0; t < a; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            }
            final View printView = LayoutInflater.from(activity).inflate(R.layout.dialog_preview, null);
            ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
            ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(memberName);
            ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);
            ((TextView) printView.findViewById(R.id.good_size_tv)).setText(templist.size() + "");
            ((TextView) printView.findViewById(R.id.sum_money_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            BillPrintGoodsAdapter adapter = new BillPrintGoodsAdapter(R.layout.bill_print_good_list_item, extraList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
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
        for(int i = 0 ; i < views.size() ; i++){
            View view = views.get(i);
            Bitmap bitmap = BitmapUtils.loadBitmapFromViewBySystem(view, DeviceUtil.dip2px(activity, 444), DeviceUtil.dip2px(activity, 630));
            bitmaps.add(bitmap);
        }
        myPrintHelper.printBitmap("jpgTestPrint",bitmaps);

    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(OrderFragment.UPDATE_ORDER_LIST);
        EventBus.getDefault().post(MemberInfoActivity.UPDATE_ORDER_LIST);
        super.onBackPressed();
    }
}
