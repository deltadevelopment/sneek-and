package no.twomonkeys.sneek.app.components;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by simenlie on 21.10.2016.
 */

public class ViewPager extends android.support.v4.view.ViewPager {
    boolean swipeable;

    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (swipeable){
            super.scrollTo(x, y);
        }
    }

    public void setSwipeable(boolean swipeable)
    {
        this.swipeable = swipeable;
    }

}
