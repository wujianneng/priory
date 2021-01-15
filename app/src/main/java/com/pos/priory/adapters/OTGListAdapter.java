package com.pos.priory.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.beans.OTGDevice;

import java.util.List;

/**
 * Created by Bruce_Chiang on 2017/3/17.
 */

public class OTGListAdapter extends ArrayAdapter<OTGDevice> {

    private int mResourceId;

    public OTGListAdapter(Context context, int resource, List<OTGDevice> objects) {
        super(context, resource, objects);
        this.mResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout _layout;
        OTGDevice _otgDev = getItem(position);

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

        ImageView ivImage = (ImageView) _layout.findViewById(R.id.adapter_otg_image);
        TextView tvDeviceName = (TextView) _layout.findViewById(R.id.adapter_otg_deviceName);
        TextView tvManufacturerName = (TextView) _layout.findViewById(R.id.adapter_otg_manufacturerName);
        TextView tvProductName = (TextView) _layout.findViewById(R.id.adapter_otg_productName);
        TextView tvProductId = (TextView) _layout.findViewById(R.id.adapter_otg_productId);
        TextView tvVendorId = (TextView) _layout.findViewById(R.id.adapter_otg_vendorId);

        if (_otgDev.getProductName().equals("READER"))
            _otgDev.setImage(R.drawable.app_logo);
        else
            _otgDev.setImage(R.drawable.app_logo);

        ivImage.setImageResource(_otgDev.getImage());
        tvDeviceName.setText(_otgDev.getDeviceName());
        tvManufacturerName.setText(_otgDev.getManufacturerName());
        tvProductName.setText(_otgDev.getProductName());
        tvProductId.setText(_otgDev.getProductId());
        tvVendorId.setText(_otgDev.getVendorId());

        return _layout;
    }
}
