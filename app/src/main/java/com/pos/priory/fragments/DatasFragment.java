package com.pos.priory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pos.priory.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DatasFragment extends BaseFragment {
    View view;
    @Bind(R.id.padding_layout)
    View paddingLayout;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.btn_select_type)
    MaterialButton btnSelectType;
    @Bind(R.id.title_layout)
    FrameLayout titleLayout;
    @Bind(R.id.container)
    FrameLayout container;

    SaleAmountDatasFragment saleAmountDatasFragment;
    SaleCountDatasFragment saleCountDatasFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_datas, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        btnSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectTypePopu();
            }
        });
        changeFragment(0);
    }

    private void showSelectTypePopu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSelectType);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.datas_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        changeFragment(0);
                        break;
                    case R.id.menu1:
                        changeFragment(1);
                        break;
                }
                btnSelectType.setText(item.getTitle());
                return true;
            }
        });
    }

    public void changeFragment(int index) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        switch (index) {
            case 0:
                if (saleCountDatasFragment != null)
                    fragmentTransaction.hide(saleCountDatasFragment);
                if (saleAmountDatasFragment == null) {
                    saleAmountDatasFragment = new SaleAmountDatasFragment();
                    fragmentTransaction.add(R.id.container, saleAmountDatasFragment);
                } else fragmentTransaction.show(saleAmountDatasFragment);
                break;
            case 1:
                if (saleAmountDatasFragment != null)
                    fragmentTransaction.hide(saleAmountDatasFragment);
                if (saleCountDatasFragment == null) {
                    saleCountDatasFragment = new SaleCountDatasFragment();
                    fragmentTransaction.add(R.id.container, saleCountDatasFragment);
                } else fragmentTransaction.show(saleCountDatasFragment);
                break;
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
