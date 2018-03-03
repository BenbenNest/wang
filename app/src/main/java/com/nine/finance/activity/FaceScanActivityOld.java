package com.nine.finance.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.facepp.sdk.Facepp;
import com.megvii.idcard.sdk.IDCard;
import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.face.bean.FaceActionInfo;
import com.nine.finance.face.bean.FeatureInfo;
import com.nine.finance.face.facecompare.FaceCompareManager;
import com.nine.finance.face.mediacodec.MediaHelper;
import com.nine.finance.face.util.CameraMatrix;
import com.nine.finance.face.util.MediaRecorderUtil;
import com.nine.finance.face.util.OpenGLDrawRect;
import com.nine.finance.face.util.OpenGLUtil;
import com.nine.finance.face.util.PointsMatrix;
import com.nine.finance.face.util.SensorEventUtil;
import com.nine.finance.idcard.util.ConUtil;
import com.nine.finance.idcard.util.DialogUtil;
import com.nine.finance.idcard.util.ICamera;
import com.nine.finance.idcard.util.IDCardIndicator;
import com.nine.finance.idcard.util.Util;
import com.nine.finance.utils.KeyUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class FaceScanActivityOld extends Activity implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    private TextureView textureView;
    private DialogUtil mDialogUtil;
    private ICamera mICamera;// 照相机工具类
    private IDCardIndicator mIndicatorView;
    private boolean mIsVertical = false, mIsDebug = false, isTextDetect = false, isClearShadow = false;
    private float faculaPass;// 光斑敏感度
    private TextView fps, fps_1;
    private TextView errorType, verticalType;
    private IDCard mIdCard;
    private HandlerThread mHandlerThread = new HandlerThread("hhh");
    private Handler mHandler;
    private ImageView image;
    private float setClear = 0.8f, setIdcard = -1f, setBound = 0.8f;
    private RelativeLayout barRel;
    private ProgressBar mBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idcardscan_layout);

        init();
    }

    private void init() {
        mIdCard = new IDCard();
        mIdCard.init(this, Util.readModel(this));
        setClear = getIntent().getFloatExtra("clear", 0.8f);
        setIdcard = getIntent().getFloatExtra("idcard", -1);
        setIdcard = 0.1f;
        setBound = getIntent().getFloatExtra("bound", 0.8f);
        faculaPass = getIntent().getFloatExtra("faculaPass", 0.3f);
        mIsVertical = getIntent().getBooleanExtra("isvertical", false);
        mIsDebug = getIntent().getBooleanExtra("isDebug", false);
        isTextDetect = getIntent().getBooleanExtra("isTextDetect", false);
        isClearShadow = getIntent().getBooleanExtra("isClearShadow", false);
        Log.w("ceshi", "setClear==" + setClear + ", setIdcard==" + setIdcard + ", setBound==" + setBound);
        Log.w("ceshi", "mIsVertical==" + mIsVertical + ", mIsDebug==" + mIsDebug + ", isTextDetect==" + isTextDetect);
        image = (ImageView) findViewById(R.id.image);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mICamera = new ICamera(mIsVertical);
        mDialogUtil = new DialogUtil(this);
        textureView = (TextureView) findViewById(R.id.idcardscan_layout_surface);
        textureView.setSurfaceTextureListener(this);
        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICamera.autoFocus();
            }
        });
        barRel = (RelativeLayout) findViewById(R.id.idcard_layout_barRel);
        fps = (TextView) findViewById(R.id.idcardscan_layout_fps);
        fps.setVisibility(View.VISIBLE);
        fps_1 = (TextView) findViewById(R.id.idcardscan_layout_fps_1);
        fps_1.setVisibility(View.VISIBLE);
        errorType = (TextView) findViewById(R.id.idcardscan_layout_error_type);
        verticalType = (TextView) findViewById(R.id.idcardscan_layout_verticalerror_type);
        mIndicatorView = (IDCardIndicator) findViewById(R.id.idcardscan_layout_indicator);
        mBar = (ProgressBar) findViewById(R.id.result_bar);
        if (mIsVertical) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            verticalType.setVisibility(View.VISIBLE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            errorType.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Camera mCamera = mICamera.openCamera(this);
        if (mCamera != null) {
            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam(this);
            textureView.setLayoutParams(layout_params);
            mIndicatorView.setLayoutParams(layout_params);

        } else {
            mDialogUtil.showDialog("打开摄像头失败");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mICamera.closeCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIdCard.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialogUtil.onDestory();
    }

    int width;
    int height;
    int orientation = 0;

    private void doPreview() {
        if (!mHasSurface)
            return;

        mICamera.startPreview(textureView.getSurfaceTexture());

        IDCard.IDCardConfig idCardConfig = mIdCard.getFaceppConfig();

        RectF rectF = mIndicatorView.getPosition();
        width = mICamera.cameraWidth;
        height = mICamera.cameraHeight;

        int left = (int) (width * rectF.left);
        int top = (int) (height * rectF.top);
        int right = (int) (width * rectF.right);
        int bottom = (int) (height * rectF.bottom);
        if (mIsVertical) {
            left = (int) (width * rectF.top);
            top = (int) (height * rectF.left);
            right = (int) (width * rectF.bottom);
            bottom = (int) (height * rectF.right);
            orientation = 180 - mICamera.orientation;
        }

        idCardConfig.orientation = orientation;
        idCardConfig.shadowAreaTh = 500;
        idCardConfig.faculaAreaTh = 500;
        idCardConfig.roi_left = left;
        idCardConfig.roi_top = top;
        idCardConfig.roi_right = right;
        idCardConfig.roi_bottom = bottom;

        mIdCard.setFaceppConfig(idCardConfig);
    }

    private boolean mHasSurface = false;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mHasSurface = true;
        doPreview();

        mICamera.actionDetect(this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mHasSurface = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    boolean isSuccess = false;
    private boolean isStartRecorder, is3DPose, isDebug, isROIDetect, is106Points, isBackCamera, isFaceProperty,
            isOneFaceTrackig, isFaceCompare, isShowFaceRect;
    private String trackModel;
    private int printTime = 31;
    private GLSurfaceView mGlSurfaceView;
    private Camera mCamera;
    private TextView debugInfoText, debugPrinttext, AttriButetext;
    private TextView featureTargetText;
    private ImageButton btnAddFeature;
    private Facepp facepp;
    private MediaRecorderUtil mediaRecorderUtil;
    private int min_face_size = 200;
    private int detection_interval = 25;
    private HashMap<String, Integer> resolutionMap;
    private SensorEventUtil sensorUtil;
    private float roi_ratio = 0.8f;
    private byte[] newestFeature;
    private byte[] carmeraImgData;

    private int screenWidth;
    private int screenHeight;
    private boolean isSurfaceCreated;

    private FaceActionInfo faceActionInfo;
    private ImageView imgIcon;

    private MediaHelper mMediaHelper;
    float confidence;
    float pitch, yaw, roll;
    long startTime;
    long time_AgeGender_end = 0;
    String AttriButeStr = "";
    private int Angle;

    int rotation = Angle;
    int preRotation = rotation;

    Facepp.Face[] compareFaces;

    long detectGenderAgeTime;
    final int DETECT_GENDER_INTERVAL = 1000;
    long featureTime = 0;
    private ArrayList<TextView> tvFeatures = new ArrayList<>();

    long matrixTime;
    private int prefaceCount = 0;
    private int mTextureID = -1;
    private SurfaceTexture mSurface;
    private CameraMatrix mCameraMatrix;
    private PointsMatrix mPointsMatrix;

    private void surfaceInit() {
        mTextureID = OpenGLUtil.createTextureID();

        mSurface = new SurfaceTexture(mTextureID);
        if (isStartRecorder) {
            mMediaHelper.startRecording(mTextureID);
        }
        // 这个接口就干了这么一件事，当有数据上来后会进到onFrameAvailable方法
//        mSurface.setOnFrameAvailableListener(this);// 设置照相机有数据时进入
        mCameraMatrix = new CameraMatrix(mTextureID);
        mPointsMatrix = new PointsMatrix(isFaceCompare);
        mPointsMatrix.isShowFaceRect = isShowFaceRect;
        mICamera.startPreview(mSurface);// 设置预览容器
        mICamera.actionDetect(this);
        if (isROIDetect)
            drawShowRect();
    }

    /**
     * 画绿色框
     */
    private void drawShowRect() {
        mPointsMatrix.vertexBuffers = OpenGLDrawRect.drawCenterShowRect(isBackCamera, mICamera.cameraWidth,
                mICamera.cameraHeight, roi_ratio);
    }


    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        if (isSuccess)
            return;
        isSuccess = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                long actionDetectTme = System.currentTimeMillis();
                IDCard.IDCardDetect iCardDetect = mIdCard.detect(data, width, height, IDCard.IMAGEMODE_NV21);
                final long DetectTme = System.currentTimeMillis() - actionDetectTme;
                final float in_bound = iCardDetect.inBound;
                final float is_idcard = iCardDetect.isIdcard;
                final float clear = iCardDetect.clear;
                String errorStr = "";

                if (clear < setClear)
                    errorStr = "请点击屏幕对焦";
                else if (in_bound < setBound)
                    errorStr = "请将身份证对准引导框";

                String fps_1Str = "";

                if (in_bound >= setBound && is_idcard >= setIdcard && clear >= setClear) {
                    long actionQualityTme = System.currentTimeMillis();
                    Log.w("ceshi", "faculaPass===" + faculaPass);
                    final IDCard.IDCardQuality idCardQuality = mIdCard.CalculateQuality(faculaPass);
                    drawFaculae(idCardQuality);
                    final long idCardQualityTme = System.currentTimeMillis() - actionQualityTme;

                    fps_1Str = "\nIdCardQualityTme: " + idCardQualityTme + "\nisfaculaePass: "
                            + idCardQuality.isfaculaePass + "\nfaculaeLenth: " + idCardQuality.faculaes.length
                            + "\nShadowLenth: " + idCardQuality.Shadows.length;
                    if (!idCardQuality.isfaculaePass)
                        errorStr = "有光斑";
                    if (idCardQuality.isfaculaePass && idCardQuality.isShadowPass) {
                        Bitmap bitmap = ConUtil.cutImage(mIndicatorView.getPosition(), data, mICamera.mCamera,
                                mIsVertical);
                        String path = ConUtil.saveBitmap(FaceScanActivityOld.this, bitmap);
//                        enterToResult(path, idCardQuality, clear, is_idcard, in_bound);
                        doOCR(path);
                    } else
                        isSuccess = false;
                } else {
                    drawFaculae(null);
                    isSuccess = false;
                }

                String fpsStr = "\nin_bound: " + in_bound + "\nis_idcard: " + is_idcard + "\nclear: " + clear
                        + "\nDetectTme: " + DetectTme;
                print(errorStr, fpsStr, fps_1Str);
            }
        });
    }

    private void onPreview(final byte[] imgData, final Camera camera){
        //检测操作放到主线程，防止贴点延迟
        int width = mICamera.cameraWidth;
        int height = mICamera.cameraHeight;

        long faceDetectTime_action = System.currentTimeMillis();
        final int orientation = sensorUtil.orientation;
        if (orientation == 0)
            rotation = Angle;
        else if (orientation == 1)
            rotation = 0;
        else if (orientation == 2)
            rotation = 180;
        else if (orientation == 3)
            rotation = 360 - Angle;


//        setConfig(rotation);

        final Facepp.Face[] faces = facepp.detect(imgData, width, height, Facepp.IMAGEMODE_NV21);
        final long algorithmTime = System.currentTimeMillis() - faceDetectTime_action;
        if (faces != null) {
            long actionMaticsTime = System.currentTimeMillis();
            ArrayList<ArrayList> pointsOpengl = new ArrayList<ArrayList>();
            ArrayList<FloatBuffer> rectsOpengl = new ArrayList<FloatBuffer>();
            if (faces.length > 0) {
                for (int c = 0; c < faces.length; c++) {

                    if (is106Points)
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK106);
                    else
                        facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK81);

                    if (is3DPose) {
                        facepp.get3DPose(faces[c]);
                    }

                    final Facepp.Face face = faces[c];
                    pitch = faces[c].pitch;
                    yaw = faces[c].yaw;
                    roll = faces[c].roll;
                    confidence = faces[c].confidence;


                    //0.4.7之前（包括）jni把所有角度的点算到竖直的坐标，所以外面画点需要再调整回来，才能与其他角度适配
                    //目前getLandmarkOrigin会获得原始的坐标，所以只需要横屏适配好其他的角度就不用适配了，因为texture和preview的角度关系是固定的
                    ArrayList<FloatBuffer> triangleVBList = new ArrayList<FloatBuffer>();
                    for (int i = 0; i < faces[c].points.length; i++) {
                        float x = (faces[c].points[i].x / width) * 2 - 1;
                        if (isBackCamera)
                            x = -x;
                        float y = (faces[c].points[i].y / height) * 2-1;
                        float[] pointf = new float[]{y, x, 0.0f};
                        FloatBuffer fb = mCameraMatrix.floatBufferUtil(pointf);
                        triangleVBList.add(fb);
                    }


                    pointsOpengl.add(triangleVBList);

                    if (mPointsMatrix.isShowFaceRect) {
                        facepp.getRect(faces[c]);
//                        FloatBuffer buffer = calRectPostion(faces[c].rect, mICamera.cameraWidth, mICamera.cameraHeight);
//                        rectsOpengl.add(buffer);
                    }
                }
            } else {
                pitch = 0.0f;
                yaw = 0.0f;
                roll = 0.0f;
            }

            synchronized (mPointsMatrix) {
                if (faces.length > 0 && is3DPose)
                    mPointsMatrix.bottomVertexBuffer = OpenGLDrawRect.drawBottomShowRect(0.15f, 0, -0.7f, pitch,
                            -yaw, roll, rotation);
                else
                    mPointsMatrix.bottomVertexBuffer = null;
                mPointsMatrix.points = pointsOpengl;
                mPointsMatrix.faceRects = rectsOpengl;
            }

            matrixTime = System.currentTimeMillis() - actionMaticsTime;

        }

        if (isSuccess)
            return;
        isSuccess = true;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (faces != null) {

                    confidence = 0.0f;
                    if (faces.length > 0) {


                        //compare ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tvFeatures.size() < faces.length) {
                                    int tvFeaturesSize = tvFeatures.size();
                                    for (int i = 0; i < faces.length - tvFeaturesSize; i++) {
                                        TextView textView = new TextView(FaceScanActivityOld.this);
                                        textView.setTextColor(0xff1a1d20);
                                        tvFeatures.add(textView);
                                    }
                                }
                                for (int i = prefaceCount; i < faces.length; i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).addView(tvFeatures.get(i));
                                }
                                for (int i = faces.length; i < tvFeatures.size(); i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).removeView(tvFeatures.get(i));
                                }
                                prefaceCount = faces.length;
                            }
                        });

                        for (int c = 0; c < faces.length; c++) {

                            final Facepp.Face face = faces[c];
                            if (isFaceProperty) {
                                long time_AgeGender_action = System.currentTimeMillis();
                                facepp.getAgeGender(faces[c]);
                                time_AgeGender_end = System.currentTimeMillis() - time_AgeGender_action;
                                String gender = "man";
                                if (face.female > face.male)
                                    gender = "woman";
                                AttriButeStr = "\nage: " + (int) Math.max(face.age, 1) + "\ngender: " + gender;
                            }


                            // 添加人脸比对
                            if (isFaceCompare) {
                                if (c == 0) {
                                    featureTime = System.currentTimeMillis();
                                }
                                if (facepp.getExtractFeature(face)) {
                                    synchronized (FaceScanActivityOld.this) {
                                        newestFeature = face.feature;
                                        carmeraImgData = imgData;

                                    }

                                    if (c == faces.length - 1) {
                                        compareFaces = faces;
                                    }

                                    final FeatureInfo featureInfo = FaceCompareManager.instance().compare(facepp, face.feature);

                                    final int index = c;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            featureTargetText = tvFeatures.get(index);
                                            if (featureInfo != null) {
                                                featureTargetText.setVisibility(View.VISIBLE);
                                                featureTargetText.setText(featureInfo.title);
                                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) featureTargetText.getLayoutParams();

                                                int txtWidth = featureTargetText.getWidth();
                                                int txtHeight = featureTargetText.getHeight();


                                                PointF noseP = null;
                                                PointF eyebrowP = null;
                                                if (is106Points){
                                                    noseP=face.points[46];
                                                    eyebrowP=face.points[37];
                                                }else{
                                                    noseP=face.points[34];
                                                    eyebrowP=face.points[19];
                                                }
                                                boolean isVertical;
                                                if (orientation==0||orientation==3){
                                                    isVertical=true;
                                                }else{
                                                    isVertical=false;
                                                }
                                                int tops= (int) (((mICamera.cameraWidth-(isVertical?eyebrowP.x:noseP.x)))*(mGlSurfaceView.getHeight()*1.0f/mICamera.cameraWidth));
                                                int lefts= (int) ((mICamera.cameraHeight-(isVertical?noseP.y:eyebrowP.y))*(mGlSurfaceView.getWidth()*1.0f/mICamera.cameraHeight));
                                                if (isBackCamera){
                                                    tops=mGlSurfaceView.getHeight()-tops;
                                                }
                                                tops=tops-txtHeight/2;
                                                lefts=lefts-txtWidth/2;
                                                params.leftMargin = lefts;
                                                params.topMargin = tops;
                                                featureTargetText.setLayoutParams(params);

                                            } else {

                                                featureTargetText.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    });

                                }
                                if (c == faces.length - 1) {
                                    featureTime = System.currentTimeMillis() - featureTime;
                                }

                            }


                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < tvFeatures.size(); i++) {
                                    ((RelativeLayout) mGlSurfaceView.getParent()).removeView(tvFeatures.get(i));
                                }
                                prefaceCount=0;
                            }
                        });
                        mPointsMatrix.rect = null;
                        compareFaces = null;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String logStr = "\ncameraWidth: " + mICamera.cameraWidth + "\ncameraHeight: "
                                    + mICamera.cameraHeight + "\nalgorithmTime: " + algorithmTime + "ms"
                                    + "\nmatrixTime: " + matrixTime + "\nconfidence:" + confidence;
                            debugInfoText.setText(logStr);
                            if (faces.length > 0 && isFaceProperty && AttriButeStr != null && AttriButeStr.length() > 0)
                                AttriButetext.setText(AttriButeStr + "\nAgeGenderTime:" + time_AgeGender_end);
                            else
                                AttriButetext.setText("");
                        }
                    });
                } else {
                    compareFaces = null;
                }
                isSuccess = false;

            }
        });
    }

    private void drawFaculae(final IDCard.IDCardQuality idCardQuality) {
        if (!mIsDebug)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIndicatorView.setiCardQuality(idCardQuality);
            }
        });
    }

    private void print(final String errorStr, final String fpsStr, final String fps_1Str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorType.setText(errorStr);
                verticalType.setText(errorStr);
                if (mIsDebug) {
                    fps.setText(fpsStr);
                    fps_1.setText(fps_1Str);
                }
            }
        });
    }

    private void enterToResult(String path, IDCard.IDCardQuality iCardQuality, float clear, float is_idcard, float in_bound) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barRel.setVisibility(View.VISIBLE);
                errorType.setText("");
                verticalType.setText("");
            }
        });
//        Intent intent = new Intent(this, IDCardResultActivity.class);
        Intent intent = new Intent();
        intent.putExtra("iCardQuality", iCardQuality);
        intent.putExtra("path", path);
        intent.putExtra("clear", clear);
        intent.putExtra("is_idcard", is_idcard);
        intent.putExtra("in_bound", in_bound);
        intent.putExtra("isClearShadow", isClearShadow);
        intent.putExtra("isTextDetect", isTextDetect);
//        startActivity(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void enterToResult(String path, String info) {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        intent.putExtra("info", info);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 对身份证照片做ocr，然后发现是正面照片，那么利用 face/extract 接口进行人脸检测，如果是背面，直接弹出对话框
     */
    public void doOCR(final String path) {
        showBar(true);
        try {
            String url = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
            RequestParams rParams = new RequestParams();
            Log.w("ceshi", "Util.API_OCRKEY===" + Util.API_SECRET + ", Util.API_OCRSECRET===" + Util.API_SECRET);
            rParams.put("api_key", KeyUtil.API_KEY);
            rParams.put("api_secret", KeyUtil.API_SECRET);
            rParams.put("image_file", new File(path));
            rParams.put("legality", 1 + "");
            AsyncHttpClient asyncHttpclient = new AsyncHttpClient();
            asyncHttpclient.post(url, rParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {
                    showBar(false);

                    try {
                        String successStr = new String(responseByte);
                        Log.w("ceshi", "ocr  onSuccess: " + successStr);
                        String info = "";
                        JSONObject jObject = new JSONObject(successStr).getJSONArray("cards").getJSONObject(0);
                        if ("back".equals(jObject.getString("side"))) {
                            IDCardActivity.mIsDirect = false;
                            AppGlobal.mIDCardBack = jObject;
                            String officeAdress = jObject.getString("issued_by");
                            String useful_life = jObject.getString("valid_date");
                            info = info + "officeAdress:  " + officeAdress + "\nuseful_life:  " + useful_life;
                        } else {
                            IDCardActivity.mIsDirect = true;
                            AppGlobal.mIDCardFront = jObject;
                            String address = jObject.getString("address");
                            String birthday = jObject.getString("birthday");
                            String gender = jObject.getString("gender");
                            String id_card_number = jObject.getString("id_card_number");
                            String name = jObject.getString("name");
                            Log.w("ceshi", "doOCR+++idCardBean.id_card_number===" + id_card_number + ", idCardBean.name===" + name);
                            String race = jObject.getString("race");
                            String side = jObject.getString("side");
                            JSONObject legalityObject = jObject.getJSONObject("legality");

                            info = info + "name:  " + name
                                    + "\nid_card_number:  " + id_card_number
                                    + "\ngender:  " + gender + "\nrace:  "
                                    + race + "\nbirthday:  " + birthday
                                    + "\naddress:  " + address;

                            String checkError = "\n";
                            try {
                                float edited = Float.parseFloat(legalityObject.getString("Edited"));
                                float ID_Photo = Float.parseFloat(legalityObject
                                        .getString("ID Photo"));
                                float Photocopy = Float.parseFloat(legalityObject.getString("Photocopy"));
                                float Screen = Float.parseFloat(legalityObject.getString("Screen"));
                                float Temporary_ID_Photo = Float.parseFloat(legalityObject.getString("Temporary ID Photo"));
                                checkError = checkError + "\nedited:  "
                                        + edited + "\nID_Photo:  " + ID_Photo
                                        + "\nPhotocopy:  " + Photocopy
                                        + "\nScreen:  " + Screen
                                        + "\nTemporary_ID_Photo:  "
                                        + Temporary_ID_Photo;
                            } catch (Exception e) {
                            }
                            info = info + checkError;
                        }
                        enterToResult(path, successStr);
//                        contentText.setText(contentText.getText().toString() + info);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showBar(false);
                        ConUtil.showToast(FaceScanActivityOld.this, "识别失败，请重新识别！");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        Log.w("ceshi", "responseBody==="
                                + new String(responseBody));
                    }
                    showBar(false);
                    ConUtil.showToast(FaceScanActivityOld.this, "识别失败，请重新识别！");
                }
            });
        } catch (FileNotFoundException e1) {
            showBar(false);
            e1.printStackTrace();
            ConUtil.showToast(FaceScanActivityOld.this, "识别失败，请重新识别！");
        }
    }

    private void showBar(final boolean flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    mBar.setVisibility(View.VISIBLE);
                } else {
                    mBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public boolean isEven01(int num) {
        if (num % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }


}
