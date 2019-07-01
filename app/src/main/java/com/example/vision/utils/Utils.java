package com.example.vision.utils;

import android.text.TextUtils;

public class Utils {

    public static String removeExtraCharacters(String oldPhone) {
        if (!TextUtils.isEmpty(oldPhone)) {
            oldPhone = oldPhone.replaceAll(" ", "");
            oldPhone = oldPhone.replaceAll("-", "");
            oldPhone = oldPhone.replaceAll("\\(", "");
            oldPhone = oldPhone.replaceAll("\\)", "");
            oldPhone = oldPhone.replaceAll("\\+91", "");
        }
        return oldPhone;
    }


}



