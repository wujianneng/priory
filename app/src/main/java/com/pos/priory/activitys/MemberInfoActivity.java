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

import com.pos.priory.R;
import com.pos.priory.beans.MemberBean;

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
    MemberBean memberBean;

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
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.class);
        edtFirstName.setText(memberBean.getLast_name()
        );
        edtName.setText(memberBean.getFirst_name());
        sexTv.setText(memberBean.getSex());
        phoneTv.setText(memberBean.getMobile());
        scoutTv.setText(memberBean.getReward() + "");
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
                setResult(4);
                finish();
                break;
        }
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
