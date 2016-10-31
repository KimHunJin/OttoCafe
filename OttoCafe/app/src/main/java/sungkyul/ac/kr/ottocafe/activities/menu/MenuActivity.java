package sungkyul.ac.kr.ottocafe.activities.menu;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.kakao.BaseActivity;
import sungkyul.ac.kr.ottocafe.activities.main.CartActivity;
import sungkyul.ac.kr.ottocafe.activities.unity.UnityPlayerActivity;
import sungkyul.ac.kr.ottocafe.adapter.NavListAdapter;
import sungkyul.ac.kr.ottocafe.adapter.ViewPagerAdapter;
import sungkyul.ac.kr.ottocafe.items.NavItem;
import sungkyul.ac.kr.ottocafe.repo.ConnectService;
import sungkyul.ac.kr.ottocafe.repo.RepoItem;
import sungkyul.ac.kr.ottocafe.utils.BackPressCloseHandler;
import sungkyul.ac.kr.ottocafe.utils.SaveDataSession;
import sungkyul.ac.kr.ottocafe.utils.StaticUrl;

/**
 * Created by HunJin on 2016-09-11.
 * 음료와 사이드메뉴의 프래그먼트를 지정하기 위해 베이스가 되는 액티비티
 */
public class MenuActivity extends BaseActivity {

    private static final String TAG = "MenuActivity";
    protected static final String PROPERTY_DEVICE_ID = "device_id";
    private BackPressCloseHandler backPressCloseHandler;

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private NavListAdapter navListAdapter;
    private ArrayList<NavItem> navItemArrayList;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ImageView imgBasket, imgNav, imgNavThumbnail;
    private TextView txtNavName, txtNavPoint;
    private ListView lstNav;
    private FloatingActionButton fbtAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initialization();
        setting();
        clickListener();
        getMyInfo();
        navClick();

    }

    void initialization() {
        pager = (ViewPager) findViewById(R.id.viewPager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        imgBasket = (ImageView) findViewById(R.id.imgBasket);
        imgNav = (ImageView) findViewById(R.id.imgNav);
        txtNavName = (TextView) findViewById(R.id.txtNavUserNickName);
        txtNavPoint = (TextView) findViewById(R.id.txtNavUserPoint);
        lstNav = (ListView) findViewById(R.id.lstNavView);
        imgNavThumbnail = (ImageView) findViewById(R.id.imgNavUser);
        navItemArrayList = new ArrayList<>();
        backPressCloseHandler = new BackPressCloseHandler(this);
        fbtAR = (FloatingActionButton)findViewById(R.id.fbtAR);
        lstAdd();
        navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav, navItemArrayList);
        lstNav.setAdapter(navListAdapter);
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

                FirebaseMessaging.getInstance().subscribeToTopic("news");
                String token = FirebaseInstanceId.getInstance().getToken();

                insertProfile(profile.getId(), token);
            }
        });
    }

    private void insertProfile(final long id, String token) {

        Map map = new HashMap();
        map.put("id", id + "");
        map.put("token", token);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.setInsert(map);
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {
                Log.e(TAG, "success insert");
                getPoint(id);
            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    void getPoint(long id) {
        Map map = new HashMap();
        map.put("name", id + "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.getInfo(map);
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {
                Log.e(TAG, "success point");
                RepoItem decode = response.body();
                txtNavPoint.setText(decode.getResult().get(0).getPOINT());
            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
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
                                Log.e(TAG, result + "");
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

    void setting() {
        toolbar.setContentInsetsAbsolute(0, 0);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext()));
        tabs.setupWithViewPager(pager);

        toggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawer,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                imgNav.setImageDrawable(getResources().getDrawable(R.drawable.main_menu));
                imgNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.openDrawer(Gravity.LEFT);
                    }
                });
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                imgNav.setImageDrawable(getResources().getDrawable(R.drawable.back));
                imgNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.closeDrawer(Gravity.LEFT);
                    }
                });
            }
        };
        drawer.setDrawerListener(toggle);
    }

    void clickListener() {
        imgBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        fbtAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UnityPlayerActivity.class));
            }
        });
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
