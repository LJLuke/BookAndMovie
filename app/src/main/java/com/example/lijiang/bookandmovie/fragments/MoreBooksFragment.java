package com.example.lijiang.bookandmovie.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lijiang.bookandmovie.MainActivity;
import com.example.lijiang.bookandmovie.R;
import com.example.lijiang.bookandmovie.adapters.MoreBooksAdapter;
import com.example.lijiang.bookandmovie.entities.BookHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreBooksFragment extends Fragment {
    private List<BookHelper> helpers;

    public MoreBooksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_morebooks, container, false);
        ((MainActivity)getActivity()).linearLayout.setVisibility(View.GONE);
        ((MainActivity)getActivity()).fragmentStatus=1;
        helpers = getArguments().getParcelableArrayList("list");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_morebooks);
        MoreBooksAdapter adapter = new MoreBooksAdapter(helpers, getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public static MoreBooksFragment newInstance(Bundle args) {
        MoreBooksFragment fragment = new MoreBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
