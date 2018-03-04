package com.nine.finance.idcard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.idcard.sdk.IDCard;
import com.megvii.idcard.sdk.IDCard.IDCardConfig;
import com.megvii.idcard.sdk.IDCard.IDCardDetect;
import com.megvii.idcard.sdk.IDCard.IDCardQuality;
import com.nine.finance.R;
import com.nine.finance.activity.IDCardActivity;
import com.nine.finance.app.AppGlobal;
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

public class IDCardScanActivity extends Activity implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

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
        try {
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
            textureView.setOnClickListener(new OnClickListener() {
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
        } catch (Exception e) {

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
        try {
            mICamera.startPreview(textureView.getSurfaceTexture());

            IDCardConfig idCardConfig = mIdCard.getFaceppConfig();

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
        } catch (Exception e) {

        }
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

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        if (isSuccess)
            return;
        isSuccess = true;
        try {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    long actionDetectTme = System.currentTimeMillis();
                    IDCardDetect iCardDetect = mIdCard.detect(data, width, height, IDCard.IMAGEMODE_NV21);
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
                        final IDCardQuality idCardQuality = mIdCard.CalculateQuality(faculaPass);
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
                            String path = ConUtil.saveBitmap(IDCardScanActivity.this, bitmap);
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
        } catch (Exception e) {

        }
    }

    private void drawFaculae(final IDCardQuality idCardQuality) {
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

    private void enterToResult(String path, IDCardQuality iCardQuality, float clear, float is_idcard, float in_bound) {
        try {
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
        } catch (Exception e) {

        }
    }

    private void enterToResult(String path, String info) {
        try {
            Intent intent = new Intent();
            intent.putExtra("path", path);
            intent.putExtra("info", info);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {

        }
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
                        ConUtil.showToast(IDCardScanActivity.this, "识别失败，请重新识别！");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        Log.w("ceshi", "responseBody===" + new String(responseBody));
                    }
                    showBar(false);
                    ConUtil.showToast(IDCardScanActivity.this, "识别失败，请重新识别！");
                }
            });
        } catch (FileNotFoundException e1) {
            showBar(false);
            e1.printStackTrace();
            ConUtil.showToast(IDCardScanActivity.this, "识别失败，请重新识别！");
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