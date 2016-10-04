package no.twomonkeys.sneek.app.components.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.twomonkeys.sneek.R;

/**
 * Created by Christian Dalsvaag on 27/09/16
 * Copyright 2MONKEYS AS
 */

public class FriendsFragment extends Fragment {

    private RecyclerView fRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static FriendsFragment newInstance(){
        FriendsFragment friendsFragment = new FriendsFragment();
        return friendsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends, container, false);

        // getActivity().setTitle("Friends");

        fRecyclerView = (RecyclerView) view.findViewById(R.id.following_recycler_view);

        fRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fRecyclerView.setHasFixedSize(true);

        String[] myDataset = {"Hello", "Hallo", "Hei", "Juice", "Kapteinen" };

        fAdapter = new FriendsAdapter(myDataset);
        fRecyclerView.setAdapter(fAdapter);

        return view;
    }
}