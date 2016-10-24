package no.twomonkeys.sneek.app.components.friends;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.feed.FeedAdapter;
import no.twomonkeys.sneek.app.shared.interfaces.ArrayCallback;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.FollowingModel;
import no.twomonkeys.sneek.app.shared.models.SuggestionModel;

/**
 * Created by Christian Dalsvaag on 27/09/16
 * Copyright 2MONKEYS AS
 */

public class FriendsFragment extends Fragment implements FriendsAdapter.Callback {

    private RecyclerView fRecyclerView;
    private FriendsAdapter fAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView toolbar_title;

    public static FriendsFragment newInstance() {
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        fRecyclerView = (RecyclerView) view.findViewById(R.id.following_recycler_view);

        fRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fRecyclerView.setHasFixedSize(true);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "arial-rounded-mt-bold.ttf");
        toolbar_title.setTypeface(type);

        fetchFollowing();

        return view;
    }

    //Data methods
    public void fetchFollowing() {
        // Fetch the data
        final FriendsFragment self = this;
        FollowingModel.fetchAll(new ArrayCallback() {
            @Override
            public void exec(ArrayList arrayList, ErrorModel errorModel) {
                Log.v("Fetching", "fetch " + arrayList);
                fAdapter = new FriendsAdapter(arrayList);
                fAdapter.addCallback(self);
                fRecyclerView.setAdapter(fAdapter);
            }
        });
    }

    public void fetchSuggestions(boolean reloadData)
    {
        SuggestionModel.fetchAll(new ArrayCallback() {
            @Override
            public void exec(ArrayList arrayList, ErrorModel errorModel) {
                System.out.println("Suggestions fetched");

            }
        });
    }

    @Override
    public void adapterDidClickSuggestion() {
        //fetch suggestion here
        //fetchSuggestions();
    }

    @Override
    public void adapterDidClickFollowing() {
        //fetchFollowing();
    }
}