package com.example.lijiang.bookandmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.bookandmovie.ArticalActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.entities.BookHelper;

import java.util.List;

/**
 * Created by cnrobin on 17-8-15.
 * Just Enjoy It!!!
 */

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookHelper> idSources;
    private Context mContext;

    public ThemeAdapter(List<BookHelper> idSources, Context mContext) {
        this.idSources = idSources;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_item, parent, false);
        return new ThemeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(mContext).load(idSources.get(position).getImg()).into(((ThemeHolder) holder).image);
        ((ThemeHolder) holder).bookName.setText(idSources.get(position).getBookName());
        ((ThemeHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ArticalActivity.class);
                intent.putExtra("bookInfo", idSources.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 11;
    }

    class ThemeHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public ImageView image;
        public TextView bookName;

        public ThemeHolder(View itemView) {
            super(itemView);
            image =(ImageView) itemView.findViewById(R.id.book_img);
            bookName = (TextView) itemView.findViewById(R.id.book_name);
            this.itemView = itemView;
        }
    }
}
