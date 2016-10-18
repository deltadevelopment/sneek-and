package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.FollowingModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PostModel> posts;

    private Context mContext;

    FeedAdapter(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    @Override
    public int getItemViewType(int position) {
        PostModel postModel = posts.get(position);
        if (postModel.getMedia_type() == 2) {
            return 0;
        } else {
            return 1;
        }
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        //return position % 2 * 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_row_item, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_row_item, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PostModel postModel = posts.get(position);
        if (postModel.getMedia_type() == 2) {
            MessageViewHolder mHolder = (MessageViewHolder) holder;
            mHolder.updateHolder(mContext, postModel);
        } else {
            ImageViewHolder iHolder = (ImageViewHolder) holder;
            iHolder.updateHolder(mContext, postModel);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
/*
    private void setRandomColor(RecyclerView.ViewHolder holder) {
        int[] colors = { R.color.circleBlue, R.color.circleGreen, R.color.circleGrey, R.color.circleRed };

        GradientDrawable background = (GradientDrawable) holder.imageCircleIv.getBackground();
        Random r = new Random();
        background.setColor(ContextCompat.getColor(mContext, colors[r.nextInt(4)]));
    }
*/


}
