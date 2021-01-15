package com.pos.priory.fragments;
/**
 * *************************************************************************************************
 * FILE:			BLEFragment.java
 * ------------------------------------------------------------------------------------------------
 * COMPANY:			FAVEPC
 * VERSION:			V1.0
 * CREATED:			2017/3/9
 * DATE:
 * AUTHOR:			Bruce_Chiang
 * ------------------------------------------------------------------------------------------------
 * \PURPOSE:
 * - None
 * \NOTES:
 * - None
 * \Global Variables:
 * - None
 * ------------------------------------------------------------------------------------------------
 * REVISION		Date			User	Description
 * V1.0			2017/3/9     	Bruce	1.support FTDI and PL2303 chipsets.
 * <p/>
 * ------------------------------------------------------------------------------------------------
 * *************************************************************************************************
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.priory.R;
import com.pos.priory.activitys.NfcActivity;
import com.pos.priory.adapters.OTGListAdapter;
import com.pos.priory.beans.OTGDevice;
import com.pos.priory.services.OTGService;
import com.pos.priory.services.ReaderModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@SuppressLint("ValidFragment")
public class OTGFragment extends Fragment {

    private Context	mContext;
    private Activity mActivity;
    //private UsbManager mUsbManager;
    private View mOTGView = null;
    private ArrayList<OTGDevice> mOTGDevices = new ArrayList<OTGDevice>();
    private Button mBtnEnumerate;
    private TextView mTextViewMsgStatus, mTextViewMsg;
    private ProgressBar mProgressBar;
    private OTGListAdapter mOTGListAdapter;
    private boolean m_bSupportOTG = false;
    private boolean m_bScanning = false;
    private OTGMsgReceiver mOTGMsgReceiver;
    //private UsbManager mUsbManager;

    public OTGFragment() {
        super();
    }
    public OTGFragment(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();
        this.mOTGMsgReceiver = new OTGMsgReceiver();
        mContext.registerReceiver(mOTGMsgReceiver, new IntentFilter(OTGService.OTG_ACTION_CONNECTED));
        mContext.registerReceiver(mOTGMsgReceiver, new IntentFilter(OTGService.OTG_ACTION_DISCONNECTED));
        mContext.registerReceiver(mOTGMsgReceiver, new IntentFilter(OTGService.OTG_ACTION_SETTING_ERROR));
        mContext.registerReceiver(mOTGMsgReceiver, new IntentFilter(OTGService.OTG_ACTION_CHANGE_INTERFACE));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.mOTGView == null) {
            this.mOTGView = inflater.inflate(R.layout.fragment_otg, container, false);
            this.mOTGListAdapter = new OTGListAdapter(this.mContext, R.layout.adapter_otgdevice, this.mOTGDevices);
            ListView lv = (ListView)mOTGView.findViewById(R.id.otg_lvDevice);
            lv.setAdapter(this.mOTGListAdapter);
            lv.setOnItemClickListener(deviceClickListener);

            this.mBtnEnumerate = (Button) mOTGView.findViewById(R.id.otg_btnEnumerate);
            this.mBtnEnumerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!m_bSupportOTG) {
                        String str = Build.DEVICE + " ";
                        mTextViewMsg.setText(str.toUpperCase() + R.string.otg_msg_no_support);
                    }
                    else {
                        enumerateDevice();
                    }
                }
            });

            this.mTextViewMsgStatus = (TextView) mOTGView.findViewById(R.id.otg_tvMsgStatus);
            this.mTextViewMsg = (TextView) mOTGView.findViewById(R.id.otg_tvMsg);

            this.mProgressBar = (ProgressBar)mOTGView.findViewById(R.id.otg_progressBar);
            this.mProgressBar.setVisibility(View.GONE);
        }


        return this.mOTGView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String str = Build.DEVICE + " ";
        if(!this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST)) {

            this.mTextViewMsgStatus.setText(str.toUpperCase() + getString(R.string.otg_msg_no_support));
            this.m_bSupportOTG = false;
        }
        else {
            this.mTextViewMsgStatus.setText(str.toUpperCase() + getString(R.string.otg_msg_support));
            this.m_bSupportOTG = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(mOTGMsgReceiver);
        mContext = null;
    }


    private void enumerateDevice() {

        if (!m_bScanning) {
            m_bScanning = true;

            this.mProgressBar.setVisibility(View.VISIBLE);
            this.mTextViewMsg.setText(R.string.otg_msg_start_enumerate);
            this.mOTGListAdapter.clear();

            //Returns a HashMap containing all USB devices currently attached.
            HashMap<String, UsbDevice> deviceList = OTGService.mUsbManager.getDeviceList();

            if (deviceList.size() == 0) {
                this.m_bScanning = false;
                this.mProgressBar.setVisibility(View.GONE);
                this.mTextViewMsg.setText(R.string.otg_msg_stop_enumerate_no_device);
            }
            else {
                //Returns an iterator over the elements in this collection.
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                while(deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    OTGDevice info = new OTGDevice(device);
                    boolean _b = false;
                    for (int i = 0; i < this.mOTGListAdapter.getCount(); i++) {
                        if (this.mOTGListAdapter.getItem(i).getDeviceName().equals(info.getDeviceName())) {
                            _b = true;
                            break;
                        }
                    }
                    if (!_b)
                    {
                        this.mOTGListAdapter.add(info);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.m_bScanning = false;
                this.mProgressBar.setVisibility(View.GONE);
                this.mTextViewMsg.setText(R.string.otg_msg_stop_enumerate);
            }
        }
    }

    private AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            UsbDevice dev =  mOTGListAdapter.getItem(i).getUsbDevice();
            TextView _tvPID = (TextView)((RelativeLayout)view).findViewById(R.id.adapter_otg_productId);
            TextView _tvVID = (TextView)((RelativeLayout)view).findViewById(R.id.adapter_otg_vendorId);
            String sp = _tvPID.getText().toString();
            String sv = _tvVID.getText().toString();
            int pid = Integer.parseInt(_tvPID.getText().toString(), 16);
            int vid = Integer.parseInt(_tvVID.getText().toString(), 16);

            int moduleType = ReaderModule.checkOTGModule(vid, pid);
            if (moduleType != -1) {
                Intent _intent = new Intent(OTGService.OTG_ACTION_CONNECT);
                _intent.putExtra(OTGService.DEVICE_CHIP, moduleType);
                _intent.putExtra(OTGService.DEVICE_OTG, dev);
                mActivity.sendBroadcast(_intent);
            }
            else {
                mTextViewMsg.setText(getString(R.string.otg_msg_driver_no_support));
            }
        }
    };

    public class OTGMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case OTGService.OTG_ACTION_CHANGE_INTERFACE:
                    mTextViewMsg.setText(getString(R.string.otg_msg_stop_service));
                    mActivity.sendBroadcast(new Intent(OTGService.OTG_ACTION_SERVICE_STOP));
                    break;
                case OTGService.OTG_ACTION_CONNECTED:
                    mTextViewMsg.setText(intent.getExtras().getString(OTGService.STRING_DATA));
                    ((NfcActivity) mContext).interfaceCtrl(OTGService.INTERFACE_OTG, true);
                    break;
                case OTGService.OTG_ACTION_DISCONNECTED:
                    mTextViewMsg.setText(intent.getExtras().getString(OTGService.STRING_DATA));
                    ((NfcActivity) mContext).interfaceCtrl(OTGService.INTERFACE_OTG, false);
                    break;
                case OTGService.OTG_ACTION_SETTING_ERROR:
                    break;
            }
        }
    }
}
