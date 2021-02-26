package com.pos.priory.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pos.priory.R;
import com.pos.priory.fragments.DemoUFragment;
import com.pos.priory.fragments.OTGFragment;
import com.pos.priory.services.OTGService;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

public class NfcActivity extends AppCompatActivity {
    public static final String TAG_CI_OTG  = "OTG";
    public static final String TAG_CI_UNLINK = "UNLINK";
    private static String COMMUNICATION_INTERFACE = TAG_CI_UNLINK;
    private NotificationReceiver mNotificationReceiver;
    private int mConnectStatus = 0;
    FragmentTransaction ft;
    DemoUFragment demoUFragment;
    OTGFragment otgFragment;
    boolean isShowOtg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        startService(new Intent(this, OTGService.class));
        this.mNotificationReceiver = new NotificationReceiver();
        this.registerReceiver(mNotificationReceiver, new IntentFilter(OTGService.OTG_ACTION_RECEIVE_DATA));


        goDemoUFragment();
        ButterKnife.bind(this);
    }

    public void goDemoUFragment(){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if(demoUFragment == null) {
            demoUFragment = new DemoUFragment(this, this);
            ft.add(R.id.content_layout, demoUFragment).commit();
        }else {
            ft.hide(otgFragment);
            ft.show(demoUFragment).commit();
        }
        isShowOtg = false;
    }

    public void goOtgFragment(){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if(otgFragment == null) {
            otgFragment = new OTGFragment(this, this);
            ft.add(R.id.content_layout, otgFragment).commit();
        }else {
            ft.hide(demoUFragment);
            ft.show(otgFragment).commit();
        }
        isShowOtg = true;
    }

    @Override
    public void onBackPressed() {
        if(isShowOtg){
            goDemoUFragment();
        }else{
           super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, OTGService.class));
        unregisterReceiver(this.mNotificationReceiver);
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] _bData;

            switch (intent.getAction()) {
                case OTGService.OTG_ACTION_RECEIVE_DATA:
                    _bData = intent.getExtras().getByteArray(OTGService.BYTES_DATA);
                    synchronized (packets) {
                        packets.add(_bData);
                    }
                    break;
            }
        }
    }

    /**
     * otg = 1; ble = 2; wifi = 4;
     * */
    public void interfaceCtrl(@NonNull String cif, boolean onoff) {

        switch (cif) {
            case OTGService.INTERFACE_OTG:
                if (onoff) {
                    mConnectStatus |= 0x01;
                    COMMUNICATION_INTERFACE = TAG_CI_OTG;
                }
                else mConnectStatus &= 0xFE;

                break;
        }
    }

    /**
     *
     */
    public boolean isConnected() {
        return (mConnectStatus > 0) ? true : false;
    }

    public String getInterface() {
        return COMMUNICATION_INTERFACE;
    }

    /**
     *
     */
    private List<byte[]> packets = new LinkedList<byte[]>();

    /**
     * send data to interface
     *
     * @param localByte
     */
    public void sendData(byte[] localByte) {

        Intent _intent = null;

        synchronized (packets) {
            if (packets.size() > 0)
                packets.clear();
        }

        _intent = new Intent(OTGService.OTG_ACTION_SEND_DATA);
        _intent.putExtra(OTGService.BYTES_DATA, localByte);

        sendBroadcast(_intent);
    }

    /**
     *
     */
    public int checkData() {
        synchronized (packets) {
            return packets.size();
        }
    }

    /**
     *
     */
    public @Nullable
    byte[] getData() {
        byte[] buffer;
        synchronized (packets) {
            if (packets.size() > 0) {
                buffer = packets.get(0);
                //java.lang.IndexOutOfBoundsException at java.util.LinkedList.remove(LinkedList.java:660)
                packets.remove(0);
                //packets.clear();

                return buffer;
            } else return null;
        }
    }

}

