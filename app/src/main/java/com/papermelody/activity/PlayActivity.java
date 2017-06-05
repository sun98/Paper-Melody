package com.papermelody.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papermelody.R;
import com.papermelody.core.calibration.CalibrationResult;
import com.papermelody.core.calibration.TransformResult;
import com.papermelody.util.CanvasUtil;
import com.papermelody.util.ImageProcessor;
import com.papermelody.util.ImageUtil;
import com.papermelody.util.TapDetectorAPI;
import com.papermelody.util.ToastUtil;
import com.papermelody.util.ViewUtil;
import com.papermelody.widget.CameraDebugView;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by HgS_1217_ on 2017/3/18.
 */

public class PlayActivity extends BaseActivity {
    /**
     * 用例：演奏乐器
     * 弹奏乐器时的界面，可能存在虚拟乐器或曲谱等内容
     */

    @BindView(R.id.canvas_play)
    CameraDebugView canvasPlay;
    @BindView(R.id.view_play)
    SurfaceView viewPlay;
    @BindView(R.id.text_mode)
    TextView textViewMode;
    @BindView(R.id.text_instrument)
    TextView textViewInstrument;
    @BindView(R.id.text_mode_name)
    TextView textViewModeName;
    @BindView(R.id.text_instrument_name)
    TextView textViewInstrumentName;
    @BindView(R.id.btn_play_over)
    Button btnPlayOver;
    @BindView(R.id.key_effect_c3)
    LinearLayout keyC3;
    @BindView(R.id.key_effect_d3)
    LinearLayout keyD3;
    @BindView(R.id.key_effect_e3)
    LinearLayout keyE3;
    @BindView(R.id.key_effect_f3)
    LinearLayout keyF3;
    @BindView(R.id.key_effect_g3)
    LinearLayout keyG3;
    @BindView(R.id.key_effect_a3)
    LinearLayout keyA3;
    @BindView(R.id.key_effect_b3)
    LinearLayout keyB3;
    @BindView(R.id.key_effect_c4)
    LinearLayout keyC4;
    @BindView(R.id.key_effect_d4)
    LinearLayout keyD4;
    @BindView(R.id.key_effect_e4)
    LinearLayout keyE4;
    @BindView(R.id.key_effect_f4)
    LinearLayout keyF4;
    @BindView(R.id.key_effect_g4)
    LinearLayout keyG4;
    @BindView(R.id.key_effect_a4)
    LinearLayout keyA4;
    @BindView(R.id.key_effect_b4)
    LinearLayout keyB4;
    @BindView(R.id.key_effect_c5)
    LinearLayout keyC5;
    @BindView(R.id.key_effect_d5)
    LinearLayout keyD5;
    @BindView(R.id.key_effect_e5)
    LinearLayout keyE5;
    @BindView(R.id.key_effect_f5)
    LinearLayout keyF5;
    @BindView(R.id.key_effect_g5)
    LinearLayout keyG5;
    @BindView(R.id.key_effect_a5)
    LinearLayout keyA5;
    @BindView(R.id.key_effect_b5)
    LinearLayout keyB5;
    @BindView(R.id.key_effect_c3m)
    LinearLayout keyC3M;
    @BindView(R.id.key_effect_d3m)
    LinearLayout keyD3M;
    @BindView(R.id.key_effect_f3m)
    LinearLayout keyF3M;
    @BindView(R.id.key_effect_g3m)
    LinearLayout keyG3M;
    @BindView(R.id.key_effect_a3m)
    LinearLayout keyA3M;
    @BindView(R.id.key_effect_c4m)
    LinearLayout keyC4M;
    @BindView(R.id.key_effect_d4m)
    LinearLayout keyD4M;
    @BindView(R.id.key_effect_f4m)
    LinearLayout keyF4M;
    @BindView(R.id.key_effect_g4m)
    LinearLayout keyG4M;
    @BindView(R.id.key_effect_a4m)
    LinearLayout keyA4M;
    @BindView(R.id.key_effect_c5m)
    LinearLayout keyC5M;
    @BindView(R.id.key_effect_d5m)
    LinearLayout keyD5M;
    @BindView(R.id.key_effect_f5m)
    LinearLayout keyF5M;
    @BindView(R.id.key_effect_g5m)
    LinearLayout keyG5M;
    @BindView(R.id.key_effect_a5m)
    LinearLayout keyA5M;

    public static final int KEY_C3 = 0;
    public static final int KEY_D3 = 1;
    public static final int KEY_E3 = 2;
    public static final int KEY_F3 = 3;
    public static final int KEY_G3 = 4;
    public static final int KEY_A3 = 5;
    public static final int KEY_B3 = 6;
    public static final int KEY_C4 = 7;
    public static final int KEY_D4 = 8;
    public static final int KEY_E4 = 9;
    public static final int KEY_F4 = 10;
    public static final int KEY_G4 = 11;
    public static final int KEY_A4 = 12;
    public static final int KEY_B4 = 13;
    public static final int KEY_C5 = 14;
    public static final int KEY_D5 = 15;
    public static final int KEY_E5 = 16;
    public static final int KEY_F5 = 17;
    public static final int KEY_G5 = 18;
    public static final int KEY_A5 = 19;
    public static final int KEY_B5 = 20;
    public static final int KEY_C3M = 21;
    public static final int KEY_D3M = 22;
    public static final int KEY_F3M = 23;
    public static final int KEY_G3M = 24;
    public static final int KEY_A3M = 25;
    public static final int KEY_C4M = 26;
    public static final int KEY_D4M = 27;
    public static final int KEY_F4M = 28;
    public static final int KEY_G4M = 29;
    public static final int KEY_A4M = 30;
    public static final int KEY_C5M = 31;
    public static final int KEY_D5M = 32;
    public static final int KEY_F5M = 33;
    public static final int KEY_G5M = 34;
    public static final int KEY_A5M = 35;

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_INSTRUMENT = "EXTRA_INSTRUMENT";
    public static final String EXTRA_CATIGORY = "EXTRA_CATIGORY";
    public static final String EXTRA_OPERN = "EXTRA_OPERN";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private int mode, instrument, category, opern;
    private CalibrationResult calibrationResult;
    private LinearLayout[] keys = new LinearLayout[36];
    private int[] voiceResId = new int[]{R.raw.c3, R.raw.d3, R.raw.e3, R.raw.f3, R.raw.g3, R.raw.a3, R.raw.b3,
            R.raw.c4, R.raw.d4, R.raw.e4, R.raw.f4, R.raw.g4, R.raw.a4, R.raw.b4, R.raw.c5, R.raw.d5, R.raw.e5,
            R.raw.f5, R.raw.g5, R.raw.a5, R.raw.b5, R.raw.c3m, R.raw.d3m, R.raw.f3m, R.raw.g3m, R.raw.a3m,
            R.raw.c4m, R.raw.d4m, R.raw.f4m, R.raw.g4m, R.raw.a4m, R.raw.c5m, R.raw.d5m, R.raw.f5m, R.raw.g5m,
            R.raw.a5m};
    private int[] voiceId = new int[36];
    private SoundPool soundPool;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private SurfaceHolder surfaceHolder;
    private Handler childHandler, mainHandler;
    private String cameraID;
    private CameraCaptureSession cameraCaptureSession;
    private ImageReader imageReader;

    private ArrayList<Integer> lastKeys = new ArrayList<>();
    // this variable is used to prevent a same key to be played in a row
    // FIXME: it is only a temporary measure because real piano will play a long sound instead of one shot
    // FIXME: this vairable should be put into the class in responsible for playing sound, not here
    //    by gigaflw

    private final Handler viewStartHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            keys[i].clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.alpha_key_show);
            keys[i].setAlpha(1);
            keys[i].startAnimation(animation);
        }

        ;
    };

    private final Handler viewGoneHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            keys[i].clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(PlayActivity.this, R.anim.alpha_key_gone);
            keys[i].startAnimation(animation);
        }

        ;
    };

    private final Handler viewEndHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            keys[i].setAlpha(0);
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        mode = intent.getIntExtra(EXTRA_MODE, 0);
        instrument = intent.getIntExtra(EXTRA_INSTRUMENT, 0);
        category = intent.getIntExtra(EXTRA_CATIGORY, 0);
        //opern = intent.getIntExtra(EXTRA_OPERN, 0);
        calibrationResult = (CalibrationResult) intent.getSerializableExtra(CalibrationActivity.EXTRA_RESULT);

        initSoundPool();
        initView();
    }

    public void processImage(Image image) {
        /**
         * Process image here
         */
        TransformResult transformResult = ImageProcessor.getKeyTransform(calibrationResult);
        Mat mat = ImageUtil.imageToBgr(image);
        List<Integer> keys = ImageProcessor.getPlaySoundKey(mat.clone(), transformResult);

        List<List<Point>> ret = TapDetectorAPI.getAllForDebug(mat);
        canvasPlay.updateInfo(
                ret.get(0), ret.get(1), ret.get(2), ret.get(3), ret.get(4), 0, 0, 0);
//        Log.w("TESTK", "" + keys);
//        Log.w("LAST_TESTK", "" + lastKeys);
        for (Integer key : keys) {
            if (!lastKeys.contains(key)){
                playSound(key);
            }
        }
        lastKeys = new ArrayList<>(keys);
    }

    private void initSoundPool() {
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(10);
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        spb.setAudioAttributes(attrBuilder.build());
        soundPool = spb.build();
    }

    private void initSurfaceSize(double scalar) {
        /* 横屏导致长宽交换 */
        int width = ViewUtil.getScreenWidth(this);
        int height = (int) (width / scalar);
        FrameLayout.LayoutParams lp;
        lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        if (Build.VERSION.SDK_INT >= 24) {
            // TODO: Android 7.0 上貌似有自动图片适配功能，暂时不太确定，需要更多的测试情况
            height = ViewUtil.getScreenHeight(this);
            lp = new FrameLayout.LayoutParams(width, height);
            lp.gravity = Gravity.CENTER;
            FrameLayout.LayoutParams lpCanvas;
            lpCanvas = new FrameLayout.LayoutParams((int)(ViewUtil.getScreenHeight(this) * scalar), ViewUtil.getScreenHeight(this));
            lpCanvas.gravity = Gravity.CENTER;
            canvasPlay.setLayoutParams(lpCanvas);
        }
        viewPlay.setLayoutParams(lp);
        CanvasUtil.setSurfaceViewSize(width, height);
    }

    private void initView() {
        /**
         * 初始化界面上的文字标签、按键响应等等
         */

        switch (mode) {
            case 0:
                textViewModeName.setText(R.string.mode_free);
                //textViewOpern.setText("");
            case 1:
                textViewModeName.setText(R.string.mode_opern);
                //textViewOpern.setText("曲谱：" + getResources().getStringArray(R.array.spinner_opern)[opern]);
        }

        if (instrument == 0 && category == 0) {
            textViewInstrumentName.setText(R.string.piano_with_21_keys);
        } else if (instrument == 0 && category == 1) {
            textViewInstrumentName.setText("乐器：15键钢琴");
        } else {
            textViewInstrumentName.setText("乐器：7孔笛");
        }

        btnPlayOver.setOnClickListener((View v) -> {
            /*先假装生成一个midi文件（实际上是从assets里复制的），
              存放在应用的数据目录（data/data/com.papermelody/）下*/
            copyMusicToData();
            Intent intent = new Intent(this, PlayListenActivity.class);
            startActivity(intent);
            finish();
        });

        keys = new LinearLayout[] {keyC3, keyD3, keyE3, keyF3, keyG3, keyA3, keyB3, keyC4, keyD4,
                keyE4, keyF4, keyG4, keyA4, keyB4, keyC5, keyD5, keyE5, keyF5, keyG5, keyA5, keyB5,
                keyC3M, keyD3M, keyF3M, keyG3M, keyA3M, keyC4M, keyD4M, keyF4M, keyG4M, keyA4M,
                keyC5M, keyD5M, keyF5M, keyG5M, keyA5M};

        for (int i = 0; i < keys.length; ++i) {
            voiceId[i] = soundPool.load(this, voiceResId[i], 1);

            // FIXME: 等键盘配置好了再打开吧
            /*final int fi = i;
            keys[i].setOnClickListener((View v) -> {
                playSound(fi);
            });*/
        }

        initSurfaceView();
    }

    private void initSurfaceView() {
        /**
         * 初始化用于显示相机预览的surfaceView
         */

        surfaceHolder = viewPlay.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (cameraDevice != null) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }
        });
    }

    private void initCamera() {
        /**
         * 初始化相机，在这里设置相机的预览获取，尺寸等等
         */

        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraID = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);  //前摄像头

        // 设置imageReader的尺寸与采样频率
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            // 获取照相机可用的符合条件的最小像素图片
            Size relativeMin = ImageUtil.getRelativeMinSize(Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
                                                            viewPlay.getWidth(), viewPlay.getHeight());
            initSurfaceSize((double) relativeMin.getWidth()/relativeMin.getHeight());

            imageReader = ImageReader.newInstance(relativeMin.getWidth(), relativeMin.getHeight(), ImageFormat.YUV_420_888, 5);
            imageReader.setOnImageAvailableListener((reader) -> {
                    Image image = null;
                    try {
                        image = imageReader.acquireLatestImage();
                        if (image == null) {
                            return;
                        }
                        processImage(image);
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
            }, mainHandler);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraID, deviceStateCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        /* 摄像头创建监听 */

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            ToastUtil.showShort("开启成功");
            cameraDevice = camera;
            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            ToastUtil.showShort("摄像头开启失败");
        }
    };

    private void takePreview() {
        /* 开启预览 */

        try {
            // 创建预览需要的CaptureRequest.Builder
            final CaptureRequest.Builder previewRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 将SurfaceView的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(surfaceHolder.getSurface());
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            previewRequestBuilder.addTarget(imageReader.getSurface());
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(),
                    imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (null == cameraDevice) {
                        return;
                    }
                    // 当摄像头已经准备好时，开始显示预览
                    cameraCaptureSession = session;
                    try {
                        // 自动对焦
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // 打开闪光灯
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // 显示预览
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                        // 获取手机方向
                        int rotation = ViewUtil.getWindowRotation(PlayActivity.this);
                        // 根据设备方向计算设置照片的方向
                        previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    ToastUtil.showShort("配置失败");
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int keyID) {
        soundPool.play(voiceId[keyID], 1, 1, 0, 0, 1);

        // FIXME: 动画效果，当键盘配置好之后可以开启
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg1 = new Message(), msg2 = new Message(), msg3 = new Message();
                    msg1.what = keyID;
                    msg2.what = keyID;
                    msg3.what = keyID;
                    viewStartHandler.sendMessage(msg1);
                    Log.d("TEST", "THREAD1");
                    Thread.sleep(100);
                    viewGoneHandler.sendMessage(msg2);
                    Log.d("TEST", "THREAD2");
                    Thread.sleep(100);
                    viewEndHandler.sendMessage(msg3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_play;
    }

    private void copyMusicToData() {
        InputStream in = null;
        FileOutputStream out = null;
        String path = getApplicationContext().getFilesDir()
                .getAbsolutePath() + "/Kissbye.mid"; // data/data目录
        File file = new File(path);
        if (!file.exists()) {
            try {
                in = getAssets().open("Kissbye.mid"); // 从assets目录下复制
                out = new FileOutputStream(file);
                int length = -1;
                byte[] buf = new byte[1024];
                while ((length = in.read(buf)) != -1) {
                    out.write(buf, 0, length);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
        ToastUtil.showShort("文件已保存至data/data/com.papermelody/下");
    }
}
