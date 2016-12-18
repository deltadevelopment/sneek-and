package no.twomonkeys.sneek.app.shared.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.DateHelper;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;
import no.twomonkeys.sneek.app.shared.helpers.PostArtifacts;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 13.10.2016.
 */

public class FeedModel extends CRUDModel {

    ArrayList<PostModel> posts;


    @Override
    void build(Map map) {
        ArrayList<Map> postsRaw = (ArrayList) map.get("posts");

        posts = new ArrayList<>();
        System.out.println("data fetched: " + map);
        for (Map postRaw : postsRaw) {
            PostModel postModel = new PostModel(postRaw);
            posts.add(postModel);
        }
        UIHelper.addArtifacts(posts);
    }

    public void fetch(final NetworkCallback scb) {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getFeedFollowing(),
                GenericContract.get_feed(),
                onDataReturned(),
                scb);
    }

    public void fetchUserMoments(int userId, NetworkCallback ncb)
    {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getUserMoments(userId),
                GenericContract.v1_get_user_moments(),
                onDataReturned(),
                ncb);
    }

    public ArrayList<PostModel> getPosts() {
        return posts;
    }


}
