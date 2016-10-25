package sungkyul.ac.kr.ottocafe.activities.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
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
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.push.PushActivity;
import com.kakao.push.PushMessageBuilder;
import com.kakao.push.PushService;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.SharedPreferencesCache;
import com.kakao.util.helper.log.Logger;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.gcm.PushMainActivity;
import sungkyul.ac.kr.ottocafe.activities.kakao.BaseActivity;
import sungkyul.ac.kr.ottocafe.activities.member.LoginActivity;
import sungkyul.ac.kr.ottocafe.activities.member.SignupActivity;
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
 * <p>
 * main activity
 */
public class MainActivity extends PushActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    protected static final String PROPERTY_DEVICE_ID = "device_id";
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        setData();
        setupSlider();
        getMyInfo();
        navClick();

    }

    @Override
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected String getDeviceUUID() {
        if (deviceUUID != null)
            return deviceUUID;

        final SharedPreferencesCache cache = Session.getAppCache();
        final String id = cache.getString(PROPERTY_DEVICE_ID);

        if (id != null) {
            deviceUUID = id;
            return deviceUUID;
        } else {
            // 적절한 알고리즘으로 기기 고유 ID를 생성한다.
            // 기기 고유 ID 생성을 위해 사용자 개인정보를 사용할 경우는 사용자 동의를 받도록 한다.
            UUID uuid = null;
            final String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                } else {
                    final String deviceId = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            Bundle bundle = new Bundle();
            bundle.putString(PROPERTY_DEVICE_ID, uuid.toString());
            cache.save(bundle);

            deviceUUID = uuid.toString();
            return deviceUUID;
        }
    }

    private void deregisterPushTokenAll() {
        PushService.deregisterPushTokenAll(new KakaoPushResponseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(getApplicationContext(), "succeeded to deregister all push token of this user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deregisterPushToken() {
        PushService.deregisterPushToken(new KakaoPushResponseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(getApplicationContext(), "succeeded to deregister push token", Toast.LENGTH_SHORT).show();
            }
        }, deviceUUID);
    }

    private void sendPushMessageToMe() {
        final String testMessage = new PushMessageBuilder("{\"content\":\"테스트 메시지\", \"friend_id\":1, \"noti\":\"test\"}").toString();
        if (testMessage == null) {
            Logger.w("failed to create push Message");
        } else {
            PushService.sendPushMessage(new KakaoPushResponseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    Toast.makeText(getApplicationContext(), "succeeded to send message", Toast.LENGTH_SHORT).show();
                }
            }, testMessage, deviceUUID);
        }
    }

    /**
     * retrofit을 활용해 데이터를 가져와야 함.
     * 아직 서버 API가 준비되지 않음.
     */
    private void setData() {

        dataProvider.setHashFile(1 + "", "http://14.63.196.255/020cafe_image/blackcoffee.jpg");
        dataProvider.setHashFile(2 + "", "http://14.63.196.255/020cafe_image/espresso.jpg");
        dataProvider.setHashFile(3 + "", "http://14.63.196.255/020cafe_image/latte.jpg");
    }

    /**
     * 카카오 유저의 닉네임, 프로필 가져오는 메서드
     */
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
     * 네비게이션 클릭 리스너
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

    /**
     * 버튼 클릭 리스너
     *
     * @param v
     */
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

    private abstract class KakaoPushResponseCallback<T> extends ApiResponseCallback<T> {
        @Override
        public void onFailure(ErrorResult errorResult) {
            Toast.makeText(self, "failure : " + errorResult, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            redirectSignupActivity();
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}
