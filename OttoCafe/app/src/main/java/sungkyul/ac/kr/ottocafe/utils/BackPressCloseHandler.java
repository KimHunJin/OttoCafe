package sungkyul.ac.kr.ottocafe.utils;

import android.app.Activity;

/**
 * Created by HunJin on 2016-09-23.
 * 두 번 클릭 종료
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
        }
    }
}
