package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import no.twomonkeys.sneek.R;

import no.twomonkeys.sneek.app.components.MainActivity;
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
 * Created by simenlie on 13.10.2016.
 */

public class FeedFragment extends Fragment implements FeedAdapter.Callback, SimpleImageViewer.Callback {

    private RecyclerView fRecyclerView;
    private FeedAdapter fAdapter;
    private FeedModel feedModel;
    private SwipyRefreshLayout swipyRefreshLayout;
    private View view;
    private ImageButton composeBtn, bredMouthBtn, cameraBtn;
    LinearLayoutManager mLayoutManager;
    RelativeLayout feedLl;


    SimpleImageViewer postSiv;
    TextView toolbarTitle;


    public interface Callback {
        public void feedFragmentOnFullScreenStart();

        public void feedFragmentOnFullScreenEnd();

        public void feedFragmentOnCameraClicked();

        public void feedFragmentOnProfileTap(UserModel userModel);
    }

    Callback callback;


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
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        feedLl = (RelativeLayout) view.findViewById(R.id.feed);

        mLayoutManager = getmLayoutManager();
        fRecyclerView = getfRecyclerView();
        swipyRefreshLayout = getSwipyRefreshLayout();


        postSiv = (SimpleImageViewer) view.findViewById(R.id.postSiv);
        postSiv.addCallback(this);

        //toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        //Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "arial-rounded-mt-bold.ttf");
        //toolbarTitle.setTypeface(type);
        fetchFeed();
        composeBtn = getComposeBtn();
        bredMouthBtn = getBredMouthBtn();
        cameraBtn = getCameraBtn();



        GradientDrawable shape =  new GradientDrawable();
        shape.setColor(ContextCompat.getColor(getActivity(), R.color.white));

        float[] rectCorners = new float[]{UIHelper.dpToPx(getContext(), 10),UIHelper.dpToPx(getContext(), 10),0,0};
        float[] newRectCorners = new float[]{rectCorners[0], rectCorners[0], rectCorners[1], rectCorners[1], rectCorners[2],rectCorners[2],rectCorners[3],rectCorners[3]};
        shape.setCornerRadii(newRectCorners);
        //    mainRl.setBackground(shape);

        feedLl.setBackground(shape);


        return view;
    }


    //Object creation

    private ImageButton getComposeBtn() {
        if (this.composeBtn == null) {
            ImageButton composeBtn = (ImageButton) view.findViewById(R.id.composeBtn);
            composeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            composeBtn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.themeColor)); // White Tint
            this.composeBtn = composeBtn;
        }
        return this.composeBtn;
    }

    private ImageButton getBredMouthBtn() {
        if (this.bredMouthBtn == null) {
            ImageButton bredMouthBtn = (ImageButton) view.findViewById(R.id.bredMouthBtn);
            bredMouthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.bredMouthBtn = bredMouthBtn;
        }
        return this.bredMouthBtn;
    }

    private ImageButton getCameraBtn() {
        if (this.cameraBtn == null) {
            ImageButton cameraBtn = (ImageButton) view.findViewById(R.id.cameraBtn);
            cameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.feedFragmentOnCameraClicked();
                }
            });
            this.cameraBtn = cameraBtn;
        }
        return this.cameraBtn;
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
            RecyclerView fRecyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);
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

    //Data retrival
    private void fetchFeed() {
        final FeedFragment mFeedFragment = this;
        if (feedModel == null) {
            feedModel = new FeedModel();
        }
        feedModel.fetch(new NetworkCallback() {
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


    public void addNewImagePost(final PostModel postModel) {
        fAdapter.addPost(postModel);
        System.out.println("ADDED POST");
        postModel.save(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                System.out.println("SUCCESS UPLOAD WHOLE");
                postModel.setImage(postModel.getImage());
                fAdapter.replacePost(postModel);
            }
        });
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
        callback.feedFragmentOnProfileTap(userModel);
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
