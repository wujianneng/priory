package com.pos.priory.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.infitack.rxretorfit2library.DownloadListener;
import com.infitack.rxretorfit2library.RetrofitManager;
import com.pos.priory.networks.ApiService;

import java.io.File;

import io.reactivex.ObservableTransformer;


/**
 * Created by Li Chen on 2017/8/17.
 */

public class UpgradeUtils {
    public static final String TAG = "UpgradeUtils";

    public static boolean isFileNullOrTemp(File file) {
        if (file == null)
            return true;
        if (file.length() < 244)
            return true;
        if (file.getName().contains("temp"))
            return true;
        return false;
    }

    /**
     * 获取指定报名软件的版本号
     *
     * @param context
     * @param packagename
     * @return
     */
    public static int getVersionCode(Context context, String packagename) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(packagename, 0).versionCode;
        } catch (Exception e) {
            LogUtils.e(TAG, "异常:" + e);
            e.printStackTrace();
            versionCode = Integer.MAX_VALUE;
        }
        return versionCode;
    }

    /**
     * 根据url获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    public static void checkToUpdate(ObservableTransformer transformer, final Context context, int serverVersionCode, final String apkurl) {
        try {
            int launcher_loca_v = getVersionCode(context, context.getPackageName()); //本地版本号

            if (serverVersionCode > launcher_loca_v || launcher_loca_v == Integer.MAX_VALUE) {//本地版本小于服务器版本，或者找不到版本号（卸载了）
                if (!TextUtils.isEmpty(apkurl)) {
                    Log.e(TAG, "a1");
                    File parentFile = new File(context.getFilesDir().getAbsolutePath() + File.separator + "priory");
                    if (!parentFile.exists())
                        parentFile.mkdirs();
                    final File launcherFile = new File(parentFile.getAbsolutePath() + "/" + getFileNameFromUrl(apkurl));
                    final File tempLauncherFile = new File(parentFile.getAbsolutePath() + "/" + "temp" + getFileNameFromUrl(apkurl));
                    if (tempLauncherFile.exists()) {
                        tempLauncherFile.delete();
                    }
                    if (launcherFile.exists()) {
                        launcherFile.delete();
                    }
                    if (!launcherFile.exists()) {
                        Log.e(TAG, "a2:" + apkurl + " parent:" + tempLauncherFile.getParent() + " file:" + tempLauncherFile.getName());
                        RetrofitManager.excuteDownloadAndSaveToPath(transformer,
                                RetrofitManager.createGson(ApiService.class).download(apkurl),
                                tempLauncherFile, new DownloadListener() {
                                    @Override
                                    public void onProgress(float percent) {
                                        Log.e(TAG, "progress:" + percent);
                                    }

                                    @Override
                                    public void onDownloadComplete(File file) {
                                        Log.e("test", "onDownloadComplete:" + file.getTotalSpace());
                                        tempLauncherFile.renameTo(launcherFile);
                                        if (!(launcherFile == null) && !(isFileNullOrTemp(launcherFile))) {
                                            showIfInstallNewApkDialog(context, launcherFile);
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.e("test", "error:" + error.toString());
                                        if (tempLauncherFile != null && tempLauncherFile.exists()) {
                                            tempLauncherFile.delete();
                                        }
                                    }
                                });
                    }
                }
            }


        } catch (Exception e) {
            Log.e("test", "exception:" + e);
        }
    }


    //是否安装新版本apk的对话框
    private static void showIfInstallNewApkDialog(final Context context, final File file) {
        try {
            new AlertDialog.Builder(context)
                    .setTitle("下載安裝包完成，請點擊升級")
                    .setCancelable(false)
                    .setPositiveButton("升級", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //打开安装界面
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(context, "com.pos.priory.fileprovider", file);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                // 4.0及后续版本，需加此行代码，否则安装完后不进入APP界面
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(intent);
                        }
                    })
                    .create().show();
        } catch (Exception e) {
            LogUtils.i(TAG, "E:" + e.getMessage());
        }
    }

}