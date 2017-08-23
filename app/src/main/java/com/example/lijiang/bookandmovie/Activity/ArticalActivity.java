package com.example.lijiang.bookandmovie.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.HttpUtil;
import com.example.lijiang.bookandmovie.entities.Actors;
import com.example.lijiang.bookandmovie.entities.BookHelper;
import com.example.lijiang.bookandmovie.entities.DetailMovie;
import com.example.lijiang.bookandmovie.entities.VideoHelper;
import com.example.lijiang.bookandmovie.fragments.DetialFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;

public class ArticalActivity extends AppCompatActivity {
    private ImageView bookImg;
    private TextView bookName;
    private TextView authorName;
    private TextView publisher;
    private TextView publishData;
    private TextView score;
    private RatingBar ratingBar;
    private TextView manNum;
    private TextView summary;
    private ImageView moreSummary;
    private int color;
    private TextView authorInfo;
    private ImageView moreAuthorInfo;
    private TextView catalog;
    private ImageView moreCatalog;
    private BookHelper helper;
    private TextView movieOrBook;
    private LinearLayout linearLayout;
    private Handler mHandler;
    private static final int LOADFINISH = 0;
    private DetailMovie detailMovie;
    private CollapsingToolbarLayout layout;
    private List<Actors> mActorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical);
        color = getLuckColor();
        Log.d("color2",color+"");
        movieOrBook = (TextView)findViewById(R.id.movieorbook);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        layout= (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        bookImg = (ImageView) findViewById(R.id.book_img);
        bookName = (TextView) findViewById(R.id.books_name);
        authorName = (TextView) findViewById(R.id.author);
        publisher = (TextView) findViewById(R.id.publisher);
        publishData = (TextView) findViewById(R.id.publish_data);
        score = (TextView) findViewById(R.id.score);
        ratingBar = (RatingBar) findViewById(R.id.stars);
        manNum = (TextView) findViewById(R.id.num_men);
        summary = (TextView) findViewById(R.id.summary);
        moreSummary = (ImageView) findViewById(R.id.more_summary);
        authorInfo = (TextView) findViewById(R.id.author_info);
        moreAuthorInfo = (ImageView) findViewById(R.id.more_authorinfo);
        catalog = (TextView) findViewById(R.id.catalog);
        moreCatalog = (ImageView) findViewById(R.id.more_catalog);
        linearLayout = (LinearLayout) findViewById(R.id.change_color);

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, color),50);

        if (getIntent().getStringExtra("id")==null){
            initBookView();
        }else {
            String id = getIntent().getStringExtra("id");
            load("http://api.douban.com/v2/movie/subject/"+id);

            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case LOADFINISH:
                            initMovieView();
                            break;
                    }
                }
            };
        }


    }

    private void initBookView() {
        movieOrBook.setText("图书");
        helper = getIntent().getParcelableExtra("bookInfo");
        linearLayout.setBackgroundResource(color);
        Log.d("color1",color+"");
        Glide.with(this).load(helper.getImg()).centerCrop().into(bookImg);
        bookName.setText(helper.getBookName());
        layout.setTitle(" ");
        authorName.setText("作者：" + helper.getAuthor());
        publisher.setText("出版社：" + helper.getPublishing());
        publishData.setText("出版日期：" + helper.getPubData());
        score.setText(helper.getRating() + "");
        ratingBar.setRating((float) helper.getRating() / 2);
        manNum.setText(helper.getManNum() + "");
        summary.setText(helper.getWords());
        authorInfo.setText(helper.getAuthorInfo());
        catalog.setText(helper.getCatalog());
        moreCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("data", helper.getCatalog());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
        moreSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putString("data", helper.getWords());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
        moreAuthorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 3);
                bundle.putString("data", helper.getAuthorInfo());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
    }

    private void initMovieView(){
        linearLayout.setBackgroundResource(color);
        movieOrBook.setText("电影");
        Glide.with(this).load(detailMovie.getImageUrl()).asBitmap().into(bookImg);
        bookName.setText(detailMovie.getTitle());
        authorName.setText("年代："+detailMovie.getYear());
        publisher.setText("类型："+detailMovie.getGenres());
        publishData.setText("国家："+detailMovie.getCountries());
        score.setText(detailMovie.getAverage()+"");
        ratingBar.setRating(Float.parseFloat(detailMovie.getStars())/10);
        manNum.setText(detailMovie.getRatingsCount()+"");
        summary.setText(detailMovie.getSummary());
        TextView tv_cate = (TextView) findViewById(R.id.tv_categray);
        tv_cate.setVisibility(View.GONE);
        
    }
    private int getLuckColor() {
        int[] colors = {R.color.colorLuck_1, R.color.colorLuck_2, R.color.colorLuck_3, R.color.colorLuck_4, R.color.colorLuck_5};
        Random random = new Random();
        int color = random.nextInt(4);
        return colors[color];
    }

    private void load(String url){
        HttpUtil.sendOkhttpRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    detailMovie = new DetailMovie();
                    JSONObject jsonObject = JSONObject.getJSONObject("rating");
                    detailMovie.setAverage(jsonObject.getInt("average"));
                    detailMovie.setStars(jsonObject.getString("stars"));
                    detailMovie.setYear(JSONObject.getString("year"));
                    JSONObject jsonObject1 = JSONObject.getJSONObject("images");
                    detailMovie.setImageUrl(jsonObject1.getString("medium"));
                    detailMovie.setTitle(JSONObject.getString("title"));
                    detailMovie.setCountries(JSONObject.getString("countries"));
                    detailMovie.setGenres(JSONObject.getString("genres"));
                    JSONArray jsonArray = JSONObject.getJSONArray("casts");
                    for (int i = 0;i < jsonArray.length();i++){
                        Actors actors = new Actors();
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject3 = jsonObject2.getJSONObject("avatars");
                        actors.setImageUrl(jsonObject3.getString("small"));
                        actors.setName(jsonObject2.getString("name"));
                        mActorsList.add(actors);
                    }
                    detailMovie.setRatingsCount(JSONObject.getInt("ratings_count"));
                    detailMovie.setSummary(JSONObject.getString("summary"));
                    mHandler.sendEmptyMessage(LOADFINISH);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }
}
