package com.example.lijiang.bookandmovie.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijiang.bookandmovie.R;

/**
 * Created by cnrobin on 17-8-17.
 * Just Enjoy It!!!
 */

public class DetialFragment extends DialogFragment {
    private TextView title;
    private TextView detials;
    private ImageView closeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_detial, container, false);
        title = (TextView) view.findViewById(R.id.title);
        detials = (TextView) view.findViewById(R.id.detial);
        closeFragment = (ImageView) view.findViewById(R.id.close);
        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        int type = bundle.getInt("type");
        String data = bundle.getString("data");
        if (type == 1) {
            title.setText("目录");
        } else if (type == 2) {
            title.setText("简介");
        } else if (type == 3) {
            title.setText("作者");
        }
        detials.setText(data);
    }

    public static DetialFragment newInstance(Bundle args) {
        DetialFragment fragment = new DetialFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
