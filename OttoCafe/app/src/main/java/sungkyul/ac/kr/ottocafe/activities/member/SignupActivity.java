package sungkyul.ac.kr.ottocafe.activities.member;

import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungkyul.ac.kr.ottocafe.activities.kakao.BaseActivity;
import sungkyul.ac.kr.ottocafe.repo.ConnectService;
import sungkyul.ac.kr.ottocafe.repo.RepoItem;
import sungkyul.ac.kr.ottocafe.utils.StaticUrl;

/**
 * Created by HunJin on 2016-09-08.
 *
 * signup using kakao
 */
public class SignupActivity extends BaseActivity {

    String TAG = "SignupActivity";

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     *
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    Log.e(TAG,ErrorCode.CLIENT_ERROR_CODE+"");
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG,"on session closed");
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile ) {
                Logger.d("UserProfile : " + userProfile);
                insertProfile(userProfile.getId(),userProfile.getNickname(),userProfile.getThumbnailImagePath());
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG,"not singed up");
            }
        });
    }

    private void insertProfile(long id, String name, String imgPath) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map map = new HashMap();
        Log.e(TAG, Session.getCurrentSession().getAppKey());
        map.put("id",id);
        map.put("name",name);
        map.put("image",imgPath);

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.setInsert(map);
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {
                redirectMainActivity();
            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {

            }
        });
    }
}