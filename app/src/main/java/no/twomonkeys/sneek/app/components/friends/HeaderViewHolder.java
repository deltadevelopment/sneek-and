package no.twomonkeys.sneek.app.components.friends;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 24.10.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    Button hFollowingBtn, hSuggestionBtn;
    Context context;
    TextView usernameTxt, usernameFirstLetters;
    SimpleDraweeView fUserPhotoSdv;
    boolean isUpdated;

    public interface Callback {
        public void headerDidTapSuggestion();
        public void headerDidTapFollowing();
    }

    Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public HeaderViewHolder(View itemView) {
        super(itemView);
        fUserPhotoSdv = (SimpleDraweeView) itemView.findViewById(R.id.fUserPhotoSdv);
        usernameTxt = (TextView) itemView.findViewById(R.id.username);
        usernameFirstLetters = (TextView) itemView.findViewById(R.id.usernameFirstLetters);
        hFollowingBtn = (Button) itemView.findViewById(R.id.hFollowingBtn);
        hSuggestionBtn = (Button) itemView.findViewById(R.id.hSuggestionBtn);
        hFollowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hFollowingBtn.setTextColor(ContextCompat.getColor(context, R.color.themeColor));
                hSuggestionBtn.setTextColor(ContextCompat.getColor(context, R.color.captionGrey));
                callback.headerDidTapFollowing();
            }
        });
        hSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hFollowingBtn.setTextColor(ContextCompat.getColor(context, R.color.captionGrey));
                hSuggestionBtn.setTextColor(ContextCompat.getColor(context, R.color.themeColor));
                callback.headerDidTapSuggestion();
            }
        });
    }

    public void update(Context context) {
        if (!isUpdated){
            isUpdated = true;
            this.context = context;
            fUserPhotoSdv.setVisibility(View.INVISIBLE);
            usernameFirstLetters.setText(DataHelper.getUsername().substring(0, 2));
            usernameTxt.setText(DataHelper.getUsername());
            Typeface type = Typeface.createFromAsset(this.context.getAssets(), "arial-rounded-mt-bold.ttf");
            hSuggestionBtn.setTypeface(type);
            hFollowingBtn.setTypeface(type);
            final UserModel userModel = new UserModel();
            userModel.setId(DataHelper.getUserId());
            userModel.fetch(new NetworkCallback() {
                @Override
                public void exec(ErrorModel errorModel) {
                    if (userModel.getProfile_picture_key() != null){
                        fUserPhotoSdv.setVisibility(View.VISIBLE);
                        userModel.loadPhoto(fUserPhotoSdv, new SimpleCallback2() {
                            @Override
                            public void callbackCall() {

                            }
                        });
                    }
                }
            });
        }

    }
}
