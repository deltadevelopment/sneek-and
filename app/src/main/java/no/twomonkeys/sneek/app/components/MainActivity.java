package no.twomonkeys.sneek.app.components;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.camera.CameraFragment;
import no.twomonkeys.sneek.app.components.feed.FeedFragment;
import no.twomonkeys.sneek.app.components.friends.FriendsFragment;
import no.twomonkeys.sneek.app.shared.helpers.CacheKeyFactory;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    public static Activity mActivity;

    private ViewPager vpPager;
    private CameraFragment cameraFragment;
    private CameraFragment feedFragment;
    static String TAG = "MainActivity";
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfiguration();
        removeTitleBar();
        setContentView(R.layout.view_pager);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager());

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Typeface type = Typeface.createFromAsset(getAssets(), "arial-rounded-mt-bold.ttf");
        toolbarTitle.setTypeface(type);


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setActionBarTitle("Friends");
                        System.out.println("PageSelect: " + position);
                        break;
                    case 1:
                        setActionBarTitle("sneek");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mActivity = this;

        vpPager.setAdapter(adapterViewPager);

        //Camera
        //getFragmentManager().beginTransaction().add(R.id.mainLayout, new CameraFragment()).commit();
        //cameraFragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.cameraFragment);
    }

    private void setActionBarTitle(String title) {
        toolbarTitle.setText(title);
        /*
        mActivity.setTitle(title);
        Spannable text = new SpannableString(title);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.themeColor)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        */
    }

    private void removeTitleBar() {
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void initConfiguration() {
        CacheKeyFactory cacheKeyFactory = new CacheKeyFactory();

        Context context = getApplicationContext();
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)//
                .setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getPackageName()))
                .setBaseDirectoryName("image")
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)
                .setVersion(1)
                .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)//
                .setMainDiskCacheConfig(diskCacheConfig)
                .setCacheKeyFactory(cacheKeyFactory)
                .build();

        Fresco.initialize(context, imagePipelineConfig);
        //Fresco.initialize(this);

        //DataHelper.setContext(this);
        //DataHelper.setMa(this);
        //Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Remove top bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setStatusBarColor(getResources().getColor(R.color.cyan));
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
                    return FeedFragment.newInstance();
                default:
                    return null;
            }
        }

    }

}

