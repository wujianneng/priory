package com.pos.priory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.pos.priory.R;
import com.pos.priory.activitys.GoodDetialActivity;
import com.pos.priory.activitys.MainActivity;
import com.pos.priory.adapters.OrderAdapter;
import com.pos.priory.adapters.RepertoryAdapter;
import com.pos.priory.beans.GoodBean;
import com.pos.priory.utils.Constants;
import com.pos.priory.utils.LogicUtils;
import com.pos.priory.utils.OkHttp3Util;
import com.pos.priory.utils.Okhttp3StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class RepertoryFragment extends BaseFragment {
    View view;
    List<GoodBean> dataList = new ArrayList<>();
    RepertoryAdapter repertoryAdapter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repertory, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        repertoryAdapter = new RepertoryAdapter(getActivity(),R.layout.repertory_list_item, dataList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(repertoryAdapter);
        repertoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), GoodDetialActivity.class);
                intent.putExtra("productcode",dataList.get(position).getProduct().getProductcode());
                intent.putExtra("count",dataList.get(position).getQuantity());
                intent.putExtra("name",dataList.get(position).getProduct().getName());
                startActivity(intent);
            }
        });
    }


    Call call;
    public void refreshRecyclerView(String str) {
        if (call != null)
            call.cancel();
        dataList.clear();
        repertoryAdapter.notifyDataSetChanged();
        if(str.equals(""))
            return;
        String url = "";
        String location = ((MainActivity) getActivity()).staffInfoBeanList.get(0).getStore();
        if (LogicUtils.isNumeric(str))
            url = Constants.GET_STOCK_URL + "?productcode=" + str + "&location=" + location;
        else
            url = Constants.GET_STOCK_URL + "?name=" + str + "&location=" + location;
        Log.e("getStockList","url:" + url);
        call = OkHttp3Util.doGetWithToken(url, sharedPreferences, new Okhttp3StringCallback(getActivity(),"getStockList") {
            @Override
            public void onSuccess(String results) throws Exception {
                 List<GoodBean> goodBeanList = gson.fromJson(results,new TypeToken<List<GoodBean>>(){}.getType());
                if(goodBeanList != null) {
                    dataList.addAll(goodBeanList);
                    repertoryAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailed(String erromsg) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
