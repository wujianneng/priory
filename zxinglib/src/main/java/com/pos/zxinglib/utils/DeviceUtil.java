package com.pos.zxinglib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class DeviceUtil {
    private static final String TAG = "DeviceUtil";
    public final static String IMSI = "IMSI";
    public final static String IMEI = "IMEI";
    public static float density = 0;
    public static int imgWidth = 0;
    public static int imgMargin = 0;

    public static float getDensity(Context context) {
        if (density < 0.01) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int getImgWidth() {
        return imgWidth;
    }

    public static int getImgMargin() {
        return imgMargin;
    }

    /**
     * dip转化成像素
     *
     * @param dipValue
     * @param scale
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean isWiFiActive(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI")
                            && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * sdcard是否可用
     *
     * @return
     */
    public static boolean isSdcardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断设备是否连接了网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean netSataus = false;
        try {
            ConnectivityManager cwjManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            cwjManager.getActiveNetworkInfo();

            if (cwjManager.getActiveNetworkInfo() != null) {
                netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return netSataus;
    }

    /**
     * 获取设备IMSI和IMEI号
     *
     * @param context
     * @return
     */
    public static Map<String, String> getIMSIAndIMEI(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();

        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        String imei = mTelephonyMgr.getDeviceId();

        map.put(IMSI, imsi);
        map.put(IMEI, imei);

        return map;
    }

    /**
     * 获取设备分辨率
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getScreenPixels(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.scaledDensity;
        imgWidth = (int) (metrics.widthPixels % 160 == 0 ? 75 * metrics.scaledDensity
                : 85 * metrics.scaledDensity);
        imgMargin = (int) (5 * density);
        return metrics;
    }

    /**
     * dip和px转换
     *
     * @param context
     * @param dipValue
     * @return
     */

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDensity(context) + 0.5f);
    }

    public static float px2dip(Context context, float pxValue) {
        return pxValue / getDensity(context) + 0.5f;
    }

    /**
     * 获取程序版本号
     *
     * @throws NameNotFoundException
     */
    public static float getAppVesionCode(Context context)
            throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(
                context.getPackageName(), Context.MODE_PRIVATE).versionCode;
    }

    /**
     * 获取程序版本名称
     *
     * @throws NameNotFoundException
     */
    public static String getAppVesionName(Context context)
            throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(
                context.getPackageName(), Context.MODE_PRIVATE).versionName;
    }

    /**
     * 获取系统版本号
     *
     * @throws NameNotFoundException
     */
    public static String getSystemVesionCode(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceSoftwareVersion();
    }

    /**
     * 获取设备类型(phone、pad)
     */
    public static String deviceStyle(Context context) {
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            return "phone";
        }

        return "pad";
    }

    // 获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    // 获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 判断手机系统版本号是否高于或等于4.4
     */
    public static boolean isHigherThan4_4() {
        String a;
        int b, c;
        a = android.os.Build.VERSION.RELEASE;
        b = Integer.parseInt(a.substring(0, 1));
        c = Integer.parseInt(a.substring(2, 3));
        if (b < 4) {
            return false;
        } else if (b > 4) {
            return true;
        } else if (b == 4) {
            if (c < 4) {
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * Check if this device has a camera
     * 检查设备是否有摄像头
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Check if this device has flashlight
     * 检查设备是否有闪光灯
     */
    public static boolean checkCameraHardwareHasFlashLight(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 返回当前屏幕是否为竖屏。  * @param context  * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


}
