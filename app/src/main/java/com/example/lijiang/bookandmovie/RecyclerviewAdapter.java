package com.example.lijiang.bookandmovie;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijiang on 2017/8/13.
 */

public class RecyclerviewAdapter extends BaseQuickAdapter<VideoHelper,BaseViewHolder>{

    private Context mContext;
    public RecyclerviewAdapter(int layoutResId, List<VideoHelper> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoHelper item) {
        Glide.with(mContext).load(item.getImageUrl()).asBitmap().into((ImageView) helper.getView(R.id.movie_image));
        ((TextView)helper.getView(R.id.movie_name)).setText(item.getTitle());
    }
}
