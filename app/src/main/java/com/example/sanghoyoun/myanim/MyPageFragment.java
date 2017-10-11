package com.example.sanghoyoun.myanim;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sanghoyoun on 2017. 10. 3..
 */

public class MyPageFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static MyPageFragment newInstance() {
        Bundle args = new Bundle();
        MyPageFragment fragment = new MyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        return view;
    }
}
