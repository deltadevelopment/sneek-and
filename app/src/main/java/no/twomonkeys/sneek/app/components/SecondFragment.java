package no.twomonkeys.sneek.app.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.twomonkeys.sneek.R;

/**
 * 27/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

public class SecondFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    public static SecondFragment newInstance(int page, String title) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        secondFragment.setArguments(args);
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }
}
