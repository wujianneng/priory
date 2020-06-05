package com.pos.priory.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pos.priory.MyApplication;
import com.pos.priory.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaleCountDatasFragment extends BaseFragment {
    View view;
    @Bind(R.id.store_name_tv)
    TextView storeNameTv;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.start_date_tv)
    TextView startDateTv;
    @Bind(R.id.end_date_tv)
    TextView endDateTv;
    @Bind(R.id.btn_search_type)
    MaterialButton btnSearchType;
    @Bind(R.id.btn_select_good_type)
    MaterialButton btnSelectGoodType;
    @Bind(R.id.btn_select_count)
    MaterialButton btnSelectCount;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sale_count_datas, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        storeNameTv.setText("銷售量(" + MyApplication.storeName + ")");
    }

    @OnClick({R.id.date_tv,R.id.start_date_tv,R.id.end_date_tv,R.id.btn_search_type,R.id.btn_select_good_type,R.id.btn_select_count})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.date_tv:
                getDate(0);
                break;
            case R.id.start_date_tv:
                getDate(1);
                break;
            case R.id.end_date_tv:
                getDate(2);
                break;
            case R.id.btn_search_type:
                showSelectTypePopu();
                break;
            case R.id.btn_select_good_type:
                showSelectGoodTypePopu();
                break;
            case R.id.btn_select_count:
                showSelectCountPopu();
                break;
        }
    }

    public void showSelectGoodTypePopu(){
        PopupMenu popupMenu = new PopupMenu(getActivity(),btnSelectGoodType);
        Menu menu = popupMenu.getMenu();
        menu.add("黃金");
        menu.add("玉石");
        menu.add("晶石");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("黃金")){

                }else if(menuItem.getTitle().equals("玉石")){

                }else {

                }
                btnSelectGoodType.setText(menuItem.getTitle());
                return true;
            }
        });
    }

    public void showSelectCountPopu(){
        PopupMenu popupMenu = new PopupMenu(getActivity(),btnSelectCount);
        Menu menu = popupMenu.getMenu();
        menu.add("10");
        menu.add("20");
        menu.add("30");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("10")){

                }else if(menuItem.getTitle().equals("20")){

                }else {

                }
                btnSelectCount.setText(menuItem.getTitle());
                return true;
            }
        });
    }

    private void getDate(final int num) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                if (num == 0) {
                    dateTv.setText(DateFormat.format("yyy-MM-dd", c));
                } else if (num == 1) {
                    startDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                } else if (num == 2) {
                    endDateTv.setText(DateFormat.format("yyy-MM-dd", c));
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showSelectTypePopu() {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnSearchType);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.date_type_menu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.menu0:
                        dateTv.setVisibility(View.VISIBLE);
                        startDateTv.setVisibility(View.GONE);
                        endDateTv.setVisibility(View.GONE);
                        break;
                    case R.id.menu1:
                        dateTv.setVisibility(View.GONE);
                        startDateTv.setVisibility(View.VISIBLE);
                        endDateTv.setVisibility(View.VISIBLE);
                        break;
                }
                btnSearchType.setText(item.getTitle());
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
