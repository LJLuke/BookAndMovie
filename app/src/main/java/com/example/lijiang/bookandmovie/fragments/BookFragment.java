package com.example.lijiang.bookandmovie.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lijiang.bookandmovie.MainActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.Utils.HttpUtil;
import com.example.lijiang.bookandmovie.Utils.RobinSnapHelper;
import com.example.lijiang.bookandmovie.adapters.ThemeAdapter;
import com.example.lijiang.bookandmovie.adapters.TitleAdapter;
import com.example.lijiang.bookandmovie.entities.BookHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment {
    private static final String TAG = "BookFragment";
    private View view;
    private int count = 0;
    private List<Integer> pagers = new ArrayList<>();
    private List<BookHelper> bookImgs = new ArrayList<>();
    public static List<BookHelper> sportSources = new ArrayList<>();
    public static List<BookHelper> scienceSources = new ArrayList<>();
    private List<BookHelper> magicSources = new ArrayList<>();
    private List<BookHelper> classicSources = new ArrayList<>();
    public static List<BookHelper> historySources = new ArrayList<>();
    public static List<BookHelper> politicsSources = new ArrayList<>();
    private List<BookHelper> romanticSources = new ArrayList<>();
    private List<BookHelper> warmSources = new ArrayList<>();
    private List<BookHelper> imageSources = new ArrayList<>();
    private List<BookHelper> literatrueSources = new ArrayList<>();
    private TextView romanticText;
    private TextView warmText;
    private TextView imageText;
    private TextView literatrueText;
    private List<TextView> texts = new ArrayList<>();
    private List[] textLists = {romanticSources, warmSources, imageSources, literatrueSources};
    private int ids[] = {R.id.nice_book, R.id.science_book, R.id.sport_book, R.id.magic_theme, R.id.classic_theme};
    private String[] titles = {"每日好书", "科学书刊", "运动图书", "魔术图书", "经典图书"};
    private List[] sources = {bookImgs, scienceSources, sportSources, magicSources, classicSources};
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    beginShow();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: "+"我被重新调用了吗");
        view = inflater.inflate(R.layout.fragment_book, container, false);
        View tempView = view.findViewById(R.id.buttons);

        romanticText = (TextView) tempView.findViewById(R.id.text_romantic);
        imageText =(TextView) tempView.findViewById(R.id.text_image);
        warmText = (TextView) tempView.findViewById(R.id.text_warm);
        literatrueText = (TextView) tempView.findViewById(R.id.text_literatrue);
        texts.add(romanticText);
        texts.add(warmText);
        texts.add(imageText);
        texts.add(literatrueText);
        literatrueText = (TextView) tempView.findViewById(R.id.text_literatrue);
        initData("体育", sportSources);
        initData("科学", scienceSources);
        initData("魔术", magicSources);
        initData("经典", classicSources);
        initData("历史", historySources);
        initData("政治", politicsSources);
        initData("言情", romanticSources);
        initData("鸡汤", warmSources);
        initData("科幻", imageSources);
        initData("文学", literatrueSources);
        Log.d(TAG, "onCreateView: " + "sx");
        pagers.add(R.drawable.history_img);
        pagers.add(R.drawable.politocs_img);
        pagers.add(R.drawable.science_img);
        pagers.add(R.drawable.sport_img);
        return view;
    }
    private void beginShow() {
        getRandomBooks();
        RecyclerView titleRecycler =(RecyclerView) view.findViewById(R.id.title_recycler);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(titleRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        titleRecycler.setLayoutManager(manager);
        TitleAdapter titleAdapter = new TitleAdapter(getContext(), pagers, this);
        titleRecycler.setAdapter(titleAdapter);
        for (int i = 0; i < 5; i++) {
            showBooks(ids[i], titles[i], sources[i]);
        }
        for (int i = 0; i < 4; i++) {
            setMyOnclickListener(texts.get(i), textLists[i]);
        }

    }

    private void showBooks(int id, String title, final List<BookHelper> list) {
        View tempView = view.findViewById(id);
        TextView moreBooks;
        moreBooks = (TextView) tempView.findViewById(R.id.morebooks);
        moreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                MoreBooksFragment fragment = MoreBooksFragment.newInstance(bundle);
                replaceFragment(fragment);
            }
        });
        RecyclerView recyclerView = (RecyclerView) tempView.findViewById(R.id.part_recycler);
        RobinSnapHelper snapHelper = new RobinSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ThemeAdapter adapter = new ThemeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        TextView textView =(TextView) tempView.findViewById(R.id.part_title);
        textView.setText(title);

    }

    private void initData(String tag, final List list) {
        String url = "https://api.douban.com/v2/book/search?tag=";
        HttpUtil.sendOkhttpRequest(url + tag, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseDate = response.body().string();
                parseJSONWithGSON(responseDate, list);
                sendMyMessage();
            }
        });
    }

    private void parseJSONWithGSON(String jsonData, List list) {
        try {

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


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "parseJSONWithGSON: ");

        }

    }

    private void sendMyMessage() {
        count++;
        if (count == 10) {
            Message message = new Message();
            message.what = 1;
            hander.sendMessage(message);
        }
    }

    private void getRandomBooks() {
        int temp;
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            temp = random.nextInt(20);
            for (int j = 1; j < 5; j++) {
                bookImgs.add((BookHelper) sources[j].get(temp));
            }
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.framlayout, fragment);
        transaction.hide(this);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setMyOnclickListener(TextView view, final List list) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                MoreBooksFragment fragment = MoreBooksFragment.newInstance(bundle);
                replaceFragment(fragment);
            }
        });


    }
}
