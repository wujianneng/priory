package com.pos.priory.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.infitack.rxretorfit2library.ModelListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.ExchangeCashCouponAdapter;
import com.pos.priory.beans.ExchangeCashCouponBean;
import com.pos.priory.beans.MemberBean;
import com.pos.priory.networks.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeCashCouponActivity extends BaseActivity {

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
    public RecyclerView recyclerView;
    @Bind(R.id.usable_integal_tv)
    TextView usableIntegalTv;
    @Bind(R.id.used_integal_tv)
    TextView usedIntegalTv;
    @Bind(R.id.own_integal_tv)
    TextView ownIntegalTv;

    ExchangeCashCouponAdapter adapter;
    ExchangeCashCouponBean.ResultBean resultBean;
    List<ExchangeCashCouponBean.ResultBean.RewardListBean> datalist = new ArrayList<>();
    MemberBean.ResultsBean memberBean;
    public double enableReward = 0, usedReward = 0, lastReward = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_cash_coupon);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        memberBean = gson.fromJson(getIntent().getStringExtra("memberInfo"), MemberBean.ResultsBean.class);
        titleTv.setText("積分兌換");
        nextTv.setText("確定");
        nextTv.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ExchangeCashCouponAdapter(this, R.layout.exchange_coupon_list_item, datalist);
        adapter.setOnItemChildClickListener(((adapter1, view, position) -> {
            ExchangeCashCouponBean.ResultBean.RewardListBean item = datalist.get(position);
            if (view.getId() == R.id.decrease_btn) {
                if (item.getCount() == 1)
                    return;
                item.setCount(item.getCount() - 1);
                adapter.notifyItemChanged(position);
                resetRewardTvs();
            } else if (view.getId() == R.id.increase_btn) {
                if (lastReward < item.getReducereward())
                    return;
                item.setCount(item.getCount() + 1);
                adapter.notifyItemChanged(position);
                resetRewardTvs();
            }
        }));
        recyclerView.setAdapter(adapter);
        getDatas();
    }

    public void resetRewardTvs() {
        usedReward = 0;
        for (ExchangeCashCouponBean.ResultBean.RewardListBean resultsBean : adapter.selectList) {
            usedReward += resultsBean.getReducereward() * resultsBean.getCount();
        }
        lastReward = enableReward - usedReward;
        if (lastReward < 0) lastReward = 0;

        usableIntegalTv.setText("可使用積分：" + enableReward);
        usedIntegalTv.setText("已扣除積分：" + usedReward);
        ownIntegalTv.setText("剩餘積分：" + lastReward);
    }

    private void getDatas() {
        showLoadingDialog("正在獲取積分兌換列表...");
        RetrofitManager.excute(RetrofitManager.createString(ApiService.class).getRewardExList(memberBean.getId()), new ModelListener() {
            @Override
            public void onSuccess(String result) throws Exception {
                hideLoadingDialog();
                ExchangeCashCouponBean bean = gson.fromJson(result, ExchangeCashCouponBean.class);
                resultBean = bean.getResult();
                enableReward = resultBean.getMember_reward();
                lastReward = resultBean.getMember_reward();
                datalist.addAll(bean.getResult().getReward_list());
                adapter.notifyDataSetChanged();
                resetRewardTvs();
            }

            @Override
            public void onFailed(String erromsg) {
                hideLoadingDialog();
            }
        });
    }

    @OnClick({R.id.next_tv, R.id.back_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_tv:
                Intent intent = new Intent();
                intent.putExtra("selectExchangeCashCouponList", gson.toJson(adapter.selectList));
                setResult(2, intent);
                finish();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
