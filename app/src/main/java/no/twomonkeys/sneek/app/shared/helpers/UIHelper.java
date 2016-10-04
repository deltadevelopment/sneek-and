package no.twomonkeys.sneek.app.shared.helpers;

import android.content.Context;
import android.util.DisplayMetrics;
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


}
