package no.twomonkeys.sneek.app.shared.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by simenlie on 04.10.2016.
 */

public class UIHelper {

    static public int screenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        return width;
    }

    static public int screenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        return height;
    }

    //TODO: Should move this out to a own util class
    static public int toAlpha(float alpha) {
        int max = 256;
        float result = (max * alpha);
        Log.v("RESULT", "Result is " + result);
        return (int) result;
    }


}
