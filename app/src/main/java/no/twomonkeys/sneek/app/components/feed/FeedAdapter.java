package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageViewHolder.Callback {
    private ArrayList<PostModel> posts;

    private Context mContext;
    private ImageViewHolder lastImageViewHolder;

    public FeedAdapter(ArrayList<PostModel> posts) {

        this.posts = posts == null ? new ArrayList<PostModel>() : posts;
    }

    public void addPost(PostModel postModel) {
        posts.add(0, postModel);
        UIHelper.addArtifacts(posts);
        notifyDataSetChanged();
    }

    public void replacePost(PostModel postModel) {
        posts.remove(0);
        posts.add(0, postModel);
        notifyDataSetChanged();
    }

    public PostModel getLastPost() {
        return this.posts.get(0);
    }

    public interface Callback {
        public void feedAdapterTap(PostModel postModel);

        public void feedAdapterLongPress();

        public void feedAdapterShowProfile(UserModel userModel);
    }

    Callback callback;

    @Override
    public int getItemViewType(int position) {
        PostModel postModel = posts.get(position);
        if (postModel.getMedia_type() == 2) {
            if (!postModel.postArtifacts.rightAlignment){
                return 0;
            }
            return 1;
        } else {
            if (!postModel.postArtifacts.rightAlignment){
                return 2;
            }
            return 3;
        }
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        //return position % 2 * 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_message, parent, false);
            return new MessageViewHolder(view);
        } else if(viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_message_right, parent, false);
            return new MessageViewHolder(view);
        }
        else if(viewType == 2){
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_image, parent, false);
            ImageViewHolder ivh = new ImageViewHolder(view);
            ivh.addCallback(this);
            return ivh;
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_image_right, parent, false);
            ImageViewHolder ivh = new ImageViewHolder(view);
            ivh.addCallback(this);
            return ivh;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PostModel postModel = posts.get(position);
        if (postModel.getMedia_type() == 2) {
            MessageViewHolder mHolder = (MessageViewHolder) holder;
            mHolder.updateHolder(mContext, postModel, position == 0);
        } else {
            ImageViewHolder iHolder = (ImageViewHolder) holder;

            iHolder.updateHolder(mContext, postModel, position == 0);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void imageViewHolderVideoStarted(ImageViewHolder imageViewHolder) {
        if (lastImageViewHolder != null) {
            lastImageViewHolder.stopVideo();
        }
        lastImageViewHolder = imageViewHolder;
    }

    @Override
    public void imageViewHolderTap(PostModel postModel) {
        callback.feedAdapterTap(postModel);
    }

    @Override
    public void imageViewHolderLongPress() {
        callback.feedAdapterLongPress();
    }

    @Override
    public void feedViewHolderDidDelete(PostModel postModel) {
        this.posts.remove(postModel);
        UIHelper.addArtifacts(posts);
        notifyDataSetChanged();
    }

    @Override
    public void feedViewHolderShowProfile(UserModel userModel) {
        callback.feedAdapterShowProfile(userModel);
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }
}
