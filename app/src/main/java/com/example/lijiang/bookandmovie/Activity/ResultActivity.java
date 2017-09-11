package com.example.lijiang.bookandmovie.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.HttpUtil;
import com.example.lijiang.bookandmovie.adapters.ResultMovieAdapter;
import com.example.lijiang.bookandmovie.entities.BookHelper;
import com.example.lijiang.bookandmovie.entities.DetailMovie;
import com.example.lijiang.bookandmovie.fragments.MoreFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mTextview;
    private Button bt_back;
    private List<DetailMovie> mDetailMovieList = new ArrayList<>();
    private List<BookHelper> books = new ArrayList<>();
    private static final int MOVIE_FINISH = 0;
    private FrameLayout linearLayout;
    private Handler mHandler;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) books);
                    MoreFragment fragment = MoreFragment.newInstance(bundle);
                    replaceFragment(fragment);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        linearLayout = (FrameLayout) findViewById(R.id.change111);
        mTextview = (TextView) findViewById(R.id.tv_title);
        bt_back = (Button) findViewById(R.id.result_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String movieName = getIntent().getStringExtra("movie");
        String bookName = getIntent().getStringExtra("book");
        mRecyclerView = (RecyclerView) findViewById(R.id.result_recyclerview);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (movieName != null) {
            mTextview.setText("看电影");
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            loadMovie("http://api.douban.com/v2/movie/search?tag=" + movieName);
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MOVIE_FINISH:
                            ResultMovieAdapter resultMovieAdapter = new ResultMovieAdapter(R.layout.item_morebooks, mDetailMovieList, ResultActivity.this);
                            resultMovieAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                            resultMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(ResultActivity.this, ArticalActivity.class);
                                    intent.putExtra("id", mDetailMovieList.get(position).getId());
                                    startActivity(intent);
                                }
                            });
                            mRecyclerView.setAdapter(resultMovieAdapter);
                    }
                }
            };
        } else if (bookName != null) {
            loadBook(bookName);
            mTextview.setText("看图书");
        }

    }

    private void loadBook(String bookName) {
        HttpUtil.sendOkhttpRequest("https://api.douban.com/v2/book/search?tag=" + bookName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResultActivity.this, "搜索失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    parseJSONWithGSON(responseData, books);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void parseJSONWithGSON(String jsonData, List list) throws Exception {


        JSONObject jo = new JSONObject(jsonData);
        JSONArray array = jo.getJSONArray("books");

        for (int i = 0; i < array.length(); i++) {
            JSONObject tempObject = array.getJSONObject(i);
            BookHelper helper = new BookHelper();
            helper.setBookName(tempObject.get("title").toString());
            JSONObject ja = tempObject.getJSONObject("images");
            helper.setImg(ja.getString("large"));
            JSONArray authorArray = tempObject.getJSONArray("author");
            String authorName = authorArray.getString(0);
            helper.setAuthor(authorName);
            String publisher = tempObject.getString("publisher");
            helper.setPublishing(publisher);
            double rating = tempObject.getJSONObject("rating").getDouble("average");
            helper.setRating(rating);
            String summary = tempObject.getString("summary");
            helper.setWords(summary);
            String catalog = tempObject.getString("catalog");
            helper.setCatalog(catalog);
            String pubData = tempObject.getString("pubdate");
            helper.setPubData(pubData);
            String authorInfo = tempObject.getString("author_intro");
            helper.setAuthorInfo(authorInfo);
            int manNum = tempObject.getJSONObject("rating").getInt("numRaters");
            helper.setManNum(manNum);
            list.add(helper);

        }
        handler.sendEmptyMessage(1);

    }

    private void loadMovie(String url) {
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
                    for (int i = 0; i < jsonArray.length(); i++) {
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
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.change111, fragment);
        transaction.commit();
    }

}
