package no.twomonkeys.sneek.app.components.main;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import no.twomonkeys.sneek.app.components.feed.FeedFragment;
import no.twomonkeys.sneek.app.components.friends.FriendsFragment;

/**
 * Created by simenlie on 25.10.2016.
 */

public class MainPagerAdapter extends FragmentPagerAdapter implements FeedFragment.Callback {


    private static int NUM_ITEMS = 2;
    private Activity activity;
    FeedFragment feedFragment;

    public FeedFragment getFeedFragment() {
        return feedFragment;
    }

    public interface Callback {
        void feedFragmentOnFullScreenStart();
        void feedFragmentOnFullScreenEnd();
        void feedFragmentOnCameraClicked();
    }

    Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public MainPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FriendsFragment.newInstance();
            case 1:
                feedFragment = FeedFragment.newInstance();
                feedFragment.addCallback(this);
                return feedFragment;
            default:
                return null;
        }
    }

    @Override
    public void feedFragmentOnFullScreenStart() {
        callback.feedFragmentOnFullScreenStart();
    }

    @Override
    public void feedFragmentOnFullScreenEnd() {
        callback.feedFragmentOnFullScreenEnd();
    }

    @Override
    public void feedFragmentOnCameraClicked() {
        callback.feedFragmentOnCameraClicked();
    }


}