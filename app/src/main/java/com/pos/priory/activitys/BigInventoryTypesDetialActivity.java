package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.networks.ApiService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BigInventoryTypesDetialActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.gold_tv)
    TextView goldTv;
    @Bind(R.id.gold_btn)
    MaterialButton goldBtn;
    @Bind(R.id.gold_layout)
    FrameLayout goldLayout;
    @Bind(R.id.yushi_tv)
    TextView yushiTv;
    @Bind(R.id.yshi_btn)
    MaterialButton yshiBtn;
    @Bind(R.id.yushi_layout)
    FrameLayout yushiLayout;
    @Bind(R.id.jingshi_tv)
    TextView jingshiTv;
    @Bind(R.id.jingshi_btn)
    MaterialButton jingshiBtn;
    @Bind(R.id.jingshi_layout)
    FrameLayout jingshiLayout;
    @Bind(R.id.finish_btn)
    MaterialButton finishBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_inventory_types_detial);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        rightImg.setVisibility(View.GONE);
        titleTv.setText("盘点详情");
        endTimeTv.setVisibility(getIntent().getStringExtra("status").equals("未完成") ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.back_btn,R.id.gold_btn,R.id.yshi_btn,R.id.jingshi_btn,R.id.finish_btn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.gold_btn:
                goTypeDetailActivity(0);
                break;
            case R.id.yshi_btn:
                goTypeDetailActivity(0);
                break;
            case R.id.jingshi_btn:
                goTypeDetailActivity(0);
                break;
            case R.id.finish_btn:
                finishInventory();
                break;
        }
    }

    public void goTypeDetailActivity(int type){
        Intent intent = new Intent(this,BigInventoryDetialActivity.class);
        intent.putExtra("inventoryId", getIntent().getIntExtra("inventoryId",0));
        intent.putExtra("status", getIntent().getStringExtra("status"));
        startActivity(intent);
    }

    private void finishInventory() {
        RetrofitManager.createString(ApiService.class)
                .updateBigInventoryById(getIntent().getIntExtra("inventoryId",0), "已完成")
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ToastUtils.showShort("已成功完成该盘点！");
                        onBackPressed();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("完成该盘点失败！");
                    }
                });
    }


}
