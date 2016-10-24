package no.twomonkeys.sneek.app.shared.helpers;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;

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

    static public Size getOptimalSize(Context context, int width, int height) {
        Size size;
        if (width != 0) {
            size = new Size(width, height);
        } else {
            int maxWidth = UIHelper.screenWidth(context);
            double maxHeight = maxWidth * 1.333333;
            size = new Size(maxWidth, (int) maxHeight);
        }

        return UIHelper.sizeForBigStory(context, size);
    }

    public static Size sizeForBigStory(Context context, Size size) {
        if (size.width < size.height) {
            if (size.height > UIHelper.screenHeight(context)) {
                float percentageBigger = (UIHelper.screenHeight(context) / size.height);
                float scale = (size.height / percentageBigger) / size.height;
                size = new Size(size.width / scale,size.height / scale);
            }
            else{
                float percentageBigger =  size.height / (UIHelper.screenHeight(context));
                float scale = (UIHelper.screenHeight(context) / percentageBigger) / UIHelper.screenHeight(context);
                size = new Size(size.width * scale,size.height * scale);
                Log.v("Final size","final size" + size.width + " : " +  size.height + " : " + scale + " : " + percentageBigger);
            }
        } else {
            if (size.width > UIHelper.screenWidth(context)) {
                float percentageBigger = (UIHelper.screenWidth(context) / size.width);
                float scale = (size.width / percentageBigger) / size.width;
                size = new Size(size.width / scale, size.height / scale);
            }
            else{
                float percentageBigger =  size.width /(UIHelper.screenWidth(context));
                float scale = (size.width / percentageBigger) / size.width;
                size = new Size(size.width * scale, size.height * scale);
            }
        }

        float padding = 5;

        if (size.width > UIHelper.screenWidth(context)) {
            float percentage = UIHelper.screenWidth(context) / size.width;
            size.height = (int) (size.height * percentage);
            size.width = UIHelper.screenWidth(context);
        }
        float width = (int) (size.width / 1.9);
        float height = (int) (size.height / 1.9);

        //Padding is 5, find how much 5 pixel from the width is in height
        float percentageOfWidth = (padding / width) * 100;
        float heightPixels = (height * percentageOfWidth) / 100;

        //the finished max height and width
        float resultWidth = width - padding;
        float resultheight = height - heightPixels;

        return new Size((int) resultWidth, (int) resultheight);

    }

    static public int dpToPx(Context c, int dp) {
        final float scale = c.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    public static float[] cornersForType(boolean rightAlignment, PostArtifacts artifacts) {
        //topleft, topright, bottomright, bottomLeft

        float spinoff = UIHelper.dpToPx(MainActivity.mActivity, 60);

        float rounded = UIHelper.dpToPx(MainActivity.mActivity, 10);
        float notRounded = UIHelper.dpToPx(MainActivity.mActivity, 2);
        float[] rectCorners;

        if (rightAlignment) {
            if (artifacts.sameUserNext && !artifacts.sameUserPrevious) {
                rectCorners = new float[]{rounded, rounded, notRounded, rounded};
            } else if (artifacts.sameUserNext && artifacts.sameUserPrevious) {
                if (artifacts.isLastInDay) {
                    if (!artifacts.isSameDay) {
                        //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                        rectCorners = new float[]{rounded, rounded, rounded, rounded};
                    } else {
                        //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight);
                        rectCorners = new float[]{rounded, notRounded, rounded, rounded};
                    }
                } else {
                    if (!artifacts.isSameDay) {
                        //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerTopRight);
                        rectCorners = new float[]{rounded, rounded, notRounded, rounded};
                    } else {
                        //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft);
                        rectCorners = new float[]{rounded, notRounded, notRounded, rounded};
                    }
                }
            } else if (!artifacts.sameUserNext && artifacts.sameUserPrevious) {
                if (!artifacts.isSameDay) {
                    //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                    rectCorners = new float[]{rounded, rounded, rounded, rounded};
                } else {
                    //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight);
                    rectCorners = new float[]{rounded, notRounded, rounded, rounded};
                }
            } else {
                //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                rectCorners = new float[]{rounded, rounded, rounded, rounded};
            }
        } else {
            //HERE GOES ALL
            //topleft, topright, bottomright, bottomLeft
            if (artifacts.sameUserNext && !artifacts.sameUserPrevious) {
                // rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                rectCorners = new float[]{rounded, rounded, rounded, notRounded};
            } else if (artifacts.sameUserNext && artifacts.sameUserPrevious) {
                if (artifacts.isLastInDay) {
                    if (!artifacts.isSameDay) {
                        // rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                        rectCorners = new float[]{rounded, rounded, rounded, rounded};
                    } else {
                        //rectCorners = (UIRectCornerTopRight | UIRectCornerBottomLeft | UIRectCornerBottomRight);
                        rectCorners = new float[]{notRounded, rounded, rounded, rounded};
                    }
                } else {
                    if (!artifacts.isSameDay) {
                        //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                        rectCorners = new float[]{rounded, rounded, rounded, notRounded};
                    } else {
                        // rectCorners = (UIRectCornerTopRight | UIRectCornerBottomRight);
                        rectCorners = new float[]{notRounded, rounded, rounded, notRounded};
                    }
                }
            }
            //topleft, topright, bottomright, bottomLeft
            else if (!artifacts.sameUserNext && artifacts.sameUserPrevious) {
                if (!artifacts.isSameDay) {
                    //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                    rectCorners = new float[]{rounded, rounded, rounded, rounded};
                } else {
                    //rectCorners = (UIRectCornerTopRight | UIRectCornerBottomLeft | UIRectCornerBottomRight);
                    rectCorners = new float[]{notRounded, rounded, rounded, rounded};
                }

            } else {
                //rectCorners = (UIRectCornerTopLeft | UIRectCornerBottomLeft | UIRectCornerBottomRight | UIRectCornerTopRight);
                rectCorners = new float[]{rounded, rounded, rounded, rounded};
            }
        }
        return rectCorners;
    }

    public static void layoutBtnRelative(Context c, Button btn, String title) {
        btn.setBackgroundColor(c.getResources().getColor(R.color.blackColor));
        btn.setTextColor(c.getResources().getColor(R.color.white));
        btn.setTypeface(Typeface.create("HelveticaNeue", 0));

        btn.setText(title);
        int margin = UIHelper.dpToPx(c, 5);
        int btnHeight = UIHelper.dpToPx(c, 30);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height = btnHeight;

        params.setMargins(0, margin * 2, 0, 0);

        Paint paint = new Paint();
        Rect bounds = new Rect();

        int text_height = 0;
        int text_width = 0;

        paint.setTypeface(btn.getTypeface());// your preference here
        paint.setTextSize(btn.getTextSize());// have this the same as your text size

        String text = btn.getText().toString();

        paint.getTextBounds(text, 0, text.length(), bounds);

        text_height = bounds.height();
        text_width = bounds.width() + (margin * 2) + 10;
        params.width = text_width;
        btn.setLayoutParams(params);
    }

}
