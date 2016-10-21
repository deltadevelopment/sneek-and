package no.twomonkeys.sneek.app.components.feed.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 21.10.2016.
 */

public class SimpleImageViewer extends RelativeLayout {
    Context context;
    SimpleDraweeView sPostSdv;
    RelativeLayout sWrapperRl;
    float touchActionDownX, touchActionDownY, touchActionMoveX, touchActionMoveY, lastX;
    boolean touchActionMoveStatus, lastScrolledUp, isClick;
    float scrollDy;
    float startY;
    RelativeLayout sRootRl;
    TextView sCaptionTxt;
    int lastBgAlpha;
    boolean movingUp;

    public interface Callback {
        public void simpleImageViewerClose();

        public void simpleImageViewerAnimatedIn();
    }

    Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public SimpleImageViewer(Context context) {
        super(context);
        initializeViews(context);
    }

    public SimpleImageViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public SimpleImageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_simple_image, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        sPostSdv = (SimpleDraweeView) findViewById(R.id.sPostSdv);
        sWrapperRl = (RelativeLayout) findViewById(R.id.sWrapperRl);
        sRootRl = (RelativeLayout) findViewById(R.id.sRootRl);
        sCaptionTxt = (TextView) findViewById(R.id.sCaptionTxt);

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouch(event);
                onTouchEvent(event);
                return true;
            }
        });
    }

    public Size getFullScreenSize(float width, float height) {
        float screenWidth = UIHelper.screenWidth(getContext());
        float screenHeight = UIHelper.screenHeight(getContext());
        float result;

        if (height > width) {
            result = width / height;
        } else {
            result = height / width;
        }

        float finalHeight = screenHeight * result;

        return new Size(screenWidth, finalHeight);
    }

    public void updatePost(PostModel postModel) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Size size = getFullScreenSize(postModel.getImage_width(), postModel.getImage_height());
        params.width = (int) size.width;
        params.height = (int) size.height;
        params.setMargins(0, 0, 0, 0);
        params.addRule(CENTER_IN_PARENT);
        sPostSdv.setLayoutParams(params);
        float extraSpace = (UIHelper.screenHeight(getContext()) - size.height) / 2;

        if (postModel.getCaption().isEmpty()) {
            sCaptionTxt.setVisibility(INVISIBLE);
        } else {
            sCaptionTxt.setTranslationY(size.height + extraSpace);
            sCaptionTxt.setVisibility(VISIBLE);
            sCaptionTxt.setText(postModel.getCaption());
        }


        sWrapperRl.setTranslationY(UIHelper.screenHeight(getContext()));
        animateIn();
        postModel.loadPhoto(sPostSdv, new SimpleCallback2() {
            @Override
            public void callbackCall() {

            }
        });
    }

    //Movement
    public void handleTouch(MotionEvent ev) {
        float threshold = 2.0f;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                touchActionDownX = (int) ev.getX();
                touchActionDownY = (int) ev.getY();
                touchActionMoveStatus = true;
                //cameraFragment.startMove(ev.getRawX());
                //wrapperDx = wrapper.getX() - ev.getRawX();
                //menuFragment.startMove(ev.getRawX());
                scrollDy = getY() - ev.getRawY();
                lastX = ev.getRawY();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                touchActionMoveStatus = true;
                break;

            case MotionEvent.ACTION_MOVE:
                isClick = false;
                if (touchActionMoveStatus) {
                    touchActionMoveX = (int) ev.getX();
                    touchActionMoveY = (int) ev.getY();


                    float ratioLeftRight = Math.abs(touchActionMoveX - touchActionDownX) / Math.abs(touchActionMoveY - touchActionDownY);
                    float ratioUpDown = Math.abs(touchActionMoveY - touchActionDownY) / Math.abs(touchActionMoveX - touchActionDownX);

                    if (touchActionMoveX < touchActionDownX && ratioLeftRight > threshold) {
                        Log.i("test", "Move Left");
                        //direction = DirectionHelper.LEFT;
                        touchActionMoveStatus = false;
                    } else if (touchActionMoveX > touchActionDownX && ratioLeftRight > threshold) {
                        Log.i("test", "Move Right");
                        //direction = DirectionHelper.RIGHT;
                        touchActionMoveStatus = false;
                    } else if (touchActionMoveY < touchActionDownY && ratioUpDown > threshold) {
                        Log.i("test", "Move Up");
                        movingUp = true;
                        //direction = DirectionHelper.UP;
                        touchActionMoveStatus = false;
                    } else if (touchActionMoveY > touchActionDownY && ratioUpDown > threshold) {
                        Log.i("test", "Move Down");
                        movingUp = false;
                        //direction = DirectionHelper.DOWN;
                        touchActionMoveStatus = false;
                    }
                }
                move(ev);
                //check whether the user changed scroll direction while dragging
                if (lastX > ev.getRawY()) {
                    lastX = ev.getRawY();
                    lastScrolledUp = true;
                } else {
                    lastX = ev.getRawY();
                    lastScrolledUp = false;
                }

                break;
            case MotionEvent.ACTION_UP: {
                if (isClick) {
                    Log.v("IS CLICK", "CLICK CLIKC");
                    /*
                    if (mViewPager.getCurrentItem() == storyModel.getMoments().size() - 1) {
                        animateOut();
                    } else {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, false);
                    }
                    */
                } else {
                    if (movingUp) {
                        if (lastScrolledUp) {
                            animateOut();
                        } else {
                            animateIn();
                        }
                    } else {
                        if (lastScrolledUp) {
                            animateIn();
                        } else {
                            animateOut();
                        }
                    }


                }

                break;
            }
        }
    }

    public int getAlpha(float scroll) {
        return (int) convertToRGBAlpha(getAlpha2(scroll));
    }

    float convertToRGBAlpha(float percentage) {
        float bigAlpha = 255;
        float result = (percentage * bigAlpha);
        return result;
    }

    float getAlpha2(float scroll) {
        float minPosition = 0;
        float maxPosition = UIHelper.screenHeight(getContext());
        float result = scroll / maxPosition;
        if (movingUp) {
            result = -result;
        }

        System.out.println("SCROLL " + scroll + " : " + maxPosition + " : " + result);

        return 0.97f - result;
    }

    public void move(MotionEvent m) {
        float result = m.getRawY() + scrollDy;


        if (movingUp) {
            if (result < 0) {
                lastBgAlpha = getAlpha(result);
                sRootRl.getBackground().setAlpha(lastBgAlpha);
                sWrapperRl.setY(result);
            } else {
                sWrapperRl.setY(0);
            }

        } else {
            if (result > 0) {
                lastBgAlpha = getAlpha(result);
                sRootRl.getBackground().setAlpha(lastBgAlpha);
                sWrapperRl.setY(result);
            } else {
                sWrapperRl.setY(0);
            }
        }
    }

    public void animateIn() {
        System.out.println("ANIMATING IN");
        ObjectAnimator animator = ObjectAnimator
                .ofPropertyValuesHolder(sRootRl.getBackground(),
                        PropertyValuesHolder.ofInt("alpha", lastBgAlpha, 255));
        animator.setTarget(sRootRl.getBackground());
        animator.setDuration(200);
        animator.start();


        sWrapperRl.animate()
                .setDuration(200)
                .translationY(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                callback.simpleImageViewerAnimatedIn();
            }
        });
    }

    public void animateOut() {

        ObjectAnimator animator = ObjectAnimator
                .ofPropertyValuesHolder(sRootRl.getBackground(),
                        PropertyValuesHolder.ofInt("alpha", lastBgAlpha, 0));
        animator.setTarget(sRootRl.getBackground());
        animator.setDuration(200);
        animator.start();

        float yPos;
        if (movingUp) {
            yPos = -UIHelper.screenHeight(getContext());
        } else {
            yPos = UIHelper.screenHeight(getContext());
        }

        sWrapperRl.animate()
                .setDuration(200)
                .translationY(yPos)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(INVISIBLE);
                        callback.simpleImageViewerClose();
                    }
                });
    }

}
