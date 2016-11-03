package sungkyul.ac.kr.ottocafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
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
import sungkyul.ac.kr.ottocafe.activities.kakao.BaseActivity;
import sungkyul.ac.kr.ottocafe.activities.menu.CartActivity;
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
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private BackPressCloseHandler backPressCloseHandler;

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private NavListAdapter navListAdapter, navListAdapter2;
    private ArrayList<NavItem> navItemArrayList, navItemArrayList2;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ImageView imgBasket, imgNav, imgNavThumbnail;
    private TextView txtNavName, txtNavPoint;
    private ListView lstNav, lstNav2;
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
        lstNav2 = (ListView) findViewById(R.id.lstNavView2);
        imgNavThumbnail = (ImageView) findViewById(R.id.imgNavUser);
        navItemArrayList = new ArrayList<>();
        navItemArrayList2 = new ArrayList<>();
        backPressCloseHandler = new BackPressCloseHandler(this);
        fbtAR = (FloatingActionButton) findViewById(R.id.fbtAR);
        lstAdd();
        navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav, navItemArrayList);
        navListAdapter2 = new NavListAdapter(getApplicationContext(), R.layout.item_nav2, navItemArrayList2);
        lstNav.setAdapter(navListAdapter);
        lstNav2.setAdapter(navListAdapter2);
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

    /**
     * 데이터 베이스에 카카오 key 값과 디바이스 토큰 값 저장.
     *
     * @param id
     * @param token
     */
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


    /**
     * 데이터베이스로부터 카카오 키 값을 이용해 포인트 정보 가져옴.
     *
     * @param id
     */
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
                txtNavPoint.setText(decode.getResult().get(0).getPOINT() + " P");
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
                        // 내정보
                        Toast.makeText(getApplicationContext(),"추후 업데이트...",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1: {
                        // 장바구니
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        break;
                    }
                    case 2: {
                        // AR메뉴
                        startActivity(new Intent(getApplicationContext(), UnityPlayerActivity.class));
                        break;
                    }

                    case 3: {
                        // 쿠폰
                        Toast.makeText(getApplicationContext(),"추후 업데이트...",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

        lstNav2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        // 설정
                    }
                    case 1: {
                        // 공지사항
                    }
                    case 2: {
                        // 버전
                    }
                    case 3: {
                        // 만든이
                    }
                }
            }
        });

    }

    /**
     * 네비게이션 항목 추가
     */
    void lstAdd() {
        navItemArrayList.add(new NavItem(0, "내정보"));
        navItemArrayList.add(new NavItem(1, "장바구니"));
        navItemArrayList.add(new NavItem(2, "AR메뉴"));
        navItemArrayList.add(new NavItem(3, "쿠폰"));

        navItemArrayList2.add(new NavItem(0, "설정"));
        navItemArrayList2.add(new NavItem(1, "공지사항"));
        navItemArrayList2.add(new NavItem(2, "버전정보"));
        navItemArrayList2.add(new NavItem(3, "만든이"));
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

    /**
     * 이미지뷰 클릭 리스너 - 인텐트
     */
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

    @Override
    protected void onResume() {
        super.onResume();
        getMyInfo();
    }
}
