package sungkyul.ac.kr.ottocafe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sungkyul.ac.kr.ottocafe.R;

/**
 * Created by HunJin on 2016-09-12.
 */
public class SideMenuListFragment extends Fragment {

    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_side_menu, container, false);

        return mView;
    }
}