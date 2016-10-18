package no.twomonkeys.sneek.app.components.feed;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import no.twomonkeys.sneek.R;

import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.FeedModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedFragment extends Fragment {

    private RecyclerView fRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private FeedModel feedModel;
    private SwipyRefreshLayout swipyRefreshLayout;
    private View view;
    LinearLayoutManager mLayoutManager;

    public static FeedFragment newInstance() {
        FeedFragment feedFragment = new FeedFragment();
        return feedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feed, container, false);
        fRecyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);
        swipyRefreshLayout = getSwipyRefreshLayout();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        fRecyclerView.setLayoutManager(mLayoutManager);
        fRecyclerView.setHasFixedSize(true);
        fetchFeed();
        return view;
    }

    private void fetchFeed() {
        if (feedModel == null) {
            feedModel = new FeedModel();
        }
        feedModel.fetch(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                // Log.v("Fetching","fetch " + arrayList);
                fAdapter = new FeedAdapter(feedModel.getPosts());
                fRecyclerView.setAdapter(fAdapter);
                onItemsLoadComplete();
                fRecyclerView.scrollToPosition(0);

            }
        });
    }

    private SwipyRefreshLayout getSwipyRefreshLayout() {
        if (this.swipyRefreshLayout == null) {
            SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
            swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    fetchFeed();
                }
            });
            this.swipyRefreshLayout = swipyRefreshLayout;
        }
        return this.swipyRefreshLayout;
    }

    void onItemsLoadComplete() {
        swipyRefreshLayout.setRefreshing(false);
    }
}
