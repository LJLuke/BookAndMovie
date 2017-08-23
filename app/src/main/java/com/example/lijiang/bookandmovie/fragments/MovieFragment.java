package com.example.lijiang.bookandmovie.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lijiang.bookandmovie.Activity.ArticalActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.adapters.RecyclerviewAdapter;
import com.example.lijiang.bookandmovie.Utils.HttpUtil;
import com.example.lijiang.bookandmovie.Utils.RobinSnapHelper;
import com.example.lijiang.bookandmovie.entities.VideoHelper;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private LinearLayout linearMovie;
    private ProgressBar progressBar;
    private ImageView imageView;
    private int count = 0;
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
        linearMovie = (LinearLayout) mView.findViewById(R.id.linear_movie);
        progressBar = (ProgressBar) mView.findViewById(R.id.progress_movie);
        imageView = (ImageView) view.findViewById(R.id.image_hide);
        linearMovie.setVisibility(View.INVISIBLE);
        upComingMovieView = mView.findViewById(R.id.upcoming_movies);
        upComingRecyclerview = (RecyclerView) upComingMovieView.findViewById(R.id.recyclerview);
        RobinSnapHelper robinSnapHelper = new RobinSnapHelper();
        robinSnapHelper.attachToRecyclerView(upComingRecyclerview);
        upComingGroup = (TextView) upComingMovieView.findViewById(R.id.group_name);
        upComingGroup.setText("即将上映");
        top250View = mView.findViewById(R.id.top_250);
        top250Recyclerview = (RecyclerView) top250View.findViewById(R.id.recyclerview);
        RobinSnapHelper robinSnapHelper1 = new RobinSnapHelper();
        robinSnapHelper1.attachToRecyclerView(top250Recyclerview);
        top250Group = (TextView) top250View.findViewById(R.id.group_name);
        top250Group.setText("Top250");
        boxOfficeView = mView.findViewById(R.id.box_office);
        boxOfficeRecyclerview = (RecyclerView) boxOfficeView.findViewById(R.id.recyclerview);
        RobinSnapHelper robinSnapHelper2 = new RobinSnapHelper();
        robinSnapHelper2.attachToRecyclerView(boxOfficeRecyclerview);
        boxOfficeGroup = (TextView) boxOfficeView.findViewById(R.id.group_name);
        boxOfficeGroup.setText("北美票房榜");

        mMZBannerView = (MZBannerView) mView.findViewById(R.id.banner);
        mMZBannerView.setIndicatorVisible(true);
        loadVideo("http://api.douban.com/v2/movie/in_theaters");
        load("http://api.douban.com/v2/movie/coming_soon", upComingList, UPCOMING_LOADFINISH);
        load("http://api.douban.com/v2/movie/top250", top250List, TOP250_LOADFINISH);
        loadBoxOffice("http://api.douban.com/v2/movie/us_box", boxOfficeList, BOXOFFICE_LOADFINISH);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HOTMOVIE_LOADFINISH:
                        mMZBannerView.setPages(hotMovieList, new MZHolderCreator<MovieFragment.BannerViewHolder>() {
                            @Override
                            public MovieFragment.BannerViewHolder createViewHolder() {
                                return new MovieFragment.BannerViewHolder();
                            }
                        });
                        break;
                    case UPCOMING_LOADFINISH:
                        TextView tv_up_all = (TextView) upComingMovieView.findViewById(R.id.all);
                        tv_up_all.setText("全部"+" "+upComingList.size());
                        count++;
                        break;
                    case TOP250_LOADFINISH:
                        TextView tv_top_all = (TextView)  top250View.findViewById(R.id.all);
                        tv_top_all.setText("全部"+" "+top250List.size());
                        count++;
                        break;
                    case BOXOFFICE_LOADFINISH:
                        TextView tv_box_all = (TextView) boxOfficeView.findViewById(R.id.all);
                        tv_box_all.setText("全部"+" "+boxOfficeList.size());
                        count++;
                        break;
                    default:
                        break;
                }
                if (count == 3) {
                    progressBar.setVisibility(View.GONE);
                    setRecyclerview(upComingList, upComingRecyclerview);
                    setRecyclerview(top250List, top250Recyclerview);
                    setRecyclerview(boxOfficeList, boxOfficeRecyclerview);
                    linearMovie.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public static class BannerViewHolder implements MZViewHolder<VideoHelper> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);

            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, VideoHelper video) {
            // 数据绑定
            Glide.with(context).load(hotMovieList.get(position).getImageUrl()).into(mImageView);

        }
    }

    private void stopShow() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void loadVideo(String videoUrl) {
        HttpUtil.sendOkhttpRequest(videoUrl, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                    stopShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0; i < 5; i++) {
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        video.setTitle(jsonObject.getString("title"));
                        video.setId(jsonObject.getString("id"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                        video.setImageUrl(jsonObject1.getString("medium"));
                        hotMovieList.add(video);
                    }
                    mHandler.sendEmptyMessage(HOTMOVIE_LOADFINISH);
                } catch (Exception e) {
                    stopShow();
                }
            }
        });
    }

    private void load(String url, final List<VideoHelper> videoList, final int LOADFINISH) {
        HttpUtil.sendOkhttpRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                    stopShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        video.setTitle(jsonObject.getString("title"));
                        video.setId(jsonObject.getString("id"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("images");
                        video.setImageUrl(jsonObject1.getString("medium"));
                        videoList.add(video);
                    }
                    mHandler.sendEmptyMessage(LOADFINISH);
                } catch (Exception E) {
                    stopShow();
                }
            }
        });
    }

    private void loadBoxOffice(String url, final List<VideoHelper> videoList, final int LOADFINISH) {
        HttpUtil.sendOkhttpRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                    stopShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject JSONObject = new JSONObject(responseData);
                    JSONArray jsonArray = new JSONArray(JSONObject.getString("subjects"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        VideoHelper video = new VideoHelper();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("subject");
                        video.setTitle(jsonObject1.getString("title"));
                        video.setId(jsonObject1.getString("id"));
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("images");
                        video.setImageUrl(jsonObject2.getString("medium"));
                        videoList.add(video);

                    }
                    mHandler.sendEmptyMessage(LOADFINISH);
                } catch (Exception E) {
                    stopShow();
                }
            }
        });
    }

    private void setRecyclerview(final List<VideoHelper> videoHelperList, RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerviewAdapter adapter1 = new RecyclerviewAdapter(R.layout.movie_recyclerview_item, videoHelperList, getContext());
        recyclerView.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ArticalActivity.class);
                intent.putExtra("id", videoHelperList.get(position).getId());
                startActivity(intent);
            }
        });
    }
}
