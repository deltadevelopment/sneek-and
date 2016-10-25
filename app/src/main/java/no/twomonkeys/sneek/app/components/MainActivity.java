package no.twomonkeys.sneek.app.components;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
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
import no.twomonkeys.sneek.app.components.main.MainPagerAdapter;
import no.twomonkeys.sneek.app.shared.helpers.CacheKeyFactory;
import no.twomonkeys.sneek.app.shared.models.PostModel;

public class MainActivity extends AppCompatActivity implements CameraFragment.Callback, MainPagerAdapter.Callback {
    MainPagerAdapter adapterViewPager;

    public static Activity mActivity;

    private ViewPager vpPager;
    private CameraFragment cameraFragment;
    private FeedFragment feedFragment;
    static String TAG = "MainActivity";
    //TextView toolbarTitle;
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfiguration();
        removeTitleBar();
        setContentView(R.layout.activity_main);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setSwipeable(true);
        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager(), this);
        adapterViewPager.addCallback(this);
/*
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Typeface type = Typeface.createFromAsset(getAssets(), "arial-rounded-mt-bold.ttf");
        toolbarTitle.setTypeface(type);
        toolbar = (Toolbar) findViewById(R.id.toolbar_top);
*/
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
        //getFragmentManager().beginTransaction().add(R.id.main, new CameraFragment()).commit();
        cameraFragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.cameraFragment);
        cameraFragment.addCallback(this);
        //cameraFragment.hide();


    }

    public void replaceFragment(android.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void setSwipeable(boolean swipeable) {
        vpPager.setSwipeable(swipeable);
    }

    private void setActionBarTitle(String title) {
        // toolbarTitle.setText(title);
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

    public ViewPager getVpPager() {
        return vpPager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the viewPager to display item no 1
        vpPager.setCurrentItem(1);
    }

    //Feed fragment delegates
    @Override
    public void feedFragmentOnFullScreenStart() {
        //toolbar.setVisibility(View.GONE);
    }

    @Override
    public void feedFragmentOnFullScreenEnd() {
        //toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void feedFragmentOnCameraClicked() {
        //getFragmentManager().beginTransaction().add(R.id.main, new CameraFragment()).commit();
        //cameraFragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.cameraFragment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.fragment_container);
        rl.setVisibility(View.VISIBLE);
        //CameraFragment cf = new CameraFragment();

        //    replaceFragment(cf);
    }

    @Override
    public void cameraFragmentTappedClose() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.fragment_container);
        rl.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void cameraFragmentDidPost(PostModel postModel) {
        cameraFragmentTappedClose();
        adapterViewPager.getFeedFragment().addNewImagePost(postModel);
    }
}

