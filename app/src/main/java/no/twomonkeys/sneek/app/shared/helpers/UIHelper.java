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

import java.util.ArrayList;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.models.LastSeenModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 04.10.2016.
 */

public class UIHelper {
    public static final int MINIMUM_NEW_POSTS = 4;
    public static final int BOX_USERNAME_SPACE = 8;
    public static final int USERNAME_HEIGHT = 15;
    public static final int SEPERATOR_HEIGHT = 60;
    public static final int NEW_SEPERATOR_HEIGHT = 50;
    public static boolean lastPos;

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
                size = new Size(size.width / scale, size.height / scale);
            } else {
                float percentageBigger = size.height / (UIHelper.screenHeight(context));
                float scale = (UIHelper.screenHeight(context) / percentageBigger) / UIHelper.screenHeight(context);
                size = new Size(size.width * scale, size.height * scale);
                Log.v("Final size", "final size" + size.width + " : " + size.height + " : " + scale + " : " + percentageBigger);
            }
        } else {
            if (size.width > UIHelper.screenWidth(context)) {
                float percentageBigger = (UIHelper.screenWidth(context) / size.width);
                float scale = (size.width / percentageBigger) / size.width;
                size = new Size(size.width / scale, size.height / scale);
            } else {
                float percentageBigger = size.width / (UIHelper.screenWidth(context));
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

    //artifacts
    public static void addArtifacts(ArrayList<PostModel> posts) {
        int indexLoop = 0;
        for (PostModel postModel : posts) {
            if (postModel.getMedia_type() == 2) {
                postModel.postArtifacts = obtainArtifacts(posts, indexLoop, postModel.getUserModel().getId(), postModel.getCreated_at(), postModel.getId(), 0);
                //postModel.cellHeight = heightForText(postModel.size.height, indexLoop, postModel.postArtifacts);
            } else {
                //Moment
                postModel.postArtifacts = obtainArtifacts(posts, indexLoop, postModel.getUserModel().getId(), postModel.getCreated_at(), postModel.getId(), 1);
                postModel.cellHeight = heightForText(postModel.size.height, indexLoop, postModel.postArtifacts);
            }
            indexLoop++;
        }
    }

    //Responsible for caluclating the artifats for each cell and next and previous
    public static PostArtifacts obtainArtifacts(ArrayList<PostModel> posts, int index, int userId, String createdAt, int momentId, int momentType) {
        if (createdAt == null) {
            createdAt = DateHelper.dateNowInString();
        }
        PostArtifacts postArtifacts = new PostArtifacts();
        Boolean isLastSeen = isLastSeen(momentType, momentId, index);

        postArtifacts.isLastSeen = isLastSeen && index > MINIMUM_NEW_POSTS - 1;
        int nextRow = index - 1;
        int prevRow = index + 1;

        if (nextRow >= 0) {
            PostModel postModel = posts.get(nextRow);
            boolean isInTimeRange = DateHelper.isSameTimeWithDates(DateHelper.dateForString(postModel.getCreated_at()), DateHelper.dateForString(createdAt));
            postArtifacts.sameUserNext = userId == postModel.getUserModel().getId() && isInTimeRange;

            Log.v("TIME RANGE", "is " + postArtifacts.sameUserNext + " : " + userId + " : " + postModel.getUserModel().getId() + " : " + isInTimeRange + " : " + createdAt + " : " + postModel.getCreated_at());

            postArtifacts.nextIsMessage = true; //Was not here on moment in iOS version
            postArtifacts.isLastInDay = !DateHelper.isSameDayWithDates(DateHelper.dateForString(postModel.getCreated_at()), DateHelper.dateForString(createdAt));
        }
        if (prevRow < posts.size()) {
            PostModel postModel = posts.get(prevRow);
            boolean isInTimeRange = DateHelper.isSameTimeWithDates(DateHelper.dateForString(createdAt), DateHelper.dateForString(postModel.getCreated_at()));
            postArtifacts.sameUserPrevious = userId == postModel.getUserModel().getId() && isInTimeRange;
            postArtifacts.previousIsMessage = true; //Not here on image in iOS version
            postArtifacts.isSameDay = DateHelper.isSameDayWithDates(DateHelper.dateForString(postModel.getCreated_at()), DateHelper.dateForString(createdAt));
        }

        boolean swap = postArtifacts.sameUserNext;
        if (nextRow < 0) {
            swap = postArtifacts.sameUserPrevious;
        }

        postArtifacts.rightAlignment = getRandomAlignment(swap);

        //   Log.v("TIME RANGE","is " + postArtifacts.sameUserNext);


        return postArtifacts;
    }

    public static boolean getRandomAlignment(boolean isSame) {
        if (isSame) {
            return lastPos;
        } else {
            lastPos = !lastPos;
            return lastPos;
        }

    }

    public static float heightForText(float height, int row, PostArtifacts artifacts) {
        float spacing = 20; //default space between different cells

        if (row == 0) {
            spacing = 7;
        }
        if (artifacts.sameUserNext && !artifacts.isLastInDay) {
            spacing = 2.5f;
        } else {
            // the username etc will show add extra space for this which is 18 (10 is height and 8 is space)
            spacing += BOX_USERNAME_SPACE + USERNAME_HEIGHT;
        }

        if (artifacts.isLastInDay) {
            spacing -= 20;
        }

        if (!artifacts.isSameDay) {
            spacing += SEPERATOR_HEIGHT;
        }

        if (artifacts.isLastSeen) {
            spacing = (spacing / 2) + NEW_SEPERATOR_HEIGHT;
        }

        return height + spacing;
    }

    //Checking on disk if the cell is the last seen by the user
    private static boolean isLastSeen(int lastSeenType, int postId, int indexValue) {
        LastSeenModel lastSeenModel = LastSeenModel.lastSeenModelFromDisk();
        if (lastSeenModel != null && indexValue != 0) {
            if (lastSeenModel.lastSeenType == lastSeenType && lastSeenModel.lastSeenId == postId) {
                return true;
            }
        }

        return false;
    }

}
