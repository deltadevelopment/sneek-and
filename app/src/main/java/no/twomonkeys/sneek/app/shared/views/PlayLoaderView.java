package no.twomonkeys.sneek.app.shared.views;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import no.twomonkeys.sneek.R;

/**
 * Created by simenlie on 20.10.2016.
 */

public class PlayLoaderView extends RelativeLayout {
    Context context;
    ImageView circleIb;
    Animator.AnimatorListener al, al2;
    boolean shouldAnimate;

    public PlayLoaderView(Context context) {
        super(context);
        initializeViews(context);
    }

    public PlayLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public PlayLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.play_loader, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        circleIb = (ImageView) findViewById(R.id.circleIb);


    }

    public void show()
    {
        setVisibility(VISIBLE);
        circleIb.setAlpha(1f);
    }

    public void startAnimating() {
        show();
        shouldAnimate = true;
        doAnimation();
    }

    public void stopAnimating() {
        shouldAnimate = false;
        setVisibility(INVISIBLE);
    }

    private void doAnimation() {
        if (shouldAnimate) {
            circleIb.animate()
                    .alpha(0.3f)
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (shouldAnimate) {
                                circleIb.animate()
                                        .alpha(1f)
                                        .setDuration(600)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                doAnimation();
                                            }
                                        })
                                        .start();
                            }
                        }
                    })
                    .start();
        }
    }
}
