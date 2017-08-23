package com.example.lijiang.bookandmovie.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.entities.Actors;

import java.util.List;

/**
 * Created by lijiang on 2017/8/23.
 */

public class ActorRecyclerViewAdapter extends BaseQuickAdapter<Actors,BaseViewHolder> {
    private Context mContext;

    public ActorRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List<Actors> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Actors item) {
        Glide.with(mContext).load(item.getImageUrl()).asBitmap().into((ImageView) helper.getView(R.id.actor_image));
        ((TextView)helper.getView(R.id.actor_name)).setText(item.getName());
    }
}
