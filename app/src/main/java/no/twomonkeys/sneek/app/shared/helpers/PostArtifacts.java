package no.twomonkeys.sneek.app.shared.helpers;

/**
 * Created by simenlie on 14.10.2016.
 */

public class PostArtifacts {


    public Boolean sameUserNext, sameUserPrevious, previousIsMessage, nextIsMessage, isSameDay, isLastInDay, isLastSeen;

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

    public static PostArtifacts newInstance(boolean sameUserPrevious, boolean previousIsMessage, boolean isSameDay)
    {
        PostArtifacts postArtifacts = new PostArtifacts();
        postArtifacts.sameUserPrevious = sameUserPrevious;
        postArtifacts.previousIsMessage = previousIsMessage;
        postArtifacts.isSameDay = isSameDay;
        postArtifacts.isLastInDay = true;
        postArtifacts.isLastSeen = true;

        return postArtifacts;
    }
}
