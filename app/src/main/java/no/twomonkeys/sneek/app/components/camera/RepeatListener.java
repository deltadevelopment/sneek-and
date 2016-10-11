package no.twomonkeys.sneek.app.components.camera;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by simenlie on 11.10.2016.
 */

public class RepeatListener implements View.OnTouchListener {

    private Handler handler = new Handler();

    private int initialInterval;
    private final int normalInterval;
    private final Listener listener;

    public interface Listener {
        void onSingleTap();

        void onLongPress();
    }

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            listener.onLongPress();
        }
    };

    private View downView;

    /**
     * @param initialInterval The interval after first click event
     * @param normalInterval  The interval after second and subsequent click
     *                        events
     */
    public RepeatListener(int initialInterval, int normalInterval,
                          Listener listener) {
        this.listener = listener;
        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(handlerRunnable);
                handler.postDelayed(handlerRunnable, initialInterval);
                downView = view;
                downView.setPressed(true);
                listener.onSingleTap();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(handlerRunnable);
                downView.setPressed(false);
                downView = null;
                return true;
        }

        return false;
    }

}
