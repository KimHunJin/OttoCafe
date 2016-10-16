package sungkyul.ac.kr.ottocafe.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sungkyul.ac.kr.ottocafe.fragments.DrinkListFragment;
import sungkyul.ac.kr.ottocafe.fragments.SideMenuListFragment;


/**
 * Created by HunJin on 2016-09-10.
 * 가장 기본이 되는 메인 페이지 어댑터
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"음료","사이드메뉴"};
    private Fragment[] fragments = new Fragment[]{new DrinkListFragment(), new SideMenuListFragment()};
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
