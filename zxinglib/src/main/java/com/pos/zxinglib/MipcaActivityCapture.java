package com.pos.zxinglib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.pos.zxinglib.camera.CameraManager;
import com.pos.zxinglib.decoding.CaptureActivityHandler;
import com.pos.zxinglib.decoding.InactivityTimer;
import com.pos.zxinglib.utils.DeviceUtil;
import com.pos.zxinglib.utils.PermissionsManager;
import com.pos.zxinglib.utils.PermissionsResultAction;
import com.pos.zxinglib.utils.ResultHandlerFactory;
import com.pos.zxinglib.view.ViewfinderView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang & WJNeng
 */
public class MipcaActivityCapture extends AppCompatActivity implements
        Callback {
    public String SAVE_BITMAP_DIR;
    private boolean isHigherThan4_4;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;

    private String currentTime;

    private String barcodePath;

    private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;

    static {
        DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
        DISPLAYABLE_METADATA_TYPES
                .add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
    }

    private SharedPreferences sharedPreferences;
    private boolean canOneCode, canQrCode, canMatrixCode, canBeep, canVibrate,
            canLight, canSeriesScan;

    ImageView btnBack, btnFlashlight;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!DeviceUtil.isScreenOriatationPortrait(this))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        SAVE_BITMAP_DIR = getExternalFilesDir("barcode").getPath();
        isHigherThan4_4 = Build.VERSION.SDK_INT >= 19;
        sharedPreferences = getSharedPreferences("QRcode", MODE_PRIVATE);
        initSharedData();

        if (isHigherThan4_4 && DeviceUtil.isScreenOriatationPortrait(this)) {
            initNocationBar();
        }
        setContentView(R.layout.activity_capture);
        CameraManager.release();
        CameraManager.init(getApplication());
        initView();
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {
                String message = String.format(Locale.getDefault(), getString(R.string.message_denied), permission);
                Toast.makeText(MipcaActivityCapture.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }


    private void initSharedData() {
        canOneCode = sharedPreferences
                .getBoolean("preferences_decode_1D", true);
        canQrCode = sharedPreferences.getBoolean("preferences_decode_QR", true);
        canMatrixCode = sharedPreferences.getBoolean(
                "preferences_decode_Data_Matrix", true);
        canBeep = sharedPreferences.getBoolean("preferences_play_beep", true);
        canVibrate = sharedPreferences.getBoolean("preferences_vibrate", true);
        canLight = sharedPreferences
                .getBoolean("preferences_front_light", true);
        canSeriesScan = sharedPreferences.getBoolean("preferences_bulk_mode",
                false);

    }

    private void initNocationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(android.R.color.transparent);// 通知栏所需颜色

    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @SuppressLint("NewApi")
    private void initView() {
        CameraManager.get().resetFramingRect();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        btnBack = (ImageView) findViewById(R.id.back_btn);
        btnFlashlight = (ImageView) findViewById(R.id.flashlight_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOrCloseFlashLight(canLight, MipcaActivityCapture.this);
            }
        });
    }


    private void openOrCloseFlashLight(boolean canLight, Context context) {
        CameraManager.get().openOrCloseFlashLight(canLight, context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        decodeFormats = null;
        characterSet = null;

        playBeep = canBeep;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        initSharedData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (CameraManager.get() != null)
            CameraManager.get().closeDriver();

    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        CameraManager.release();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     * @throws IOException
     */
    public void handleDecode(Result result, Bitmap barcode) {
        try {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                    DateFormat.SHORT);
            currentTime = formatter.format(new Date(result.getTimestamp()));
            Map<ResultMetadataType, Object> metadata = (Map<ResultMetadataType, Object>) result
                    .getResultMetadata();
            StringBuilder metadataText = null;
            String meta = "";
            if (metadata != null) {
                metadataText = new StringBuilder(20);
                for (Map.Entry<ResultMetadataType, Object> entry : metadata
                        .entrySet()) {
                    if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                        metadataText.append(entry.getValue()).append('\n');
                    }
                }
                meta = metadataText.toString();
            }


            String type = ResultHandlerFactory.makeResultHandler(this, result);
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            String resultString = result.getText();
            String format = result.getBarcodeFormat().toString();
//            saveBarcodeBitmap("", barcode);
            if ("CODE_128".equalsIgnoreCase(result.getBarcodeFormat()
                    .toString())) { // 如果是code128码，且长度为14位，且以0开头，则删除0的开头
                if (!"".equals(resultString) && resultString.length() == 14
                        && resultString.startsWith("0")) {
                    resultString = resultString.substring(1,
                            resultString.length());
                }
            }

            if (resultString.equals("")) {
                Toast.makeText(MipcaActivityCapture.this, "Scan failed!",
                        Toast.LENGTH_SHORT).show();
            }
            if (!canSeriesScan) {
                Intent intent = new Intent();
                intent.putExtra("resultString", resultString);
                intent.putExtra("resultBitmapPath", barcodePath);
                intent.putExtra("time", currentTime);
                intent.putExtra("meta", meta);
                intent.putExtra("format", format);
                intent.putExtra("type", type);
                intent.putExtra("tag", 0);
                setResult(1, intent);
                MipcaActivityCapture.this.finish();
            } else {
                Intent intent = new Intent(MipcaActivityCapture.this,
                        MipcaActivityCapture.class);
                this.startActivity(intent);
                MipcaActivityCapture.this.finish();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void saveBarcodeBitmap(String bitName, Bitmap bm)
            throws IOException {
        try {
            File temp = new File(SAVE_BITMAP_DIR);
            if (!temp.exists()) {
                temp.mkdir();
            }
            barcodePath = SAVE_BITMAP_DIR + bitName + ".png";
            File f = new File(barcodePath);

            FileOutputStream fOut = null;

            fOut = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet, canOneCode, canQrCode, canMatrixCode);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        if (viewfinderView != null)
            viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (canBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (canVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO 自动生成的方法存根
        super.onActivityResult(requestCode, resultCode, data);
    }

}