package com.nine.finance.idcard;

import android.content.Context;
import android.util.Log;

import com.megvii.idcard.sdk.IDCard;
import com.megvii.licensemanager.sdk.LicenseManager;
import com.nine.finance.idcard.util.ConUtil;
import com.nine.finance.utils.KeyUtil;

/**
 * Created by changqing on 2018/2/12.
 */

public class AuthManager {

    private static String TAG = "AuthManager";

    public static void checkIDCardAuthState(Context context, final AuthCallBack authCallBack) {

        final LicenseManager licenseManager = new LicenseManager(context);
        licenseManager.setExpirationMillis(IDCard.getApiExpication(context) * 1000);
        // licenseManager.setAgainRequestTime(againRequestTime);

        String uuid = ConUtil.getUUIDString(context);
        long apiName = IDCard.getApiName();
        String content = licenseManager.getContext(uuid, LicenseManager.DURATION_365DAYS, apiName);

        String errorStr = licenseManager.getLastError();
        Log.w("ceshi", "getContent++++errorStr===" + errorStr);
        licenseManager.setAuthTimeBufferMillis(0);
        licenseManager.takeLicenseFromNetwork(uuid, KeyUtil.API_KEY, KeyUtil.API_SECRET, apiName,
                LicenseManager.DURATION_30DAYS, "IDCard", "1", true, new LicenseManager.TakeLicenseCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "IDCard onSuccess");
                        authCallBack.authState(true);
                    }

                    @Override
                    public void onFailed(int i, byte[] bytes) {
                        Log.d(TAG, "IDCard onFailed :" + new String(bytes));
                        authCallBack.authState(false);
                    }
                });

    }


    public interface AuthCallBack {
        public void authState(boolean flag);
    }

}
