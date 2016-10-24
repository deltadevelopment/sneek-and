package no.twomonkeys.sneek.app.components.friends;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import no.twomonkeys.sneek.R;

/**
 * Created by simenlie on 24.10.2016.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    Button hFollowingBtn, hSuggestionBtn;
    Context context;
   public  interface Callback
    {
        public void headerDidTapSuggestion();
        public void headerDidTapFollowing();
    }

    Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public HeaderViewHolder(View itemView) {
        super(itemView);
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
        this.context = context;
        Typeface type = Typeface.createFromAsset(this.context.getAssets(), "arial-rounded-mt-bold.ttf");
        hSuggestionBtn.setTypeface(type);
        hFollowingBtn.setTypeface(type);
    }
}
