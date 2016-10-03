package no.twomonkeys.sneek.app.components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.friends.FriendsFragment;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public static class MainPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MainPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            Log.v(TAG, "Calling getItem");
            switch (position) {
                case 0:
                    return FriendsFragment.newInstance(0, "Page # 1");
                case 1:
                    return SecondFragment.newInstance(1, "Page # 2");
                default:
                    return null;
            }
        }

    }

}

