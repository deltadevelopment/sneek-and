package no.twomonkeys.sneek.app.shared.models;

import java.util.HashMap;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.MapCallback;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;

/**
 * Created by Christian Dalsvaag on 04/10/16
 * Copyright 2MONKEYS AS
 */

public class UserModel extends CRUDModel{

    private int id, year_born;
    private String username, email, created_at, updated_at,
            profile_picture_key, profile_picture_url, password;
    private boolean is_following;
    UserSessionModel userSessionModel;

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
    public void build(Map map){
        id = integerFromObject(map.get("id"));
        username = (String) map.get("username");
        email = (String) map.get("email");
        created_at = (String) map.get("created_at");
        updated_at = (String) map.get("updated_at");
        profile_picture_key = (String) map.get("profile_picture_key");
        profile_picture_url = (String) map.get("profile_picture_url");
        is_following = booleanFromObject(map.get("is_following"));
    }

    public UserModel(Map map){
        build(map);
    }
    public UserModel(){}

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


}
