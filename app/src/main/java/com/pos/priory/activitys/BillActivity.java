package com.pos.priory.activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
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
import com.pos.zxinglib.utils.PermissionsManager;
import com.pos.zxinglib.utils.PermissionsResultAction;

import org.greenrobot.eventbus.EventBus;

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
                previewDialog = showPreviewDialog(this, goodList, orderNumberTv.getText().toString(), getIntent().getStringExtra("memberName"),
                        createDateTv.getText().toString(), getIntent().getDoubleExtra("sumMoney", 0), MyApplication.staffInfoBean.getStoreid());
                break;
            case R.id.back_btn:
                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (previewDialog != null)
            previewDialog.dismiss();
    }

    AlertDialog previewDialog;

    public static AlertDialog showPreviewDialog(final Activity activity, List<GoodBean> goodList, String orderNumber,
                                                String memberName, String createDate, double sumMoney, int storeid) {
        final View printView = LayoutInflater.from(activity).inflate(R.layout.dialog_preview, null);
        ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumber);
        ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(memberName);
        ((TextView) printView.findViewById(R.id.date_tv)).setText(createDate);
        ((TextView) printView.findViewById(R.id.good_size_tv)).setText("共" + goodList.size() + "件");
        ((TextView) printView.findViewById(R.id.sum_money_tv)).setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        if (storeid == 4) {
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.GONE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.VISIBLE);
        } else {
            printView.findViewById(R.id.maco_store_info_layout).setVisibility(View.VISIBLE);
            printView.findViewById(R.id.zhuhai_store_info_layout).setVisibility(View.GONE);
        }
        RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
        BillPrintGoodsAdapter adapter = new BillPrintGoodsAdapter(R.layout.bill_print_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        listview.setLayoutManager(mLayoutManager);
        listview.setAdapter(adapter);
        final AlertDialog previewDialog = new AlertDialog.Builder(activity).setView(printView).setCancelable(false).create();
        previewDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                print(activity, printView);
            }
        }, 1000);
        return previewDialog;
    }


    public static void print(final Activity activity, View view) {
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
        //实例化类
        PrintHelper photoPrinter = new PrintHelper(activity);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);//设置填充的类型，填充的类型指的是在A4纸上打印时的填充类型，两种模式

        //打印
        Bitmap bitmap = BitmapUtils.loadBitmapFromViewBySystem(view);
        photoPrinter.printBitmap("jpgTestPrint", bitmap);//这里的第一个参数是打印的jobName
//        A5 - 148x210

//        // Get a PrintManager instance
//        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
//        // Set job name, which will be displayed in the print queue
//        String jobName = getString(R.string.app_name) + " Document";
//
//        // Start a print job, passing in a PrintDocumentAdapter implementation
//        // to handle the generation of a print document
//        printManager.print(jobName, new MyPrintDocumentAdapter(this),
//                null); //
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(OrderFragment.UPDATE_ORDER_LIST);
        EventBus.getDefault().post(MemberInfoActivity.UPDATE_ORDER_LIST);
        super.onBackPressed();
    }
}
