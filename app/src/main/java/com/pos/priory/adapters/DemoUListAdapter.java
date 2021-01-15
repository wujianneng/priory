package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.beans.DemoU;

import java.util.ArrayList;

/**
 * Created by Bruce_Chiang on 2017/3/15.
 */

public class DemoUListAdapter extends ArrayAdapter<DemoU> {

    private int mResourceId;

    public DemoUListAdapter(Context context, int resource, ArrayList<DemoU> objects) {
        super(context, resource, objects);
        this.mResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout _layout;
        DemoU _demoU = getItem(position);

        if (convertView == null)
        {
            _layout = new RelativeLayout(getContext());
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(mResourceId, _layout, true);
        }
        else
        {
            _layout = (RelativeLayout)convertView;
        }

        TextView tvPC = (TextView) _layout.findViewById(R.id.adapter_demou_pc);
        TextView tvEPC = (TextView) _layout.findViewById(R.id.adapter_demou_cpc);
        TextView tvCRC16 = (TextView) _layout.findViewById(R.id.adapter_demou_crc16);
        TextView tvCount = (TextView) _layout.findViewById(R.id.adapter_demou_count);
        TextView tvPercentage = (TextView) _layout.findViewById(R.id.adapter_demou_percentage);

        tvPC.setText(_demoU.PC());
        tvEPC.setText(_demoU.EPC());
        tvCRC16.setText(_demoU.CRC16());
        tvCount.setText(_demoU.Count());
        tvPercentage.setText(_demoU.Percentage());

        return _layout;
    }
}
