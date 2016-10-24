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

import java.util.ArrayList;
import java.util.Random;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.feed.ImageViewHolder;
import no.twomonkeys.sneek.app.shared.models.FollowingModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import retrofit2.http.HEAD;

/**
 * 27/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderViewHolder.Callback {

    public interface Callback {
        void adapterDidClickSuggestion();
        void adapterDidClickFollowing();
    }

    private Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    private ArrayList<FollowingModel> followings;

    private Context mContext;

    FriendsAdapter(ArrayList<FollowingModel> followings) {
        this.followings = followings;
    }

    @Override
    public void headerDidTapSuggestion() {
        callback.adapterDidClickSuggestion();
    }

    @Override
    public void headerDidTapFollowing() {
        callback.adapterDidClickFollowing();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView firstLettersTv;
        ImageView imageCircleIv;
        TextView usernameTv;

        ViewHolder(View view) {
            super(view);
            usernameTv = (TextView) view.findViewById(R.id.username);
            imageCircleIv = (ImageView) view.findViewById(R.id.imageCircle);
            firstLettersTv = (TextView) view.findViewById(R.id.usernameFirstLetters);
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
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_friends, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder hHolder = (HeaderViewHolder) holder;
            hHolder.addCallback(this);
            hHolder.update(this.mContext);
        } else {
            FollowingModel following = followings.get(position - 1);
            ViewHolder vholder = (ViewHolder) holder;
            vholder.usernameTv.setText(following.getFollowee().getUsername());
            vholder.firstLettersTv.setText(following.getFollowee().getUsername().substring(0, 2));
            setRandomColor(vholder);
        }
    }

    @Override
    public int getItemCount() {
        return followings.size() + 1;
    }

    private void setRandomColor(ViewHolder holder) {
        int[] colors = {R.color.circleBlue, R.color.circleGreen, R.color.circleGrey, R.color.circleRed};

        GradientDrawable background = (GradientDrawable) holder.imageCircleIv.getBackground();
        Random r = new Random();
        background.setColor(ContextCompat.getColor(mContext, colors[r.nextInt(4)]));
    }

}
