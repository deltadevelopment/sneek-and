package no.twomonkeys.sneek.app.shared.models;

import java.util.Map;

/**
 * Created by Christian Dalsvaag on 04/10/16
 * Copyright 2MONKEYS AS
 */

public class UserModel extends CRUDModel{

    private int id;
    private String username, email, created_at, updated_at,
            profile_picture_key, profile_picture_url;
    private boolean is_following;


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

}
