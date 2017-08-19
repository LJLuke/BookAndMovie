package com.example.lijiang.bookandmovie.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.bookandmovie.HttpUtil;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.RecyclerviewAdapter;
import com.example.lijiang.bookandmovie.VideoHelper;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private View mView;
    private View upComingMovieView;
    private View top250View;
    private View boxOfficeView;
    private RecyclerView upComingRecyclerview;
    private RecyclerView top250Recyclerview;
    private RecyclerView boxOfficeRecyclerview;
    private TextView upComingGroup;
    private TextView top250Group;
    private TextView boxOfficeGroup;
    private MZBannerView mMZBannerView;
    static List<VideoHelper> hotMovieList = new ArrayList<>();
    private List<VideoHelper> upComingList = new ArrayList<>();
    private List<VideoHelper> top250List = new ArrayList<>();
    private List<VideoHelper> boxOfficeList = new ArrayList<>();
    private Handler mHandler;
    private static final int UPCOMING_LOADFINISH = 0;
    private static final int HOTMOVIE_LOADFINISH = 1;
    private static final int TOP250_LOADFINISH = 2;
    private static final int BOXOFFICE_LOADFINISH = 3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_movie, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upComingMovieView = mView.findViewById(R.id.upcoming_movies);
        upComingRecyclerview = (RecyclerView) upComingMovieView.findViewById(R.id.recyclerview);
        upComingGroup = (TextView) upComingMovieView.findViewById(R.id.group_name);
        upComingGroup.setText("即将上映");

        top250View = mView.findViewById(R.id.top_250);
        top250Recyclerview = (RecyclerView) top250View.findViewById(R.id.recyclerview);
        top250Group = (TextView) top250View.findViewById(R.id.group_name);
        top250Group.setText("Top250");

        boxOfficeView = mView.findViewById(R.id.box_office);
        boxOfficeRecyclerview = (RecyclerView) boxOfficeView.findViewById(R.id.recyclerview);
        boxOfficeGroup = (TextView) boxOfficeView.findViewById(R.id.group_name);
        boxOfficeGroup.setText("北美票房榜");

        mMZBannerView = (MZBannerView) mView.findViewById(R.id.banner);
        mMZBannerView.setIndicatorVisible(true);
        loadVideo("http://api.douban.com/v2/movie/in_theaters");
        load("http://api.douban.com/v2/movie/coming_soon",upComingList,UPCOMING_LOADFINISH);
        load("http://api.douban.com/v2/movie/top250",top250List,TOP250_LOADFINISH);
        loadBoxOffice("http://api.douban.com/v2/movie/us_box",boxOfficeList,BOXOFFICE_LOADFINISH);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case HOTMOVIE_LOADFINISH:
                        mMZBannerView.setPages(hotMovieList, new MZHolderCreator<MovieFragment.BannerViewHolder>() {
                            @Override
                            public MovieFragment.BannerViewHolder createViewHolder() {
                                return new MovieFragment.BannerViewHolder();
                            }
                        });
                        break;
                    case UPCOMING_LOADFINISH:
                        setRecyclerview(upComingList,upComingRecyclerview);
                        break;
                    case TOP250_LOADFINISH:
                        setRecyclerview(top250List,top250Recyclerview);
                        break;
                    case BOXOFFICE_LOADFINISH:
                        setRecyclerview(boxOfficeList,boxOfficeRecyclerview);
                        break;
                    default:
                        break;
                }
            }
        };
    }
    public static class BannerViewHolder implements MZViewHolder<VideoHelper> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);

            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, VideoHelper video) {
            // 数据绑定
            Glide.with(context).load(hotMovieList.get(position).getImageUrl()).into(mImageView);

        }
    }

    private void loadVideo(String videoUrl) {
        HttpUtil.sendOkHttpRequst(videoUrl, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0;i<5;i++){
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        video.setTitle(jsonObject.getString("title"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                        video.setImageUrl(jsonObject1.getString("medium"));
                        hotMovieList.add(video);
                        mHandler.sendEmptyMessage(HOTMOVIE_LOADFINISH);
                    }
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }
    private void load(String url, final List<VideoHelper> videoList,final int LOADFINISH){
        HttpUtil.sendOkHttpRequst(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0;i<jsonArray.length();i++){
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        video.setTitle(jsonObject.getString("title"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                        video.setImageUrl(jsonObject1.getString("medium"));
                        videoList.add(video);
                        mHandler.sendEmptyMessage(LOADFINISH);
                    }
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }

    private void loadBoxOffice(String url, final List<VideoHelper> videoList,final int LOADFINISH){
        HttpUtil.sendOkHttpRequst(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0;i<jsonArray.length();i++){
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("subject");
                        video.setTitle(jsonObject1.getString("title"));
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("images");
                        video.setImageUrl(jsonObject2.getString("medium"));
                        videoList.add(video);
                        mHandler.sendEmptyMessage(LOADFINISH);
                    }
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }
    private void setRecyclerview(List<VideoHelper> videoHelperList,RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerviewAdapter adapter1 = new RecyclerviewAdapter(R.layout.movie_recyclerview_item,videoHelperList,getContext());
        recyclerView.setAdapter(adapter1);
    }
}