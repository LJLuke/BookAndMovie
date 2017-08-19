package com.example.lijiang.bookandmovie.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.adapters.TabPagerAdapter;
import com.example.lijiang.bookandmovie.fragments.BookFragment;
import com.example.lijiang.bookandmovie.fragments.MovieFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner spinner;
    private Fragment wherearei;
    private BookFragment bookFragment = new BookFragment();
    private MovieFragment movieFragment = new MovieFragment();
    public LinearLayout linearLayout;
    public int fragmentStatus = 0;
    private int count = 0;
    private TabLayout mTabLayout;
    private TabPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerList.add("电影");
        spinnerList.add("图书");
        spinner = (Spinner) findViewById(R.id.spinner);

        mFragmentList.add(movieFragment);
        mFragmentList.add(bookFragment);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),spinnerList,mFragmentList);
        mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (fragmentStatus == 1) {
            this.linearLayout.setVisibility(View.VISIBLE);
            fragmentStatus = 0;
        }
        super.onBackPressed();
    }

}
