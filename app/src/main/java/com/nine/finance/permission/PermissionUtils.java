package com.nine.finance.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import static com.nine.finance.permission.Permissions.REQUEST_CODE_CAMERA;
import static com.nine.finance.permission.Permissions.REQUEST_CODE_SD;

/**
 * Created by changqing.zhao on 2017/1/12.
 */
public class PermissionUtils {

    public static boolean checkSDPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestSDPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, Permissions.PERMISSIONS_STORAGE, REQUEST_CODE_SD);
    }

    public static boolean checkCameraPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, Permissions.PERMISSIONS_CAMERA, REQUEST_CODE_SD);
    }

    public static void requestSDAndCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, Permissions.PERMISSIONS_STORAGE_CAMERA, REQUEST_CODE_CAMERA);
    }

    public static void onRequestPermissionsResultWithSD(final Activity activity, int photo_type, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_CAMERA == requestCode) {
            if (!PermissionUtils.checkSDPermission(activity) && !PermissionUtils.checkCameraPermission(activity)) {
                PermissionDialogUtils.showSDAndCameraPermissionDialog(activity);
            } else if (!PermissionUtils.checkSDPermission(activity)) {
                PermissionDialogUtils.showSDPermissionDialog(activity);
            } else if (!PermissionUtils.checkCameraPermission(activity)) {
                PermissionDialogUtils.showCameraPermissionDialog(activity);
            } else {

            }
        }
    }


//    public static void checkSDPermission(Activity activity, UpdateInfo updateInfo) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
//            int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (permissionRead != PackageManager.PERMISSION_GRANTED || permissionWrite != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, Constants.PERMISSIONS_STORAGE, REQUEST_CODE_SD);
//            } else {
//                DownloadService downloadService = new DownloadService();
//                downloadService.startDownloading(activity, updateInfo);
//            }
//        } else {
//            DownloadService downloadService = new DownloadService();
//            downloadService.startDownloading(activity, updateInfo);
//        }
//    }
}
