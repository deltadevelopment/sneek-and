package no.twomonkeys.sneek.app.shared.helpers;

import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 14.10.2016.
 */

public class PostArtifacts {


    public Boolean sameUserNext, sameUserPrevious, previousIsMessage, nextIsMessage, isSameDay, isLastInDay, isLastSeen, rightAlignment;

    public PostArtifacts(Boolean sameUserNext, Boolean sameUserPrevious, Boolean previousIsMessage) {
        this.sameUserNext = sameUserNext;
        this.sameUserPrevious = sameUserPrevious;
        this.previousIsMessage = previousIsMessage;
    }

    public PostArtifacts() {
        this.sameUserNext = false;
        this.sameUserPrevious = false;
        this.previousIsMessage = false;
        this.nextIsMessage = false;
        this.isSameDay = false;
        this.isLastInDay = false;
        this.isLastSeen = false;
    }

    public static PostArtifacts newInstance(boolean sameUserPrevious, boolean previousIsMessage, boolean isSameDay) {
        PostArtifacts postArtifacts = new PostArtifacts();
        postArtifacts.sameUserPrevious = sameUserPrevious;
        postArtifacts.previousIsMessage = previousIsMessage;
        postArtifacts.isSameDay = isSameDay;
        postArtifacts.isLastInDay = true;
        postArtifacts.isLastSeen = true;

        return postArtifacts;
    }

    public static PostArtifacts artifactsFor(PostModel postModel, PostModel previousPost, PostModel nextPost) {
        PostArtifacts postArtifacts = new PostArtifacts();
        if (previousPost != null) {
            boolean previousIsMessage = previousPost.getMedia_type() == 2;
            postArtifacts.previousIsMessage = previousIsMessage;
        }
        if (previousPost != null && postModel != null) {
            boolean sameUserprev = previousPost.getUserModel().getId() == postModel.getUserModel().getId();
            boolean isSameDay = DateHelper.isSameDayWithDates(DateHelper.dateForString(postModel.getCreated_at()), DateHelper.dateForString(previousPost.getCreated_at()));
            postArtifacts.sameUserPrevious = sameUserprev;
            postArtifacts.isSameDay = isSameDay;
        }

        if (nextPost != null) {
            postArtifacts.nextIsMessage = nextPost.getMedia_type() == 2;
        }
        if (nextPost != null && postModel != null) {
            boolean isInTimeRange = DateHelper.isSameTimeWithDates(DateHelper.dateForString(nextPost.getCreated_at()), DateHelper.dateForString(postModel.getCreated_at()));
            postArtifacts.isLastInDay = !DateHelper.isSameDayWithDates(DateHelper.dateForString(postModel.getCreated_at()), DateHelper.dateForString(nextPost.getCreated_at()));
            postArtifacts.sameUserNext = nextPost.getUserModel().getId() == postModel.getUserModel().getId() && isInTimeRange;
        }


        return postArtifacts;
    }
}
