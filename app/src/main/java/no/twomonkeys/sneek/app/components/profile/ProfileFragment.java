package no.twomonkeys.sneek.app.components.profile;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.components.feed.FeedAdapter;
import no.twomonkeys.sneek.app.components.feed.views.EditView;
import no.twomonkeys.sneek.app.components.feed.views.SimpleImageViewer;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.KeyboardUtil;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.FeedModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 28.10.2016.
 */
public class ProfileFragment extends Fragment implements FeedAdapter.Callback, SimpleImageViewer.Callback {

    private RecyclerView fRecyclerView;
    private FeedAdapter fAdapter;
    private FeedModel feedModel;
    private SwipyRefreshLayout swipyRefreshLayout;
    private View view;
    LinearLayoutManager mLayoutManager;
    RelativeLayout feedLl;
    SimpleImageViewer postSiv;
    UserModel userModel;
    TextView toolbarTitle;
    ImageButton pCloseBtn;

    public interface Callback {
        public void profileFragmentOnFullScreenStart();

        public void profileFragmentOnFullScreenEnd();

        public void profileFragmentOnCameraClicked();

        public void profileFragmentOnClose();
    }

    Callback callback;


    public static no.twomonkeys.sneek.app.components.feed.FeedFragment newInstance() {
        no.twomonkeys.sneek.app.components.feed.FeedFragment feedFragment = new no.twomonkeys.sneek.app.components.feed.FeedFragment();
        return feedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        feedLl = (RelativeLayout) view.findViewById(R.id.feed);

        mLayoutManager = getmLayoutManager();
        fRecyclerView = getfRecyclerView();
        swipyRefreshLayout = getSwipyRefreshLayout();
        postSiv = (SimpleImageViewer) view.findViewById(R.id.pSimpleImgViewer);
        postSiv.addCallback(this);
        pCloseBtn = (ImageButton) view.findViewById(R.id.pCloseBtn);
        pCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close here
                callback.profileFragmentOnClose();
            }
        });

        toolbarTitle = (TextView) view.findViewById(R.id.pToolbar_title);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "arial-rounded-mt-bold.ttf");
        //toolbarTitle.setTypeface(type);
        return view;
    }

    private LinearLayoutManager getmLayoutManager() {
        if (this.mLayoutManager == null) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            //mLayoutManager.setStackFromEnd(true);
            mLayoutManager.setReverseLayout(true);
            this.mLayoutManager = mLayoutManager;
        }
        return this.mLayoutManager;
    }


    private RecyclerView getfRecyclerView() {
        if (this.fRecyclerView == null) {
            RecyclerView fRecyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);
            fRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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

            fRecyclerView.setLayoutManager(mLayoutManager);
            fRecyclerView.setHasFixedSize(true);

            this.fRecyclerView = fRecyclerView;
        }

        return this.fRecyclerView;
    }

    private SwipyRefreshLayout getSwipyRefreshLayout() {
        if (this.swipyRefreshLayout == null) {
            SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.pSwipyrefreshlayout);
            swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    fetchUser(userModel);
                }
            });
            this.swipyRefreshLayout = swipyRefreshLayout;
        }
        return this.swipyRefreshLayout;
    }

    public void updateUser(UserModel userModel) {
        this.userModel = userModel;
        this.toolbarTitle.setText(userModel.getUsername());
        fetchUser(userModel);
    }

    //Data retrival
    private void fetchUser(UserModel userModel) {
        final ProfileFragment mFeedFragment = this;
        if (feedModel == null) {
            feedModel = new FeedModel();
        }
        feedModel.fetchUserMoments(userModel.getId(), new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                fAdapter = new FeedAdapter(feedModel.getPosts());
                fAdapter.addCallback(mFeedFragment);
                fRecyclerView.setAdapter(fAdapter);
                onItemsLoadComplete();
                fRecyclerView.scrollToPosition(0);
            }
        });
    }

    void onItemsLoadComplete() {
        swipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void feedAdapterTap(PostModel postModel) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MainActivity mact = (MainActivity) getActivity();
        mact.setSwipeable(false);
        // mact.getToolbar().setVisibility(View.GONE);

        postSiv.setVisibility(View.VISIBLE);
        postSiv.updatePost(postModel);
        postSiv.setVisibility(View.VISIBLE);
    }

    @Override
    public void feedAdapterLongPress() {

    }

    @Override
    public void feedAdapterShowProfile(UserModel userModel) {

    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    //simple image viewer delegate
    @Override
    public void simpleImageViewerClose() {
        MainActivity mact = (MainActivity) getActivity();
        mact.setSwipeable(true);
        //  mact.getToolbar().setVisibility(View.VISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void simpleImageViewerAnimatedIn() {

    }
}

