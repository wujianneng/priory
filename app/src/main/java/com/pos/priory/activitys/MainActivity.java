package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.fragments.DetialListFragment;
import com.pos.priory.fragments.InventoryFragment;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.fragments.QueryFragment;
import com.pos.priory.fragments.RepertoryFragment;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.zxinglib.MipcaActivityCapture;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    OrderFragment orderFragment;
    QueryFragment queryFragment;
    RepertoryFragment repertoryFragment;
    InventoryFragment inventoryFragment;
    DetialListFragment detialListFragment;


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
    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.edt_search)
    public EditText edtSearch;
    @Bind(R.id.repertory_search_layout)
    FrameLayout repertorySearchLayout;
    @Bind(R.id.btn_clear)
    ImageView btnClear;

    public List<StaffInfoBean> staffInfoBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }

        staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY,""),
                new TypeToken<List<StaffInfoBean>>(){}.getType());

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
        if(staffInfoBeanList != null && staffInfoBeanList.size() != 0 && staffInfoBeanList.get(0).getPermission().equals("店長")) {
            Log.e("permission","permission:" + staffInfoBeanList.get(0).getPermission());
            navigation.addItem(new BottomNavigationItem(R.drawable.tab_order, "訂單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_query, "查單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_repertory, "倉庫"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_inventory, "盤點"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_detiallist, "清單"))
                    .setFirstSelectedPosition(0)
                    .initialise();
        }else {
            navigation.addItem(new BottomNavigationItem(R.drawable.tab_order, "訂單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_query, "查單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_repertory, "倉庫"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_inventory, "盤點"))
                    .setFirstSelectedPosition(0)
                    .initialise();
        }
        navigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        titleTv.setText("訂單");
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
                        if (detialListFragment != null && !detialListFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(detialListFragment).commit();
                        break;
                    case 1:
                        titleTv.setText("查單");
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
                        if (detialListFragment != null && !detialListFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(detialListFragment).commit();
                        queryFragment.showKeyBord();
                        break;
                    case 2:
                        titleTv.setText("倉庫");
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
                        if (detialListFragment != null && !detialListFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(detialListFragment).commit();
                        LogicUtils.openKeybord(edtSearch,MainActivity.this);
                        break;
                    case 3:
                        titleTv.setText("盤點");
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
                        if (detialListFragment != null && !detialListFragment.isHidden())
                            getSupportFragmentManager().beginTransaction().hide(detialListFragment).commit();
                        break;
                    case 4:
                        titleTv.setText("訂貨清單");
                        settingImg.setVisibility(View.GONE);
                        scanImg.setVisibility(View.GONE);
                        repertorySearchLayout.setVisibility(View.GONE);
                        if (detialListFragment == null) {
                            detialListFragment = new DetialListFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.container_layout, detialListFragment).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().show(detialListFragment).commit();
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

    @OnClick({R.id.setting_img, R.id.scan_img, R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_img:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.scan_img:
                startActivityForResult(new Intent(MainActivity.this, MipcaActivityCapture.class), 1000);
                break;
            case R.id.btn_clear:
                edtSearch.setText("");
                break;
        }
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
