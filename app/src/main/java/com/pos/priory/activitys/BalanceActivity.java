package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.priory.R;
import com.pos.priory.beans.InvoicesResultBean;
import com.pos.priory.coustomViews.CustomDialog;
import com.pos.priory.utils.ColseActivityUtils;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BalanceActivity extends BaseActivity {

    double sumMoney = 0, hasPayedCardMoney = 0, hasPayedCashMoney = 0, hasPayedCouponMoney = 0, hasPayedAlipayMoney = 0, hasPayedWechatMoney = 0, hasPayedIntegralMoney = 300, needPayMoney = 0;
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.radio_btn_card)
    CheckBox radioBtnCard;
    @Bind(R.id.edt_card_money)
    EditText edtCardMoney;
    @Bind(R.id.radio_btn_cash)
    CheckBox radioBtnCash;
    @Bind(R.id.edt_cash_money)
    EditText edtCashMoney;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_finish)
    CardView btnFinish;
    @Bind(R.id.count_tv)
    TextView countTv;
    @Bind(R.id.member_name_tv)
    TextView memberNameTv;
    @Bind(R.id.member_phone_tv)
    TextView memberPhoneTv;
    @Bind(R.id.order_number_tv)
    TextView orderNumberTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.radio_btn_coupon)
    CheckBox radioBtnCoupon;
    @Bind(R.id.edt_coupon_money)
    EditText edtCouponMoney;
    @Bind(R.id.radio_btn_alipay)
    CheckBox radioBtnAlipay;
    @Bind(R.id.edt_alipay_money)
    EditText edtAlipayMoney;
    @Bind(R.id.radio_btn_wechat)
    CheckBox radioBtnWechat;
    @Bind(R.id.edt_wechat_money)
    EditText edtWechatMoney;
    @Bind(R.id.radio_btn_integral)
    CheckBox radioBtnIntegral;
    @Bind(R.id.integral_tv)
    TextView integralTv;
    @Bind(R.id.right_img)
    ImageView rightImg;


    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        titleTv.setText("結算");
        rightImg.setVisibility(View.GONE);
        sumMoney = getIntent().getDoubleExtra("sumMoney", 0);
        sumMoney = new BigDecimal(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney)).doubleValue();
        moneyTv.setText(sumMoney + "");
        needPayMoney = sumMoney;
        needMoneyTv.setText("餘額 $" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
        radioBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnCard.isChecked()) {
                    radioBtnCard.setChecked(true);
                    edtCardMoney.setVisibility(View.VISIBLE);
                } else {
                    radioBtnCard.setChecked(false);
                    edtCardMoney.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });
        radioBtnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnCash.isChecked()) {
                    radioBtnCash.setChecked(true);
                    edtCashMoney.setVisibility(View.VISIBLE);
                } else {
                    radioBtnCash.setChecked(false);
                    edtCashMoney.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });
        radioBtnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnCoupon.isChecked()) {
                    radioBtnCoupon.setChecked(true);
                    edtCouponMoney.setVisibility(View.VISIBLE);
                } else {
                    radioBtnCoupon.setChecked(false);
                    edtCouponMoney.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });
        radioBtnAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnAlipay.isChecked()) {
                    radioBtnAlipay.setChecked(true);
                    edtAlipayMoney.setVisibility(View.VISIBLE);
                } else {
                    radioBtnAlipay.setChecked(false);
                    edtAlipayMoney.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });
        radioBtnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnWechat.isChecked()) {
                    radioBtnWechat.setChecked(true);
                    edtWechatMoney.setVisibility(View.VISIBLE);
                } else {
                    radioBtnWechat.setChecked(false);
                    edtWechatMoney.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });
        radioBtnIntegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnIntegral.isChecked()) {
                    radioBtnIntegral.setChecked(true);
                    integralTv.setVisibility(View.VISIBLE);
                } else {
                    radioBtnIntegral.setChecked(false);
                    integralTv.setVisibility(View.GONE);
                }
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }
        });

        edtCardMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                    hasPayedCardMoney = 0;
                else
                    hasPayedCardMoney = Double.parseDouble(charSequence.toString());
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtCashMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                    hasPayedCashMoney = 0;
                else
                    hasPayedCashMoney = Double.parseDouble(charSequence.toString());
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtCouponMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                    hasPayedCouponMoney = 0;
                else
                    hasPayedCouponMoney = Double.parseDouble(charSequence.toString());
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtAlipayMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                    hasPayedAlipayMoney = 0;
                else
                    hasPayedAlipayMoney = Double.parseDouble(charSequence.toString());
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtWechatMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                    hasPayedWechatMoney = 0;
                else
                    hasPayedWechatMoney = Double.parseDouble(charSequence.toString());
                needPayMoney = sumMoney - getSumHasPayMoney();
                needMoneyTv.setText("剩餘：" + needPayMoney);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public double getSumHasPayMoney() {
        double money = 0;
        if (radioBtnCard.isChecked())
            money += hasPayedCardMoney;
        if (radioBtnCash.isChecked())
            money += hasPayedCashMoney;
        if (radioBtnCoupon.isChecked())
            money += hasPayedCouponMoney;
        if (radioBtnAlipay.isChecked())
            money += hasPayedAlipayMoney;
        if (radioBtnWechat.isChecked())
            money += hasPayedWechatMoney;
        if (radioBtnIntegral.isChecked())
            money += hasPayedIntegralMoney;
        return money;
    }

    @OnClick({R.id.btn_finish, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                if (needPayMoney > 0) {
                    Toast.makeText(BalanceActivity.this, "还需付" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney), Toast.LENGTH_SHORT).show();
                    return;
                }
                invoice();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }

    CustomDialog customDialog;
    InvoicesResultBean invoicesResultBean;

    private void invoice() {
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在结算..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ordernumber", getIntent().getStringExtra("ordernumber"));
        paramMap.put("amount", sumMoney);
        OkHttp3Util.doPostWithToken(Constants.INVOICES_URL + "/", gson.toJson(paramMap), sharedPreferences,
                new Okhttp3StringCallback(this, "invoice") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        invoicesResultBean = gson.fromJson(results, InvoicesResultBean.class);
                        transaction(invoicesResultBean.getInvoicenumber());
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(BalanceActivity.this, "结算失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void transaction(String invoicesNumber) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("invoicenumber", invoicesNumber);
        paramMap.put("amount", sumMoney);
        paramMap.put("paymentmethod", radioBtnCard.isChecked() ? "信用卡" : "現金");
        OkHttp3Util.doPostWithToken(Constants.TRANSACTION_URL + "/", gson.toJson(paramMap), sharedPreferences,
                new Okhttp3StringCallback(this, "transaction") {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        Intent intent = new Intent(BalanceActivity.this, BillActivity.class);
                        intent.putExtra("goodlist", getIntent().getStringExtra("goodlist"));
                        intent.putExtra("sumMoney", getIntent().getDoubleExtra("newOrderSumMoney", 0));
                        intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                        intent.putExtra("receiveMoney", sumMoney);
                        intent.putExtra("returnMoney", needPayMoney * -1);
                        intent.putExtra("ordernumber", getIntent().getStringExtra("ordernumber"));
                        startActivity(intent);
                        ColseActivityUtils.finishWholeFuntionActivitys();
                        finish();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(BalanceActivity.this, "结算失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        deleteInvoice();
    }


    private void deleteInvoice() {
        if (invoicesResultBean == null) {
            finish();
            return;
        }
        if (customDialog == null)
            customDialog = new CustomDialog(this, "正在删除发票..");
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                customDialog = null;
            }
        });
        customDialog.show();
        OkHttp3Util.doDeleteWithToken(Constants.INVOICES_URL + "/" + invoicesResultBean.getId() + "/update/",
                sharedPreferences, new Okhttp3StringCallback() {
                    @Override
                    public void onSuccess(String results) throws Exception {
                        customDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailed(String erromsg) {
                        customDialog.dismiss();
                        Toast.makeText(BalanceActivity.this, "删除发票失败", Toast.LENGTH_SHORT).show();
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
