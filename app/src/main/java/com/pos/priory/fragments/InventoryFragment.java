package com.pos.priory.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.activitys.BigInventoryActivity;
import com.pos.priory.activitys.DayInventoryActivity;
import com.pos.priory.activitys.DetialListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lenovo on 2018/12/29.
 */

public class InventoryFragment extends BaseFragment {
    View view;
    @Bind(R.id.day_inventory_btn)
    MaterialButton dayInventoryBtn;
    @Bind(R.id.big_inventory_btn)
    MaterialButton bigInventoryBtn;
    @Bind(R.id.ding_huo_list_btn)
    MaterialButton dingHuoListBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.day_inventory_btn, R.id.big_inventory_btn,R.id.ding_huo_list_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.day_inventory_btn:
                startActivity(new Intent(getActivity(), DayInventoryActivity.class));
                break;
            case R.id.big_inventory_btn:
                startActivity(new Intent(getActivity(), BigInventoryActivity.class));
                break;
            case R.id.ding_huo_list_btn:
                startActivity(new Intent(getActivity(), DetialListActivity.class));
                break;
        }
    }


}
