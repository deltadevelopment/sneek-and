package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 13.10.2016.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView draweeView;
    TextView createdAtTv, usernameTv;
    LinearLayout imageRowLl;
    RelativeLayout loadingRl;
    ProgressBar progressBar;

    ImageViewHolder(View view) {
        super(view);
        loadingRl = (RelativeLayout) view.findViewById(R.id.loadingRl);
        imageRowLl = (LinearLayout) view.findViewById(R.id.imageRowLl);
        draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
        usernameTv = (TextView) itemView.findViewById(R.id.usernameTv);
        createdAtTv = (TextView) itemView.findViewById(R.id.createdAtTv);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
    }


    public void updateHolder(Context context, PostModel postModel) {
        Size size = UIHelper.getOptimalSize(context, postModel.getImage_width(), postModel.getImage_height());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) size.width;
        params.height = (int) size.height;
        params.setMargins(0, 0, 0, 0);
        draweeView.setLayoutParams(params);
        loadingRl.setLayoutParams(params);

        usernameTv.setText(postModel.getUserModel().getUsername());
        createdAtTv.setText(DateHelper.shortTime(postModel.getCreated_at()));
        postModel.loadPhoto(draweeView, new SimpleCallback2() {
            @Override
            public void callbackCall() {
                loadingRl.setVisibility(View.GONE);
            }
        });
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
        Log.v("PADDING", "Padding is now " + bottomPadding + " : " + topPadding);
        imageRowLl.setPadding(padding, topPadding, padding, bottomPadding);
        float rounded = UIHelper.dpToPx(MainActivity.mActivity, 10);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(rounded);
        //roundingParams.setBorder(color, 1.0f);
        //roundingParams.setRoundAsCircle(true);
        //roundingParams.setCornersRadii(0, rounded, rounded, rounded);
        float[] rectCorners = UIHelper.cornersForType(false, artifacts);

        roundingParams.setCornersRadii(rectCorners[0], rectCorners[1], rectCorners[2], rectCorners[3]);

        draweeView.getHierarchy().setRoundingParams(roundingParams);

        GradientDrawable shape = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        float[] newRectCorners = new float[]{rectCorners[0], rectCorners[0], rectCorners[1], rectCorners[1], rectCorners[2], rectCorners[2], rectCorners[3], rectCorners[3]};
        shape.setCornerRadii(newRectCorners);

        shape.setColor(ContextCompat.getColor(context, R.color.cameraGrey));

        loadingRl.setBackground(shape);
    }
}
