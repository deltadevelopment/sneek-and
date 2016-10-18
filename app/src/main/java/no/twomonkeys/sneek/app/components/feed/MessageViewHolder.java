package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    TextView messageTv;
    TextView createdAtTv, usernameTv;
    LinearLayout linearLayout;
    RelativeLayout messageRl;
    Context context;

    MessageViewHolder(View view){
        super(view);
        messageRl = (RelativeLayout) view.findViewById(R.id.messageRl);
        linearLayout = (LinearLayout) view.findViewById(R.id.messageRowLl);
        messageTv = (TextView) view.findViewById(R.id.messageTv);
        usernameTv = (TextView) itemView.findViewById(R.id.usernameTv);
        createdAtTv = (TextView) itemView.findViewById(R.id.createdAtTv);
    }

    public void updateHolder(Context context, PostModel postModel)
    {
        this.context = context;
        PostArtifacts artifacts = postModel.getPostArtifacts();
        int padding = UIHelper.dpToPx(MainActivity.mActivity, 10);
        int topPadding = padding;
        int bottomPadding = padding;

        if (artifacts.sameUserPrevious) {
            topPadding = 0;
        }

        if (artifacts.sameUserNext && !artifacts.isLastInDay) {
            usernameTv.setVisibility(View.GONE);
            createdAtTv.setVisibility(View.GONE);
            bottomPadding = 0;
        } else {
            usernameTv.setVisibility(View.VISIBLE);
            createdAtTv.setVisibility(View.VISIBLE);
        }
        linearLayout.setPadding(padding, topPadding, padding, bottomPadding);

        messageTv.setText(postModel.getBody());
        usernameTv.setText(postModel.getUserModel().getUsername());
        createdAtTv.setText(DateHelper.shortTime(postModel.getCreated_at()));

        GradientDrawable shape =  new GradientDrawable();
        float[] rectCorners = UIHelper.cornersForType(false, artifacts);
        float[] newRectCorners = new float[]{rectCorners[0], rectCorners[0], rectCorners[1], rectCorners[1], rectCorners[2],rectCorners[2],rectCorners[3],rectCorners[3]};
        shape.setCornerRadii(newRectCorners);

        shape.setColor(ContextCompat.getColor(context, R.color.themeColor));

        // now find your view and add background to it

        messageRl.setBackground(shape);

    }

}
