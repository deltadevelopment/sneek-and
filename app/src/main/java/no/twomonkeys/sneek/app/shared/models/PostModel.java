package no.twomonkeys.sneek.app.shared.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import no.twomonkeys.sneek.app.components.MainActivity;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.MediaManager;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.ProgressRequestBody;
import no.twomonkeys.sneek.app.shared.helpers.Size;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 13.10.2016.
 */

public class PostModel extends CRUDModel implements ProgressRequestBody.UploadCallbacks {
    private int id, media_type, caption_position, story_id, image_width, image_height, file_size, expireIndex;
    private String media_key, media_url, thumbnail_url, caption, body, created_at, expires_at;
    private boolean quarantine, pinned;
    private UserModel userModel;
    //virtual
    private Bitmap image;
    private File mediaFile;
    public PostArtifacts postArtifacts;
    float cellHeight;
    Size size;
    TokenModel tokenModel;

    public File getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(File mediaFile) {
        this.mediaFile = mediaFile;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    public interface AsyncCallback {
        void fileRetrieved(File file);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public PostModel(Map map) {
        build(map);
    }

    public PostModel() {
        tokenModel = new TokenModel();
    }

    public static PostModel newMessageInstance(PostModel previousPostModel) {
        PostModel postModel = new PostModel();
        postModel.setMedia_type(2);
        postModel.created_at = DateHelper.dateNowInString();

        UserModel userModel = new UserModel();
        userModel.setUsername("userDeviceUSername");

        postModel.userModel = userModel;
        //Need to check this when login is in place
        previousPostModel.postArtifacts.isLastInDay = false;
        previousPostModel.postArtifacts.sameUserNext = true;

        postModel.postArtifacts = PostArtifacts.newInstance(true /*previousPostModel.id == DataHelper.getUserId()*/,
                previousPostModel.media_type == 2,
                DateHelper.isSameDayWithDates(DateHelper.dateForString(previousPostModel.getCreated_at()), new Date()));

        return postModel;
    }

    public static PostModel newImageInstance(PostModel previousPostModel) {
        PostModel postModel = new PostModel();
        postModel.setMedia_type(0);
        postModel.created_at = DateHelper.dateNowInString();

        UserModel userModel = new UserModel();
        userModel.setId(DataHelper.getUserId());
        userModel.setUsername(DataHelper.getUsername());

        postModel.userModel = userModel;
        //Need to check this when login is in place
        previousPostModel.postArtifacts.isLastInDay = false;
        previousPostModel.postArtifacts.sameUserNext = true;

        postModel.postArtifacts = PostArtifacts.newInstance(true /*previousPostModel.id == DataHelper.getUserId()*/,
                previousPostModel.media_type == 2,
                DateHelper.isSameDayWithDates(DateHelper.dateForString(previousPostModel.getCreated_at()), new Date()));

        return postModel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCaption_position(int caption_position) {
        this.caption_position = caption_position;
    }

    public void setStory_id(int story_id) {
        this.story_id = story_id;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public void setMedia_key(String media_key) {
        this.media_key = media_key;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public void setQuarantine(boolean quarantine) {
        this.quarantine = quarantine;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setPostArtifacts(PostArtifacts postArtifacts) {
        this.postArtifacts = postArtifacts;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    public void setSize(Size size) {
        this.size = size;
    }


    @Override
    void build(Map map) {
        System.out.println("MAP IS : " + map);
        id = integerFromObject(map.get("id"));
        media_type = integerFromObject(map.get("media_type"));
        caption_position = integerFromObject(map.get("caption_position"));
        story_id = integerFromObject(map.get("story_id"));
        image_width = integerFromObject(map.get("image_width"));
        image_height = integerFromObject(map.get("image_height"));
        file_size = integerFromObject(map.get("file_size"));

        media_key = (String) map.get("media_key");
        if (map.get("media_url") != null){
            media_url = (String) map.get("media_url");
        }

        thumbnail_url = (String) map.get("thumbnail_url");
        caption = (String) map.get("caption");
        body = (String) map.get("body");
        created_at = (String) map.get("created_at");
        expires_at = (String) map.get("expires_at");

        quarantine = booleanFromObject(map.get("quarantine"));
        pinned = booleanFromObject(map.get("pinned"));

        if (map.get("user") != null) {
            userModel = new UserModel((Map) map.get("user"));
        }
        //This should be fixed later
        size = UIHelper.getOptimalSize(MainActivity.mActivity, image_width, image_height);
    }


    public void loadPhoto(final SimpleDraweeView sdv, final SimpleCallback2 scb) {
        Uri uri;
        if (media_type == 0) {
            if (media_url != null){
                DataHelper.addCacheHelp(media_key, media_url);
                uri = Uri.parse(media_url);
            }
            else{
                DataHelper.addCacheHelp(media_key, "blalbla");
                System.out.println("LOADING FILE" + mediaFile.length());
                uri = Uri.fromFile(mediaFile);
            }

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

    public boolean hasCachedVideo() {
        String filename = media_key + ".mp4";

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        File file = new File(path, "/" + filename);

        if (file.exists()) {
            if (file.length() == 0) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public File getVideoFile() {
        String filename = media_key + ".mp4";

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES);
        File file = new File(path, "/" + filename);
        return file;
    }

    //video
    public void fetchVideo(final Activity activity, final AsyncCallback acb) {
        final File videoFile = getVideoFile();
        if (hasCachedVideo()) {
            acb.fileRetrieved(videoFile);
            System.out.println("IS CACHED " + videoFile.length());
        } else {

            try {
                videoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Donwload using stream");

            MediaManager.downloadVideoAsync(getMedia_url(), videoFile, new SimpleCallback2() {
                @Override
                public void callbackCall() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            acb.fileRetrieved(videoFile);
                        }
                    });

                }
            });
        }
    }

    //Called from controllers
    public void save(NetworkCallback ncb) {
        if (media_type == 2) {
            //Save message
            this.body = body.length() > 500 ? body.substring(0, 499) : body;
            HashMap innerMap = new HashMap();
            innerMap.put("body", body);
            innerMap.put("media_type", media_type);
            innerMap.put("expires", expireIndex);

            HashMap<String, HashMap> map = new HashMap();
            map.put("post", innerMap);

            NetworkHelper.sendRequest(NetworkHelper.getNetworkService().postPost(map),
                    GenericContract.v1_post_post(),
                    onDataReturned(),
                    ncb);
        } else {
            //Save image
            saveImage(ncb);
        }
    }

    //Generate upload url
    public void saveImage(final NetworkCallback scb) {
        //Generate upload url
        tokenModel.save(new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                //Upload to S3
                uploadMedia(scb);
            }
        });
    }

    //Upload to S3
    public void uploadMedia(final NetworkCallback scb) {
        setMedia_key(tokenModel.getMedia_key());
        //setMedia_url(tokenModel.getMedia_url());
        Log.v("TOKENOMDEOl", "tokenModel " + tokenModel.getMedia_url() + " : " + mediaFile.length());
        upload(mediaFile, tokenModel.getMedia_url(), new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                //Store to BEN
                savePost(scb);
            }
        });
    }

    public void upload(File file, String url, NetworkCallback scb) {
        NetworkHelper.uploadFile(file, url, scb, this);
        //NetworkHelper.uploadFile(file, url, scb);
    }

    //Save post to BEN
    public void savePost(NetworkCallback scb) {
        HashMap innerMap = new HashMap();
        innerMap.put("media_key", media_key);
        innerMap.put("media_type", media_type);
        innerMap.put("caption", caption == null ? "" : caption);
        innerMap.put("caption_position", caption_position);
        //innerMap.put("longitude", longitude);
        //innerMap.put("latitude", latitude);
        //innerMap.put("place_id", place_id);
        innerMap.put("image_width", image_width);
        innerMap.put("image_height", image_height);
        innerMap.put("expires", expireIndex);

        HashMap<String, HashMap> map = new HashMap();
        map.put("post", innerMap);
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().postPost(map),
                GenericContract.v1_post_post(),
                onDataReturned(),
                scb);
    }

    public void generateFile(Context context, SimpleCallback2 scb) {
        //create a file to write bitmap data
        mediaFile = new File(context.getCacheDir(), "tmp.jpg");
        try {
            mediaFile.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(mediaFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            scb.callbackCall();
            System.out.println("COMPRESSED IMAGE IS NOW " + mediaFile.length()/1024 + "Kb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //setters

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setExpireIndex(int expireIndex) {
        this.expireIndex = expireIndex;
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

    public int getExpireIndex() {
        return expireIndex;
    }
}


