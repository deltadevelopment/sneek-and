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
}
