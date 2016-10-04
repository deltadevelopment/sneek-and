package no.twomonkeys.sneek.app.components.friends;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.models.FollowingModel;

/**
 * 27/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<FollowingModel> followings;

    private Context mContext;

    FriendsAdapter(ArrayList<FollowingModel> followings){
        this.followings = followings;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView firstLettersTv;
        ImageView imageCircleIv;
        TextView usernameTv;


        ViewHolder(View view){
            super(view);
            usernameTv = (TextView) view.findViewById(R.id.username);
            imageCircleIv = (ImageView) view.findViewById(R.id.imageCircle);
            firstLettersTv = (TextView) view.findViewById(R.id.usernameFirstLetters);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FollowingModel following = followings.get(position);

        holder.usernameTv.setText(following.getFollowee().getUsername());
        holder.firstLettersTv.setText(following.getFollowee().getUsername().substring(0, 2));

        setRandomColor(holder);

    }

    @Override
    public int getItemCount() {
        return followings.size();
    }

    private void setRandomColor(ViewHolder holder) {
        int[] colors = { R.color.circleBlue, R.color.circleGreen, R.color.circleGrey, R.color.circleRed };

        GradientDrawable background = (GradientDrawable) holder.imageCircleIv.getBackground();
        Random r = new Random();
        background.setColor(ContextCompat.getColor(mContext, colors[r.nextInt(4)]));
    }

}
