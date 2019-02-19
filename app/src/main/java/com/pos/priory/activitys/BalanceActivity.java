package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class BalanceActivity extends BaseActivity {

    double sumMoney = 0, hasPayedCardMoney = 0, hasPayedCashMoney = 0, needPayMoney = 0;
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
    @Bind(R.id.need_money_tv)
    TextView needMoneyTv;
    @Bind(R.id.radio_btn_card)
    CheckBox radioBtnCard;
    @Bind(R.id.edt_card_money)
    EditText edtCardMoney;
    @Bind(R.id.card_input_layout)
    CardView cardInputLayout;
    @Bind(R.id.radio_btn_cash)
    CheckBox radioBtnCash;
    @Bind(R.id.edt_cash_money)
    EditText edtCashMoney;
    @Bind(R.id.cash_input_layout)
    CardView cashInputLayout;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.btn_finish)
    CardView btnFinish;


    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT < 19) {
            paddingLaout.setVisibility(View.GONE);
        }
        sumMoney = getIntent().getDoubleExtra("sumMoney", 0);

        moneyTv.setText(LogicUtils.getKeepLastOneNumberAfterLittlePoint(sumMoney));
        needPayMoney = sumMoney;
        needMoneyTv.setText("餘額 $" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
        radioBtnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnCard.isChecked()) {
                    radioBtnCard.setChecked(true);
                    radioBtnCash.setChecked(false);
                    cardInputLayout.setVisibility(View.VISIBLE);
                    cashInputLayout.setVisibility(View.INVISIBLE);
                } else {
                    radioBtnCard.setChecked(false);
                    radioBtnCash.setChecked(true);
                    cardInputLayout.setVisibility(View.INVISIBLE);
                    cashInputLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        radioBtnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioBtnCash.isChecked()) {
                    radioBtnCard.setChecked(false);
                    radioBtnCash.setChecked(true);
                    cardInputLayout.setVisibility(View.INVISIBLE);
                    cashInputLayout.setVisibility(View.VISIBLE);
                } else {
                    radioBtnCard.setChecked(true);
                    radioBtnCash.setChecked(false);
                    cardInputLayout.setVisibility(View.VISIBLE);
                    cashInputLayout.setVisibility(View.INVISIBLE);
                }
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
                if (radioBtnCard.isChecked()) {
                    needPayMoney = sumMoney - hasPayedCardMoney;
                    needMoneyTv.setText("餘額 $" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
                }
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
                if (radioBtnCash.isChecked()) {
                    needPayMoney = sumMoney - hasPayedCashMoney;
                    needMoneyTv.setText("餘額 $" + LogicUtils.getKeepLastOneNumberAfterLittlePoint(needPayMoney));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.btn_finish, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                if (needPayMoney > 0) {
                    Toast.makeText(BalanceActivity.this, "还需付" + needPayMoney , Toast.LENGTH_SHORT).show();
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
                        intent.putExtra("returnMoney",needPayMoney * -1);
                        intent.putExtra("ordernumber",getIntent().getStringExtra("ordernumber"));
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


}
