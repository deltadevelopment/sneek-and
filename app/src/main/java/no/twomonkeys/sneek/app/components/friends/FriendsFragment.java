package no.twomonkeys.sneek.app.components.friends;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by Christian Dalsvaag on 27/09/16
 * Copyright 2MONKEYS AS
 */

public class FriendsFragment extends Fragment implements FriendsAdapter.Callback {

    private RecyclerView fRecyclerView;
    private FriendsAdapter fAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private TextView toolbar_title;
    private Callback callback;

    public interface Callback {
        void friendsFragmentDidTapUser();
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

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
        //toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);

        //Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "arial-rounded-mt-bold.ttf");
        //toolbar_title.setTypeface(type);


        fAdapter = new FriendsAdapter();
        fAdapter.addCallback(this);
        fAdapter.addCallback(this);
        fRecyclerView.setAdapter(fAdapter);

        fRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        fetchFollowing();

        return view;
    }

    //Data methods
    public void fetchFollowing() {

        final FriendsFragment self = this;
        FollowingModel.fetchAll(new ArrayCallback() {
            @Override
            public void exec(ArrayList arrayList, ErrorModel errorModel) {
                fAdapter.setFollowings(arrayList);
                fAdapter.notifyDataSetChanged();
            }
        });

    }

    public void fetchSuggestions() {
        SuggestionModel.fetchAll(new ArrayCallback() {
            @Override
            public void exec(ArrayList arrayList, ErrorModel errorModel) {
                fAdapter.setSuggestions(arrayList);
                fAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void adapterDidClickSuggestion() {
        fetchSuggestions();
    }

    @Override
    public void adapterDidClickFollowing() {
        fetchSuggestions();
    }

    @Override
    public void friendsAdapterDidClickCell() {
        callback.friendsFragmentDidTapUser();
    }
}