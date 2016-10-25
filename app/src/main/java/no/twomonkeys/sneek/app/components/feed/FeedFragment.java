package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import no.twomonkeys.sneek.R;

import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.components.feed.views.EditView;
import no.twomonkeys.sneek.app.components.feed.views.SimpleImageViewer;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.KeyboardUtil;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.FeedModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedFragment extends Fragment implements EditView.Callback, KeyboardUtil.Callback, FeedAdapter.Callback, SimpleImageViewer.Callback {

    private RecyclerView fRecyclerView;
    private FeedAdapter fAdapter;
    private FeedModel feedModel;
    private SwipyRefreshLayout swipyRefreshLayout;
    private View view;
    LinearLayoutManager mLayoutManager;
    RelativeLayout feedLl;
    EditView editView;
    int editViewOriginalHeight;
    boolean keyboardIsActive, keyboardDidHide;
    SimpleImageViewer postSiv;
    TextView toolbarTitle;


    public interface Callback {
        public void feedFragmentOnFullScreenStart();

        public void feedFragmentOnFullScreenEnd();

        public void feedFragmentOnCameraClicked();
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
        editView = getEditView();
        KeyboardUtil keyboardUtil = getKeyboardUtil();
        postSiv = (SimpleImageViewer) view.findViewById(R.id.postSiv);
        postSiv.addCallback(this);

        toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "arial-rounded-mt-bold.ttf");
        toolbarTitle.setTypeface(type);
        fetchFeed();
        return view;
    }

    //Object creation
    private KeyboardUtil getKeyboardUtil() {
        KeyboardUtil keyboardUtil = new KeyboardUtil(getActivity());
        keyboardUtil.addCallback(this);
        keyboardUtil.enable();
        return keyboardUtil;
    }

    private EditView getEditView() {
        if (this.editView == null) {
            EditView editView = (EditView) view.findViewById(R.id.editView);
            editView.addCallback(this);
            this.editView = editView;
        }
        return this.editView;
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
                    if (keyboardIsActive) {
                        hideKeyboard();
                    } else {

                    }
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

    //EditView delegate methods
    @Override
    public void editViewSizeChange(int sizeChange) {
        ViewGroup.LayoutParams lp = editView.getLayoutParams();
        if (editViewOriginalHeight == 0) {
            editViewOriginalHeight = lp.height;
        }

        int result = (sizeChange - 53);
        if (result < 0) {
            result = 0;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, result + editViewOriginalHeight - UIHelper.dpToPx(getActivity(), 40));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        editView.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) swipyRefreshLayout.getLayoutParams();

        //50 default
        int marginBottom = 50 + result;
        l.setMargins(0, 0, 0, UIHelper.dpToPx(getActivity(), 50) + result);
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void editViewDidPost(String postMsg) {
        hideKeyboard();
        PostModel postModel = PostModel.newMessageInstance(fAdapter.getLastPost());
        postModel.setBody(postMsg);
        postModel.setExpireIndex(0);

        fAdapter.addPost(postModel);

        postModel.save(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                if (errorModel == null) {
                    System.out.println("POSTED MSG SUCCESS");
                }
            }
        });
    }

    @Override
    public void editViewDidClickCamera() {
        callback.feedFragmentOnCameraClicked();
    }

    //KeyboardUtil delegate methods
    @Override
    public void keyboardUtilOnSizeChange(int height) {
        if (height != 0) {
            if (feedLl.getPaddingBottom() != height) {
                keyboardDidHide = false;
                editView.editModeStarted();
                keyboardIsActive = true;
                showKeyboard();

                feedLl.setPadding(0, 0, 0, height);
                mLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        } else {
            if (editView.getHeight() != UIHelper.dpToPx(getActivity(), 50)) {
                if (!keyboardDidHide) {
                    keyboardDidHide = true;
                    hideKeyboard();
                    editView.editModeEnded();
                }

            }
            if (feedLl.getPaddingBottom() != 0) {
                //reset the padding of the contentView
                feedLl.setPadding(0, 0, 0, 0);
            }
        }
    }

    //Helper methods
    private void showKeyboard() {
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) swipyRefreshLayout.getLayoutParams();
        l.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 90));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIHelper.dpToPx(getActivity(), 90));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editView.setLayoutParams(layoutParams);
    }

    private void hideKeyboard() {
        System.out.println("Hiding keyboard");
        editView.editModeEnded();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editView.getEditEt().getWindowToken(), 0);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) swipyRefreshLayout.getLayoutParams();
        l.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 50));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIHelper.dpToPx(getActivity(), 50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editView.setLayoutParams(layoutParams);
    }

    public void addNewImagePost(final PostModel postModel) {
        final PostModel newPostModel = PostModel.newImageInstance(fAdapter.getLastPost());
        newPostModel.setImage_width(postModel.getImage().getWidth());
        newPostModel.setImage_height(postModel.getImage().getHeight());
        newPostModel.setMediaFile(postModel.getMediaFile());
        newPostModel.setCaption(postModel.getCaption());
        newPostModel.setExpireIndex(postModel.getExpireIndex());
        fAdapter.addPost(newPostModel);
        newPostModel.save(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                System.out.println("SUCCESS UPLOAD WHOLE");
                newPostModel.setImage(postModel.getImage());
                fAdapter.replacePost(newPostModel);
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
