package com.example.lijiang.bookandmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lijiang.bookandmovie.entities.BookHelper;
import com.example.lijiang.bookandmovie.fragments.DetialFragment;

import java.util.Random;

public class ArticalActivity extends AppCompatActivity {
    private ImageView bookImg;
    private TextView bookName;
    private TextView authorName;
    private TextView publisher;
    private TextView publishData;
    private TextView score;
    private RatingBar ratingBar;
    private TextView manNum;
    private TextView summary;
    private ImageView moreSummary;
    private TextView authorInfo;
    private ImageView moreAuthorInfo;
    private TextView catalog;
    private ImageView moreCatalog;
    private BookHelper helper;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical);
        bookImg = (ImageView) findViewById(R.id.book_img);
        bookName = (TextView) findViewById(R.id.books_name);
        authorName = (TextView) findViewById(R.id.author);
        publisher = (TextView) findViewById(R.id.publisher);
        publishData = (TextView) findViewById(R.id.publish_data);
        score = (TextView) findViewById(R.id.score);
        ratingBar = (RatingBar) findViewById(R.id.stars);
        manNum = (TextView) findViewById(R.id.num_men);
        summary = (TextView) findViewById(R.id.summary);
        moreSummary = (ImageView) findViewById(R.id.more_summary);
        authorInfo = (TextView) findViewById(R.id.author_info);
        moreAuthorInfo = (ImageView) findViewById(R.id.more_authorinfo);
        catalog = (TextView) findViewById(R.id.catalog);
        moreCatalog = (ImageView) findViewById(R.id.more_catalog);
        linearLayout = (LinearLayout) findViewById(R.id.change_color);
        initView();
        moreCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("data", helper.getCatalog());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
        moreSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putString("data", helper.getWords());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
        moreAuthorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 3);
                bundle.putString("data", helper.getAuthorInfo());
                DetialFragment.newInstance(bundle).show(getFragmentManager(), "");
            }
        });
    }

    private void initView() {
        helper = getIntent().getParcelableExtra("bookInfo");
        linearLayout.setBackgroundResource(getLuckColor());
        Glide.with(this).load(helper.getImg()).centerCrop().into(bookImg);
        bookName.setText(helper.getBookName());
        authorName.setText("作者：" + helper.getAuthor());
        publisher.setText("出版社：" + helper.getPublishing());
        publishData.setText("出版日期：" + helper.getPubData());
        score.setText(helper.getRating() + "");
        ratingBar.setRating((float) helper.getRating() / 2);
        manNum.setText(helper.getManNum() + "");
        summary.setText(helper.getWords());
        authorInfo.setText(helper.getAuthorInfo());
        catalog.setText(helper.getCatalog());
    }

    private int getLuckColor() {
        int[] colors = {R.color.colorLuck_1, R.color.colorLuck_2, R.color.colorLuck_3, R.color.colorLuck_4, R.color.colorLuck_5};
        Random random = new Random();
        int color = random.nextInt(4);
        return colors[color];
    }
}
