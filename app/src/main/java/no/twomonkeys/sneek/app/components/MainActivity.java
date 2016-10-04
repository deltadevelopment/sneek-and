package no.twomonkeys.sneek.app.components;

import android.app.Activity;
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

    static Activity mActivity;

    private ViewPager vpPager;

    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager());

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mActivity.setTitle("Friends");
                        System.out.println("PageSelect: "+ position);
                        break;
                    case 1:
                        mActivity.setTitle("sneek");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mActivity = this;

        vpPager.setAdapter(adapterViewPager);

    }
    @Override
    protected void onStart() {
        super.onStart();
        // Set the viewPager to display item no 1
        vpPager.setCurrentItem(1);
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
            switch (position) {
                case 0:
                    return FriendsFragment.newInstance();
                case 1:
                    return SecondFragment.newInstance();
                default:
                    return null;
            }
        }

    }

}

