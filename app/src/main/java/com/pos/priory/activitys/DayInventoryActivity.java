package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.networks.ApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 日盘点
 */
public class DayInventoryActivity extends BaseActivity {

    @Bind(R.id.gold_count_tv)
    TextView goldCountTv;
    @Bind(R.id.gold_count_edt)
    EditText goldCountEdt;
    @Bind(R.id.spar_count_tv)
    TextView sparCountTv;
    @Bind(R.id.spar_count_edt)
    EditText sparCountEdt;
    @Bind(R.id.else_count_tv)
    TextView elseCountTv;
    @Bind(R.id.else_count_edt)
    EditText elseCountEdt;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.hand_count_tv)
    TextView handCountTv;
    @Bind(R.id.hand_count_edt)
    EditText handCountEdt;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.gold_summit_btn)
    TextView goldSummitBtn;
    @Bind(R.id.spar_summit_btn)
    TextView sparSummitBtn;
    @Bind(R.id.hand_summit_btn)
    TextView handSummitBtn;
    @Bind(R.id.else_summit_btn)
    TextView elseSummitBtn;
    @Bind(R.id.gold_layout)
    CardView goldLayout;
    @Bind(R.id.spar_layout)
    CardView sparLayout;
    @Bind(R.id.hand_layout)
    CardView handLayout;
    @Bind(R.id.else_layout)
    CardView elseLayout;

    JSONObject goldObj,sparObj,handObj,elseObj;

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_day_inventory);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("日盘点");
        rightImg.setVisibility(View.GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDatas();
    }

    private void getDatas() {
        RetrofitManager.createString(ApiService.class)
                .getDailyInventorys()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("catalog").equals("黄金")) {
                                goldLayout.setVisibility(View.VISIBLE);
                                goldCountTv.setText(jsonObject.getInt("sysquantity") + "件");
                                if(jsonObject.getInt("quantity") != 0){
                                    goldLayout.setAlpha(0.5f);
                                    goldCountEdt.setEnabled(false);
                                    goldCountEdt.setText(jsonObject.getInt("sysquantity") + "");
                                    goldSummitBtn.setEnabled(false);
                                }
                                goldObj = jsonObject;
                            } else if (jsonObject.getString("catalog").equals("晶石")) {
                                sparLayout.setVisibility(View.VISIBLE);
                                sparCountTv.setText(jsonObject.getInt("sysquantity") + "件");
                                if(jsonObject.getInt("quantity") != 0){
                                    sparLayout.setAlpha(0.5f);
                                    sparCountEdt.setEnabled(false);
                                    sparCountEdt.setText(jsonObject.getInt("sysquantity") + "");
                                    sparSummitBtn.setEnabled(false);
                                }
                                sparObj = jsonObject;
                            } else if (jsonObject.getString("catalog").equals("手绳")) {
                                handLayout.setVisibility(View.VISIBLE);
                                handCountTv.setText(jsonObject.getInt("sysquantity") + "件");
                                if(jsonObject.getInt("quantity") != 0){
                                    handLayout.setAlpha(0.5f);
                                    handCountEdt.setEnabled(false);
                                    handCountEdt.setText(jsonObject.getInt("sysquantity") + "");
                                    handSummitBtn.setEnabled(false);
                                }
                                handObj = jsonObject;
                            } else if (jsonObject.getString("catalog").equals("其他")) {
                                elseLayout.setVisibility(View.VISIBLE);
                                elseCountTv.setText(jsonObject.getInt("sysquantity") + "件");
                                if(jsonObject.getInt("quantity") != 0){
                                    elseLayout.setAlpha(0.5f);
                                    elseCountEdt.setEnabled(false);
                                    elseCountEdt.setText(jsonObject.getInt("sysquantity") + "");
                                    elseSummitBtn.setEnabled(false);
                                }
                                elseObj = jsonObject;
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @OnClick({R.id.gold_summit_btn, R.id.spar_summit_btn, R.id.hand_summit_btn, R.id.else_summit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gold_summit_btn:
                doSummit(0);
                break;
            case R.id.spar_summit_btn:
                doSummit(1);
                break;
            case R.id.hand_summit_btn:
                doSummit(2);
                break;
            case R.id.else_summit_btn:
                doSummit(3);
                break;
        }
    }

    CustomDialog customDialog;
    private void doSummit(int i){
        try {
            customDialog = new CustomDialog(this, "提交中..");
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    customDialog = null;
                }
            });
            customDialog.show();
            int id = 0, count = 0;
            if (i == 0) {
                id = goldObj.getInt("id");
                count = Integer.parseInt(goldCountEdt.getText().toString());
                if (goldCountEdt.getText().toString().equals("") || goldCountEdt.getText().toString().equals(".")) {
                    Toast.makeText(DayInventoryActivity.this, "请先输入数量", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(goldCountEdt.getText().toString()) > goldObj.getInt("sysquantity")){
                    Toast.makeText(DayInventoryActivity.this, "盘点数量不能大于系统数量", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (i == 1) {
                id = sparObj.getInt("id");
                count = Integer.parseInt(sparCountEdt.getText().toString());
                if (sparCountEdt.getText().toString().equals("") || sparCountEdt.getText().toString().equals(".")) {
                    Toast.makeText(DayInventoryActivity.this, "请先输入数量", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(sparCountEdt.getText().toString()) > sparObj.getInt("sysquantity")){
                    Toast.makeText(DayInventoryActivity.this, "盘点数量不能大于系统数量", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (i == 2) {
                id = handObj.getInt("id");
                count = Integer.parseInt(handCountEdt.getText().toString());
                if (handCountEdt.getText().toString().equals("") || handCountEdt.getText().toString().equals(".")) {
                    Toast.makeText(DayInventoryActivity.this, "请先输入数量", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(handCountEdt.getText().toString()) > handObj.getInt("sysquantity")){
                    Toast.makeText(DayInventoryActivity.this, "盘点数量不能大于系统数量", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (i == 3) {
                id = elseObj.getInt("id");
                count = Integer.parseInt(elseCountEdt.getText().toString());
                if (elseCountEdt.getText().toString().equals("") || elseCountEdt.getText().toString().equals(".")) {
                    Toast.makeText(DayInventoryActivity.this, "请先输入数量", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(elseCountEdt.getText().toString()) > elseObj.getInt("sysquantity")){
                    Toast.makeText(DayInventoryActivity.this, "盘点数量不能大于系统数量", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            RetrofitManager.createString(ApiService.class)
                    .updateDailyInventoryById(id, count)
                    .compose(this.<String>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            customDialog.dismiss();
                            getDatas();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            customDialog.dismiss();
                            Toast.makeText(DayInventoryActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            customDialog.dismiss();
            Log.e("test","erro:" + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
