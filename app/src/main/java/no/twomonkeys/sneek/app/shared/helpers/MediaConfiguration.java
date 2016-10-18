package no.twomonkeys.sneek.app.shared.helpers;

import android.graphics.Bitmap;

/**
 * Created by simenlie on 14.10.2016.
 */

public class MediaConfiguration {
   static Bitmap photoTaken;

    public static Bitmap getPhotoTaken() {
        return photoTaken;
    }

    public static void setPhotoTaken(Bitmap photoTaken) {
        MediaConfiguration.photoTaken = photoTaken;
    }
}
