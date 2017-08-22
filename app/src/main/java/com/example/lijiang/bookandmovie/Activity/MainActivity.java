package com.example.lijiang.bookandmovie.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.MyListView;
import com.example.lijiang.bookandmovie.Utils.RecordSQLiteOpenHelper;
import com.example.lijiang.bookandmovie.adapters.TabPagerAdapter;
import com.example.lijiang.bookandmovie.fragments.BookFragment;
import com.example.lijiang.bookandmovie.fragments.MovieFragment;

import org.zackratos.ultimatebar.UltimateBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> tabList = new ArrayList<>();
    private BookFragment bookFragment = new BookFragment();
    private MovieFragment movieFragment = new MovieFragment();
    public int fragmentStatus = 0;
    public TabLayout mTabLayout;
    private TabPagerAdapter mPagerAdapter;
    public ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private TextView tv_top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabList.add("电影");
        tabList.add("图书");
        mFragmentList.add(movieFragment);
        mFragmentList.add(bookFragment);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabList, mFragmentList);
        mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorLuck_3), 50);

        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (fragmentStatus == 1) {
            this.mTabLayout.setVisibility(View.VISIBLE);
            this.mViewPager.setVisibility(View.VISIBLE);
            fragmentStatus = 0;
        }
        super.onBackPressed();
    }

}
