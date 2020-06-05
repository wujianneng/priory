package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.FragmentTransaction;
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


    @Bind(R.id.container_layout)
    FrameLayout containerLayout;
    @Bind(R.id.navigation)
    BottomNavigationBar navigation;
    @Bind(R.id.container)
    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
//        getAppVersionFromServer();
//        getStoreList();
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
                        if (jsonArray.length() != 0) {
                            MyApplication.getContext().storeListJsonString = s;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.getString("name").equals(MyApplication.getContext().staffInfoBean.getShop())) {

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
                        UpgradeUtils.checkToUpdate(MainActivity.this.bindToLifecycle(),MainActivity.this, code, url);
                    }
                });
    }

    protected void initViews() {
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
                        orderFragment = BaseActivity.handleTabFragments(MainActivity.this,R.id.container_layout,
                                OrderFragment.class,orderFragment,queryFragment,repertoryFragment,inventoryFragment,datasFragment);
                        break;
                    case 1:
                        queryFragment = BaseActivity.handleTabFragments(MainActivity.this,R.id.container_layout,
                                QueryFragment.class,queryFragment,orderFragment,repertoryFragment,inventoryFragment,datasFragment);
                        break;
                    case 2:
                        repertoryFragment = BaseActivity.handleTabFragments(MainActivity.this,R.id.container_layout,
                                RepertoryFragment.class,repertoryFragment,orderFragment,queryFragment,inventoryFragment,datasFragment);
                        break;
                    case 3:
                        inventoryFragment = BaseActivity.handleTabFragments(MainActivity.this,R.id.container_layout,
                                InventoryFragment.class,inventoryFragment,orderFragment,queryFragment,repertoryFragment,datasFragment);
                        break;
                    case 4:
                        datasFragment = BaseActivity.handleTabFragments(MainActivity.this,R.id.container_layout,
                                DatasFragment.class,datasFragment,orderFragment,queryFragment,repertoryFragment,inventoryFragment);
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

}
