package sungkyul.ac.kr.ottocafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HunJin on 2016-09-23.
 * 데이터 세션 저장
 */
public class SaveDataSession {
    //파일에서 데이터 가져오는거
    public static String getAppPreferences(Context context, String key) {
        String returnValue = null;
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("ottocafe", 0);
        returnValue = pref.getString(key, "");
        return returnValue;
    }

    //파일에 저장하는거
    public static void setAppPreferences(Context context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("ottocafe", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }
}
