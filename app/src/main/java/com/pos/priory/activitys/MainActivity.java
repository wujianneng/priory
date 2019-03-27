package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.MyApplication;
import com.pos.priory.R;
import com.pos.priory.beans.StaffInfoBean;
import com.pos.priory.fragments.DatasFragment;
import com.pos.priory.fragments.InventoryFragment;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.fragments.QueryFragment;
import com.pos.priory.fragments.RepertoryFragment;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.Constants;
import com.pos.zxinglib.MipcaActivityCapture;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.padding_laout)
    View paddingLaout;
    @Bind(R.id.edt_search)
    public EditText edtSearch;
    @Bind(R.id.repertory_search_layout)
    FrameLayout repertorySearchLayout;
    @Bind(R.id.btn_clear)
    ImageView btnClear;


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

        List<StaffInfoBean> staffInfoBeanList = gson.fromJson(sharedPreferences.getString(Constants.CURRENT_STAFF_INFO_KEY,""),
                new TypeToken<List<StaffInfoBean>>(){}.getType());
        MyApplication.staffInfoBean = staffInfoBeanList.get(0);

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

        navigation.addItem(new BottomNavigationItem(R.drawable.tab_order, "訂單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_query, "查單"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_repertory, "倉庫"))
                    .addItem(new BottomNavigationItem(R.drawable.tab_inventory, "盤點"))
                    .addItem(new BottomNavigationItem(R.drawable.data, "數據"))
                    .setFirstSelectedPosition(0)
                    .initialise();
        navigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        titleTv.setText("訂 單");
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
                        titleTv.setText("查 單");
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
                        titleTv.setText("倉 庫");
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
                        titleTv.setText("盤 點");
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
                        titleTv.setText("數 據");
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

    @OnClick({R.id.setting_img, R.id.scan_img, R.id.btn_clear})
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
        }
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
                switch (item.getItemId()){
                    case R.id.menu0:
                        break;
                    case R.id.menu1:
                        break;
                    case R.id.menu2:
                        startActivity(new Intent(MainActivity.this,EditPasswordActivity.class));
                        break;
                    case R.id.menu3:
                        ColseActivityUtils.closeAllAcitivty();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        break;
                }
                return true;
            }
        });
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
