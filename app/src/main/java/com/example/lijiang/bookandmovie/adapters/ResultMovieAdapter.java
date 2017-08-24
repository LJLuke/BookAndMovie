package com.example.lijiang.bookandmovie.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.entities.DetailMovie;

import java.util.List;

/**
 * Created by lijiang on 2017/8/23.
 */

public class ResultMovieAdapter extends BaseQuickAdapter<DetailMovie,BaseViewHolder> {
    private Context mContext;
    public ResultMovieAdapter(@LayoutRes int layoutResId, @Nullable List<DetailMovie> data,Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailMovie item) {
        Glide.with(mContext).load(item.getImageUrl()).into((ImageView)helper.getView(R.id.morebooks_img));
        ((TextView)helper.getView(R.id.morebooks_name)).setText(item.getTitle());
        ((RatingBar)helper.getView(R.id.stars)).setRating(Float.parseFloat(item.getStars())/10);
        ((TextView)helper.getView(R.id.rating)).setText(item.getAverage()+"");
        ((TextView)helper.getView(R.id.author)).setText("类型："+item.getGenres());
        ((TextView)helper.getView(R.id.publishing)).setText("时间："+item.getYear());
    }
}
