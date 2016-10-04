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

    public static SecondFragment newInstance(){
        SecondFragment secondFragment = new SecondFragment();
        Bundle args = new Bundle();
        secondFragment.setArguments(args);
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        // getActivity().setTitle("sneek");

        return view;
    }
}
