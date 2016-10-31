package sungkyul.ac.kr.ottocafe.activities.kakao;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import sungkyul.ac.kr.ottocafe.activities.main.MainActivity;
import sungkyul.ac.kr.ottocafe.activities.member.LoginActivity;
import sungkyul.ac.kr.ottocafe.activities.member.SignupActivity;
import sungkyul.ac.kr.ottocafe.activities.menu.MenuActivity;
import sungkyul.ac.kr.ottocafe.utils.GlobalApplication;

/**
 * Created by HunJin on 2016-09-08.
 *
 * kakao base activity overriding by activity
 */
public class BaseActivity extends AppCompatActivity {
    protected static Activity self;

    @Override
    protected void onResume() {
        super.onResume();
        GlobalApplication.setCurrentActivity(this);
        self = BaseActivity.this;
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = GlobalApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            GlobalApplication.setCurrentActivity(null);
        }
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectMainActivity() {
        final Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}