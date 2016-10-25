package no.twomonkeys.sneek.app.shared.models;

import java.util.ArrayList;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.MapCallback;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;
import no.twomonkeys.sneek.app.shared.interfaces.ArrayCallback;

/**
 * Created by Christian Dalsvaag on 04/10/16
 * Copyright 2MONKEYS AS
 */

public class FollowingModel extends CRUDModel {

    private int user_id, followee_id;
    private UserModel followee;

    @Override
    void build(Map map) {
        user_id = integerFromObject(map.get("user_id"));
        followee_id = integerFromObject(map.get("followee_id"));
        followee = new UserModel((Map) map.get("followee"));
    }

    public FollowingModel(Map map){
        build(map);
    }

    public static void fetchAll(final ArrayCallback arrayCallback){

        final ArrayList followings = new ArrayList<FollowingModel>();

        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getFollowing(),
                GenericContract.generic_parse(),
                new MapCallback() {
                    @Override
                    public void callbackCall(Map map) {
                        ArrayList<Map> followingsArray = (ArrayList<Map>) map.get("followings");

                        for(Map followingMap : followingsArray){
                            followings.add(new UserModel((Map)followingMap.get("followee")));
                        }
                    }
                },
                new NetworkCallback() {
                    @Override
                    public void exec(ErrorModel errorModel) {

                        arrayCallback.exec(followings, errorModel);
                    }
                }
        );

    }

    public int getUser_id() {
        return user_id;
    }

    public UserModel getFollowee() {
        return followee;
    }

    public int getFollowee_id() {
        return followee_id;
    }


}
