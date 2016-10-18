package no.twomonkeys.sneek.app.shared.models;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.util.Map;

import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 13.10.2016.
 */

public class PostModel extends CRUDModel {
    private int id, media_type, caption_position, story_id, image_width, image_height, file_size;
    private String media_key, media_url, thumbnail_url, caption, body, created_at, expires_at;
    private boolean quarantine, pinned;
    private UserModel userModel;
    //virtual
    public PostArtifacts postArtifacts;
    float cellHeight;
    Size size;

    public PostModel(Map map) {
        build(map);
    }

    @Override
    void build(Map map) {
        id = integerFromObject(map.get("id"));
        media_type = integerFromObject(map.get("media_type"));
        caption_position = integerFromObject(map.get("caption_position"));
        story_id = integerFromObject(map.get("story_id"));
        image_width = integerFromObject(map.get("image_width"));
        image_height = integerFromObject(map.get("image_height"));
        file_size = integerFromObject(map.get("file_size"));

        media_key = (String) map.get("media_key");
        media_url = (String) map.get("media_url");
        thumbnail_url = (String) map.get("thumbnail_url");
        caption = (String) map.get("caption");
        body = (String) map.get("body");
        created_at = (String) map.get("created_at");
        expires_at = (String) map.get("expires_at");

        quarantine = booleanFromObject(map.get("quarantine"));
        pinned = booleanFromObject(map.get("pinned"));

        userModel = new UserModel((Map) map.get("user"));

        //This should be fixed later
        size = UIHelper.getOptimalSize(MainActivity.mActivity, image_width, image_height);
    }


    public void loadPhoto(SimpleDraweeView sdv, final SimpleCallback2 scb) {
        Uri uri;
        if (media_type == 0) {
            DataHelper.addCacheHelp(media_key, media_url);
            uri = Uri.parse(media_url);
        } else {
            DataHelper.addCacheHelp(media_key, thumbnail_url);
            uri = Uri.parse(thumbnail_url);
        }
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {

                scb.callbackCall();
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                // FLog.d("Intermediate image received")
                Log.v("Img Re", "Recieved");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setOldController(sdv.getController())
                .setControllerListener(controllerListener)
                .build();
        sdv.setController(controller);

    }


    //Getters

    public int getId() {
        return id;
    }

    public int getMedia_type() {
        return media_type;
    }

    public int getCaption_position() {
        return caption_position;
    }

    public int getStory_id() {
        return story_id;
    }

    public int getImage_width() {
        return image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public int getFile_size() {
        return file_size;
    }

    public String getMedia_key() {
        return media_key;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getCaption() {
        return caption;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public boolean isQuarantine() {
        return quarantine;
    }

    public boolean isPinned() {
        return pinned;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public PostArtifacts getPostArtifacts() {
        return postArtifacts;
    }
}


