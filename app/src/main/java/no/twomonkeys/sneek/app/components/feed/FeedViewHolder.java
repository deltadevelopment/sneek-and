package no.twomonkeys.sneek.app.components.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

/**
 * Created by simenlie on 27.10.2016.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder {
    PostModel postModel;
    Context context;

    public interface Callback {
        public void imageViewHolderVideoStarted(ImageViewHolder imageViewHolder);
        public void imageViewHolderTap(PostModel postModel);
        public void imageViewHolderLongPress();
        public void feedViewHolderDidDelete(PostModel postModel);
        public void feedViewHolderShowProfile(UserModel userModel);
    }

    Callback callback;

    public FeedViewHolder(View itemView) {
        super(itemView);
    }

    public void showPopUpMenu(View view) {
        //  callback.imageViewHolderLongPress();
        PopupMenu popupMenu = new PopupMenu(context, view);
        //MenuInflater menuInflater = popupMenu.getMenuInflater();
        //menuInflater.inflate(R.menu.my_contextual_menu, popupMenu.getMenu());
        if (postModel.getUserModel().getId() == DataHelper.getUserId()) {
            popupMenu.getMenu().add(0, 0, Menu.NONE, "Show profile");
            popupMenu.getMenu().add(0, 1, Menu.NONE, postModel.isPinned() ? "Unkeep" : "Keep");
            popupMenu.getMenu().add(0, 2, Menu.NONE, "Delete");
            if (!postModel.isPinned()) {
                popupMenu.getMenu().add(0, 3, Menu.NONE, "Post expires in " + DateHelper.shortTimeAfter(postModel.getExpires_at()));
                colorMenuItem(popupMenu.getMenu().getItem(3), R.color.captionGrey);
            }
            if (postModel.isPinned()) {
                colorMenuItem(popupMenu.getMenu().getItem(1), R.color.themeColor);
            }
            colorMenuItem(popupMenu.getMenu().getItem(2), R.color.themeColor);

        } else {
            popupMenu.getMenu().add(0, 4, Menu.NONE, "Report");
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        //go to profile
                        break;
                    case 1:
                        showPinUnpinDialog();
                        break;
                    case 2:
                        showDeleteMomentDialog();
                        break;
                    case 3:
                        //expires
                    case 4:
                        //report
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void pinUnpinMoment() {
        if (postModel.isPinned()) {
            //Unpin
            postModel.deleteKeep(new NetworkCallback() {
                @Override
                public void exec(ErrorModel errorModel) {

                }
            });
        } else {
            //Pin
            postModel.saveKeep(new NetworkCallback() {
                @Override
                public void exec(ErrorModel errorModel) {

                }
            });
        }
    }

    private void deleteMoment() {
        postModel.delete(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                if (errorModel != null) {
                    boolean isNull = callback == null;
                    System.out.println("GOT HERE AND " + isNull);
                    callback.feedViewHolderDidDelete(postModel);
                }
            }
        });
    }

    private void showPinUnpinDialog() {
        String title = context.getResources().getString(!postModel.isPinned() ? R.string.pin_title : R.string.pin_remove_title);
        String body = context.getResources().getString(!postModel.isPinned() ? R.string.pin_body : R.string.pin_remove_body);

        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        pinUnpinMoment();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDeleteMomentDialog() {

        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(context.getResources().getString(R.string.delete_moment_title))
                .setMessage(context.getResources().getString(R.string.delete_moment_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteMoment();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void colorMenuItem(MenuItem menuItem, int colorId) {
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorId)), 0, s.length(), 0);
        menuItem.setTitle(s);
    }

}
