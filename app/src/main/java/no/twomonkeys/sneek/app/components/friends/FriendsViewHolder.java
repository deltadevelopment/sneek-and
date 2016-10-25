package no.twomonkeys.sneek.app.components.friends;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 25.10.2016.
 */

public class FriendsViewHolder extends RecyclerView.ViewHolder {


    TextView firstLettersTv;
    ImageView imageCircleIv;
    TextView usernameTv;
    SimpleDraweeView fUserPhotoSdv;
    Context context;
    UserModel userModel;

    FriendsViewHolder(View view) {
        super(view);
        usernameTv = (TextView) view.findViewById(R.id.username);
        imageCircleIv = (ImageView) view.findViewById(R.id.imageCircle);
        firstLettersTv = (TextView) view.findViewById(R.id.usernameFirstLetters);
        fUserPhotoSdv = (SimpleDraweeView) view.findViewById(R.id.fUserPhotoSdv);
    }

    public void update(Context context, UserModel userModel) {
        this.userModel = userModel;
        this.context = context;
        usernameTv.setText(userModel.getUsername());
        firstLettersTv.setText(userModel.getUsername().substring(0, 2));
        setRandomColor(this);

        float rounded = UIHelper.dpToPx(MainActivity.mActivity, 35);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(rounded);
        //roundingParams.setCornersRadii(rectCorners[0], rectCorners[1], rectCorners[2], rectCorners[3]);
        //fUserPhotoSdv.getHierarchy().setRoundingParams(roundingParams);
        fUserPhotoSdv.setVisibility(View.INVISIBLE);
        if (hasPhoto()) {
            loadPhoto();
        } else {

        }
    }

    private boolean hasPhoto() {
        if (userModel.getLastPost() != null) {
            return true;
        }
        if (userModel.getProfile_picture_key() != null) {
            return true;
        }
        return false;
    }

    private void loadPhoto() {
        fUserPhotoSdv.setVisibility(View.VISIBLE);
        SimpleCallback2 imageFetchedCb = new SimpleCallback2() {
            @Override
            public void callbackCall() {
                System.out.println("CALLED");
            }
        };
        if (userModel.getProfile_picture_key() == null) {
            if (userModel.getLastPost() == null) {

            } else {
                userModel.getLastPost().loadPhoto(fUserPhotoSdv, imageFetchedCb);
            }
        } else {
            userModel.loadPhoto(fUserPhotoSdv, imageFetchedCb);
        }
    }

    private void setRandomColor(FriendsViewHolder holder) {
        int[] colors = {R.color.circleBlue, R.color.circleGreen, R.color.circleGrey, R.color.circleRed};

        GradientDrawable background = (GradientDrawable) holder.imageCircleIv.getBackground();
        Random r = new Random();
        background.setColor(ContextCompat.getColor(context, colors[r.nextInt(4)]));
    }


}
