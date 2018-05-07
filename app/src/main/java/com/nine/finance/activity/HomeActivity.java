package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nine.finance.R;
import com.nine.finance.business.UserManager;
import com.nine.finance.constant.Constant;
import com.nine.finance.permission.PermissionDialogUtils;
import com.nine.finance.permission.PermissionUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.BusinessRectView;
import com.nine.finance.view.CircleAvatarView;
import com.oragee.banners.BannerView;

import java.util.ArrayList;
import java.util.List;

import static com.nine.finance.permission.Permissions.REQUEST_CODE_CAMERA;


public class HomeActivity extends BaseActivity {

    BusinessRectView accountRectView;
    CircleAvatarView avatarView;
    BannerView bannerView;
    private int[] imgs = {R.drawable.lunbo_image1,R.drawable.lunbo_image2,R.drawable.lunbo_image3};
    private List<View> viewList;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
//        startActivity(HomeActivity.this, PersonalInfoActivity.class);
//        FacePicActivity.startActivity(HomeActivity.this);
//        startActivity(HomeActivity.this, VerifyCodeActivity.class);
//        startActivity(HomeActivity.this, SubmitApplyActivity.class);
//        startActivity(HomeActivity.this, FillAccountInfoActivity.class);
//        startActivity(HomeActivity.this,BindBankCardActivity.class);
//        com.nine.finance.activity.bank.BankCardScanActivity.startActivityForResult(HomeActivity.this, 100);
//        startActivity(HomeActivity.this,FillAccountInfoActivity.class);
//        startActivity(HomeActivity.this, BankListActivity.class);
//        startActivity(HomeActivity.this, FillMobileActivity.class);
//        startActivity(HomeActivity.this, ForgetPasswordActivity.class);
//        test();
//        startActivity(HomeActivity.this, RegisterActivity.class);
    }

    public void test() {
        String s = "bankid=asfadsfdasfdsaf";
        String result = s.substring(s.lastIndexOf("="));
        System.out.print(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String num = data.getStringExtra("num");
            if (num != null) {
                ToastUtils.showCenter(HomeActivity.this, "卡号：" + num);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.checkSDPermission(HomeActivity.this)) {
            PermissionUtils.requestSDAndCameraPermission(HomeActivity.this);
        }
//        if (UserManager.checkLogin(this) && avatarView != null) {
//            Glide.with(HomeActivity.this).load(AppGlobal.getUserInfo().getHead()).into(avatarView);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_CAMERA == requestCode) {
            if (!PermissionUtils.checkSDPermission(this) && !PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showSDAndCameraPermissionDialog(this);
            } else if (!PermissionUtils.checkSDPermission(this)) {
                PermissionDialogUtils.showSDPermissionDialog(this);
            } else if (!PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showCameraPermissionDialog(this);
            } else {
//                takePhoto();
            }
        }
    }

    private void init() {
        viewList = new ArrayList<View>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView image = new ImageView(this);
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //设置显示格式
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageResource(imgs[i]);
            viewList.add(image);
        }
        bannerView = (BannerView) findViewById(R.id.banner);
        bannerView.setViewList(viewList);
        bannerView.startLoop(true);
        avatarView = (CircleAvatarView) findViewById(R.id.iv_head);
        avatarView.setImageResource(R.drawable.head_default);
        initListener();
        accountRectView = (BusinessRectView) findViewById(R.id.create_account);
        accountRectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                } else {
                    LoginActivity.startActivity(HomeActivity.this);
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                }
            }
        });
        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    startActivity(HomeActivity.this, PersonalInfoActivity.class);
                } else {
                    LoginActivity.startActivity(HomeActivity.this);
                }
            }
        });
        avatarView.setImageResource(R.drawable.logo_icon);
    }

    private void initListener() {
        findViewById(R.id.iv_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                } else {
                    //TODO test
                    LoginActivity.startActivity(HomeActivity.this);
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                }
            }
        });
        findViewById(R.id.iv_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(HomeActivity.this, 0, "商品", Constant.GOODS, "");
            }
        });
        findViewById(R.id.iv_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(HomeActivity.this, 0, "理财产品", Constant.FINANCE_PRODUCT, "");
            }
        });
        findViewById(R.id.iv_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    WebViewActivity.startActivity(HomeActivity.this, 0, "我的订单", Constant.MY_ORDRE, "");
                } else {
                    LoginActivity.startActivity(HomeActivity.this);
                }
            }
        });
    }


}
