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

        views.add(printView);
        print(activity, views);
    }

    /**
     * 对RecyclerView进行截图
     */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
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
//        //实例化类
//        PrintHelper photoPrinter = new PrintHelper(activity);
//        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);//设置填充的类型，填充的类型指的是在A4纸上打印时的填充类型，两种模式
//
//        //打印
//        Bitmap bitmap = BitmapUtils.loadBitmapFromViewBySystem(view);
//        photoPrinter.printBitmap("jpgTestPrint", bitmap);//这里的第一个参数是打印的jobName
//        A5 - 148x210
        List<Bitmap> bitmaps = new ArrayList<>();
        MyPrintHelper myPrintHelper = new MyPrintHelper(activity);
        for(int i = 0 ; i < views.size() ; i++){
            View view = views.get(i);
            Bitmap bitmap = BitmapUtils.loadBitmapFromViewBySystem(view, DeviceUtil.dip2px(activity, 444), DeviceUtil.dip2px(activity, 630));
            bitmaps.add(bitmap);
        }
        myPrintHelper.printBitmap("jpgTestPrint",bitmaps);

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
