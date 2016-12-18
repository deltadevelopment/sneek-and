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
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.MapCallback;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;

/**
 * Created by Christian Dalsvaag on 04/10/16
 * Copyright 2MONKEYS AS
 */

public class UserModel extends CRUDModel {

    private int id, year_born;
    private String username, email, created_at, updated_at,
            profile_picture_key, profile_picture_url, password;
    private boolean is_following;
    UserSessionModel userSessionModel;
    PostModel lastPost;

    public UserSessionModel getUserSessionModel() {
        return userSessionModel;
    }

    public void setUserSessionModel(UserSessionModel userSessionModel) {
        this.userSessionModel = userSessionModel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setYear_born(int year_born) {
        this.year_born = year_born;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void build(Map map) {
        id = integerFromObject(map.get("id"));
        username = (String) map.get("username");
        email = (String) map.get("email");
        created_at = (String) map.get("created_at");
        updated_at = (String) map.get("updated_at");
        profile_picture_key = (String) map.get("profile_picture_key");
        profile_picture_url = (String) map.get("profile_picture_url");
        is_following = booleanFromObject(map.get("is_following"));
        System.out.println("last moment " + map);
        if (map.get("last_moment") != null) {
            lastPost = new PostModel((Map) map.get("last_moment"));
        }
    }

    public UserModel(Map map) {
        build(map);
    }

    public UserModel() {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getProfile_picture_key() {
        return profile_picture_key;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public boolean is_following() {
        return is_following;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public PostModel getLastPost() {
        return lastPost;
    }

    public void setLastPost(PostModel lastPost) {
        this.lastPost = lastPost;
    }

    public void setProfile_picture_key(String profile_picture_key) {
        this.profile_picture_key = profile_picture_key;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public interface UserExistsCallback {
        public void exists(boolean exists);
    }

    public static void exists(String username, NetworkCallback scb, final UserExistsCallback uec) {
        MapCallback callback = new MapCallback() {
            @Override
            public void callbackCall(Map map) {
                boolean exists = (boolean) map.get("username_exists");
                uec.exists(exists);
            }
        };
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getUsernameExists(username), GenericContract.v1_get_user_username_exists(), callback, scb);
    }

    public void save(NetworkCallback scb) {
        HashMap innerMap = new HashMap();
        innerMap.put("username", username);
        innerMap.put("password", password);
        innerMap.put("year_born", year_born);
        HashMap<String, HashMap> map = new HashMap();
        map.put("user", innerMap);

        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().postUser(map),
                GenericContract.v1_post_user(),
                onDataReturned(),
                scb);
    }

    public void loadPhoto(final SimpleDraweeView sdv, final SimpleCallback2 scb) {
        Uri uri;
        DataHelper.addCacheHelp(profile_picture_key, profile_picture_url);
        uri = Uri.parse(profile_picture_url);

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

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setOldController(sdv.getController())
                .setControllerListener(controllerListener)
                .build();
        sdv.setController(controller);
    }

    public void fetch(NetworkCallback scb) {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getUser(id),
                GenericContract.v1_get_user(),
                onDataReturned(),
                scb);
    }


}
