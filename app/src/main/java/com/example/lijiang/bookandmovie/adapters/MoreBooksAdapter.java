package com.example.lijiang.bookandmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.bookandmovie.Activity.ArticalActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.entities.BookHelper;

import java.util.List;

/**
 * Created by cnrobin on 17-8-16.
 * Just Enjoy It!!!
 */

public class MoreBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BookHelper> list;
    private Context mContext;


    public MoreBooksAdapter(List list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_morebooks, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //((MyViewHolder) holder).someWords.setText(list.get(position).getWords());
        ((MyViewHolder) holder).publishing.setText("出版社：" + list.get(position).getPublishing());
        ((MyViewHolder) holder).author.setText("作者：" + list.get(position).getAuthor());
        ((MyViewHolder) holder).ratingBar.setRating((float) (list.get(position).getRating() / 2));
        ((MyViewHolder) holder).rating.setText(list.get(position).getRating() + "");
        ((MyViewHolder) holder).moreBookName.setText(list.get(position).getBookName());
        ((MyViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ArticalActivity.class);
                intent.putExtra("bookInfo", list.get(position));
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(list.get(position).getImg()).centerCrop().into(((MyViewHolder) holder).moreBookImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView moreBookImg;
        public TextView moreBookName;
        public RatingBar ratingBar;
        public TextView rating;
        public TextView author;
        public TextView publishing;
        public TextView someWords;
        public View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            moreBookImg = (ImageView) itemView.findViewById(R.id.morebooks_img);
            moreBookName = (TextView) itemView.findViewById(R.id.morebooks_name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.stars);
            rating = (TextView) itemView.findViewById(R.id.rating);
            author = (TextView) itemView.findViewById(R.id.author);
            publishing = (TextView) itemView.findViewById(R.id.publishing);
            //someWords = (TextView) itemView.findViewById(R.id.somewords);
        }
    }
}
