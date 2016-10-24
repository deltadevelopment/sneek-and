package no.twomonkeys.sneek.app.components.feed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.Date;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.helpers.VideoHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.views.PlayLoaderView;
import no.twomonkeys.sneek.app.shared.views.SneekVideoView;

/**
 * Created by simenlie on 13.10.2016.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView draweeView;
    TextView createdAtTv, usernameTv;
    LinearLayout imageRowLl;
    RelativeLayout loadingRl;
    ProgressBar progressBar;
    PlayLoaderView playLoaderV;
    SneekVideoView postVideoView;
    PostModel postModel;
    private MediaController mediaControls;
    private int mPlayerPosition;
    VideoHelper videoHelper;
    View videoViewRp;
    private MediaPlayer mediaPlayer;
    Context context;
    boolean isVisible = true;
    boolean longPressing;
    TextView iDateTxt;
    LinearLayout userLayout;
    RelativeLayout playLoaderRl;

    public interface Callback {
        public void imageViewHolderVideoStarted(ImageViewHolder imageViewHolder);

        public void imageViewHolderTap(PostModel postModel);
    }

    Callback callback;

    ImageViewHolder(View view) {
        super(view);
        loadingRl = (RelativeLayout) view.findViewById(R.id.loadingRl);
        imageRowLl = (LinearLayout) view.findViewById(R.id.imageRowLl);
        draweeView = getDraweeView();
        usernameTv = (TextView) itemView.findViewById(R.id.usernameTv);
        createdAtTv = (TextView) itemView.findViewById(R.id.createdAtTv);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        playLoaderV = getPlayLoaderV();
        postVideoView = getPostVideoView();
        videoViewRp = (View) itemView.findViewById(R.id.videoViewRp);
        iDateTxt = (TextView) itemView.findViewById(R.id.iDateTxt);
        userLayout = (LinearLayout) itemView.findViewById(R.id.userLayout);
        playLoaderRl = (RelativeLayout) itemView.findViewById(R.id.playLoaderRl);
    }

    private SimpleDraweeView getDraweeView() {
        if (this.draweeView == null) {
            SimpleDraweeView draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (longPressing) {
                        longPressing = false;
                    } else {
                        System.out.println("SINGLE TAP IMAGE");
                        callback.imageViewHolderTap(postModel);
                    }
                }
            });
            draweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longPressing = true;
                    System.out.println("LONG PRESSING");
                    return false;
                }
            });
            this.draweeView = draweeView;
        }
        return this.draweeView;
    }

    private SneekVideoView getPostVideoView() {
        if (this.postVideoView == null) {
            SneekVideoView postVideoView = (SneekVideoView) itemView.findViewById(R.id.postVideoView);

            this.postVideoView = postVideoView;
        }

        return this.postVideoView;
    }

    private PlayLoaderView getPlayLoaderV() {
        if (this.playLoaderV == null) {
            PlayLoaderView playLoaderV = (PlayLoaderView) itemView.findViewById(R.id.playLoaderV);
            playLoaderV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadVideo();
                }
            });

            this.playLoaderV = playLoaderV;
        }

        return this.playLoaderV;
    }

    private void loadVideo() {
        callback.imageViewHolderVideoStarted(this);
        this.playLoaderV.startAnimating();
        System.out.println("FETCHING MOVIEW");
        final ImageViewHolder self = this;
        postModel.fetchVideo((Activity) context, new PostModel.AsyncCallback() {
            @Override
            public void fileRetrieved(File file) {
                System.out.println("FILE retrieved");
                self.loadVideo2(file);
            }
        });
    }

    public void stopVideo() {
        postVideoView.stopPlayback();
        postVideoView.setVisibility(View.INVISIBLE);
        draweeView.setVisibility(View.VISIBLE);
        playLoaderV.show();
    }

    public void loadVideo2(File file) {
        postVideoView.setVisibility(View.VISIBLE);

        if (mediaControls == null) {
            mediaControls = new MediaController(context);
        }
        postVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                postVideoView.setVisibility(View.INVISIBLE);
                draweeView.setVisibility(View.VISIBLE);
                playLoaderV.show();
            }
        });

        postVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                System.out.println("ERROR");
                mPlayerPosition = postVideoView.getCurrentPosition();
                if (isVisible) {
                    postVideoView.resume();
                }
                postVideoView.requestFocus();
                return true;
            }
        });
        postVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            // video started; hide the placeholder.
                            playLoaderV.stopAnimating();
                            draweeView.setVisibility(View.INVISIBLE);
                            return true;
                        }
                        return false;
                    }
                });
                mediaPlayer = mp;
                if (isVisible) {
                    postVideoView.start();
                }

            }
        });

        // postVideoView.setVideoURI(Uri.fromFile(file));
        postVideoView.setVideoPath(file.getAbsolutePath());
        postVideoView.requestFocus();
        //postVideoView.start();
    }


    public void updateHolder(Context context, final PostModel postModel) {
        this.context = context;
        this.postModel = postModel;
        boolean rightAlignment = postModel.getUserModel().getId() == DataHelper.getUserId();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) userLayout.getLayoutParams();
        Size size = UIHelper.getOptimalSize(context, postModel.getImage_width(), postModel.getImage_height());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = (int) size.width;
        params.height = (int) size.height;
        params.setMargins(0, 0, 0, 0);
        if (rightAlignment) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        else{
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        draweeView.setLayoutParams(params);
        loadingRl.setLayoutParams(params);
        playLoaderRl.setLayoutParams(params);

        postVideoView.setLayoutParams(params);
        videoViewRp.setLayoutParams(params);

        usernameTv.setText(postModel.getUserModel().getUsername());
        createdAtTv.setText(DateHelper.shortTime(postModel.getCreated_at()));
        playLoaderV.setVisibility(View.INVISIBLE);

        postModel.loadPhoto(draweeView, new SimpleCallback2() {
            @Override
            public void callbackCall() {
                loadingRl.setVisibility(View.GONE);
                if (postModel.getMedia_type() == 1) {
                    // playLoaderV.startAnimating();
                    playLoaderV.setVisibility(View.VISIBLE);
                } else {
                    playLoaderV.setVisibility(View.INVISIBLE);
                }
            }
        });


        PostArtifacts artifacts = postModel.getPostArtifacts();

        if (artifacts.isSameDay) {
            iDateTxt.setVisibility(View.GONE);
        } else {
            iDateTxt.setVisibility(View.VISIBLE);
            if (postModel.getCreated_at() != null) {
                iDateTxt.setText(DateHelper.prettyMonthYear(postModel.getCreated_at()));
                //[self.dateLabel setText:[DateHelper prettyStringFromDate3:createdAt]];
                System.out.println("PRETTY " + DateHelper.prettyStringFromDate(postModel.getCreated_at()));
            } else {
                iDateTxt.setText(DateHelper.dateNowInString());
            }
        }

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
            if (artifacts.isLastInDay) {
                bottomPadding = 0;
            }
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

        float[] rectCorners = UIHelper.cornersForType(rightAlignment, artifacts);

        roundingParams.setCornersRadii(rectCorners[0], rectCorners[1], rectCorners[2], rectCorners[3]);

        draweeView.getHierarchy().setRoundingParams(roundingParams);

        GradientDrawable shape = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        float[] newRectCorners = new float[]{rectCorners[0], rectCorners[0], rectCorners[1], rectCorners[1], rectCorners[2], rectCorners[2], rectCorners[3], rectCorners[3]};
        shape.setCornerRadii(newRectCorners);
        shape.setColor(ContextCompat.getColor(context, R.color.cameraGrey));
        loadingRl.setBackground(shape);
        //videoViewRp.setBackground(shape);
        // postVideoView.setBackground(shape);
        test(rectCorners, size);
    }

    void test(float[] rectCorners, Size size) {
        GradientDrawable shape = new GradientDrawable();
        // top-left, top-right, bottom-right, bottom-left
        float[] newRectCorners = new float[]{rectCorners[0], rectCorners[0], rectCorners[1], rectCorners[1], rectCorners[2], rectCorners[2], rectCorners[3], rectCorners[3]};
        shape.setCornerRadii(newRectCorners);
        shape.setColor(ContextCompat.getColor(context, R.color.transparent));
        Drawable d = new BitmapDrawable(context.getResources(), createMask((int) size.width, (int) size.height, rectCorners));
        d.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
        videoViewRp.setBackground(d);
    }

    private Bitmap createMask(int width, int height, float[] rectCorners) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);

        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //canvas.drawRoundRect(new RectF(0, 100, width, height/2), 100, 100, paint);
        System.out.println("Rounding params is " + rectCorners[0] + " : " + rectCorners[1] + " : " + rectCorners[2] + " : " + rectCorners[3] + " : ");
        Path path = RoundedRect(0, 0, width, height, 26, 26,
                isRounded(rectCorners[0]), isRounded(rectCorners[1]), isRounded(rectCorners[2]), isRounded(rectCorners[3]));
        canvas.drawPath(path, paint);

        return mask;
    }

    public boolean isRounded(float value) {
        if (value == 26) {
            return true;
        } else {
            return false;
        }
    }

    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl
    ) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

}
