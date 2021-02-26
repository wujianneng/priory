package com.pos.priory.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.infitack.rxretorfit2library.ModelGsonListener;
import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.beans.MemberDetailResultBean;
import com.pos.priory.networks.ApiService;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/31.
 */

public class MemberInfoActivity extends BaseActivity {

    public static final String UPDATE_ORDER_LIST = "memberInfoActivity_update_list";
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
    @Bind(R.id.edt_first_name)
    EditText edtFirstName;
    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.sex_tv)
    TextView sexTv;
    @Bind(R.id.phone_tv)
    EditText phoneTv;
    @Bind(R.id.scout_tv)
    TextView scoutTv;
    @Bind(R.id.data_layout)
    CardView dataLayout;
    @Bind(R.id.register_address_tv)
    TextView registerAddressTv;
    @Bind(R.id.order_title)
    TextView orderTitle;
    @Bind(R.id.btn_save)
    MaterialButton btnSave;
    MemberBean.ResultsBean memberBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        ButterKnife.bind(this);
        initViews();
    }


    protected void initViews() {
        titleTv.setText("会员信息");
        rightImg.setVisibility(View.GONE);
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);

        getMembeDetail();
    }

    private void getMembeDetail(){
        RetrofitManager.excuteGson(RetrofitManager.createGson(ApiService.class).getMemberDetail(memberBean.getId()),new ModelGsonListener<MemberDetailResultBean>(){
            @Override
            public void onSuccess(MemberDetailResultBean result) throws Exception {
                edtFirstName.setText(result.getFirstname()
                );
                edtName.setText(result.getLastname());
                sexTv.setText(memberBean.getGender());
                phoneTv.setText(memberBean.getMobile());
                scoutTv.setText(memberBean.getReward() + "");
                registerAddressTv.setText("注冊地點：" + memberBean.getShop());
                orderTitle.setText("注冊時間：" + memberBean.getCreated());
            }

            @Override
            public void onFailed(String erromsg) {

            }
        });
    }


    @OnClick({R.id.back_btn, R.id.sex_tv, R.id.btn_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.sex_tv:
                showChoiceSexDialog();
                break;
            case R.id.btn_save:
                doSave();
                break;
        }
    }

    private void doSave() {
        if(phoneTv.getText().toString().isEmpty()){
            showToast("請先輸入電話");
            return;
        }
        if(edtFirstName.getText().toString().isEmpty()){
            showToast("請先輸入姓氏");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneTv.getText().toString());
        params.put("lastname", edtFirstName.getText().toString());
        if (!edtName.getText().toString().isEmpty())
            params.put("firstname", edtName.getText().toString());
        params.put("gender", sexTv.getText().toString().equals("男") ? 1 : 2);
        showLoadingDialog("正在修改會員信息...");
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).editMember(memberBean.getId(),params), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                 finish();
            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
                showToast("修改會員信息失敗");

            }
        });
    }

    int yourChoice;
    AlertDialog choiceSexDialog;

    private void showChoiceSexDialog() {
        if (choiceSexDialog == null) {
            final String[] items = {"男", "女"};
            yourChoice = 0;
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(this);
            singleChoiceDialog.setTitle("请选择性别");
            // 第二个参数是默认选项，此处设置为0
            singleChoiceDialog.setSingleChoiceItems(items, yourChoice,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yourChoice = which;
                        }
                    });
            singleChoiceDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yourChoice != -1) {
                                sexTv.setText(items[yourChoice]);
                            }
                            choiceSexDialog.dismiss();
                        }
                    });
            choiceSexDialog = singleChoiceDialog.create();
            choiceSexDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    choiceSexDialog = null;
                }
            });
            choiceSexDialog.show();
        }
    }
}
