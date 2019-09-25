package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.adapters.TablePrintGoodsAdapter;
import com.pos.priory.beans.DayReportBean;
import com.pos.priory.fragments.DatasFragment;
import com.pos.priory.fragments.InventoryFragment;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.fragments.QueryFragment;
import com.pos.priory.fragments.RepertoryFragment;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.DateUtils;
import com.pos.priory.utils.UpgradeUtils;
import com.pos.zxinglib.MipcaActivityCapture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    OrderFragment orderFragment;
    QueryFragment queryFragment;
    RepertoryFragment repertoryFragment;
    InventoryFragment inventoryFragment;
    DatasFragment datasFragment;


    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.setting_img)
    ImageView settingImg;
    @Bind(R.id.scan_img)
    ImageView scanImg;
    @Bind(R.id.container_layout)
    FrameLayout containerLayout;
    @Bind(R.id.navigation)
    BottomNavigationBar navigation;
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.edt_search)
    public EditText edtSearch;
    @Bind(R.id.repertory_search_layout)
    LinearLayout repertorySearchLayout;
    @Bind(R.id.btn_clear)
    ImageView btnClear;
    @Bind(R.id.btn_select)
    MaterialButton btnSelect;
    @Bind(R.id.repertory_search_card)
    CardView repertorySearchCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppVersionFromServer();
        getStoreList();
    }

    private void getStoreList() {
        RetrofitManager.createString(ApiService.class).getAppStoreList()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);
                        if(jsonArray.length() != 0){
                            MyApplication.getContext().storeListJsonString = s;
                            for(int i = 0 ; i < jsonArray.length() ;i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("name").equals(MyApplication.getContext().staffInfoBean.getStore())){
                                    MyApplication.getContext().storeName = jsonObject.getString("name");
                                    MyApplication.getContext().storeAddress = jsonObject.getString("address");
                                    MyApplication.getContext().storeTel = jsonObject.getString("tel");
                                    MyApplication.getContext().region = jsonObject.getString("region");
                                }
                            }
                        }

                    }
                });
    }

    private void getAppVersionFromServer() {
        RetrofitManager.createString(ApiService.class).getAppVersionCode()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.getInt("version");
                        String url = jsonObject.getString("url");
                        UpgradeUtils.checkToUpdate(MainActivity.this,code,url);
                    }
                });
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    protected void initViews() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnClear.setVisibility(charSequence.toString().equals("") ? View.INVISIBLE : View.VISIBLE);
                if (repertoryFragment != null)
                    repertoryFragment.refreshRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        navigation.setMode(BottomNavigationBar.MODE_FIXED);
        navigation.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navigation.setInActiveColor(R.color.blue_text);
        navigation.addItem(new BottomNavigationItem(R.drawable.tab_order, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.tab_query, "查单"))
                .addItem(new BottomNavigationItem(R.drawable.tab_repertory, "仓库"))
                .addItem(new BottomNavigationItem(R.drawable.tab_inventory, "盘点"))
                .addItem(new BottomNavigationItem(R.drawable.data, "数据"))
                .setFirstSelectedPosition(0)
                .initialise();
        navigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        titleTv.setText("订 单");
                        settingImg.setVisibility(View.VISIBLE);
                        scanImg.setVisibility(View.GONE);
                        repertorySearchLayout.setVisibility(View.GONE);
                        if (orderFragment == null) {
                            orderFragment = new OrderFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, orderFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(orderFragment).commit();
                        }
                        if (queryFragment != null && !queryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(queryFragment).commit();
                        if (repertoryFragment != null && !repertoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(repertoryFragment).commit();
                        if (inventoryFragment != null && !inventoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(inventoryFragment).commit();
                        if (datasFragment != null && !datasFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(datasFragment).commit();
                        break;
                    case 1:
                        titleTv.setText("查 单");
                        settingImg.setVisibility(View.GONE);
                        scanImg.setVisibility(View.GONE);
                        repertorySearchLayout.setVisibility(View.GONE);
                        if (queryFragment == null) {
                            queryFragment = new QueryFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, queryFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(queryFragment).commit();
                        }
                        if (orderFragment != null && !orderFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(orderFragment).commit();
                        if (repertoryFragment != null && !repertoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(repertoryFragment).commit();
                        if (inventoryFragment != null && !inventoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(inventoryFragment).commit();
                        if (datasFragment != null && !datasFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(datasFragment).commit();
//                        queryFragment.showKeyBord();
                        break;
                    case 2:
                        titleTv.setText("仓 库");
                        settingImg.setVisibility(View.GONE);
                        scanImg.setVisibility(View.VISIBLE);
                        repertorySearchLayout.setVisibility(View.VISIBLE);
                        if (repertoryFragment == null) {
                            repertoryFragment = new RepertoryFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, repertoryFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(repertoryFragment).commit();
                        }
                        if (queryFragment != null && !queryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(queryFragment).commit();
                        if (orderFragment != null && !orderFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(orderFragment).commit();
                        if (inventoryFragment != null && !inventoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(inventoryFragment).commit();
                        if (datasFragment != null && !datasFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(datasFragment).commit();
                        break;
                    case 3:
                        titleTv.setText("盘 点");
                        settingImg.setVisibility(View.GONE);
                        scanImg.setVisibility(View.GONE);
                        repertorySearchLayout.setVisibility(View.GONE);
                        if (inventoryFragment == null) {
                            inventoryFragment = new InventoryFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, inventoryFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(inventoryFragment).commit();
                        }
                        if (queryFragment != null && !queryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(queryFragment).commit();
                        if (repertoryFragment != null && !repertoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(repertoryFragment).commit();
                        if (orderFragment != null && !orderFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(orderFragment).commit();
                        if (datasFragment != null && !datasFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(datasFragment).commit();
                        break;
                    case 4:
                        titleTv.setText("数 据");
                        settingImg.setVisibility(View.GONE);
                        scanImg.setVisibility(View.GONE);
                        repertorySearchLayout.setVisibility(View.GONE);
                        if (datasFragment == null) {
                            datasFragment = new DatasFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, datasFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(datasFragment).commit();
                        }
                        if (queryFragment != null && !queryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(queryFragment).commit();
                        if (repertoryFragment != null && !repertoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(repertoryFragment).commit();
                        if (orderFragment != null && !orderFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(orderFragment).commit();
                        if (inventoryFragment != null && !inventoryFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(inventoryFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        navigation.selectTab(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @OnClick({R.id.setting_img, R.id.scan_img, R.id.btn_clear, R.id.btn_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_img:
                showMainMenu();
                break;
            case R.id.scan_img:
                startActivityForResult(new Intent(MainActivity.this, MipcaActivityCapture.class), 1000);
                break;
            case R.id.btn_clear:
                edtSearch.setText("");
                break;
            case R.id.btn_select:
                showRepertoryMenu();
                break;
        }
    }

    private void showRepertoryMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnSelect);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.repertory_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        btnSelect.setText(item.getTitle());
                        repertoryFragment.onChangeRepertoryListener(true);
                        repertorySearchCard.setVisibility(View.VISIBLE);
                        scanImg.setVisibility(View.VISIBLE);
                        break;
                    case R.id.menu1:
                        btnSelect.setText(item.getTitle());
                        repertoryFragment.onChangeRepertoryListener(false);
                        repertorySearchCard.setVisibility(View.INVISIBLE);
                        scanImg.setVisibility(View.INVISIBLE);
                        break;
                }
                return true;
            }
        });
    }

    private void showMainMenu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, settingImg);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        getDayReport();
                        break;
                    case R.id.menu1:
                        startActivity(new Intent(MainActivity.this, EditPasswordActivity.class));
                        break;
                    case R.id.menu2:
                        ColseActivityUtils.closeAllAcitivty();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    public void getDayReport(){
        RetrofitManager.createString(ApiService.class).getDayReport()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("test","result:" + s);
                        DayReportBean dayReportBean = gson.fromJson(s,DayReportBean.class);
                        printGoldTable(dayReportBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test","throwable:" + gson.toJson(throwable));
                        Toast.makeText(MainActivity.this,"查不到日报表数据",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void printGoldTable(DayReportBean dayReportBean) {
        Log.e("test", "printGoldTable");
        List<View> views = new ArrayList<>();
        List<DayReportBean.ItemsBean> templist = new ArrayList<>();
        templist.addAll(dayReportBean.getItems());
        if(dayReportBean.getRefunditem().size() != 0){
            DayReportBean.ItemsBean returnTitleBean = new DayReportBean.ItemsBean();
            returnTitleBean.setProductname("退货标题");
            templist.add(returnTitleBean);
            for(DayReportBean.RefunditemBean refunditemBean : dayReportBean.getRefunditem()){
                DayReportBean.ItemsBean itemsBean = new DayReportBean.ItemsBean();
                itemsBean.setProductname(refunditemBean.getProductname());
                itemsBean.setCatalog(refunditemBean.getCatalog());
                itemsBean.setDiscount(refunditemBean.getDiscount());
                itemsBean.setDiscountprice(refunditemBean.getDiscountprice());
                itemsBean.setQuantity(refunditemBean.getQuantity());
                itemsBean.setPrice(refunditemBean.getPrice());
                itemsBean.setStock(refunditemBean.getStock());
                itemsBean.setWeight(refunditemBean.getWeight());
                itemsBean.setReturnItem(true);
                templist.add(itemsBean);
            }
        }

        int perPageSize = 18;
        int size = templist.size() / perPageSize;
        int a = templist.size() % perPageSize;
        if (a != 0) {
            size++;
        }
        Log.e("test", "size:" + size + " a:" + a + "templist.size():" + templist.size());
        for (int i = 0; i < size; i++) {
            List<DayReportBean.ItemsBean> extraList = new ArrayList<>();
            if (i == (size - 1)) {
                if(a == 0){
                    a = perPageSize;
                }
                for (int t = 0; t < a; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }

            } else {
                for (int t = 0; t < perPageSize; t++) {
                    extraList.add(templist.get(t + perPageSize * i));
                }
            }
            Log.e("test", "MyApplication.getContext().region:" + MyApplication.getContext().region);
            int layoutid = 0;
            if (MyApplication.getContext().region.equals("中国大陆")) {
                layoutid = R.layout.gold_daliy_table;
            } else {
                layoutid = R.layout.gold_daliy_table2;
            }
            final View printView = LayoutInflater.from(MainActivity.this).inflate(layoutid, null);
            if(extraList.size() != 0 && !extraList.get(0).isReturnItem()){
                printView.findViewById(R.id.list_title_layout0).setVisibility(View.VISIBLE);
                printView.findViewById(R.id.list_title_layout1).setVisibility(View.GONE);
            }else {
                printView.findViewById(R.id.list_title_layout0).setVisibility(View.GONE);
                printView.findViewById(R.id.list_title_layout1).setVisibility(View.VISIBLE);
            }
            ((TextView) printView.findViewById(R.id.store_tv)).setText(MyApplication.staffInfoBean.getStore());
            ((TextView) printView.findViewById(R.id.date_tv)).setText(DateUtils.getDateOfToday());
            ((TextView) printView.findViewById(R.id.page_tv)).setText((i + 1) + "/" + size);
            ((TextView) printView.findViewById(R.id.sum_pay_tv)).setText(dayReportBean.getTurnovertotal() + "");
            ((TextView) printView.findViewById(R.id.cash_tv)).setText(dayReportBean.getCash() + "");
            ((TextView) printView.findViewById(R.id.coupon_tv)).setText(dayReportBean.getVoucher() + "");
            ((TextView) printView.findViewById(R.id.card_pay_tv)).setText(dayReportBean.getCredit() + "");
            ((TextView) printView.findViewById(R.id.ali_pay_tv)).setText(dayReportBean.getAlipay() + "");
            ((TextView) printView.findViewById(R.id.wechat_pay_tv)).setText(dayReportBean.getWechatpay() + "");
            ((TextView) printView.findViewById(R.id.return_mount_tv)).setText(dayReportBean.getRefund() + "");
            ((TextView) printView.findViewById(R.id.gold_amount_tv)).setText(dayReportBean.getGoldturnover() + "");
            ((TextView) printView.findViewById(R.id.gold_count_tv)).setText(dayReportBean.getGolditemcount() + "");
            ((TextView) printView.findViewById(R.id.gold_weight_tv)).setText(dayReportBean.getGolditemweight() + "g");
            ((TextView) printView.findViewById(R.id.spar_amount_tv)).setText(dayReportBean.getCystalturnover() + "");
            ((TextView) printView.findViewById(R.id.spar_count_tv)).setText(dayReportBean.getCystalitemcount() + "");
            ((TextView) printView.findViewById(R.id.hand_amount_tv)).setText(dayReportBean.getBraceletturnover() + "");
            ((TextView) printView.findViewById(R.id.hand_count_tv)).setText(dayReportBean.getBraceletitemcount() + "");

            RecyclerView listview = (RecyclerView) printView.findViewById(R.id.good_list);
            TablePrintGoodsAdapter adapter = new TablePrintGoodsAdapter(R.layout.gold_table_print_good_list_item, extraList,
                    i,dayReportBean.getItems().size(),dayReportBean.getRefunditem().size(),perPageSize);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            listview.setLayoutManager(mLayoutManager);
            listview.setAdapter(adapter);
            views.add(printView);
        }
        Log.e("test", "viewsize:" + views.size());
        BillActivity.print(this, views);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                String str = data.getStringExtra("resultString");
                Log.e("result", "result:" + str);
                edtSearch.setText(str);
                break;
        }
    }
}
