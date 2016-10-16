package sungkyul.ac.kr.ottocafe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.menu.DetailMenuActivity;
import sungkyul.ac.kr.ottocafe.adapter.MenuListAdapter;
import sungkyul.ac.kr.ottocafe.items.MenuItem;
import sungkyul.ac.kr.ottocafe.utils.RecyclerViewOnItemClickListener;

/**
 * Created by HunJin on 2016-09-12.
 */
public class DrinkListFragment extends Fragment {

    private View mView;
    //    private ListView lst;
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

    void getImage() {
        menuListAdapter.addData(new MenuItem(0, "아이스 아메리카노", "4,000", "원두를 갈아 만든 그냥 쓴 커피", "http://14.63.196.255/020cafe_image/blackcoffee.jpg"));
        menuListAdapter.addData(new MenuItem(1, "카페라떼", "4,500", "우유를 타서 부드럽게 마실 수 있는 커피", "http://14.63.196.255/020cafe_image/latte.jpg"));
        menuListAdapter.addData(new MenuItem(2, "에스프레소", "4,000", "유럽식 입맛을 즐기고 싶다면.. 에스프레소", "http://14.63.196.255/020cafe_image/espresso.jpg"));
    }

    void initialization() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));


//        lst = (ListView)mView.findViewById(R.id.lst_drink);
    }

    void lstClcikListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity(), recyclerView, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
                Intent it = new Intent(getActivity(), DetailMenuActivity.class);

                it.putExtra("CoffeeKey", menuListAdapter.getItems().get(position).getmNumber());
                it.putExtra("CoffeeName",  menuListAdapter.getItems().get(position).getmName());
                it.putExtra("CoffeeImage",  menuListAdapter.getItems().get(position).getmImageUrl());
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
