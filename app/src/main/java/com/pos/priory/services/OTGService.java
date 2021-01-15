package com.pos.priory.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.io.IOException;

import tw.com.prolific.driver.pl2303.PL2303Driver;

/**
 * Created by Bruce_Chiang on 2017/3/17.
 */

public class OTGService extends Service {

    private static final String OTG_ACTION_PERMISSION = "com.favepc.USB_PERMISSION";
    public static final String OTG_ACTION_SERVICE_START = "OTG_ACTION_SERVICE_START";
    public static final String OTG_ACTION_SERVICE_STOP = "OTG_ACTION_SERVICE_STOP";
    public static final String OTG_ACTION_CONNECT = "OTG_ACTION_CONNECT";
    public static final String OTG_ACTION_CONNECTED = "OTG_ACTION_CONNECTED";
    public static final String OTG_ACTION_SEND_DATA	= "OTG_ACTION_SEND_DATA";
    public static final String OTG_ACTION_RECEIVE_DATA	= "OTG_ACTION_RECEIVE_DATA";
    public static final String OTG_ACTION_DISCONNECT = "OTG_ACTION_DISCONNECT";
    public static final String OTG_ACTION_DISCONNECTED = "OTG_ACTION_DISCONNECTED";
    public static final String OTG_ACTION_DISCONNECTED_DEMO = "OTG_ACTION_DISCONNECTED_DEMO";
    public static final String OTG_ACTION_DISCONNECTED_DEMOUR = "OTG_ACTION_DISCONNECTED_DEMOUR";
    public static final String OTG_ACTION_DISCONNECTED_COMMON = "OTG_ACTION_DISCONNECTED_COMMON";
    public static final String OTG_ACTION_SETTING_ERROR = "OTG_ACTION_SETTING_ERROR";
    public static final String OTG_ACTION_CHANGE_INTERFACE = "OTG_ACTION_CHANGE_INTERFACE";

    public static final String INTERFACE_OTG = "INTERFACE_OTG";
    public static final String DEVICE_OTG = "DEVICE_OTG";
    public static final String DEVICE_MANAGER = "DEVICE_MANAGER";
    public static final String DEVICE_CHIP = "DEVICE_CHIP";
    public static final String BYTES_DATA = "BYTES_DATA";
    public static final String STRING_DATA = "STRING_DATA";

    private static final int OTG_DISP_CHAR = 0;
    private static final int OTG_LINEFEED_CODE_CRLF = 1;
    private static final int OTG_LINEFEED_CODE_LF = 2;
    private int mDisplayType = OTG_DISP_CHAR;
    private int mReadLinefeedCode = OTG_LINEFEED_CODE_LF;
    private int mWriteLinefeedCode = OTG_LINEFEED_CODE_LF;


    public static UsbManager mUsbManager;

    private MsgOTGReceiver mMsgOTGReceiver;
    private PL2303Driver mPL2303Driver = null;
    private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B38400;
    private PL2303Driver.DataBits mDataBits = PL2303Driver.DataBits.D8;
    private PL2303Driver.Parity mParity = PL2303Driver.Parity.NONE;
    private PL2303Driver.StopBits mStopBits = PL2303Driver.StopBits.S1;
    private PL2303Driver.FlowControl mFlowControl = PL2303Driver.FlowControl.OFF;
    private D2xxManager mFTDIDriver = null;
    private FT_Device mFTDIDevice = null;
    private int mFTDIDeviceCount = -1;
    private int mFTDICurrentIndex = -1;
    private boolean mOTGConnected = false;
    private boolean mOTGStop = true;
    private boolean	mOTGFreeTime = true;
    private Thread 	mReceiveThread;

    private enum OTGChip {
        OTHER,
        PL2303,
        FTXXX;
    }
    private OTGChip mOTGChip = OTGChip.OTHER;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mUsbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        if(mUsbManager == null){
            Log.e("test","mUsbManager == null");
        }else{
            Log.e("test","mUsbManager != null");
        }

        this.mMsgOTGReceiver = new MsgOTGReceiver();
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_SERVICE_START));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_SERVICE_STOP));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_CONNECT));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_SEND_DATA));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_DISCONNECT));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(OTG_ACTION_PERMISSION));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        registerReceiver(mMsgOTGReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mOTGConnected = false;
        this.mOTGStop = true;
        if (this.mReceiveThread != null) {
            this.mReceiveThread.interrupt();
            this.mReceiveThread = null;
        }
        if (this.mOTGChip == OTGChip.PL2303) {
            if (this.mPL2303Driver != null) {
                this.mPL2303Driver.end();
                this.mPL2303Driver = null;
            }
        }
        else if (this.mOTGChip == OTGChip.FTXXX) {
            if(this.mFTDIDevice != null) {
                synchronized(this.mFTDIDevice) {
                    if( true == this.mFTDIDevice.isOpen()) {
                        this.mFTDIDevice.close();
                    }
                }
            }
        }

        unregisterReceiver(mMsgOTGReceiver);
    }

    public class MsgOTGReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case OTG_ACTION_SERVICE_START:
                    break;
                case OTG_ACTION_SERVICE_STOP:
                    mOTGConnected = false;
                    mOTGStop = true;
                    if (mReceiveThread != null) {
                        mReceiveThread.interrupt();
                        mReceiveThread = null;
                    }
                    if (mOTGChip == OTGChip.PL2303) {
                        if (mPL2303Driver != null) {
                            mPL2303Driver.end();
                            mPL2303Driver = null;
                        }
                    }
                    else if (mOTGChip == OTGChip.FTXXX) {
                        if(mFTDIDevice != null) {
                            synchronized(mFTDIDevice) {
                                if( true == mFTDIDevice.isOpen()) {
                                    mFTDIDevice.close();
                                }
                            }
                        }
                    }
                    break;
                case OTG_ACTION_CONNECT:
                    switch (intent.getExtras().getInt(DEVICE_CHIP)) {
                        case ReaderModule.CHIP_PL2303:
                            connectTo(OTGChip.PL2303, (UsbDevice) intent.getExtras().get(DEVICE_OTG));
                            break;
                        case ReaderModule.CHIP_FTXXX:
                            connectTo(OTGChip.FTXXX, (UsbDevice) intent.getExtras().get(DEVICE_OTG));
                            break;
                    }
                    break;
                case OTG_ACTION_SEND_DATA:
                    byte[] _data = intent.getExtras().getByteArray(BYTES_DATA);
                    if (mOTGChip == OTGChip.PL2303) {
                        if (mPL2303Driver != null && mPL2303Driver.isConnected()) {
                            mPL2303Driver.write(_data, _data.length);
                        }
                        else {
                            mOTGConnected = false;
                        }
                    }
                    else if (mOTGChip == OTGChip.FTXXX)
                        if (mFTDIDevice.isOpen()) {
                            mFTDIDevice.setLatencyTimer((byte)16);
                            mFTDIDevice.write(_data, _data.length);
                        }
                        else {
                            mOTGConnected = false;
                        }
                    break;
                case OTG_ACTION_DISCONNECT:
                    break;
                case OTG_ACTION_PERMISSION:
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null) {
                            //connectToHasPermission(mOTGChip);
                        }
                     }
                     else {
                        sendBroadcast(OTG_ACTION_DISCONNECTED, "permission denied for device.");
                     }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    mOTGConnected = false;
                    mOTGStop = true;
                    if (mReceiveThread != null) {
                        mReceiveThread.interrupt();
                        mReceiveThread = null;
                    }
                    if (mOTGChip == OTGChip.PL2303) {
                        if (mPL2303Driver != null) {
                            mPL2303Driver.end();
                            mPL2303Driver = null;
                        }
                    }
                    else if (mOTGChip == OTGChip.FTXXX) {
                        mFTDIDeviceCount = -1;
                        mFTDICurrentIndex = -1;
                        try { Thread.sleep(50); }
                        catch (InterruptedException e) { e.printStackTrace(); }

                        if(mFTDIDevice != null) {
                            synchronized(mFTDIDevice) {
                                if( true == mFTDIDevice.isOpen()) {
                                    mFTDIDevice.close();
                                }
                            }
                        }
                    }
                    sendBroadcast(OTG_ACTION_DISCONNECTED, "device detached.");
                    sendBroadcast(OTG_ACTION_DISCONNECTED_DEMO, "device detached.");
                    sendBroadcast(OTG_ACTION_DISCONNECTED_DEMOUR, "device detached.");
                    sendBroadcast(OTG_ACTION_DISCONNECTED_COMMON, "device detached.");
                    break;
            }
        }
    }


    private synchronized void connectTo(OTGChip chip, UsbDevice device) {

        if (mOTGConnected) return;
        this.mOTGChip = chip;

        if (chip == OTGChip.PL2303) {
            if (this.mPL2303Driver != null) {
                this.mPL2303Driver.end();
                this.mPL2303Driver = null;
            }
            this.mPL2303Driver = new PL2303Driver((UsbManager)getSystemService(Context.USB_SERVICE), this, OTG_ACTION_PERMISSION);
            if (this.mPL2303Driver.enumerate()) {
                loadDefaultSettingValues(OTGChip.PL2303);
                runDataReceived(OTGChip.PL2303);
                this.mOTGConnected = true;
                sendBroadcast(OTG_ACTION_CONNECTED, "Connected to PL2303 Driver.");
            } else {
                sendBroadcast(OTG_ACTION_DISCONNECTED, "Cannot connect to PL2303 Driver.");
            }
        }
        else if (chip == OTGChip.FTXXX) {
            try {
                this.mFTDIDriver = D2xxManager.getInstance(this);
                if(!this.mFTDIDriver.setVIDPID(0x0403, 0xada1)) {
                    sendBroadcast(OTG_ACTION_SETTING_ERROR, "FTDIDriver.setVIDPID() error");
                    return;
                }
                int dc = this.mFTDIDriver.createDeviceInfoList(this);
                if (dc > 0) {
                    if( this.mFTDIDeviceCount != dc ) {
                        this.mFTDIDeviceCount = dc;
                    }
                    loadDefaultSettingValues(OTGChip.FTXXX);
                    runDataReceived(OTGChip.FTXXX);
                    this.mOTGConnected = true;
                    sendBroadcast(OTG_ACTION_CONNECTED, "Connected to FTDI Driver.");
                }
                else {
                    this.mFTDIDeviceCount = -1;
                    this.mFTDICurrentIndex = -1;
                    sendBroadcast(OTG_ACTION_DISCONNECTED, "Cannot connect to FTDI Driver.");
                }
            } catch (D2xxManager.D2xxException ex) { ex.printStackTrace(); }

        }
    }

    private synchronized void connectToHasPermission(OTGChip chip) {
        if (chip == OTGChip.PL2303) {
            if (this.mPL2303Driver != null) {
                this.mPL2303Driver.end();
                this.mPL2303Driver = null;
            }
            this.mPL2303Driver = new PL2303Driver((UsbManager)getSystemService(Context.USB_SERVICE), this, OTG_ACTION_PERMISSION);
            if (this.mPL2303Driver.enumerate()) {
                loadDefaultSettingValues(OTGChip.PL2303);
                runDataReceived(OTGChip.PL2303);
                this.mOTGChip = OTGChip.PL2303;
                this.mOTGConnected = true;
                sendBroadcast(OTG_ACTION_CONNECTED, "Connected to PL2303 Driver.");
            } else {
                this.mPL2303Driver.end();
                this.mPL2303Driver = null;
                sendBroadcast(OTG_ACTION_DISCONNECTED, "Cannot connect to PL2303 Driver.");
            }
        }
        else if (chip == OTGChip.FTXXX) {
            if(!this.mFTDIDriver.setVIDPID(0x0403, 0xada1)) {
                sendBroadcast(OTG_ACTION_SETTING_ERROR, "FTDIDriver.setVIDPID() error");
                return;
            }

            int dc = this.mFTDIDriver.createDeviceInfoList(this);
            if (dc > 0) {
                if( this.mFTDIDeviceCount != dc ) {
                    this.mFTDIDeviceCount = dc;
                }
                loadDefaultSettingValues(OTGChip.FTXXX);
                runDataReceived(OTGChip.FTXXX);
                this.mOTGConnected = true;
                sendBroadcast(OTG_ACTION_CONNECTED, "Connected to FTDI Driver.");
            }
            else {
                this.mFTDIDeviceCount = -1;
                this.mFTDICurrentIndex = -1;
                sendBroadcast(OTG_ACTION_DISCONNECTED, "Cannot connect to FTDI Driver.");
            }

        }
    }

    private void loadDefaultSettingValues(OTGChip chip) {
        if (chip == OTGChip.PL2303) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mPL2303Driver.isConnected()) {
                if (!mPL2303Driver.InitByBaudRate(PL2303Driver.BaudRate.B38400, 700)) {
                    if(!mPL2303Driver.PL2303Device_IsHasPermission()) {
                        Toast.makeText(this.getApplicationContext(), "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (!mPL2303Driver.PL2303Device_IsSupportChip())
                            Toast.makeText(this.getApplicationContext(), "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                    String res = pref.getString("display_list", Integer.toString(OTG_DISP_CHAR));
                    mDisplayType = Integer.valueOf(res);

                    res = pref.getString("readlinefeedcode_list", Integer.toString(OTG_LINEFEED_CODE_CRLF));
                    mReadLinefeedCode = Integer.valueOf(res);

                    res = pref.getString("writelinefeedcode_list", Integer.toString(OTG_LINEFEED_CODE_CRLF));
                    mWriteLinefeedCode = Integer.valueOf(res);

                    res = pref.getString("databits_list", PL2303Driver.DataBits.D8.toString());
                    mDataBits = PL2303Driver.DataBits.valueOf(res);

                    res = pref.getString("parity_list", PL2303Driver.Parity.NONE.toString());
                    mParity = PL2303Driver.Parity.valueOf(res);

                    res = pref.getString("stopbits_list", PL2303Driver.StopBits.S1.toString());
                    mStopBits = PL2303Driver.StopBits.valueOf(res);

                    res = pref.getString("flowcontrol_list", PL2303Driver.FlowControl.OFF.toString());
                    mFlowControl = PL2303Driver.FlowControl.valueOf(res);

                    res = pref.getString("baudrate_list", PL2303Driver.BaudRate.B38400.toString());
                    mBaudrate = PL2303Driver.BaudRate.valueOf(res);

                    try {
                        mPL2303Driver.setup(mBaudrate, mDataBits, mStopBits, mParity, mFlowControl);
                    } catch (IOException e) { e.printStackTrace(); }
                }
            }
            else {
                Toast.makeText(this.getApplicationContext(), "PL2303Driver.isConnected = false", Toast.LENGTH_SHORT).show();
            }
        }
        else if (chip == OTGChip.FTXXX) {
            if( null == mFTDIDevice) {
                mFTDIDevice = mFTDIDriver.openByIndex(this, 0);
            }
            else {
                synchronized(mFTDIDevice) {
                    mFTDIDevice = mFTDIDriver.openByIndex(this, 0);
                }
            }
            mFTDIDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
            mFTDIDevice.setBaudRate(38400);
            mFTDIDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
            mFTDIDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte)0xa, (byte)0xd);
        }
    }

    private void runDataReceived(final OTGChip chip) {

        this.mOTGStop = false;
        this.mReceiveThread = new Thread(new Runnable() {
            byte[] temp = new byte[256];
            int size;
            @Override
            public void run() {
                while (!mOTGStop) {
                    /*if (mOTGFreeTime) {
                        try {
                            Thread.sleep(300L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    try {
                        if(chip == OTGChip.PL2303) {
                            if (mPL2303Driver == null) return;
                            //temp = new byte[256];
                            size = mPL2303Driver.read(temp);
                            if (size > 0) {
                                byte[] buff = new byte[size];
                                System.arraycopy(temp, 0, buff, 0, size);
                                sendBroadcast(OTG_ACTION_RECEIVE_DATA, buff);
                            }
                        }
                        else {
                            synchronized(mFTDIDevice) {
                                size = mFTDIDevice.getQueueStatus();
                                if (size > 0) {
                                    temp = new byte[size];
                                    size = mFTDIDevice.read(temp, size);
                                    sendBroadcast(OTG_ACTION_RECEIVE_DATA, temp);
                                }
                            }
                        }
                        Thread.sleep(16);
                    } catch (Throwable e){
                        e.printStackTrace();}
                }
            }
        });
        this.mReceiveThread.start();


    }

    private void sendBroadcast(String action, @NonNull byte[] data) {
        Intent i = new Intent(action);
        i.putExtra(BYTES_DATA, data);
        sendBroadcast(i);
    }

    private void sendBroadcast(String action, @NonNull String data) {
        Intent i = new Intent(action);
        i.putExtra(STRING_DATA, data);
        sendBroadcast(i);
    }
}
