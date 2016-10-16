package sungkyul.ac.kr.ottocafe.repo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by HunJin on 2016-09-23.
 */
public interface ConnectService {
    @FormUrlEncoded
    @POST("db_insert.php")
    Call<RepoItem> setInsert(
            @FieldMap Map<String, String> setInsert
    );

    @FormUrlEncoded
    @POST("db_get_user_info.php")
    Call<RepoItem> getInfo(
            @FieldMap Map<String , String> getInfo
    );

    @FormUrlEncoded
    @POST("auto_management.php")
    Call<RepoItem> setStockReflect(
            @FieldMap Map<String, String> setStock
    );

}
