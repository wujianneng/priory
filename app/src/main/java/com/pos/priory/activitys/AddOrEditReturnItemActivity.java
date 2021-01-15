package com.pos.priory.activitys;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.beans.ReturnFilterBean;
import com.pos.priory.beans.WarehouseBean;
import com.pos.priory.networks.ApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddOrEditReturnItemActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.next_tv)
    TextView nextTv;
    @Bind(R.id.title_layout)
    CardView titleLayout;
    @Bind(R.id.edt_weight)
    EditText edtWeight;
    @Bind(R.id.start_date_tv)
    TextView startDateTv;
    @Bind(R.id.edt_price)
    EditText edtPrice;

    boolean isCreating = true;
    WarehouseBean.ResultsBean.ItemBean editingBean;

    private List<ReturnFilterBean.ResultBean.TypeBean> type;
    private List<ReturnFilterBean.ResultBean.ProductBean> product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_return);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        isCreating = getIntent().getBooleanExtra("isCreate", true);
        titleTv.setText(isCreating ? "添加回收产品" : "编辑回收产品");
        nextTv.setVisibility(View.VISIBLE);
        nextTv.setText("保存");
        startDateTv.setEnabled(isCreating ? true : false);
        startDateTv.setTextColor(isCreating ? Color.BLACK : Color.GRAY);
        edtPrice.setEnabled(isCreating ? true : false);
        if (!isCreating) {
            editingBean = gson.fromJson(getIntent().getStringExtra("returnBean"),
                    WarehouseBean.ResultsBean.ItemBean.class);
            edtWeight.setText(editingBean.getReturninfo().getWeight() + "");
            startDateTv.setText(editingBean.getReturninfo().getDate());
            edtPrice.setText(editingBean.getReturninfo().getCost() + "");
        }
    }



    @OnClick({R.id.back_btn, R.id.next_tv,  R.id.start_date_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.start_date_tv:
                getDate();
                break;
            case R.id.next_tv:
                doSave();
                break;
        }
    }


    private void getDate() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                startDateTv.setText(DateFormat.format("yyy-MM-dd", c).toString() + " 00:00:00");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void doSave() {
        if (isCreating) {
            RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).createReturnItem(
                    getIntent().getIntExtra("warehouseId", 0),"回收",
                    startDateTv.getText().toString(), edtPrice.getText().toString(), edtWeight.getText().toString()),
                    new ModelListener() {
                        @Override
                        public void onSuccess(String result) throws Exception {
                            Toast.makeText(AddOrEditReturnItemActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post("refreshReturnList");
                            finish();
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            Toast.makeText(AddOrEditReturnItemActivity.this, "添加失敗！", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("weight", edtWeight.getText().toString());
            Log.e("test", "params:" + gson.toJson(paramMap));
            RetrofitManager.excute(bindToLifecycle(), RetrofitManager.createString(ApiService.class).editReturnItem(editingBean.getId(),
                    RequestBody.create(MediaType.parse("application/json"), gson.toJson(paramMap))),
                    new ModelListener() {
                        @Override
                        public void onSuccess(String result) throws Exception {
                            Toast.makeText(AddOrEditReturnItemActivity.this, "編輯成功！", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post("refreshReturnList");
                            finish();
                        }

                        @Override
                        public void onFailed(String erromsg) {
                            Toast.makeText(AddOrEditReturnItemActivity.this, "編輯失敗！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
