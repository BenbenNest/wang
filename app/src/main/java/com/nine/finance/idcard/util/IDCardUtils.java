package com.nine.finance.idcard.util;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by changqing on 2018/2/14.
 */

public class IDCardUtils {

    public void getIDCardInfo(String successStr) {
        try {
            Log.w("ceshi", "ocr  onSuccess: " + successStr);
            String info = "";
            JSONObject jObject = new JSONObject(successStr).getJSONArray("cards").getJSONObject(0);
            if ("back".equals(jObject.getString("side"))) {
                String officeAdress = jObject.getString("issued_by");
                String useful_life = jObject.getString("valid_date");
                info = info + "officeAdress:  " + officeAdress + "\nuseful_life:  " + useful_life;
            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
