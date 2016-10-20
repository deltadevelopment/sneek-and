package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import no.twomonkeys.sneek.R;

import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.KeyboardUtil;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.FeedModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedFragment extends Fragment implements EditView.Callback, KeyboardUtil.Callback {

    private RecyclerView fRecyclerView;
    private RecyclerView.Adapter fAdapter;
    private FeedModel feedModel;
    private SwipyRefreshLayout swipyRefreshLayout;
    private View view;
    LinearLayoutManager mLayoutManager;
    RelativeLayout feedLl;
    EditView editView;
    int editViewOriginalHeight;
    boolean keyboardIsActive;


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
        feedLl = (RelativeLayout) view.findViewById(R.id.feed);

        mLayoutManager = getmLayoutManager();
        fRecyclerView = getfRecyclerView();
        swipyRefreshLayout = getSwipyRefreshLayout();
        editView = getEditView();
        KeyboardUtil keyboardUtil = getKeyboardUtil();

        fetchFeed();
        return view;
    }

    //Object creation
    private KeyboardUtil getKeyboardUtil()
    {
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
        if (feedModel == null) {
            feedModel = new FeedModel();
        }
        feedModel.fetch(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                fAdapter = new FeedAdapter(feedModel.getPosts());
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

    //KeyboardUtil delegate methods
    @Override
    public void keyboardUtilOnSizeChange(int height) {
        if (height != 0) {
            if (feedLl.getPaddingBottom() != height) {
                editView.editModeStarted();
                keyboardIsActive = true;
                showKeyboard();

                feedLl.setPadding(0, 0, 0, height);
                mLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        } else {
            if (editView.getHeight() != UIHelper.dpToPx(getActivity(), 50)) {
                hideKeyboard();
                editView.editModeEnded();
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
        System.out.println("CLICKED");
        editView.editModeEnded();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editView.editEt.getWindowToken(), 0);
        RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) swipyRefreshLayout.getLayoutParams();
        l.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 50));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIHelper.dpToPx(getActivity(), 50));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editView.setLayoutParams(layoutParams);
    }
}
