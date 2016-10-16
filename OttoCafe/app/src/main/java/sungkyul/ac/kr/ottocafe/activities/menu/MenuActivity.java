package sungkyul.ac.kr.ottocafe.activities.menu;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.adapter.ViewPagerAdapter;

/**
 * Created by HunJin on 2016-09-11.
 * 음료와 사이드메뉴의 프래그먼트를 지정하기 위해 베이스가 되는 액티비티
 */
public class MenuActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initialization();
        setting();

    }

    void initialization() {
        pager = (ViewPager)findViewById(R.id.viewPager);
        tabs = (TabLayout) findViewById(R.id.tabs);
    }

    void setting() {
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),getApplicationContext()));
        tabs.setupWithViewPager(pager);
    }
}
