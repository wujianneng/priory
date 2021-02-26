package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.SeletFittingAdapter;
import com.pos.priory.beans.FittingBean;
import com.pos.priory.networks.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFittingActivity extends BaseActivity {

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
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    SeletFittingAdapter adapter;
    List<FittingBean.ResultsBean> fittingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fitting);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        titleTv.setText("添加配件");
        nextTv.setText("確定");
        nextTv.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false));
        adapter = new SeletFittingAdapter(R.layout.fitting_list_item,fittingList);
        recyclerView.setAdapter(adapter);

        getFittings();
    }

    private void getFittings() {
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getFittings(true), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
              FittingBean fittingBean = gson.fromJson(result,FittingBean.class);
              fittingList.addAll(fittingBean.getResults());
              adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String erromsg) {

            }
        });
    }

    @OnClick({R.id.back_btn,R.id.next_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectFittingList", gson.toJson(adapter.selectFittingList));
                setResult(3, intent);
                finish();
                break;
        }
    }
}
