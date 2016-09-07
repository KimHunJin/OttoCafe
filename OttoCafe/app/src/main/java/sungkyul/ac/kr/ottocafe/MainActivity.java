package sungkyul.ac.kr.ottocafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sungkyul.ac.kr.ottocafe.activity.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 화면이 보이고 2초 뒤에 로그인 페이지로 넘어감
        Thread mTimer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent mIntro = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(mIntro);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
