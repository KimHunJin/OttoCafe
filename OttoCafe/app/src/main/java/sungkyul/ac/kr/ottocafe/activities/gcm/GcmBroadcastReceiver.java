package sungkyul.ac.kr.ottocafe.activities.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by HunJin on 2016-10-20.
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = context.getPackageName();
        String calssName = GcmIntentService.class.getName();
        ComponentName comp = new ComponentName(packageName, calssName);
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
