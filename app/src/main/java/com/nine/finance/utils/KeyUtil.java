package com.nine.finance.utils;


import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeyUtil {

//    public static String API_KEY;
//    public static String API_SECRET;

//    public static String API_KEY = "y50eGwdGy3mFAzJsniPaCuS3TNICGMk8";
//    public static String API_SECRET = "0Xu9ibqi4dC7z4rYCRSZlR52PiytIlFw";
    public static String API_KEY = "O4B2QX6jHyv89w5uBGFogJSSM-miS3HX";
    public static String API_SECRET = "TGO2GLNc9gapbfzN0_zLDhpJg_5hvQ5v";//TGO2GLNc9gapbfzN0_zLDhpJg_5hvQ5v

    public static boolean isReadKey(Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            inputStream = context.getAssets().open("key");
            while ((count = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String str = new String(byteArrayOutputStream.toByteArray());
        String authKey = null;
        String authScrect = null;
        try {
            String[] strs = str.split(";");
            authKey = strs[0].trim();
            authScrect = strs[1].trim();
        } catch (Exception e) {
        }
        API_KEY = authKey;
        API_SECRET = authScrect;
        if (API_KEY == null || API_SECRET == null)
            return false;

        return true;
    }


}
