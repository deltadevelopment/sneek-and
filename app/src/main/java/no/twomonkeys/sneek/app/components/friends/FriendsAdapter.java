package no.twomonkeys.sneek.app.components.friends;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Random;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.feed.FeedViewHolder;
import no.twomonkeys.sneek.app.components.feed.ImageViewHolder;
import no.twomonkeys.sneek.app.shared.models.FollowingModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.SuggestionModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;
import retrofit2.http.HEAD;

/**
 * 27/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderViewHolder.Callback, FriendsViewHolder.Callback {

    @Override
    public void friendsViewHolderDidTap() {
        callback.friendsAdapterDidClickCell();
    }

    public interface Callback {
        void adapterDidClickSuggestion();
        void adapterDidClickFollowing();
        void friendsAdapterDidClickCell();
    }

    private Callback callback;
    private boolean isSuggestions;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public HeaderViewHolder headerViewHolder;
    private ArrayList<UserModel> followings;
    private ArrayList<UserModel> suggestions;

    private Context mContext;

    FriendsAdapter() {
    }

    public void setFollowings(ArrayList<UserModel> followings) {
        this.followings = followings;
    }

    public void setSuggestions(ArrayList<UserModel> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public void headerDidTapSuggestion() {
        this.isSuggestions = true;
        if (this.suggestions == null) {
            callback.adapterDidClickSuggestion();
        } else {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void headerDidTapFollowing() {
        this.isSuggestions = false;
        if (followings == null) {
            callback.adapterDidClickFollowing();
        } else {
            this.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_header, parent, false);
            if (headerViewHolder == null){
                headerViewHolder = new HeaderViewHolder(view);
            }
            return headerViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_friends, parent, false);
            FriendsViewHolder fvh = new FriendsViewHolder(view);
            fvh.addCallback(this);
            return fvh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder hHolder = (HeaderViewHolder) holder;
            hHolder.addCallback(this);
            hHolder.update(this.mContext);
        } else {
            UserModel following = isSuggestions ? suggestions.get(position - 1) : followings.get(position - 1);
            FriendsViewHolder vholder = (FriendsViewHolder) holder;
            vholder.update(this.mContext, following);
        }
    }

    @Override
    public int getItemCount() {
        if (isSuggestions) {
            return suggestions != null ? suggestions.size() + 1 : 0;
        }
        return followings != null ? followings.size() + 1 : 0;
    }


}
