package com.example.lijiang.bookandmovie.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.HttpUtil;
import com.example.lijiang.bookandmovie.adapters.RecyclerviewAdapter;
import com.example.lijiang.bookandmovie.adapters.ResultMovieAdapter;
import com.example.lijiang.bookandmovie.entities.Actors;
import com.example.lijiang.bookandmovie.entities.DetailMovie;
import com.example.lijiang.bookandmovie.entities.VideoHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<DetailMovie> mDetailMovieList = new ArrayList<>();
    private static final int MOVIE_FINISH = 0;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        mRecyclerView = (RecyclerView) findViewById(R.id.result_recyclerview);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        String movieName = getIntent().getStringExtra("movie");
        load("http://api.douban.com/v2/movie/search?tag="+movieName);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MOVIE_FINISH:
                        ResultMovieAdapter resultMovieAdapter = new ResultMovieAdapter(R.layout.item_morebooks,mDetailMovieList,ResultActivity.this);
                        resultMovieAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                        resultMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                Intent intent = new Intent(ResultActivity.this,ArticalActivity.class);
                                intent.putExtra("id",mDetailMovieList.get(position).getId());
                                startActivity(intent);
                            }
                        });
                        mRecyclerView.setAdapter(resultMovieAdapter);
                }
            }
        };
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
                    JSONArray jsonArray = JSONObject.getJSONArray("subjects");
                    for (int i = 0;i < jsonArray.length();i++){
                        DetailMovie detailMovie = new DetailMovie();
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject = jsonObject2.getJSONObject("rating");
                        detailMovie.setAverage(jsonObject.getInt("average"));
                        detailMovie.setStars(jsonObject.getString("stars"));
                        detailMovie.setYear(jsonObject2.getString("year"));
                        JSONObject jsonObject1 = jsonObject2.getJSONObject("images");
                        detailMovie.setImageUrl(jsonObject1.getString("medium"));
                        detailMovie.setId(jsonObject2.getString("id"));
                        detailMovie.setTitle(jsonObject2.getString("title"));
                        detailMovie.setGenres(jsonObject2.getString("genres"));
                        mDetailMovieList.add(detailMovie);
                    }

                    mHandler.sendEmptyMessage(MOVIE_FINISH);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }

}
