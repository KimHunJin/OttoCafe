package sungkyul.ac.kr.ottocafe.activities.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;
import com.hkm.slider.Tricks.ViewPagerEx;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.kakao.BaseActivity;
import sungkyul.ac.kr.ottocafe.activities.menu.MenuActivity;
import sungkyul.ac.kr.ottocafe.activities.unity.UnityPlayerActivity;
import sungkyul.ac.kr.ottocafe.adapter.NavListAdapter;
import sungkyul.ac.kr.ottocafe.items.NavItem;
import sungkyul.ac.kr.ottocafe.utils.BackPressCloseHandler;
import sungkyul.ac.kr.ottocafe.utils.DataProvider;
import sungkyul.ac.kr.ottocafe.utils.NumZeroForm;
import sungkyul.ac.kr.ottocafe.utils.SaveDataSession;

/**
 * Created by HunJin on 2016-09-09.
 *
 * the main page
 */
public class MainActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    private BackPressCloseHandler backPressCloseHandler;

    private SliderLayout mDemoSlider;
    private NavListAdapter navListAdapter;
    private ArrayList<NavItem> navItemArrayList;
    DataProvider dataProvider;
    private LinearLayout btnMenu;
    private TextView txtNavName, txtNavPoint;
    private ListView lstNav;
    private ImageView imgNavThumbnail;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        setData();
        setupSlider();
        getMyInfo();
        navClick();

    }

    private void setData() {

        dataProvider.setHashFile(1 + "", "http://14.63.196.255/020cafe_image/blackcoffee.jpg");
        dataProvider.setHashFile(2 + "", "http://14.63.196.255/020cafe_image/espresso.jpg");
        dataProvider.setHashFile(3 + "", "http://14.63.196.255/020cafe_image/latte.jpg");
    }

    private void getMyInfo() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile profile) {
                SaveDataSession.setAppPreferences(getApplicationContext(), "UserId", profile.getId() + "");
                txtNavName.setText(profile.getNickname().toString());
                Picasso.with(getApplicationContext()).load(profile.getThumbnailImagePath()).into(imgNavThumbnail);
            }
        });
    }

    /**
     * 초기화
     */
    private void initialization() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        dataProvider = new DataProvider();
        btnMenu = (LinearLayout) findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
        txtNavName = (TextView) findViewById(R.id.txtNavUserNickName);
        txtNavPoint = (TextView) findViewById(R.id.txtNavUserPoint);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        lstNav = (ListView) findViewById(R.id.lstNavView);
        imgNavThumbnail = (ImageView) findViewById(R.id.imgNavUser);

        backPressCloseHandler = new BackPressCloseHandler(this);

        navItemArrayList = new ArrayList<>();

        lstAdd();

        navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav, navItemArrayList);

        lstNav.setAdapter(navListAdapter);
    }

    /**
     * navigation list click listener
     */
    void navClick() {
        lstNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        UserManagement.requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                redirectLoginActivity();
                            }
                        });
                        break;
                    }
                    case 1: {
                        UserManagement.requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {

                            }

                            @Override
                            public void onNotSignedUp() {

                            }

                            @Override
                            public void onSuccess(Long result) {
                                redirectLoginActivity();
                            }
                        });
                        break;
                    }
                }
            }
        });
    }

    /**
     * 네비게이션 항목 추가
     */
    void lstAdd() {
        navItemArrayList.add(new NavItem(0, "로그아웃"));
        navItemArrayList.add(new NavItem(1, "회원탈퇴"));
    }

    /**
     * 슬라이드를 통해 이미지를 처리하는 매서드입니다.
     */
    @SuppressLint("ResourceAsColor")
    private void setupSlider() {
        // remember setup first
        Log.e("setup", "setup slider");
        mDemoSlider.setPresetTransformer(TransformerL.DepthPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(4);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.colorPrimaryDark, R.color.colorAccent);
        final NumZeroForm n = new NumZeroForm(this);
        mDemoSlider.setNumLayout(n);
        mDemoSlider.setPresetTransformer("DepthPage");
//        mDemoSlider.stopAutoCycle();

        defaultCompleteSlider(dataProvider.getFileSrcHorizontal());
    }

    // 이미지를 URL로 가져올 땐 이 부분을 사용합니다.
    // 이미지를 src로 가져오기 위해서는 이 부분을 주석처리 하면 됩니다.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView coreSlider) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDemoSlider.stopAutoCycle();
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_ar: {
                startActivity(new Intent(getApplicationContext(), UnityPlayerActivity.class));
                break;
            }
            case R.id.btn_cart: {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                break;
            }
        }
    }

    /**
     * 뒤로가기 키를 눌렀을 때
     */
    @Override
    public void onBackPressed() {
        //핸들러 작동
        backPressCloseHandler.onBackPressed();
        Toast.makeText(getApplicationContext(), "한 번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
    }
}