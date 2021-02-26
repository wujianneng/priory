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
 * V1.0			2017/3/24     	Bruce	1.First create version
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.pos.priory.R;
import com.pos.priory.activitys.NfcActivity;
import com.pos.priory.adapters.DemoUListAdapter;
import com.pos.priory.beans.DemoU;
import com.pos.priory.services.OTGService;
import com.pos.priory.services.ReaderService;

import java.math.BigInteger;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DemoUFragment extends Fragment {

    private Context mContext;
    private Activity mActivity;
    private View mDemoView = null;
    private DemoUListAdapter mDemoUListAdapter;
    private ArrayList<DemoU> mDemoUs = new ArrayList<DemoU>();

    private Button mBtnStart, mBtnClear;
    private TextView mTextViewCount;
    private ProgressBar mProgressBar;
    private boolean m_bDemoUToggle = false;
    private boolean m_bDemoUAutoDetect = false;
    private int mRunCount = 0;

    private ReaderService mReaderService;
    private DemoMsgReceiver mDemoMsgReceiver;

    public DemoUFragment() {
        super();
    }

    public DemoUFragment(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = getActivity();
        this.mReaderService = new ReaderService();
        this.mDemoMsgReceiver = new DemoMsgReceiver();
        this.mContext.registerReceiver(mDemoMsgReceiver, new IntentFilter(OTGService.OTG_ACTION_DISCONNECTED_DEMO));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.mDemoView == null) {
            this.mDemoView = inflater.inflate(R.layout.fragment_demou, container, false);
            this.mDemoUListAdapter = new DemoUListAdapter(this.mContext, R.layout.adapter_demou, this.mDemoUs);
            ListView lv = (ListView) mDemoView.findViewById(R.id.demou_lvTags);
            lv.setAdapter(this.mDemoUListAdapter);

            this.mTextViewCount = (TextView) this.mDemoView.findViewById(R.id.demou_tvcount);

            this.mBtnStart = (Button) this.mDemoView.findViewById(R.id.demou_btnStart);
            this.mBtnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!m_bDemoUToggle) {
                        if (((NfcActivity) mContext).isConnected()) {
                            //stopAutoReceiveBackground();
                            m_bDemoUAutoDetect = false;
                            mAutoHandler.removeCallbacks(mRunnableAutoBackground);

                            m_bDemoUToggle = true;
                            mHandler.post(mRunnableBackground);

                            mBtnStart.setText("STOP");
                            mProgressBar.setVisibility(View.VISIBLE);
                        } else {
                            ((NfcActivity) mContext).goOtgFragment();
//                            Toast.makeText(mContext, "All of the communication interface are unlinked.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        init();
                        //startAutoReceiveBackground();
                        m_bDemoUAutoDetect = true;
                        mAutoHandler.post(mRunnableAutoBackground);
                    }
                }
            });

            this.mBtnClear = (Button) this.mDemoView.findViewById(R.id.demou_btnClear);
            this.mBtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRunCount = 0;
                    if (mDemoUs != null)
                        mDemoUs.clear();
                    if (mDemoUListAdapter != null)
                        mDemoUListAdapter.notifyDataSetChanged();
                    mTextViewCount.setText("");


                }
            });

            this.mProgressBar = (ProgressBar) this.mDemoView.findViewById(R.id.demou_progressBar);
            this.mProgressBar.setVisibility(View.GONE);

        }

        return this.mDemoView;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            init();
            m_bDemoUAutoDetect = false;
            mAutoHandler.removeCallbacks(mRunnableAutoBackground);
        } else {
            if (((NfcActivity) mContext).isConnected()) {
                m_bDemoUAutoDetect = true;
                mAutoHandler.post(mRunnableAutoBackground);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        init();
        m_bDemoUAutoDetect = false;
        mAutoHandler.removeCallbacks(mRunnableAutoBackground);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext.unregisterReceiver(mDemoMsgReceiver);
        mContext = null;
    }

    /**
     *
     */
    public class DemoMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case OTGService.OTG_ACTION_DISCONNECTED_DEMO:
                    if (m_bDemoUToggle) {
                        init();
                        m_bDemoUAutoDetect = false;
                        mAutoHandler.removeCallbacks(mRunnableAutoBackground);
                        Toast.makeText(mContext, intent.getExtras().getString(OTGService.STRING_DATA), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }


    /***
     *
     */
    private void init() {

        mBtnStart.setText("RUN");
        mProgressBar.setVisibility(View.GONE);
        m_bDemoUToggle = false;
        mHandler.removeCallbacks(mRunnableBackground);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean _bCompare = false;

            switch (msg.what) {
                case 1:
                    //mTextViewCount.setText(Integer.toString((int)msg.obj));
                    break;
                case 2:
                    String[] array = ((String) msg.obj).split(ReaderService.COMMAND_END);

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].regionMatches(1, "U", 0, 1) && array[i].length() >= 4) {

                            final String str = array[i].substring(2, array[i].length());
                            if (str.indexOf("U") != -1)
                                break;
                            try {
                                if (ReaderService.Format.crc16(new BigInteger(str, 16).toByteArray()) != 0x1D0F)
                                    break;
                            } catch (NumberFormatException ex) {
                                Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            DemoU _demoU = new DemoU(
                                    str.substring(0, 4),
                                    str.substring(4, str.length() - 4),
                                    str.substring(str.length() - 4));

                            if (mDemoUListAdapter.getCount() > 0) {
                                for (int j = 0; j < mDemoUListAdapter.getCount(); j++) {
                                    if (mDemoUs.get(j).EPC().equals(_demoU.EPC()) && mDemoUs.get(j).CRC16().equals(_demoU.CRC16())) {

                                        int _number = Integer.valueOf(mDemoUs.get(j).Count()) + 1;
                                        _demoU.Count(String.valueOf(_number));
                                        _demoU.Percentage(String.valueOf((int) (_number * 100 / mRunCount)) + "%");
                                        updateView(false, j, _demoU);
                                        _bCompare = true;
                                        break;
                                    } else {
                                        _bCompare = false;
                                    }
                                }
                            } else {
                                _bCompare = true;
                                _demoU.Count(String.valueOf(1));
                                _demoU.Percentage(String.valueOf((int) (1 * 100 / mRunCount)) + "%");
                                updateView(true, 0, _demoU);
                            }
                            if (!_bCompare) {
                                _demoU.Count(String.valueOf(1));
                                _demoU.Percentage(String.valueOf((int) (1 * 100 / mRunCount)) + "%");
                                updateView(true, mDemoUListAdapter.getCount(), _demoU);
                            }
                        } else {
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Handler mAutoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean _bCompare = false;

            switch (msg.what) {
                case 1:
                    //mTextViewCount.setText(Integer.toString((int)msg.obj));
                    break;
                case 2:
                    String[] array = ((String) msg.obj).split(ReaderService.COMMAND_END);

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].regionMatches(1, "U", 0, 1) && array[i].length() >= 4) {

                            final String str = array[i].substring(2, array[i].length());
                            if (str.indexOf("U") != -1)
                                break;
                            try {
                                if (ReaderService.Format.crc16(new BigInteger(str, 16).toByteArray()) != 0x1D0F)
                                    break;
                            } catch (NumberFormatException ex) {
                                Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            DemoU _demoU = new DemoU(
                                    str.substring(0, 4),
                                    str.substring(4, str.length() - 4),
                                    str.substring(str.length() - 4));

                            if (mDemoUListAdapter.getCount() > 0) {
                                for (int j = 0; j < mDemoUListAdapter.getCount(); j++) {
                                    if (mDemoUs.get(j).EPC().equals(_demoU.EPC()) && mDemoUs.get(j).CRC16().equals(_demoU.CRC16())) {

                                        int _number = Integer.valueOf(mDemoUs.get(j).Count()) + 1;
                                        _demoU.Count(String.valueOf(_number));
                                        _demoU.Percentage(String.valueOf((int) (_number * 100 / mRunCount)) + "%");
                                        updateView(false, j, _demoU);
                                        _bCompare = true;
                                        break;
                                    } else {
                                        _bCompare = false;
                                    }
                                }
                            } else {
                                _bCompare = true;
                                _demoU.Count(String.valueOf(1));
                                _demoU.Percentage(String.valueOf((int) (1 * 100 / mRunCount)) + "%");
                                updateView(true, 0, _demoU);
                            }
                            if (!_bCompare) {
                                _demoU.Count(String.valueOf(1));
                                _demoU.Percentage(String.valueOf((int) (1 * 100 / mRunCount)) + "%");
                                updateView(true, mDemoUListAdapter.getCount(), _demoU);
                            }
                        } else {
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * @param action   true is add view, otherwise is set view.
     * @param position view position.
     * @param demoU    demoU class
     */
    private void updateView(boolean action, int position, DemoU demoU) {

        if (action)
            mDemoUs.add(position, demoU);
        else
            mDemoUs.set(position, demoU);
        mDemoUListAdapter.notifyDataSetChanged();
    }

    private Runnable mRunnableBackground = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                int timeOut = 100;
                int index;
                byte[] bsData;
                String strData;
                String strSubData = "";

                @Override
                public void run() {

                    while (m_bDemoUToggle) {
                        //[TX]
                        mRunCount++;
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                mTextViewCount.setText(Integer.toString(mDemoUs.size()));
                            }
                        });

                        if (!((NfcActivity) mContext).isConnected()) {
                            mActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    init();
                                }
                            });
                            return;
                        }
                        ((NfcActivity) mContext).sendData(mReaderService.U());

                        //[RX]
                        timeOut = 100;
                        while (timeOut > 1) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (((NfcActivity) mContext).checkData() > 0) {
                                bsData = ((NfcActivity) mContext).getData();
                                if (bsData == null) return;
                                if (bsData.length > 0) {
                                    strData = new String(bsData);
                                    if (strSubData.length() > 0) {
                                        strData = strSubData + strData;
                                        strSubData = "";
                                    }
                                    if ((index = strData.indexOf(ReaderService.COMMANDU_END)) != -1) {
                                        Message msg = new Message();
                                        msg.what = 2;
                                        msg.obj = strData.substring(0, index);
                                        mHandler.sendMessage(msg);
                                        timeOut = 1;
                                    } else {
                                        strSubData = strData;
                                        timeOut++;
                                    }
                                }
                            }
                            timeOut--;
                        }
                    }
                }
            }).start();
        }
    };

    private Runnable mRunnableAutoBackground = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                int index;
                byte[] bsData;
                String strData;
                String strSubData = "";

                @Override
                public void run() {

                    while (m_bDemoUAutoDetect) {
                        if (!((NfcActivity) mContext).isConnected()) {
                            mActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    init();
                                    m_bDemoUAutoDetect = false;
                                    mAutoHandler.removeCallbacks(mRunnableAutoBackground);
                                }
                            });
                            return;
                        }

                        //[RX]
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (((NfcActivity) mContext).checkData() > 0) {
                            bsData = ((NfcActivity) mContext).getData();
                            if (bsData == null) return;
                            if (bsData.length > 0) {
                                strData = new String(bsData);
                                if (strSubData.length() > 0) {
                                    strData = strSubData + strData;
                                    strSubData = "";
                                }
                                if ((index = strData.indexOf(ReaderService.COMMANDU_END)) != -1) {
                                    mRunCount++;
                                    mActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            mTextViewCount.setText(Integer.toString(mDemoUs.size()));
                                        }
                                    });
                                    Message msg = new Message();
                                    msg.what = 2;
                                    msg.obj = strData.substring(0, index);
                                    mAutoHandler.sendMessage(msg);
                                } else {
                                    strSubData = strData;
                                }
                            }
                        }
                    }
                }
            }).start();
        }
    };
}
