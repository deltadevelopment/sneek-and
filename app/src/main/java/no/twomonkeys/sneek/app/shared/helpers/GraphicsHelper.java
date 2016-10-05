package no.twomonkeys.sneek.app.shared.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;

/**
 * Created by simenlie on 05.10.2016.
 */

public class GraphicsHelper {

    public static Bitmap mirrorImage(Bitmap originalBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        matrix.preScale(-1, 1);

        Log.v("TEST", "width " + originalBitmap.getWidth() + " " + originalBitmap.getHeight());
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth(), originalBitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
