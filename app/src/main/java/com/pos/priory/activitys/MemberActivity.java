package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.adapters.QueryMemberAdapter;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.networks.RetrofitManager;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;
import com.pos.priory.utils.RunOnUiThreadSafe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lenovo on 2018/12/30.
 */

public class MemberActivity extends BaseActivity {

    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.register_tv)
    TextView registerTv;
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.edt_member_input)
    EditText edtMemberInput;
    @Bind(R.id.member_recycler_view)
    RecyclerView memberRecyclerView;

    QueryMemberAdapter memberAdapter;
    List<MemberBean> memberList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beForeInitViews() {
        setContentView(R.layout.activity_member);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        memberAdapter = new QueryMemberAdapter(R.layout.query_member_list_item, memberList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        memberRecyclerView.setLayoutManager(mLayoutManager);
        memberRecyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MemberActivity.this, AddNewOrderActivity.class);
                intent.putExtra("memberId", memberList.get(position).getId());
                intent.putExtra("memberMobile", memberList.get(position).getMobile());
                intent.putExtra("memberReward", memberList.get(position).getReward());
                intent.putExtra("memberName", memberList.get(position).getLast_name() +
                        memberList.get(position).getFirst_name());
                startActivity(intent);
            }
        });

        edtMemberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    refreshMemberRecyclerView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    Disposable memberCall;

    private void refreshMemberRecyclerView(String str) {
        if (memberCall != null)
            memberCall.dispose();
        memberList.clear();
        memberAdapter.notifyDataSetChanged();
        if(str.equals(""))
            return;
        memberCall = RetrofitManager.createString(ApiService.class)
                .getMembers(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        final List<MemberBean> memberBeanList = gson.fromJson(s, new TypeToken<List<MemberBean>>() {
                        }.getType());
                        if (memberBeanList != null) {
                            new RunOnUiThreadSafe(MemberActivity.this) {
                                @Override
                                public void runOnUiThread() {
                                    memberList.addAll(memberBeanList);
                                    memberAdapter.notifyDataSetChanged();
                                }
                            };
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @OnClick({R.id.register_tv, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tv:
                startActivity(new Intent(MemberActivity.this, RegisterMemberActivity.class));
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
