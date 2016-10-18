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
    public static final int MINIMUM_NEW_POSTS = 4;
    public static final int BOX_USERNAME_SPACE = 8;
    public static final int USERNAME_HEIGHT = 15;
    public static final int SEPERATOR_HEIGHT = 60;
    public static final int NEW_SEPERATOR_HEIGHT = 50;

    @Override
    void build(Map map) {
        ArrayList<Map> postsRaw = (ArrayList) map.get("posts");

            posts = new ArrayList<>();

        for (Map postRaw : postsRaw) {
            PostModel postModel = new PostModel(postRaw);
            posts.add(postModel);
        }
        addArtifacts();
    }

    public void fetch(final NetworkCallback scb) {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getFeedFollowing(),
                GenericContract.get_feed(),
                onDataReturned(),
                scb);
    }

    public ArrayList<PostModel> getPosts() {
        return posts;
    }

    public float heightForText(float height, int row, PostArtifacts artifacts) {
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

    public void addArtifacts() {
        int indexLoop = 0;
        for (PostModel postModel : posts) {
            if (postModel.getMedia_type() == 2) {
                postModel.postArtifacts = obtainArtifacts(indexLoop, postModel.getUserModel().getId(), postModel.getCreated_at(), postModel.getId(), 0);
                postModel.cellHeight = heightForText(postModel.size.height, indexLoop, postModel.postArtifacts);
            } else {
                //Moment
                postModel.postArtifacts = obtainArtifacts(indexLoop, postModel.getUserModel().getId(), postModel.getCreated_at(), postModel.getId(), 1);
                postModel.cellHeight = heightForText(postModel.size.height, indexLoop, postModel.postArtifacts);
            }
            indexLoop++;
        }
    }

    //Responsible for caluclating the artifats for each cell and next and previous
    public PostArtifacts obtainArtifacts(int index, int userId, String createdAt, int momentId, int momentType) {
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

            Log.v("TIME RANGE","is " + postArtifacts.sameUserNext + " : " + userId + " : " + postModel.getUserModel().getId() + " : " + isInTimeRange + " : " + createdAt + " : " + postModel.getCreated_at());

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

     //   Log.v("TIME RANGE","is " + postArtifacts.sameUserNext);


        return postArtifacts;
    }

    //Checking on disk if the cell is the last seen by the user
    private boolean isLastSeen(int lastSeenType, int postId, int indexValue) {
        LastSeenModel lastSeenModel = LastSeenModel.lastSeenModelFromDisk();
        if (lastSeenModel != null && indexValue != 0) {
            if (lastSeenModel.lastSeenType == lastSeenType && lastSeenModel.lastSeenId == postId) {
                return true;
            }
        }

        return false;
    }

}
