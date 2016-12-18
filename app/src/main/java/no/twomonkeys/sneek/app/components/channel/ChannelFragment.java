package no.twomonkeys.sneek.app.components.channel;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.feed.FeedAdapter;
import no.twomonkeys.sneek.app.components.feed.FeedFragment;
import no.twomonkeys.sneek.app.components.feed.views.EditView;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.KeyboardUtil;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 02.12.2016.
 */

public class ChannelFragment extends Fragment  implements EditView.Callback, KeyboardUtil.Callback {
    EditView editView;
    View view;
    boolean keyboardIsActive, keyboardDidHide;
    private RecyclerView cRecyclerView;
    int editViewOriginalHeight;
    LinearLayoutManager mLayoutManager;
    private SwipyRefreshLayout swipyRefreshLayout;
    private FeedAdapter fAdapter;
    RelativeLayout feedLl;



    public interface Callback {
        public void channelFragmentOnCameraClicked();
    }

    Callback callback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_channel, container, false);
        KeyboardUtil keyboardUtil = getKeyboardUtil();
        editView = getEditView();
        //initCamera();
        return view;
    }

    private EditView getEditView() {
        if (this.editView == null) {
            EditView editView = (EditView) view.findViewById(R.id.editView);
            editView.addCallback(this);
            this.editView = editView;
        }
        return this.editView;
    }

    private KeyboardUtil getKeyboardUtil() {
        KeyboardUtil keyboardUtil = new KeyboardUtil(getActivity());
        keyboardUtil.addCallback(this);
        keyboardUtil.enable();
        return keyboardUtil;
    }

    private RecyclerView getfRecyclerView() {
        if (this.cRecyclerView == null) {
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

            this.cRecyclerView = fRecyclerView;
        }

        return this.cRecyclerView;
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

        UserModel userModel = new UserModel();
        userModel.setId(DataHelper.getUserId());
        userModel.setUsername(DataHelper.getUsername());

        PostModel postModel = new PostModel(); //PostModel.newMessageInstance(fAdapter.getLastPost());
        postModel.setMedia_type(2);
        postModel.setUserModel(userModel);
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
        callback.channelFragmentOnCameraClicked();
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
}
