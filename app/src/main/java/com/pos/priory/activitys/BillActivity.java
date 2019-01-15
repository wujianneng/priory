package com.pos.priory.activitys;

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
import com.pos.priory.R;
import com.pos.priory.adapters.BillGoodsAdapter;
import com.pos.priory.adapters.BillPrintGoodsAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.utils.BitmapUtils;
import com.pos.priory.utils.DateUtils;
import com.pos.zxinglib.utils.PermissionsManager;
import com.pos.zxinglib.utils.PermissionsResultAction;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BillActivity extends BaseActivity {

    @Bind(R.id.padding_laout)
    View paddingLaout;
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
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        orderNumberTv.setText(getIntent().getStringExtra("ordernumber"));
        createDateTv.setText(DateUtils.getCurrentTime());
        moneyTv.setText(getIntent().getDoubleExtra("sumMoney", 0) + "");
        edtCashMoney.setText(getIntent().getDoubleExtra("receiveMoney", 0) + "");
        smallChangeTv.setText(getIntent().getDoubleExtra("returnMoney", 0) + "");
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
                showPreviewDialog();
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
        if(previewDialog != null)
            previewDialog.dismiss();
    }

    AlertDialog previewDialog;
    View printView;

    private void showPreviewDialog() {
        printView = LayoutInflater.from(this).inflate(R.layout.dialog_preview, null);
        ((TextView) printView.findViewById(R.id.order_number_tv)).setText(orderNumberTv.getText().toString());
        ((TextView) printView.findViewById(R.id.buyer_name_tv)).setText(getIntent().getStringExtra("memberName"));
        ((TextView) printView.findViewById(R.id.date_tv)).setText(createDateTv.getText().toString());
        RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
        BillPrintGoodsAdapter adapter = new BillPrintGoodsAdapter(R.layout.bill_print_good_list_item, goodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        listview.setLayoutManager(mLayoutManager);
        listview.setAdapter(adapter);
        previewDialog = new AlertDialog.Builder(this).setView(printView).setCancelable(false).create();
        previewDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                print(printView);
            }
        }, 1000);
    }


    private void print(View view) {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {
                String message = String.format(Locale.getDefault(), getString(com.pos.zxinglib.R.string.message_denied), permission);
                Toast.makeText(BillActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        //实例化类
        PrintHelper photoPrinter = new PrintHelper(this);
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
        sharedPreferences.edit().putBoolean("isRefreshOrderFragment", true).commit();
        super.onBackPressed();
    }
}
