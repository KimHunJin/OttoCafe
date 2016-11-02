package sungkyul.ac.kr.ottocafe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.menu.DetailMenuActivity;
import sungkyul.ac.kr.ottocafe.activities.unity.UnityPlayerActivity;
import sungkyul.ac.kr.ottocafe.adapter.MenuListAdapter;
import sungkyul.ac.kr.ottocafe.items.MenuItem;
import sungkyul.ac.kr.ottocafe.repo.ConnectService;
import sungkyul.ac.kr.ottocafe.repo.RepoItem;
import sungkyul.ac.kr.ottocafe.utils.CostChange;
import sungkyul.ac.kr.ottocafe.utils.RecyclerViewOnItemClickListener;
import sungkyul.ac.kr.ottocafe.utils.StaticUrl;

/**
 * Created by HunJin on 2016-09-12.
 * 음료 리스트 프래그먼트
 */
public class DrinkListFragment extends Fragment {

    private View mView;
    private MenuListAdapter menuListAdapter;
    private RecyclerView recyclerView;

    static final String TAG = "DrinkListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_drink_list, container, false);
        initialization();

        menuListAdapter = new MenuListAdapter(mView.getContext());
        getImage();
        recyclerView.setAdapter(menuListAdapter);

        lstClcikListener();

        return mView;
    }

    /**
     * retrofit을 사용하여 이미지 가져와야 함.
     * 아직 서버 API 준비 안됨
     */
    void getImage() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.getDrink();
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {
                RepoItem decode = response.body();
                String err = decode.getErr();
                Log.e(TAG, "errcode : " + err);
                if (err.equals("200")) {
                    for (int i = 0; i < decode.getResult().size(); i++) {
                        int key = Integer.parseInt(decode.getResult().get(i).getKEY());
                        String name = decode.getResult().get(i).getNAME();
                        String cost = decode.getResult().get(i).getPRICE();
                        String contents = decode.getResult().get(i).getEXPLANATION();
//                        String contents = "설명입니다. 아직 미완성 입니다."; // need contents
                        String image = decode.getResult().get(i).getIMAGE();
                        if (image == null) {
                            image = "http://14.63.196.255:8080/020cafe/image/logo.png";
                        } else {
                            image = "http://14.63.196.255:8080/020cafe/" + image.substring(3, image.length());
                        }

//                        Log.e(TAG, key + " " + name + " " + price + " " + contents + " " + image);

                        menuListAdapter.addData(new MenuItem(i % 2, key, name, cost, contents, image));
                    }
                }
            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {

            }
        });

//        menuListAdapter.addData(new MenuItem(0, "아이스 아메리카노", "4000", "원두를 갈아 만든 그냥 쓴 커피", "http://14.63.196.255/020cafe_image/blackcoffee.jpg"));
//        menuListAdapter.addData(new MenuItem(1, "카페라떼", "4500", "우유를 타서 부드럽게 마실 수 있는 커피", "http://14.63.196.255/020cafe_image/latte.jpg"));
//        menuListAdapter.addData(new MenuItem(2, "에스프레소", "4000", "유럽식 입맛을 즐기고 싶다면.. 에스프레소", "http://14.63.196.255/020cafe_image/espresso.jpg"));
    }

    void initialization() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rcv_drink);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
    }

    void lstClcikListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity(), recyclerView, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
                Intent it = new Intent(getActivity(), DetailMenuActivity.class);

                it.putExtra("CoffeeKey", menuListAdapter.getItems().get(position).getmNumber());
                it.putExtra("CoffeeName", menuListAdapter.getItems().get(position).getmName());
                it.putExtra("CoffeeImage", menuListAdapter.getItems().get(position).getmImageUrl());
                it.putExtra("CoffeePrice", menuListAdapter.getItems().get(position).getmCost());
                it.putExtra("CoffeeExplanation", menuListAdapter.getItems().get(position).getmExplain());
                it.putExtra("separate",0);
                startActivity(it);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "long click");
            }
        }
        ));
    }
}
