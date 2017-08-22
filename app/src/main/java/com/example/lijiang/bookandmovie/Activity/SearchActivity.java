package com.example.lijiang.bookandmovie.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.MyListView;
import com.example.lijiang.bookandmovie.Utils.RecordSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> spinnerList = new ArrayList<>();
    private Spinner spinner;
    private EditText et_search;
    private TextView tv_tip;
    private MyListView mMyListView;
    private TextView tv_clear;
    private RecordSQLiteOpenHelper mHelper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase mDatabase;
    private BaseAdapter mBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinnerList.add("电影");
        spinnerList.add("图书");
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        initSearchView();
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    if (!hasData) {
                        insertData(et_search.getText().toString().trim());
                        queryData("");
                    }
                    Toast.makeText(SearchActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tv_tip.setText("搜索历史");
                } else {
                    tv_tip.setText("搜索结果");
                }
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
            }
        });

        mMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                et_search.setText(name);
                Toast.makeText(SearchActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        });

        Date date = new Date();
        long time = date.getTime();
        insertData("Leo" + time);

        // 第一次进入查询所有的历史记录
        queryData("");
    }

    private void initSearchView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        mMyListView = (MyListView) findViewById(R.id.my_listView);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
    }

    private void deleteData() {
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.execSQL("delete from records");
        mDatabase.close();
    }

    private boolean hasData(String tempName) {
        Cursor cursor = mHelper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    private void insertData(String tempName) {
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.execSQL("insert into records(name) values('" + tempName + "')");
        mDatabase.close();
    }

    private void queryData(String tempName) {
        Cursor cursor = mHelper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        mBaseAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"},
                new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mMyListView.setAdapter(mBaseAdapter);
        mBaseAdapter.notifyDataSetChanged();
    }
}
