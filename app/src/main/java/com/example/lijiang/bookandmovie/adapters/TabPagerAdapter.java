package com.example.lijiang.bookandmovie.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.lijiang.bookandmovie.fragments.BookFragment;
import com.example.lijiang.bookandmovie.fragments.MovieFragment;

import java.util.List;

/**
 * Created by lijiang on 2017/8/19.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private List<String> mList;
    private List<Fragment> mFragmentList;
    public TabPagerAdapter(FragmentManager fm, List<String> list,List<Fragment> mFragmentList) {
        super(fm);
        mList = list;
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
       return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position);
    }
}
