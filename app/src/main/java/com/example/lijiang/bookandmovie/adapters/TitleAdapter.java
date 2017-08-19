package com.example.lijiang.bookandmovie.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijiang.bookandmovie.MainActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.entities.BookHelper;
import com.example.lijiang.bookandmovie.fragments.BookFragment;
import com.example.lijiang.bookandmovie.fragments.MoreBooksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnrobin on 17-8-14.
 * Just Enjoy It!!!
 */

public class TitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<List<BookHelper>> sources = new ArrayList<>();
    private Context mContext;
    private List<Integer> idSources;
    private BookFragment mFragment;
    private String[] titleSources = {"历史：博古方能通今", "政治：什么是政治?", "科学：科学技术新发展", "体育：梅西还是Ｃ罗"};

    public TitleAdapter(Context mContext, List<Integer> idSources, BookFragment mFragment) {
        this.mContext = mContext;
        this.idSources = idSources;
        this.mFragment = mFragment;
        sources.add(BookFragment.historySources);
        sources.add(BookFragment.politicsSources);
        sources.add(BookFragment.scienceSources);
        sources.add(BookFragment.sportSources);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyHolder) holder).titleImg.setImageResource(idSources.get(position));
        ((MyHolder) holder).titleText.setText((position + 1) + "/" + idSources.size());
        ((MyHolder) holder).titleTop.setText(titleSources[position]);
        ((MyHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) sources.get(position));
                replaceFragment(MoreBooksFragment.newInstance(bundle));
            }
        });
    }

    @Override
    public int getItemCount() {
        return idSources.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView titleImg;
        public TextView titleText;
        public TextView titleTop;
        public View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleImg =(ImageView) itemView.findViewById(R.id.title_img);
            titleText = (TextView) itemView.findViewById(R.id.whereami);
            titleTop = (TextView) itemView.findViewById(R.id.title_top);
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager manager = ((MainActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.framlayout, fragment);
        transaction.hide(mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
