package com.example.lijiang.bookandmovie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lijiang.bookandmovie.fragments.BookFragment;
import com.example.lijiang.bookandmovie.fragments.MovieFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner spinner;
    private TextView movieButton;
    private TextView bookButton;
    private Fragment wherearei;
    private BookFragment bookFragment = new BookFragment();
    private MovieFragment movieFragment = new MovieFragment();
    public LinearLayout linearLayout;
    public int fragmentStatus = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerList.add("图书");
        spinnerList.add("电影");
        linearLayout = (LinearLayout) findViewById(R.id.linear_change);
        movieButton = (TextView) findViewById(R.id.text_movie);
        bookButton = (TextView) findViewById(R.id.text_book);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        replaceFragment(movieFragment);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wherearei instanceof MovieFragment) {
                } else {
                    changeFragment(movieFragment);
                }
            }
        });
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wherearei instanceof BookFragment) {
                } else {
                    changeFragment(bookFragment);
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        wherearei = fragment;
        transaction.replace(R.id.framlayout, fragment);
        transaction.commit();

    }

    private void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (count == 0) {
            transaction.add(R.id.framlayout, fragment);
            count++;
        }
        transaction.hide(wherearei);
        transaction.show(fragment);
        wherearei = fragment;
        transaction.commit();
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
