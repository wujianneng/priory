package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.beans.OrderBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.fragments.OrderFragment;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class ReturnBalanceActivity extends BaseActivity {
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.index_tv)
    TextView indexTv;
    @Bind(R.id.tip_tv)
    TextView tipTv;
    @Bind(R.id.tip_layout)
    FrameLayout tipLayout;
    @Bind(R.id.edt_cas_money)
    TextView edtCasMoney;
    @Bind(R.id.btn_next)
    MaterialButton btnNext;
    double sumMoney = 0;
    @Bind(R.id.right_img)
    ImageView rightImg;

    String checkedGoodListString;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_return_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("结算");
        sumMoney = -1 * getIntent().getDoubleExtra("sumMoney", 0);
        checkedGoodListString = getIntent().getStringExtra("checkedGoodList");
        edtCasMoney.setText((int) sumMoney + "");
        moneyTv.setText((int) sumMoney + "");
    }

    @OnClick({R.id.btn_next, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                doReturnGoods();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    CustomDialog customDialog;
    private void doReturnGoods() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在结算..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        Map<String, Object> paramMap = new HashMap<>();
        if(checkedGoodListString != null){
            List<OrderBean.ItemsBean> itemsBeanList = gson.fromJson(getIntent().getStringExtra("checkedGoodList"),
                    new TypeToken<List<OrderBean.ItemsBean>>() {
                    }.getType());
            List<Map<String, Object>> returnorderitems = new ArrayList<>();
            for(OrderBean.ItemsBean itemsBean : itemsBeanList){
                Map<String, Object> returnorderitemMap = new HashMap<>();
                returnorderitemMap.put("orderitemid",itemsBean.getId());
                returnorderitemMap.put("weight",itemsBean.getWeight());
                returnorderitems.add(returnorderitemMap);
            }
            paramMap.put("returnorderitems",returnorderitems);
        }
        Log.e("test","params:" + gson.toJson(paramMap));
        RetrofitManager.createString(ApiService.class)
                .returnGoods(RequestBody.create(MediaType.parse("application/json"), gson.toJson(paramMap)))
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        customDialog.dismiss();
                        EventBus.getDefault().post(OrderFragment.UPDATE_ORDER_LIST);
                        EventBus.getDefault().post(MemberInfoActivity.UPDATE_ORDER_LIST);
                        ColseActivityUtils.finishWholeFuntionActivitys();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        customDialog.dismiss();
                        Log.e("test", "throwable:" + throwable.getMessage());
                        Toast.makeText(ReturnBalanceActivity.this, "结算失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
