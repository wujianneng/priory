package com.pos.priory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.R;
import com.pos.priory.adapters.DataGoldAdapter;
import com.pos.priory.beans.DatasSaleBean;
import com.pos.priory.networks.ApiService;
import com.pos.priory.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DatasFragment extends BaseFragment {
    View view;
    @Bind(R.id.month_money_tv)
    TextView monthMoneyTv;
    @Bind(R.id.gold_recyclerview)
    RecyclerView goldRecyclerview;
    @Bind(R.id.spar_recyclerview)
    RecyclerView sparRecyclerview;

    DataGoldAdapter goldAdapter,sparAdapter;
    List<DatasSaleBean> goldlist = new ArrayList<>();
    List<DatasSaleBean> sparlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_datas, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        goldRecyclerview.setLayoutManager(mLayoutManager);
        goldAdapter = new DataGoldAdapter(R.layout.datas_sale_list,goldlist);
        goldRecyclerview.setAdapter(goldAdapter);

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mLayoutManager2.setOrientation(OrientationHelper.VERTICAL);
        sparRecyclerview.setLayoutManager(mLayoutManager2);
        sparAdapter = new DataGoldAdapter(R.layout.datas_sale_list,sparlist);
        sparRecyclerview.setAdapter(sparAdapter);
        getDatas();
    }

    private void getDatas() {
        RetrofitManager.createString(ApiService.class)
                .getDatas()
                .compose(this.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject jsonObject = new JSONObject(s);
                        monthMoneyTv.setText(DateUtils.getCurrentMonth() + "月营业额： " + jsonObject.getInt("turnover") + "元");
                        JSONArray itemcountgold = jsonObject.getJSONArray("itemcountgold");
                        JSONArray itemscountcrystal = jsonObject.getJSONArray("itemscountcrystal");
                        for(int i = 0 ; i < itemcountgold.length() ;i++){
                            DatasSaleBean bean = new DatasSaleBean();
                            bean.setProductName(itemcountgold.getJSONObject(i).getString("stock__product__name"));
                            bean.setCount(itemcountgold.getJSONObject(i).getInt("catalog_count"));
                            goldlist.add(bean);
                        }
                        goldAdapter.notifyDataSetChanged();
                        for(int i = 0 ; i < itemscountcrystal.length() ;i++){
                            DatasSaleBean bean = new DatasSaleBean();
                            bean.setProductName(itemscountcrystal.getJSONObject(i).getString("stock__product__name"));
                            bean.setCount(itemscountcrystal.getJSONObject(i).getInt("catalog_count"));
                            sparlist.add(bean);
                        }
                        sparAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
