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

import com.pos.priory.R;
import com.pos.priory.adapters.DataGoldAdapter;
import com.pos.priory.beans.DatasSaleBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        for(int i = 0 ; i < 10 ; i++){
            DatasSaleBean bean = new DatasSaleBean();
            bean.setCount(10 * i);
            bean.setProductName("产品" + i);
            goldlist.add(bean);
            sparlist.add(bean);
        }
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
